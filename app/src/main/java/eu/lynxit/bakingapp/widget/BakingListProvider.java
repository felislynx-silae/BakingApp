package eu.lynxit.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.model.Ingredient;

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
        ingredientList.clear();
        // todo EXTRA_RECIPE_ID <-- extract from db
        for (int i = 0; i < 10; i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(1.25*i);
            ingredient.setMeasure("Test "+i);
            ingredient.setIngredient("Ingr "+i);
            ingredientList.add(ingredient);
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
