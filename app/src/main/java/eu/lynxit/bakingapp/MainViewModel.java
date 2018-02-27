package eu.lynxit.bakingapp;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.lynxit.bakingapp.database.RecipeEntity;
import eu.lynxit.bakingapp.model.Ingredient;
import eu.lynxit.bakingapp.model.Recipe;
import eu.lynxit.bakingapp.model.Step;
import eu.lynxit.bakingapp.rest.RecipeClient;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lynx on 15/02/18.
 */

public class MainViewModel extends ViewModel {
    private static final String RECIPE_ENDPOINT = "https://go.udacity.com/android-baking-app-json";
    public MutableLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();
    private Recipe selectedRecipe;
    private Step selectedStep;

    public void initializeRecipes() {
        BakingAppApplication.getInstance().getDatabase().recipeDAO()
                .getNumberOfRows()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Observable<List<Recipe>>>() {

                    @Override
                    public Observable<List<Recipe>> apply(Integer integer) throws Exception {
                        if (integer > 0) {
                            return BakingAppApplication.getInstance().getDatabase().recipeDAO()
                                    .loadAllRecipes()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io()).map(new Function<List<RecipeEntity>, List<Recipe>>() {
                                        @Override
                                        public List<Recipe> apply(List<RecipeEntity> recipeEntities) throws Exception {
                                            List<Recipe> tmp = new ArrayList<>();
                                            for (RecipeEntity entity : recipeEntities) {
                                                tmp.add(fromRecipeEntity(entity));
                                            }
                                            return tmp;
                                        }
                                    }).toObservable();
                        } else {
                            return RecipeClient.getRecipeClient().getRecipes(RECIPE_ENDPOINT)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io()).map(
                                    new Function<List<Recipe>, List<Recipe>>() {
                                        @Override
                                        public List<Recipe> apply(List<Recipe> recipes) throws Exception {
                                            List<RecipeEntity> recipeEntitiesConverted = new ArrayList<>();
                                            for (Recipe recipe : recipes) {
                                                recipeEntitiesConverted.add(recipeToEntity(recipe));
                                            }
                                            insertRecipes(recipeEntitiesConverted);
                                            return recipes;
                                        }
                                    }
                            );
                        }
                    }
                }).flatMapObservable(new Function<Observable<List<Recipe>>, ObservableSource<List<Recipe>>>() {
            @Override
            public ObservableSource<List<Recipe>> apply(Observable<List<Recipe>> listObservable) throws Exception {
                return listObservable;
            }
        }).subscribeWith(new Observer<List<Recipe>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Recipe> recipes) {
                mRecipes.setValue(recipes);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ViewModel", "ERRROR " + e.getMessage() + " e " + e.getClass().getName());
            }

            @Override
            public void onComplete() {

            }
        });
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

    public Recipe fromRecipeEntity(RecipeEntity entity) {
        Recipe recipe = new Recipe();
        recipe.setId(entity.recipeId);
        recipe.setName(entity.name);
        recipe.setServings(entity.servings);
        try {
            String ingredients = entity.ingredients;
            ObjectMapper mapper = new ObjectMapper();
            Ingredient[] array = mapper.readValue(ingredients, Ingredient[].class);
            recipe.setIngredients(Arrays.asList(array));
        } catch (Exception e) {
            Log.d("ViewModel", "Error " + e.getMessage() + " " + e.getClass().getName());
        }
        try {
            String steps = entity.steps;
            ObjectMapper mapper = new ObjectMapper();
            Step[] array = mapper.readValue(steps, Step[].class);
            recipe.setSteps(Arrays.asList(array));
        } catch (Exception e) {
            Log.d("ViewModel", "Error " + e.getMessage() + " " + e.getClass().getName());
        }
        return recipe;
    }
    private void insertRecipes(final List<RecipeEntity> recipeEntitiesConverted) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BakingAppApplication.getInstance().getDatabase().recipeDAO().insertRecipes(
                        recipeEntitiesConverted
                );
                return null;
            }
        }.execute();
    }
}
