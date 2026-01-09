package com.github.mahmudindev.mcmod.dimensionfixer.world;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.LinkedList;
import java.util.List;

public class DimensionAliasData {
    @SerializedName("dimension_types")
    private final List<String> dimensionTypes = new LinkedList<>();
    private final List<String> dimensions = new LinkedList<>();

    public List<String> getDimensionTypes() {
        return List.copyOf(dimensionTypes);
    }

    public boolean containDimensionType(String dimension) {
        return this.dimensionTypes.contains(dimension);
    }

    public boolean containDimensionType(Identifier dimension) {
        return this.containDimensionType(String.valueOf(dimension));
    }

    public boolean containDimensionType(ResourceKey<DimensionType> dimension) {
        return this.containDimensionType(dimension.identifier());
    }

    public void addDimensionType(String dimensionType) {
        if (this.containDimensionType(dimensionType)) {
            return;
        }

        this.dimensionTypes.add(dimensionType);
    }

    public void addDimensionType(Identifier dimensionType) {
        this.addDimensionType(String.valueOf(dimensionType));
    }

    public void addDimensionType(ResourceKey<DimensionType> dimensionType) {
        this.addDimensionType(dimensionType.identifier());
    }

    public void addAllDimensionType(List<String> dimensionTypes) {
        dimensionTypes.forEach(this::addDimensionType);
    }

    public List<String> getDimensions() {
        return List.copyOf(this.dimensions);
    }

    public boolean containDimension(String dimension) {
        return this.dimensions.contains(dimension);
    }

    public boolean containDimension(Identifier dimension) {
        return this.containDimension(String.valueOf(dimension));
    }

    public boolean containDimension(ResourceKey<Level> dimension) {
        return this.containDimension(dimension.identifier());
    }

    public void addDimension(String dimension) {
        if (this.containDimension(dimension)) {
            return;
        }

        this.dimensions.add(dimension);
    }

    public void addDimension(Identifier dimension) {
        this.addDimension(String.valueOf(dimension));
    }

    public void addDimension(ResourceKey<Level> dimension) {
        this.addDimension(dimension.identifier());
    }

    public void addAllDimension(List<String> dimensions) {
        dimensions.forEach(this::addDimension);
    }
}
