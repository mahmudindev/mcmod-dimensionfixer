package com.github.mahmudindev.mcmod.dimensionfixer.world;

import com.google.gson.annotations.SerializedName;

public class DimensionTweakData {
    @SerializedName("fix_portal_search_radius")
    private Boolean fixPortalSearchRadius;
    @SerializedName("override_flat_check")
    private Boolean overrideFlatCheck;

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
