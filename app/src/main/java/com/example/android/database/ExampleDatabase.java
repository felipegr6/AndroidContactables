package com.example.android.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.DAO.ContactDao;
import com.example.android.DAO.DeltaDao;
import com.example.android.DAO.EmailDao;
import com.example.android.DAO.PhoneDao;
import com.example.android.entity.Contact;
import com.example.android.entity.Delta;
import com.example.android.entity.Email;
import com.example.android.entity.Phone;

@Database(entities = {Contact.class, Phone.class, Email.class, Delta.class}, version = 1)
public abstract class ExampleDatabase extends RoomDatabase {

    private static ExampleDatabase INSTANCE;

    public static void init(Context context) {
        if (INSTANCE == null)
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            ExampleDatabase.class,
                            "example_db")
                            .build();
    }

    public static ExampleDatabase getDatabase() {
        if (INSTANCE == null) {
            throw new RuntimeException("Calls init method before.");
        }
        return INSTANCE;
    }


    public abstract ContactDao contactDao();

    public abstract EmailDao emailDao();

    public abstract PhoneDao phoneDao();

    public abstract DeltaDao deltaDao();
}
