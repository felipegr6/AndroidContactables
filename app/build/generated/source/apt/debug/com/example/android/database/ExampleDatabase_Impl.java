package com.example.android.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import com.example.android.DAO.ContactDao;
import com.example.android.DAO.ContactDao_Impl;
import com.example.android.DAO.DeltaDao;
import com.example.android.DAO.DeltaDao_Impl;
import com.example.android.DAO.EmailDao;
import com.example.android.DAO.EmailDao_Impl;
import com.example.android.DAO.PhoneDao;
import com.example.android.DAO.PhoneDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class ExampleDatabase_Impl extends ExampleDatabase {
  private volatile ContactDao _contactDao;

  private volatile EmailDao _emailDao;

  private volatile PhoneDao _phoneDao;

  private volatile DeltaDao _deltaDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `contact` (`contact_id` TEXT NOT NULL, `name` TEXT, `external_id` TEXT, PRIMARY KEY(`contact_id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `phone` (`value` TEXT NOT NULL, `contact_id` TEXT, PRIMARY KEY(`value`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `email` (`value` TEXT NOT NULL, `contact_id` TEXT, PRIMARY KEY(`value`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `delta` (`delta` INTEGER NOT NULL, PRIMARY KEY(`delta`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"1df4ad26c7c5c6f7acdc6fcc8e5046e5\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `contact`");
        _db.execSQL("DROP TABLE IF EXISTS `phone`");
        _db.execSQL("DROP TABLE IF EXISTS `email`");
        _db.execSQL("DROP TABLE IF EXISTS `delta`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsContact = new HashMap<String, TableInfo.Column>(3);
        _columnsContact.put("contact_id", new TableInfo.Column("contact_id", "TEXT", true, 1));
        _columnsContact.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsContact.put("external_id", new TableInfo.Column("external_id", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysContact = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesContact = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoContact = new TableInfo("contact", _columnsContact, _foreignKeysContact, _indicesContact);
        final TableInfo _existingContact = TableInfo.read(_db, "contact");
        if (! _infoContact.equals(_existingContact)) {
          throw new IllegalStateException("Migration didn't properly handle contact(com.example.android.entity.Contact).\n"
                  + " Expected:\n" + _infoContact + "\n"
                  + " Found:\n" + _existingContact);
        }
        final HashMap<String, TableInfo.Column> _columnsPhone = new HashMap<String, TableInfo.Column>(2);
        _columnsPhone.put("value", new TableInfo.Column("value", "TEXT", true, 1));
        _columnsPhone.put("contact_id", new TableInfo.Column("contact_id", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPhone = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPhone = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPhone = new TableInfo("phone", _columnsPhone, _foreignKeysPhone, _indicesPhone);
        final TableInfo _existingPhone = TableInfo.read(_db, "phone");
        if (! _infoPhone.equals(_existingPhone)) {
          throw new IllegalStateException("Migration didn't properly handle phone(com.example.android.entity.Phone).\n"
                  + " Expected:\n" + _infoPhone + "\n"
                  + " Found:\n" + _existingPhone);
        }
        final HashMap<String, TableInfo.Column> _columnsEmail = new HashMap<String, TableInfo.Column>(2);
        _columnsEmail.put("value", new TableInfo.Column("value", "TEXT", true, 1));
        _columnsEmail.put("contact_id", new TableInfo.Column("contact_id", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmail = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmail = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmail = new TableInfo("email", _columnsEmail, _foreignKeysEmail, _indicesEmail);
        final TableInfo _existingEmail = TableInfo.read(_db, "email");
        if (! _infoEmail.equals(_existingEmail)) {
          throw new IllegalStateException("Migration didn't properly handle email(com.example.android.entity.Email).\n"
                  + " Expected:\n" + _infoEmail + "\n"
                  + " Found:\n" + _existingEmail);
        }
        final HashMap<String, TableInfo.Column> _columnsDelta = new HashMap<String, TableInfo.Column>(1);
        _columnsDelta.put("delta", new TableInfo.Column("delta", "INTEGER", true, 1));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDelta = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDelta = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDelta = new TableInfo("delta", _columnsDelta, _foreignKeysDelta, _indicesDelta);
        final TableInfo _existingDelta = TableInfo.read(_db, "delta");
        if (! _infoDelta.equals(_existingDelta)) {
          throw new IllegalStateException("Migration didn't properly handle delta(com.example.android.entity.Delta).\n"
                  + " Expected:\n" + _infoDelta + "\n"
                  + " Found:\n" + _existingDelta);
        }
      }
    }, "1df4ad26c7c5c6f7acdc6fcc8e5046e5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "contact","phone","email","delta");
  }

  @Override
  public ContactDao contactDao() {
    if (_contactDao != null) {
      return _contactDao;
    } else {
      synchronized(this) {
        if(_contactDao == null) {
          _contactDao = new ContactDao_Impl(this);
        }
        return _contactDao;
      }
    }
  }

  @Override
  public EmailDao emailDao() {
    if (_emailDao != null) {
      return _emailDao;
    } else {
      synchronized(this) {
        if(_emailDao == null) {
          _emailDao = new EmailDao_Impl(this);
        }
        return _emailDao;
      }
    }
  }

  @Override
  public PhoneDao phoneDao() {
    if (_phoneDao != null) {
      return _phoneDao;
    } else {
      synchronized(this) {
        if(_phoneDao == null) {
          _phoneDao = new PhoneDao_Impl(this);
        }
        return _phoneDao;
      }
    }
  }

  @Override
  public DeltaDao deltaDao() {
    if (_deltaDao != null) {
      return _deltaDao;
    } else {
      synchronized(this) {
        if(_deltaDao == null) {
          _deltaDao = new DeltaDao_Impl(this);
        }
        return _deltaDao;
      }
    }
  }
}
