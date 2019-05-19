package blacksmith.sullivanway.dao;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public abstract class AppDatabase extends RoomDatabase {
    private static int instanceCount= 0;
    private static AppDatabase instance = null;

    synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instanceCount++;
            instance = Room.databaseBuilder(context, AppDatabase.class, "subway").build();
        }
        return instance;
    }

    public static int getInstanceCount() {
        return instanceCount;
    }

}
