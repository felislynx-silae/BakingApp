package eu.lynxit.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

/**
 * Created by lynx on 24/02/18.
 */

public class BakingWidgetIntentService extends IntentService {
    public static final String ACTION_UPDATE = "eu.lynxit.bakingapp.action.update";
    public static final String ACTION_NEXT = "eu.lynxit.bakingapp.action.next";
    public static final String ACTION_PREV = "eu.lynxit.bakingapp.action.prev";
    public static final String EXTRA_RECIPE_ID = "eu.lynxit.bakingapp.extra.RECIPE_ID";
    public static final String EXTRA_WIDGET_ID = "eu.lynxit.bakingapp.extra.WIDGET_ID";

    public BakingWidgetIntentService() {
        super("BakingWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 0);
            final int widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, 0);
            if (ACTION_NEXT.equals(action)) {
                next(widgetId, recipeId);
            } else if (ACTION_PREV.equals(action)) {
                prev(widgetId, recipeId);
            } else if (ACTION_UPDATE.equals(action)) {
                update();
            }
        }
    }

    private void next(int widgetId, int currentId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        BakingWidgetProvider.updateSingleWidget(this, appWidgetManager, widgetId, currentId++);
    }

    private void prev(int widgetId, int currentId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        BakingWidgetProvider.updateSingleWidget(this, appWidgetManager, widgetId, currentId++);
    }

    private void update() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        int[] recipeIds = new int[appWidgetIds.length];
        for (int i = 0; i < appWidgetIds.length; i++) {
            recipeIds[i] = 0;
        }
        BakingWidgetProvider.updateAllWidgets(this, appWidgetManager, appWidgetIds, recipeIds);
    }
}
