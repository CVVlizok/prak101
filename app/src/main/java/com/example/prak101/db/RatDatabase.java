package com.example.prak101.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.ColumnInfo;

import com.example.prak101.db.Rat;
import com.example.prak101.db.RatDao;
@Database(entities = {Rat.class}, version = 1, exportSchema = false)
public abstract class RatDatabase extends RoomDatabase {
    public abstract RatDao getRatDao(); // Абстрактный метод для получения DAO для работы с крысами
}

