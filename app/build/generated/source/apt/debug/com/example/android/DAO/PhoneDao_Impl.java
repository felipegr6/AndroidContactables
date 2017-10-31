package com.example.android.DAO;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.android.entity.Phone;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class PhoneDao_Impl implements PhoneDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfPhone;

  public PhoneDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPhone = new EntityInsertionAdapter<Phone>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `phone`(`value`,`contact_id`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Phone value) {
        if (value.getValue() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getValue());
        }
        if (value.getContactId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getContactId());
        }
      }
    };
  }

  @Override
  public void insertPhone(Phone phone) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfPhone.insert(phone);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Phone> getPhonesFromContact(String contactId) {
    final String _sql = "SELECT * FROM phone WHERE contact_id IS ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (contactId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, contactId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfValue = _cursor.getColumnIndexOrThrow("value");
      final int _cursorIndexOfContactId = _cursor.getColumnIndexOrThrow("contact_id");
      final List<Phone> _result = new ArrayList<Phone>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Phone _item;
        _item = new Phone();
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        _item.setValue(_tmpValue);
        final String _tmpContactId;
        _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
        _item.setContactId(_tmpContactId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Phone> getAll() {
    final String _sql = "SELECT * FROM phone";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfValue = _cursor.getColumnIndexOrThrow("value");
      final int _cursorIndexOfContactId = _cursor.getColumnIndexOrThrow("contact_id");
      final List<Phone> _result = new ArrayList<Phone>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Phone _item;
        _item = new Phone();
        final String _tmpValue;
        _tmpValue = _cursor.getString(_cursorIndexOfValue);
        _item.setValue(_tmpValue);
        final String _tmpContactId;
        _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
        _item.setContactId(_tmpContactId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
