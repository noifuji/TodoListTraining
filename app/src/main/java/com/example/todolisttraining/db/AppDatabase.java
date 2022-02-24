package com.example.todolisttraining.db;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RenameColumn;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TaskEntity.class}, version = 2, exportSchema = true)
public abstract class AppDatabase  extends RoomDatabase {

    private static AppDatabase sInstance;
    public static final String DATABASE_NAME = "mydb";

    public abstract TaskDAO taskDAO();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
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
}
