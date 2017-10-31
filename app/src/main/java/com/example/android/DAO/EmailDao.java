package com.example.android.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.entity.Email;

import java.util.List;

@Dao
public interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEmail(Email phone);

    @Query("SELECT * FROM email WHERE contact_id IS :contactId")
    List<Email> getEmailsFromContact(String contactId);

    @Query("SELECT * FROM email")
    List<Email> getAll();
}
