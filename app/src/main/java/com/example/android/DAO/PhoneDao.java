package com.example.android.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.entity.Phone;

import java.util.List;

@Dao
public interface PhoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhone(Phone phone);

    @Query("SELECT * FROM phone WHERE contact_id IS :contactId")
    List<Phone> getPhonesFromContact(String contactId);

    @Query("SELECT * FROM phone")
    List<Phone> getAll();
}
