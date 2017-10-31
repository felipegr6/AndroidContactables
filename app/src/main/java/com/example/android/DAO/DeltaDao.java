package com.example.android.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.entity.Delta;

@Dao
public interface DeltaDao {

    @Query("SELECT * FROM delta ORDER BY delta DESC LIMIT 1")
    Delta getLastDate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Delta d);
}
