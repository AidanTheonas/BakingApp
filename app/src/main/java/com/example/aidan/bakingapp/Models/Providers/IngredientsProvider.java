package com.example.aidan.bakingapp.Models.Providers;

import android.net.Uri;

import com.example.aidan.bakingapp.Models.IngredientsColumns;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static com.example.aidan.bakingapp.Models.Providers.IngredientsProvider.AUTHORITY;

@ContentProvider(
        authority = AUTHORITY,
        database = DataSource.class
)
public class IngredientsProvider {
    static final String AUTHORITY = "com.example.aidan.bakingapp.content.android.provider";

    @TableEndpoint(table = DataSource.INGREDIENTS_TABLE)
    public static class Ingredients {
        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredients",
                defaultSort = IngredientsColumns.COLUMN_TIMESTAMP + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");
    }
}
