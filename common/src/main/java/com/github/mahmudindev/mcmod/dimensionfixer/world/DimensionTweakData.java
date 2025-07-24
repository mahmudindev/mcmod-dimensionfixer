package com.github.mahmudindev.mcmod.dimensionfixer.world;

import com.google.gson.annotations.SerializedName;

public class DimensionTweakData {
    @SerializedName("fix_sleeping")
    private Boolean fixSleeping;
    @SerializedName("fix_portal_search_radius")
    private Boolean fixPortalSearchRadius;
    @SerializedName("override_flat_check")
    private Boolean overrideFlatCheck;

    public Boolean getFixSleeping() {
        return this.fixSleeping;
    }

    public void setFixSleeping(Boolean fixSleeping) {
        this.fixSleeping = fixSleeping;
    }

    public Boolean getFixPortalSearchRadius() {
        return this.fixPortalSearchRadius;
    }

    public void setFixPortalSearchRadius(Boolean fixPortalSearchRadius) {
        this.fixPortalSearchRadius = fixPortalSearchRadius;
    }

    public Boolean getOverrideFlatCheck() {
        return this.overrideFlatCheck;
    }

    public void setOverrideFlatCheck(Boolean overrideFlatCheck) {
        this.overrideFlatCheck = overrideFlatCheck;
    }
}
