package eu.lynxit.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;

import eu.lynxit.bakingapp.BakingAppApplication;
import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.database.RecipeEntity;
import eu.lynxit.bakingapp.model.Ingredient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lynx on 24/02/18.
 */

public class BakingListProvider implements RemoteViewsService.RemoteViewsFactory {
    public static final String EXTRA_RECIPE_ID = "eu.lynxit.bakingapp.extra.RECIPE_ID";
    public static final String ACTION_UPDATE_RECIPE = "eu.lynxit.bakingapp.action.UPDATE_RECIPE";
    private ArrayList<Ingredient> ingredientList = new ArrayList();
    private Context context = null;
    int recipeId = 1;
    int appWidgetId = 0;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int tmpWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            if(tmpWidgetID == appWidgetId) {
                recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                        1);
            }
        }
    };
    public BakingListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                1);
    }

    @Override
    public void onCreate() {
        context.registerReceiver(receiver,new IntentFilter(ACTION_UPDATE_RECIPE));
    }

    @Override
    public void onDataSetChanged() {
        ingredientList.clear();
        RecipeEntity entity = BakingAppApplication.getInstance().getDatabase().recipeDAO()
                .findRecipe(recipeId)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .blockingGet();
        if(entity != null) {
            try {
                String ingredients = entity.ingredients;
                ObjectMapper mapper = new ObjectMapper();
                Ingredient[] array = mapper.readValue(ingredients, Ingredient[].class);
                ingredientList.addAll(Arrays.asList(array));
            } catch (Exception e) {
                Log.d("BakingListProvider", "Error " + e.getMessage() + " " + e.getClass().getName());
            }
        }
    }

    @Override
    public void onDestroy() {
        try{
            context.unregisterReceiver(receiver);
        } catch (Exception e){

        }
    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_listview_item_ingridient);
        Ingredient ingredient = ingredientList.get(position);
        remoteView.setTextViewText(R.id.widget_ingredient_name, ingredient.getIngredient());
        remoteView.setTextViewText(R.id.widget_ingredient_quantity, String.format(" %.2f", ingredient.getQuantity()));
        remoteView.setTextViewText(R.id.widget_ingredient_measure, ingredient.getMeasure());
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
