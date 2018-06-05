package com.example.aidan.bakingapp.Models;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

public interface IngredientsColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @NotNull
    String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    @Unique(onConflict = ConflictResolutionType.REPLACE)
    String COLUMN_BAKE_NAME = "bake_name";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String COLUMN_INGREDIENTS = "ingredients";

    @DataType(DataType.Type.INTEGER)
    String COLUMN_TIMESTAMP = "timestamp";
}
