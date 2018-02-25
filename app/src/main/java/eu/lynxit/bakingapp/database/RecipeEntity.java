package eu.lynxit.bakingapp.database;

import android.arch.persistence.room.Entity;

import io.reactivex.annotations.NonNull;

/**
 * Created by lynx on 25/02/18.
 */

@Entity(primaryKeys = {"recipeId"})
public class RecipeEntity {
    @NonNull
    public Long recipeId;
    public String name;
    public String ingredients;
    public String steps;
    public Integer servings;
    public String image;
    
}
