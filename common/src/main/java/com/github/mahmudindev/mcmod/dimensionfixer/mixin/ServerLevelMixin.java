package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.AliasDragonFight;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionTweakData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.storage.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {
    @Shadow @Final private ServerLevelData serverLevelData;
    @Unique
    private AliasDragonFight aliasDragonFight;

    private ServerLevelMixin(
            WritableLevelData levelData,
            ResourceKey<Level> dimension,
            RegistryAccess registryAccess,
            Holder<DimensionType> dimensionTypeRegistration,
            Supplier<ProfilerFiller> profiler,
            boolean isClientSide,
            boolean isDebug,
            long biomeZoomSeed,
            int maxChainedNeighborUpdates
    ) {
        super(
                levelData,
                dimension,
                registryAccess,
                dimensionTypeRegistration,
                profiler,
                isClientSide,
                isDebug,
                biomeZoomSeed,
                maxChainedNeighborUpdates
        );
    }

    @Shadow public abstract DimensionDataStorage getDataStorage();

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/raid/Raids;getFileId(Lnet/minecraft/core/Holder;)Ljava/lang/String;"
            )
    )
    private String initRaidsGetFieldId(
            Holder<DimensionType> holder,
            Operation<String> original
    ) {
        if (DimensionManager.isAliasDimension(this, Level.END)) {
            return original.call(this.registryAccess()
                    .registryOrThrow(Registries.DIMENSION_TYPE)
                    .getHolderOrThrow(BuiltinDimensionTypes.END));
        }

        return original.call(holder);
    }

    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;END:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> initDragonFightEndKey(ResourceKey<Level> original) {
        if (DimensionManager.isAliasDimension(this, Level.END)) {
            return this.dimension();
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/dimension/BuiltinDimensionTypes;END:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<DimensionType> initDragonFightEndTypeKey(
            ResourceKey<DimensionType> original
    ) {
        if (DimensionManager.isAliasDimension(this, Level.END)) {
            return this.dimensionTypeId();
        }

        return original;
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/WorldData;endDragonFightData()Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;"
            )
    )
    private EndDragonFight.Data initDragonFightDataLoad(
            WorldData instance,
            Operation<EndDragonFight.Data> original
    ) {
        if (this.dimension() != Level.END) {
            this.aliasDragonFight = this.getDataStorage().computeIfAbsent(
                    AliasDragonFight::load,
                    AliasDragonFight::new,
                    AliasDragonFight.FIELD
            );

            return this.aliasDragonFight.loadData();
        }

        return original.call(instance);
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"
            )
    )
    private void tickSetDayTimeSleeping(
            ServerLevel instance,
            long value,
            Operation<Void> original
    ) {
        long dayTime = this.levelData.getDayTime();

        original.call(instance, value);

        if (this.serverLevelData instanceof DerivedLevelData) {
            if (dayTime != this.levelData.getDayTime()) {
                return;
            }

            boolean fixSleeping = false;

            if (DimensionManager.isAliasDimension(this, Level.OVERWORLD)) {
                DimensionTweakData tweak = DimensionManager.getTweak(Level.OVERWORLD);
                if (tweak != null) {
                    Boolean fixSleepingX = tweak.getFixSleeping();
                    if (fixSleepingX == null || fixSleepingX) {
                        fixSleeping = true;
                    }
                } else {
                    fixSleeping = true;
                }
            }

            if (!fixSleeping) {
                DimensionTweakData tweak = DimensionManager.getTweak(this.dimension());
                if (tweak == null) {
                    return;
                }

                Boolean fixSleepingX = tweak.getFixSleeping();
                if (fixSleepingX == null || !fixSleepingX) {
                    return;
                }
            }

            DerivedLevelDataAccessor sld = (DerivedLevelDataAccessor) this.serverLevelData;
            ServerLevelData serverLevelData = sld.getWrapped();
            serverLevelData.setDayTime(value);
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;resetWeatherCycle()V"
            )
    )
    private void tickResetWeatherCycleSleeping(
            ServerLevel instance,
            Operation<Void> original
    ) {
        boolean raining = this.levelData.isRaining();

        original.call(instance);

        if (this.serverLevelData instanceof DerivedLevelData) {
            if (raining != this.levelData.isRaining()) {
                return;
            }

            boolean fixSleeping = false;

            if (DimensionManager.isAliasDimension(this, Level.OVERWORLD)) {
                DimensionTweakData tweak = DimensionManager.getTweak(Level.OVERWORLD);
                if (tweak != null) {
                    Boolean fixSleepingX = tweak.getFixSleeping();
                    if (fixSleepingX == null || fixSleepingX) {
                        fixSleeping = true;
                    }
                } else {
                    fixSleeping = true;
                }
            }

            if (!fixSleeping) {
                DimensionTweakData tweak = DimensionManager.getTweak(this.dimension());
                if (tweak == null) {
                    return;
                }

                Boolean fixSleepingX = tweak.getFixSleeping();
                if (fixSleepingX == null || !fixSleepingX) {
                    return;
                }
            }

            DerivedLevelDataAccessor sld = (DerivedLevelDataAccessor) this.serverLevelData;
            ServerLevelData serverLevelData = sld.getWrapped();
            serverLevelData.setRainTime(0);
            serverLevelData.setRaining(false);
            serverLevelData.setThunderTime(0);
            serverLevelData.setThundering(false);
        }
    }

    @WrapOperation(
            method = "saveLevelData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/WorldData;setEndDragonFightData(Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;)V"
            )
    )
    private void saveLevelDataSetEndDragonFightDataSave(
            WorldData instance,
            EndDragonFight.Data data,
            Operation<Void> original
    ) {
        if (this.dimension() != Level.END) {
            this.aliasDragonFight.saveData(data);

            return;
        }

        original.call(instance, data);
    }

    @WrapMethod(method = "isFlat")
    public boolean isFlatOverride(Operation<Boolean> original) {
        DimensionTweakData tweak = DimensionManager.getTweak(this.dimension());
        if (tweak != null) {
            Boolean overrideFlatCheck = tweak.getOverrideFlatCheck();
            if (overrideFlatCheck != null) {
                return overrideFlatCheck;
            }
        }

        return original.call();
    }
}
