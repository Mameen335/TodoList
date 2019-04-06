package com.mameen.todolist.data;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mameen.todolist.data.daos.TaskDao;
import com.mameen.todolist.data.tables.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
