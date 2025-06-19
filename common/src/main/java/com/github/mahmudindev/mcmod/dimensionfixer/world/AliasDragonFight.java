package com.github.mahmudindev.mcmod.dimensionfixer.world;

import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class AliasDragonFight extends SavedData {
    public static SavedDataType<AliasDragonFight> TYPE = new SavedDataType<>(
            "dragon_fight",
            AliasDragonFight::new,
            EndDragonFight.Data.CODEC.xmap(
                    AliasDragonFight::new,
                    aliasDragonFight -> aliasDragonFight.data
            ),
            null
    );

    private EndDragonFight.Data data;

    public AliasDragonFight() {
        this.setDirty();
    }

    private AliasDragonFight(EndDragonFight.Data data) {
        this.data = data;
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
}
