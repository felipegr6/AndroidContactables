package com.example.android.DAO;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.android.entity.Contact;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class ContactDao_Impl implements ContactDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfContact;

  public ContactDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContact = new EntityInsertionAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `contact`(`contact_id`,`name`,`external_id`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        if (value.getContactId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getContactId());
        }
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getExternalId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getExternalId());
        }
      }
    };
  }

  @Override
  public void insertContact(Contact contact) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfContact.insert(contact);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Contact> query() {
    final String _sql = "SELECT * from contact ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfContactId = _cursor.getColumnIndexOrThrow("contact_id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfExternalId = _cursor.getColumnIndexOrThrow("external_id");
      final List<Contact> _result = new ArrayList<Contact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Contact _item;
        _item = new Contact();
        final String _tmpContactId;
        _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
        _item.setContactId(_tmpContactId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpExternalId;
        _tmpExternalId = _cursor.getString(_cursorIndexOfExternalId);
        _item.setExternalId(_tmpExternalId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
