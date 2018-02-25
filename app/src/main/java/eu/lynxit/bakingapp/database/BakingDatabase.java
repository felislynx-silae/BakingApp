package eu.lynxit.bakingapp.database;

/**
 * Created by lynx on 25/02/18.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RecipeEntity.class}, version = 1)
public abstract class BakingDatabase extends RoomDatabase {
    public abstract RecipeDAO recipeDAO();
}