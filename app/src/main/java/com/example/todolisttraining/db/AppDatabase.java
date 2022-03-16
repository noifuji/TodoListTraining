package com.example.todolisttraining.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TaskEntity.class}, version = 3, exportSchema = true)
public abstract class AppDatabase  extends RoomDatabase {

    private static AppDatabase sInstance;
    public static final String DATABASE_NAME = "mydb";

    public abstract TaskLocalDataSource getTaskLocalDataSource();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return sInstance;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tasks "
                    + " ADD COLUMN is_important INTEGER not null default 0");
            database.execSQL("ALTER TABLE tasks "
                    + " RENAME COLUMN first_name TO text");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tasks "
                    + " ADD COLUMN is_removed INTEGER not null default 0");
            database.execSQL("ALTER TABLE tasks "
                    + " ADD COLUMN uuid TEXT");
        }
    };
}
