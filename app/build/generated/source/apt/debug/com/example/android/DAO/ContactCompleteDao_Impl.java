package com.example.android.DAO;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;
import com.example.android.entity.Contact;
import com.example.android.entity.Email;
import com.example.android.entity.Phone;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContactCompleteDao_Impl implements ContactCompleteDao {
  private final RoomDatabase __db;

  public ContactCompleteDao_Impl(RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public List<ContactComplete> load() {
    final String _sql = "SELECT * from contact";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final ArrayMap<String, ArrayList<Email>> _collectionEmails = new ArrayMap<String, ArrayList<Email>>();
      final ArrayMap<String, ArrayList<Phone>> _collectionPhones = new ArrayMap<String, ArrayList<Phone>>();
      final int _cursorIndexOfContactId = _cursor.getColumnIndexOrThrow("contact_id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final List<ContactComplete> _result = new ArrayList<ContactComplete>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ContactComplete _item;
        final Contact _tmpContact;
        if (! (_cursor.isNull(_cursorIndexOfContactId) && _cursor.isNull(_cursorIndexOfName))) {
          _tmpContact = new Contact();
          final String _tmpContactId;
          _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
          _tmpContact.setContactId(_tmpContactId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          _tmpContact.setName(_tmpName);
        }  else  {
          _tmpContact = null;
        }
        _item = new ContactComplete();
        if (!_cursor.isNull(_cursorIndexOfContactId)) {
          final String _tmpKey = _cursor.getString(_cursorIndexOfContactId);
          ArrayList<Email> _tmpCollection = _collectionEmails.get(_tmpKey);
          if(_tmpCollection == null) {
            _tmpCollection = new ArrayList<Email>();
            _collectionEmails.put(_tmpKey, _tmpCollection);
          }
          _item.emails = _tmpCollection;
        }
        if (!_cursor.isNull(_cursorIndexOfContactId)) {
          final String _tmpKey_1 = _cursor.getString(_cursorIndexOfContactId);
          ArrayList<Phone> _tmpCollection_1 = _collectionPhones.get(_tmpKey_1);
          if(_tmpCollection_1 == null) {
            _tmpCollection_1 = new ArrayList<Phone>();
            _collectionPhones.put(_tmpKey_1, _tmpCollection_1);
          }
          _item.phones = _tmpCollection_1;
        }
        _item.contact = _tmpContact;
        _result.add(_item);
      }
      __fetchRelationshipEmailAscomExampleAndroidEntityEmail(_collectionEmails);
      __fetchRelationshipphoneAscomExampleAndroidEntityPhone(_collectionPhones);
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  private void __fetchRelationshipEmailAscomExampleAndroidEntityEmail(final ArrayMap<String, ArrayList<Email>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT email_id,value,contact_id FROM `Email` WHERE email_id IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    final Cursor _cursor = __db.query(_stmt);
    try {
      final int _itemKeyIndex = _cursor.getColumnIndex("email_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfEmailId = _cursor.getColumnIndexOrThrow("email_id");
      final int _cursorIndexOfValue = _cursor.getColumnIndexOrThrow("value");
      final int _cursorIndexOfContactId = _cursor.getColumnIndexOrThrow("contact_id");
      while(_cursor.moveToNext()) {
        if (!_cursor.isNull(_itemKeyIndex)) {
          final String _tmpKey = _cursor.getString(_itemKeyIndex);
          ArrayList<Email> _tmpCollection = _map.get(_tmpKey);
          if (_tmpCollection != null) {
            final Email _item_1;
            _item_1 = new Email();
            final int _tmpEmailId;
            _tmpEmailId = _cursor.getInt(_cursorIndexOfEmailId);
            _item_1.setEmailId(_tmpEmailId);
            final String _tmpValue;
            _tmpValue = _cursor.getString(_cursorIndexOfValue);
            _item_1.setValue(_tmpValue);
            final String _tmpContactId;
            _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
            _item_1.setContactId(_tmpContactId);
            _tmpCollection.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipphoneAscomExampleAndroidEntityPhone(final ArrayMap<String, ArrayList<Phone>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT phone_id,value,contact_id FROM `phone` WHERE phone_id IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    final Cursor _cursor = __db.query(_stmt);
    try {
      final int _itemKeyIndex = _cursor.getColumnIndex("phone_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfPhoneId = _cursor.getColumnIndexOrThrow("phone_id");
      final int _cursorIndexOfValue = _cursor.getColumnIndexOrThrow("value");
      final int _cursorIndexOfContactId = _cursor.getColumnIndexOrThrow("contact_id");
      while(_cursor.moveToNext()) {
        if (!_cursor.isNull(_itemKeyIndex)) {
          final String _tmpKey = _cursor.getString(_itemKeyIndex);
          ArrayList<Phone> _tmpCollection = _map.get(_tmpKey);
          if (_tmpCollection != null) {
            final Phone _item_1;
            _item_1 = new Phone();
            final int _tmpPhoneId;
            _tmpPhoneId = _cursor.getInt(_cursorIndexOfPhoneId);
            _item_1.setPhoneId(_tmpPhoneId);
            final String _tmpValue;
            _tmpValue = _cursor.getString(_cursorIndexOfValue);
            _item_1.setValue(_tmpValue);
            final String _tmpContactId;
            _tmpContactId = _cursor.getString(_cursorIndexOfContactId);
            _item_1.setContactId(_tmpContactId);
            _tmpCollection.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
