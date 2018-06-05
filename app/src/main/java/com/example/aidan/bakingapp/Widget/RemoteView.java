package com.example.aidan.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.aidan.bakingapp.Models.IngredientsColumns;
import com.example.aidan.bakingapp.Models.Providers.IngredientsProvider;
import com.example.aidan.bakingapp.R;

public class RemoteView extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteView(this.getApplicationContext());
    }

    class WidgetRemoteView implements RemoteViewsService.RemoteViewsFactory {

        private Cursor cursor;
        private Context context;

        WidgetRemoteView(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (cursor != null) cursor.close();

            cursor = getContentResolver().query(IngredientsProvider.Ingredients.CONTENT_URI,
                    null,
                    null,
                    null,
                    IngredientsColumns.COLUMN_TIMESTAMP);
        }

        @Override
        public void onDestroy() {
            if (cursor != null) {
                cursor.close();
            }
        }

        @Override
        public int getCount() {
            if (cursor == null) {
                return 0;
            } else {
                return cursor.getCount();
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (cursor == null || cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToPosition(i);
            int bakePosition = cursor.getColumnIndex(IngredientsColumns.COLUMN_BAKE_NAME);
            int ingredientsPosition = cursor.getColumnIndex(IngredientsColumns.COLUMN_INGREDIENTS);

            String bakeName = cursor.getString(bakePosition);
            String ingredients = cursor.getString(ingredientsPosition);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

            remoteViews.setTextViewText(R.id.tv_bake_title, bakeName);
            remoteViews.setTextViewText(R.id.tv_bake_ingredients, ingredients);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
