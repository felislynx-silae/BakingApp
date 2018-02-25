package eu.lynxit.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import eu.lynxit.bakingapp.R;

/**
 * Created by lynx on 24/02/18.
 */

public class BakingWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        update(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int[] recipeIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateSingleWidget(context, appWidgetManager, appWidgetIds[i], recipeIds[i]);
        }
    }

    public static void updateSingleWidget(Context context, AppWidgetManager appWidgetManager, int id, int recipeId) {
        RemoteViews rv = generateRemoteView(context, id, recipeId);
        appWidgetManager.updateAppWidget(id, rv);
    }

    private static void update(Context context) {
        Intent intent = new Intent(context, BakingWidgetIntentService.class);
        intent.setAction(BakingWidgetIntentService.ACTION_UPDATE);
        context.startService(intent);
    }

    private static RemoteViews generateRemoteView(Context context, int id, int recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingridients);

        Intent listIntent = new Intent(context, BakingWidgetService.class);
        listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        listIntent.setData(Uri.parse(listIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_content, listIntent);

        Intent nextIntent = new Intent(context, BakingWidgetIntentService.class);
        nextIntent.putExtra(BakingWidgetIntentService.EXTRA_RECIPE_ID, recipeId);
        nextIntent.putExtra(BakingWidgetIntentService.EXTRA_WIDGET_ID, id);
        nextIntent.setAction(BakingWidgetIntentService.ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_next, nextPendingIntent);

        Intent prevIntent = new Intent(context, BakingWidgetIntentService.class);
        prevIntent.putExtra(BakingWidgetIntentService.EXTRA_RECIPE_ID, recipeId);
        prevIntent.putExtra(BakingWidgetIntentService.EXTRA_WIDGET_ID, id);
        nextIntent.setAction(BakingWidgetIntentService.ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getService(context, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_prev, prevPendingIntent);

        return views;
    }
}

