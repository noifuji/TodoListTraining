package com.example.todolisttraining;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private TaskDAO taskDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        taskDao = db.taskDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

/*    @Test
    public void writeTaskAndReadInList() throws Exception {
        TaskEntity task = new TaskEntity();


        taskDao.insert(task);
        List<TaskEntity> all = taskDao.getAll();
        assertEquals(1, all.size());
    }

    @Test
    public void deleteTask() throws Exception {
        TaskEntity task = new TaskEntity();
        task.id = 1;
        task.text = "Hello";

        taskDao.insert(task);

        task = new TaskEntity();
        task.id = 1;

        taskDao.delete(task);

        List<TaskEntity> all = taskDao.getAll();
        assertEquals(0, all.size());

    }*/
}