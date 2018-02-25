package eu.lynxit.bakingapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by lynx on 25/02/18.
 */
@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertRecipes(List<RecipeEntity> recipeList);

    @Update
    public void updateRecipes(List<RecipeEntity> recipes);

    @Update
    public void updateFile(RecipeEntity recipe);

    @Delete
    public void deleteRecipes(List<RecipeEntity> recipes);

    @Query("SELECT * FROM RecipeEntity")
    public Maybe<List<RecipeEntity>> loadAllRecipes();

    @Query("SELECT * FROM RecipeEntity WHERE recipeId == :rid")
    public Maybe<RecipeEntity> findRecipe(Integer rid);
}
