package eu.lynxit.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import eu.lynxit.bakingapp.BakingAppApplication;
import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.database.RecipeEntity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lynx on 24/02/18.
 */

public class BakingWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
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
        appWidgetManager.updateAppWidget(new ComponentName(context, BakingWidgetProvider.class), rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.widget_content);
    }

    private static void update(Context context) {
        Intent intent = new Intent(context, BakingWidgetIntentService.class);
        intent.setAction(BakingWidgetIntentService.ACTION_UPDATE);
        context.startService(intent);
    }

    private static RemoteViews generateRemoteView(Context context, int id, int recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingridients);
        if(recipeId<1){
            recipeId = 1;
        }
        RecipeEntity entity = BakingAppApplication.getInstance().getDatabase().recipeDAO()
                .findRecipe(recipeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .blockingGet();
        if (entity != null) {
            views.setTextViewText(R.id.widget_title, entity.name);
        } else {
            if(recipeId>1) {
                recipeId -= 1;
            }
        }

        Intent listIntent = new Intent(context, BakingWidgetService.class);
        listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        listIntent.putExtra(BakingListProvider.EXTRA_RECIPE_ID, recipeId);
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
        prevIntent.setAction(BakingWidgetIntentService.ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getService(context, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_prev, prevPendingIntent);

        Intent updateList = new Intent(BakingListProvider.ACTION_UPDATE_RECIPE);
        updateList.putExtra(BakingListProvider.EXTRA_RECIPE_ID,recipeId);
        updateList.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);
        context.sendBroadcast(updateList);

        return views;
    }
}

