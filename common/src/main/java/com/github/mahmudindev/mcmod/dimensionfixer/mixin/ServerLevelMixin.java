package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.DragonFight;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {
    @Unique private DragonFight dragonFight;

    @Shadow public abstract DimensionDataStorage getDataStorage();

    protected ServerLevelMixin(
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

    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;END:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> initDragonFightEndKey(ResourceKey<Level> original) {
        if (DimensionManager.isAlias(this, Level.END)) {
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
        if (DimensionManager.isAlias(this, Level.END)) {
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
            this.dragonFight = this.getDataStorage().computeIfAbsent(
                    DragonFight::load,
                    DragonFight::new,
                    DragonFight.FIELD
            );

            return this.dragonFight.loadData();
        }

        return original.call(instance);
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
            this.dragonFight.saveData(data);

            return;
        }

        original.call(instance, data);
    }
}
