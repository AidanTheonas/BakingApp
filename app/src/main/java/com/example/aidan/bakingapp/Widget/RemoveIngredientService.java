package com.example.aidan.bakingapp.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.aidan.bakingapp.Models.Providers.IngredientsProvider;
import com.example.aidan.bakingapp.R;

public class RemoveIngredientService extends IntentService {

    public static final String REMOVE_INGREDIENT_ACTION = "com.example.aidan.bakingapp.remove_ingredient_action";

    public RemoveIngredientService() {
        super("RemoveIngredientService");
    }

    @SuppressWarnings("unused")
    public static void startRemoveIngredient(Context context) {
        Intent intent = new Intent(context, RemoveIngredientService.class);
        intent.setAction(REMOVE_INGREDIENT_ACTION);
        context.startService(intent);
    }

    public void handleRemoveIngredient() {
        int data = getContentResolver().delete(IngredientsProvider.Ingredients.CONTENT_URI,
                null,
                null);
        if (data > 0) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
            int widgetsIds[] = widgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
            widgetManager.notifyAppWidgetViewDataChanged(widgetsIds, R.id.gv_widget_bakes_recipes);
            BakingAppWidget.updateBakingAppWidget(this, widgetManager, widgetsIds);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (REMOVE_INGREDIENT_ACTION.equals(action)) {
                handleRemoveIngredient();
            }
        }
    }
}
