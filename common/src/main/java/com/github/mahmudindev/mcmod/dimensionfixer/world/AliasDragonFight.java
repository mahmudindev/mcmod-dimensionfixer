package com.github.mahmudindev.mcmod.dimensionfixer.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Arrays;
import java.util.Optional;

public class AliasDragonFight extends SavedData {
    public static String FIELD = "dragon_fight";

    private EndDragonFight.Data data;

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putBoolean("NeedsStateScanning", this.data.needsStateScanning());
        compoundTag.putBoolean("DragonKilled", this.data.dragonKilled());
        compoundTag.putBoolean("PreviouslyKilled", this.data.previouslyKilled());
        compoundTag.putBoolean("IsRespawning", this.data.isRespawning());
        this.data.dragonUUID().ifPresent(uuid -> {
            compoundTag.putUUID("Dragon", uuid);
        });
        this.data.exitPortalLocation().ifPresent(blockPos -> {
            compoundTag.putIntArray("ExitPortalLocation", new int[]{
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ()
            });
        });
        this.data.gateways().ifPresent(gateways -> {
            compoundTag.putIntArray("Gateways", gateways);
        });

        return compoundTag;
    }

    public EndDragonFight.Data loadData() {
        if (this.data != null) {
            return this.data;
        }

        return EndDragonFight.Data.DEFAULT;
    }

    public void saveData(EndDragonFight.Data data) {
        this.data = data;
        this.setDirty();
    }

    public static AliasDragonFight load(CompoundTag compoundTag) {
        AliasDragonFight aliasDragonFight = new AliasDragonFight();

        aliasDragonFight.data = new EndDragonFight.Data(
                compoundTag.getBoolean("NeedsStateScanning"),
                compoundTag.getBoolean("DragonKilled"),
                compoundTag.getBoolean("PreviouslyKilled"),
                compoundTag.getBoolean("IsRespawning"),
                Optional.of(compoundTag.contains("Dragon")).map(exist -> {
                    if (exist) {
                        return compoundTag.getUUID("Dragon");
                    }
                    return null;
                }),
                Optional.of(compoundTag.contains("ExitPortalLocation")).map(exist -> {
                    if (exist) {
                        int[] v = compoundTag.getIntArray("ExitPortalLocation");
                        if (v.length < 3) {
                            return null;
                        }
                        return new BlockPos(v[0], v[1], v[2]);
                    }
                    return null;
                }),
                Optional.of(compoundTag.contains("Gateways")).map(exist -> {
                    if (exist) {
                        int[] v = compoundTag.getIntArray("Gateways");
                        if (v.length < 1) {
                            return null;
                        }
                        return Arrays.stream(v).boxed().toList();
                    }
                    return null;
                })
        );

        return aliasDragonFight;
    }
}
