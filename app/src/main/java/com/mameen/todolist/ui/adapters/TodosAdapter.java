package com.mameen.todolist.ui.adapters;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mameen.todolist.R;
import com.mameen.todolist.data.DatabaseClient;
import com.mameen.todolist.data.tables.Task;

import java.util.List;

public class TodosAdapter extends ArrayAdapter<Task> {

    static final String TAG = TodosAdapter.class.getSimpleName();

    private Activity context;
    private List<Task> tasks;

    public TodosAdapter(@NonNull Activity context, @NonNull List<Task> tasks) {
        super(context, R.layout.row_task, tasks);

        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_task, null, true);

        final Task task = tasks.get(position);

        TextView tvTaskTitle = (TextView) rowView.findViewById(R.id.tvTaskTitle);
        CheckBox chDone = (CheckBox) rowView.findViewById(R.id.chDone);

        tvTaskTitle.setText(task.getTitle());
        chDone.setChecked(task.isDone());

        chDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setDone(isChecked);
                new UpdateTask().execute(task);
            }
        });

        return rowView;
    }

    private class UpdateTask extends AsyncTask<Task, Void, Void> {

        private Task task;

        @Override
        protected Void doInBackground(Task... prams) {
            task = prams[0];
            DatabaseClient.getInstance(context).getAppDatabase()
                    .taskDao()
                    .update(task);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Task (( " + task.getTitle() + " )) Updated", Toast.LENGTH_LONG).show();
        }
    }
}
