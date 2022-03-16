package com.example.todolisttraining.db;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseTaskApi implements TaskApi {

    private static final String TAG = "FirebaseApi";
    private static final String FIRESTORE_COLLECTION_PATH = "tasks";
    private FirebaseFirestore mFirestore;

    public FirebaseTaskApi() {
        this.mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public Single<List<TaskEntity>> fetchTasks() {
        return Single.create((sub) -> {
            mFirestore.collection(FIRESTORE_COLLECTION_PATH)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List r = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaskEntity t = new TaskEntity();
                                t.setId(0);
                                t.setText((String) document.getData().get("text"));
                                t.setImportant((Boolean) document.getData().get("isImportant"));
                                t.setRemoved((Boolean) document.getData().get("isRemoved"));
                                t.setUuid((String) document.getData().get("uuid"));
                                r.add(t);
                            }
                            sub.onSuccess(r);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            sub.onError(task.getException());
                        }
                    });
        });
    }

    @Override
    public Completable updateTask(TaskEntity task) {
        return Completable.create((sub) -> {
            Map<String, Object> docData = new HashMap<>();
            docData.put("id", task.getId());
            docData.put("text", task.getText());
            docData.put("isImportant", task.isImportant());
            docData.put("isRemoved", task.isRemoved());
            docData.put("uuid", task.getUuid());

            final Task<Void> firestoreTask = mFirestore.collection(FIRESTORE_COLLECTION_PATH)
                    .document(task.getUuid())
                    .set(docData);

            Log.d(TAG, Thread.currentThread().getName());

            firestoreTask.addOnSuccessListener(aVoid -> {
                Log.d(TAG, Thread.currentThread().getName());
                sub.onComplete();
            }).addOnFailureListener(e -> sub.onError(e));
        });
    }

}
