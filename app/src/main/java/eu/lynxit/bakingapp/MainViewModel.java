package eu.lynxit.bakingapp;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.lynxit.bakingapp.model.Recipe;
import eu.lynxit.bakingapp.model.Step;

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
}
