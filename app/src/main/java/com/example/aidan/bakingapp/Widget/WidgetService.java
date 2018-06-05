package com.example.aidan.bakingapp.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.aidan.bakingapp.R;

public class WidgetService extends IntentService {
    public static final String UPDATE_WIDGET_ACTION = "com.example.aidan.bakingapp.update_widget_action";

    public WidgetService() {
        super("BakesWidgetService");
    }

    public static void actionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(UPDATE_WIDGET_ACTION);
        context.startService(intent);
    }

    private void updateWidgetAction() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int widgetsIds[] = widgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        widgetManager.notifyAppWidgetViewDataChanged(widgetsIds, R.id.gv_widget_bakes_recipes);
        BakingAppWidget.updateBakingAppWidget(this, widgetManager, widgetsIds);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (UPDATE_WIDGET_ACTION.equals(action)) {
                updateWidgetAction();
            }
        }
    }
}
