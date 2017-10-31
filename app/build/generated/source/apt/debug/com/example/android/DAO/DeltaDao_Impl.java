package com.example.android.DAO;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.android.entity.Delta;
import java.lang.Override;
import java.lang.String;

public class DeltaDao_Impl implements DeltaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfDelta;

  public DeltaDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDelta = new EntityInsertionAdapter<Delta>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `delta`(`delta`) VALUES (?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Delta value) {
        stmt.bindLong(1, value.getDelta());
      }
    };
  }

  @Override
  public void insert(Delta d) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfDelta.insert(d);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Delta getLastDate() {
    final String _sql = "SELECT * FROM delta ORDER BY delta DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfDelta = _cursor.getColumnIndexOrThrow("delta");
      final Delta _result;
      if(_cursor.moveToFirst()) {
        _result = new Delta();
        final long _tmpDelta;
        _tmpDelta = _cursor.getLong(_cursorIndexOfDelta);
        _result.setDelta(_tmpDelta);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
