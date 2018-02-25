package eu.lynxit.bakingapp;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.facebook.stetho.Stetho;

import eu.lynxit.bakingapp.database.BakingDatabase;

/**
 * Created by lynx on 22/02/18.
 */

public class BakingAppApplication extends Application {
    private static BakingAppApplication instance;
    private BakingDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public BakingDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = Room.databaseBuilder(getApplicationContext(),
                    BakingDatabase.class, "bakingAppDatabase").build();
        }
        return mDatabase;
    }

    public static BakingAppApplication getInstance() {
        return instance;
    }
}
