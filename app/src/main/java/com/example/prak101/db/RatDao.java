package com.example.prak101.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.ColumnInfo;

import java.util.List;

@Dao
public interface RatDao {

    @Insert
    void addRat(Rat rat);

    @Delete
    void deleteRat(Rat rat);

    @Query("SELECT * FROM rats")
    List<Rat> getAllRats();

    @Query("UPDATE rats SET Name = :name WHERE Age = :age")
    void updateRatName(String name, int age);

    @Query("DELETE FROM rats WHERE Color = :color")
    void deleteRatByColor(String color);

    @Query("SELECT * FROM rats WHERE Name = :name")
    Rat findRatByName(String name);

    // Другие запросы, если необходимо
}
