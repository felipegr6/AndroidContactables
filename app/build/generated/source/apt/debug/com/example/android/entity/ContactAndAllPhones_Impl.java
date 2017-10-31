package com.example.android.entity;

import android.arch.persistence.room.RoomDatabase;

public class ContactAndAllPhones_Impl extends ContactAndAllPhones {
  private final RoomDatabase __db;

  public ContactAndAllPhones_Impl(RoomDatabase __db) {
    this.__db = __db;
  }
}
