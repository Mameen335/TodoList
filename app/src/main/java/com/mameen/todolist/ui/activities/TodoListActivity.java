package com.mameen.todolist.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.mameen.todolist.R;
import com.mameen.todolist.data.DatabaseClient;
import com.mameen.todolist.data.tables.Task;
import com.mameen.todolist.ui.adapters.TodosAdapter;
import com.mameen.todolist.utils.PermissionChecker;

import java.util.List;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindClick;
import io.github.sporklibrary.android.annotations.BindView;

public class TodoListActivity extends AppCompatActivity {

    static final String TAG = TodoListActivity.class.getSimpleName();

    private PermissionChecker permissionChecker = new PermissionChecker();

    private static final String[] RequiredPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @BindView(R.id.lstTodo)
    private ListView lstTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

//        setTitle(getResources().getString(R.string.app_name));

        Spork.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPermissions(RequiredPermissions);
    }

    private void checkPermissions(@NonNull String[] permissions) {
        Log.e(TAG, "oncheckPermissions");
        permissionChecker.verifyPermissions(TodoListActivity.this, permissions, new PermissionChecker.VerifyPermissionsCallback() {

            @Override
            public void onPermissionAllGranted() {
                GetTasks gt = new GetTasks();
                gt.execute();
            }

            @Override
            public void onPermissionDeny(String[] permissions) {
                Toast.makeText(TodoListActivity.this
                        , "Please grant required permissions.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @BindClick(R.id.btnAddTask)
    private void addTask() {
        Intent intent = new Intent(TodoListActivity.this, AddTaskActivity.class);
        startActivity(intent);
    }

    private class GetTasks extends AsyncTask<Void, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Void... voids) {
            List<Task> taskList = DatabaseClient
                    .getInstance(TodoListActivity.this)
                    .getAppDatabase()
                    .taskDao()
                    .getAll();
            return taskList;
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);

            if (tasks.size() > 0) {
                TodosAdapter adapter = new TodosAdapter(TodoListActivity.this, tasks);
                lstTodo.setAdapter(adapter);
            } else {
                Toast.makeText(TodoListActivity.this
                        , "There are to tasks!"
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

}
