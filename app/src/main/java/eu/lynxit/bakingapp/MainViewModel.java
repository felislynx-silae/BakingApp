package eu.lynxit.bakingapp;

import android.arch.lifecycle.ViewModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.lynxit.bakingapp.database.RecipeEntity;
import eu.lynxit.bakingapp.model.Recipe;
import eu.lynxit.bakingapp.model.Step;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lynx on 15/02/18.
 */

public class MainViewModel extends ViewModel {
    private List<Recipe> recipes = new ArrayList<>();
    private Recipe selectedRecipe;
    private Step selectedStep;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void initializeRecipes(Context context) {
        if (recipes.isEmpty()) {
            try {
                InputStream inputStream = context.getAssets().open("baking.json");
                ObjectMapper mapper = new ObjectMapper();
                Recipe[] recipeArray = mapper.readerFor(Recipe[].class).readValue(inputStream);
                Collections.addAll(recipes, recipeArray);

                BakingAppApplication.getInstance().getDatabase().recipeDAO()
                        .loadAllRecipes()
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new MaybeObserver<List<RecipeEntity>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<RecipeEntity> recipeEntities) {
                                if (recipeEntities == null || recipeEntities.isEmpty()) {
                                    List<RecipeEntity> recipeEntitiesConverted = new ArrayList<>();
                                    for (Recipe recipe : recipes) {
                                        recipeEntitiesConverted.add(recipeToEntity(recipe));
                                    }
                                    BakingAppApplication.getInstance().getDatabase().recipeDAO().insertRecipes(
                                            recipeEntitiesConverted
                                    );
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } catch (Exception e) {
                Log.d("Lynx", "Exception");
            }
        }
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }

    public Step getSelectedStep() {
        return selectedStep;
    }

    public void setSelectedStep(Step selectedStep) {
        this.selectedStep = selectedStep;
    }

    public RecipeEntity recipeToEntity(Recipe recipe) {
        RecipeEntity entity = new RecipeEntity();
        try {
            entity.recipeId = recipe.getId();
            entity.name = recipe.getName();
            ObjectMapper mapper = new ObjectMapper();
            String ingredients = mapper.writeValueAsString(recipe.getIngredients());
            entity.ingredients = ingredients;
            String steps = mapper.writeValueAsString(recipe.getSteps());
            entity.steps = steps;
            entity.servings = recipe.getServings();
            entity.image = recipe.getImage();
        } catch (Exception e) {
            Log.d("ViewModel", "Marshall exception");
        }
        return entity;
    }
}
