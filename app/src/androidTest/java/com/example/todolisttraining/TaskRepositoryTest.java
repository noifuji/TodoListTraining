package com.example.todolisttraining;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todolisttraining.db.AppDatabase;
import com.example.todolisttraining.db.TaskApi;
import com.example.todolisttraining.db.TaskLocalDataSource;
import com.example.todolisttraining.db.TaskEntity;
import com.example.todolisttraining.db.TaskRemoteDataSource;
import com.example.todolisttraining.db.TaskRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TaskRepositoryTest {
    private AppDatabase db;
    private TaskRepository mTaskRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        TaskLocalDataSource localDS = db.getTaskLocalDataSource();
        TaskRemoteDataSource remoteDS = new TaskRemoteDataSource(new TaskApiStub());
        mTaskRepository =new TaskRepository(localDS, remoteDS);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    /**
     * タスクの追加と読み込みができることを確認する。
     * @throws Exception
     */
    @Test
    public void testWriteAndRead() throws Exception {
        TaskEntity task = new TaskEntity();
        task.setId(1);
        task.setText("aaa");

        mTaskRepository.insert(task).blockingAwait();


        mTaskRepository.getAll()
                .test()
                .awaitCount(1)
                .assertValue(tasks -> tasks.size() == 1);

    }

    /**
     * タスクが削除されることを確認する。
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        TaskEntity task = new TaskEntity();
        task.setId(1);
        task.setText("aaa");

        mTaskRepository.insert(task).blockingAwait();

        mTaskRepository.delete(task).blockingAwait();

        mTaskRepository.getAll()
                .test()
                .assertValue(tasks -> tasks.size() == 0);

    }

    @Test
    public void testWriteWhenError() throws Exception {
        TaskRepository repo = new TaskRepository(db.getTaskLocalDataSource(),
                new TaskRemoteDataSource(new TaskApiStubError()));

        TaskEntity task = new TaskEntity();
        task.setId(1);
        task.setText("aaa");

        repo.insert(task)
                .test()
                .awaitCount(1)
                .assertError(RuntimeException.class);

    }


    /**
     * タスクのオンラインストレージのスタブ
     */
    class TaskApiStub implements TaskApi {

        @Override
        public Single<List<TaskEntity>> fetchTasks() {
            return Single.create((emitter -> emitter.onSuccess(null)));
        }

        @Override
        public Completable updateTask(TaskEntity task) {
            return Completable.create((emitter -> emitter.onComplete()));
        }
    }

    /**
     * タスクのオンラインストレージのエラー発生用スタブ
     */
    class TaskApiStubError implements TaskApi {

        @Override
        public Single<List<TaskEntity>> fetchTasks() {
            return Single.create((emitter -> emitter.onError(new RuntimeException())));
        }

        @Override
        public Completable updateTask(TaskEntity task) {
            return Completable.create((emitter -> emitter.onError(new RuntimeException())));
        }
    }
}