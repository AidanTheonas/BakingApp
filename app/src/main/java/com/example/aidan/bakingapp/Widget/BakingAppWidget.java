package com.example.aidan.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.aidan.bakingapp.MainActivity;
import com.example.aidan.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = generateViews(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews generateViews(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view);
        Intent remoteViewIntent = new Intent(context, RemoteView.class);
        remoteViews.setRemoteAdapter(R.id.gv_widget_bakes_recipes, remoteViewIntent);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.gv_widget_bakes_recipes, pendingIntent);

        Intent removeIngredientIntent = new Intent(context, RemoveIngredientService.class);
        removeIngredientIntent.setAction(RemoveIngredientService.REMOVE_INGREDIENT_ACTION);
        PendingIntent removeIngredientPendingIntent = PendingIntent.getService(context, 777, removeIngredientIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_remove_ingredient, removeIngredientPendingIntent);

        //Launch activity if no ingredient has been added
        remoteViews.setOnClickPendingIntent(R.id.tv_no_ingredients, pendingIntent);

        remoteViews.setEmptyView(R.id.gv_widget_bakes_recipes, R.id.tv_no_ingredients);
        return remoteViews;

    }

    public static void updateBakingAppWidget(Context context, AppWidgetManager manager, int[] widgetsIds) {
        for (int appWidgetId : widgetsIds) {
            updateAppWidget(context, manager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.actionUpdateWidget(context);
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

