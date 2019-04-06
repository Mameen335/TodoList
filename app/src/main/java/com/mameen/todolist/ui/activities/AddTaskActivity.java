package com.mameen.todolist.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mameen.todolist.R;
import com.mameen.todolist.data.DatabaseClient;
import com.mameen.todolist.data.tables.Task;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindClick;
import io.github.sporklibrary.android.annotations.BindView;

public class AddTaskActivity extends AppCompatActivity {

    static final String TAG = AddTaskActivity.class.getSimpleName();

    @BindView(R.id.etTaskTitle)
    private EditText etTaskTitle;

    @BindView(R.id.btnSave)
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setTitle(getResources().getString(R.string.add_task));

        Spork.bind(this);
    }

    @BindClick(R.id.btnSave)
    private void insertTask(){
        btnSave.setEnabled(false);
        final String taskTitle = etTaskTitle.getText().toString().trim();

        if (taskTitle.isEmpty()) {
            etTaskTitle.setError(getResources().getString(R.string.task_title_required));
            etTaskTitle.requestFocus();
            btnSave.setEnabled(true);
            return;
        }

        SaveTask st = new SaveTask();
        st.execute(taskTitle);
    }

    private class SaveTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... parms) {

            //creating a task
            Task task = new Task();
            task.setTitle(parms[0]);
            task.setDone(false);

            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .taskDao()
                    .insert(task);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btnSave.setEnabled(true);
            finish();
            startActivity(new Intent(getApplicationContext(), TodoListActivity.class));
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        }
    }
}
