package com.example.android.entity;

import android.arch.persistence.room.RoomDatabase;

public class ContactAndAllEmails_Impl extends ContactAndAllEmails {
  private final RoomDatabase __db;

  public ContactAndAllEmails_Impl(RoomDatabase __db) {
    this.__db = __db;
  }
}
