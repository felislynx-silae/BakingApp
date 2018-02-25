package eu.lynxit.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
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
    private ArrayList<Ingredient> ingredientList = new ArrayList();
    private Context context = null;
    private int appWidgetId;

    public BakingListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Integer recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                1);
        ingredientList.clear();
        RecipeEntity entity = BakingAppApplication.getInstance().getDatabase().recipeDAO()
                .findRecipe(recipeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .blockingGet();
        try {
            String ingredients = entity.ingredients;
            ObjectMapper mapper = new ObjectMapper();
            Ingredient[] array = mapper.readValue(ingredients, Ingredient[].class);
            ingredientList.addAll(Arrays.asList(array));
        } catch (Exception e){
            Log.d("Lynx", "BŁĄD");
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

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
