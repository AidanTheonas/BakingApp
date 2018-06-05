package com.example.aidan.bakingapp.Models.Providers;

import com.example.aidan.bakingapp.Models.IngredientsColumns;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(
        version = DataSource.DATABASE_VERSION
)
public class DataSource {
    @Table(IngredientsColumns.class)
    public static final String INGREDIENTS_TABLE = "tbl_ingredients";
    static final int DATABASE_VERSION = 5;
}
