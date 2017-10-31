package com.example.android.async;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.database.ExampleDatabase;
import com.example.android.entity.Contact;
import com.example.android.entity.Delta;
import com.example.android.entity.Email;
import com.example.android.entity.Phone;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.android.basiccontactables.ContactablesLoaderCallbacks.TAG;

public class AsyncOperations {

    private static long l;

    private AsyncOperations() {

    }

    public static Observable<String> getContacts() {
        return Observable
                .defer(new Callable<ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> call() throws Exception {
                        StringBuilder sb = new StringBuilder();
                        List<Contact> load = ExampleDatabase.getDatabase().contactDao().query();
                        for (Contact c : load) {
                            sb.append(c.getName());
                            sb.append(String.format(" %s", c.getExternalId()));
                            List<Email> emailsFromContact = ExampleDatabase.getDatabase().emailDao().getEmailsFromContact(c.getContactId());
                            List<Phone> phonesFromContact = ExampleDatabase.getDatabase().phoneDao().getPhonesFromContact(c.getContactId());
                            for (Email e : emailsFromContact) {
                                sb.append("\n\t");
                                sb.append(e.getValue());
                            }
                            for (Phone p : phonesFromContact) {
                                sb.append("\n\t");
                                sb.append(p.getValue());
                            }
                            sb.append("\n");
                        }
                        return Observable.just(sb.toString());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void persistContacts(final Cursor cursor) {
        l = System.currentTimeMillis();

        Observable<PlainContact> plainContactObservable = getPlainContactObservable(cursor);

        plainContactObservable.flatMap(new Function<PlainContact, ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> apply(@NonNull PlainContact plainContact) throws Exception {
                return Observable.just(plainContact)
                        .subscribeOn(Schedulers.computation())
                        .map(new Function<PlainContact, Boolean>() {
                            @Override
                            public Boolean apply(@NonNull PlainContact plainContact) throws Exception {
                                return persistData(plainContact);
                            }
                        });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        Log.d(TAG, String.valueOf(aBoolean));
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.d(TAG, "onError", throwable);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                        Log.d(TAG, String.valueOf(System.currentTimeMillis() - l));
                    }
                });
    }

    public static Observable<PlainContact> getPlainContactObservable(final Cursor cursor) {
        return Observable
                .create(new ObservableOnSubscribe<PlainContact>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<PlainContact> observableEmitter) throws Exception {
                        if (cursor.getCount() == 0) {
                            observableEmitter.onError(new RuntimeException("No items"));
                            return;
                        }

                        Delta lastDate = ExampleDatabase.getDatabase().deltaDao().getLastDate();

                        long delta = 0L;
                        if (lastDate != null)
                            delta = lastDate.getDelta();


                        int phoneColumnIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
                        int emailColumnIndex = cursor.getColumnIndex(CommonDataKinds.Email.ADDRESS);
                        int nameColumnIndex = cursor.getColumnIndex(CommonDataKinds.Contactables.DISPLAY_NAME);
                        int lookupColumnIndex = cursor.getColumnIndex(CommonDataKinds.Contactables.LOOKUP_KEY);
                        int typeColumnIndex = cursor.getColumnIndex(CommonDataKinds.Contactables.MIMETYPE);
                        int deltaColumnIndex = cursor.getColumnIndex(CommonDataKinds.Contactables.CONTACT_LAST_UPDATED_TIMESTAMP);
                        int idColumnIndex = cursor.getColumnIndex(CommonDataKinds.Contactables.CONTACT_ID);

                        cursor.moveToFirst();

                        do {
                            long deltaContact = Long.parseLong(cursor.getString(deltaColumnIndex));

                            if (deltaContact < delta)
                                continue;

                            PlainContact pc = new PlainContact();

                            Log.d(TAG, cursor.getString(deltaColumnIndex));

                            String currentLookupKey = cursor.getString(lookupColumnIndex);
                            String displayName = cursor.getString(nameColumnIndex);
                            String externalId = cursor.getString(idColumnIndex);

                            pc.setName(displayName);
                            pc.setContactId(currentLookupKey);
                            pc.setExternalId(externalId);

                            String mimeType = cursor.getString(typeColumnIndex);

                            switch (mimeType) {
                                case CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                                    String phoneNumber = formatPhoneNumber(cursor.getString(phoneColumnIndex));
                                    pc.setPhone(phoneNumber);
                                    break;
                                case CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                                    String email = cursor.getString(emailColumnIndex);
                                    pc.setEmail(email);
                                    break;
                            }

                            observableEmitter.onNext(pc);

                        } while (cursor.moveToNext());
                        Delta d = new Delta();
                        d.setDelta(System.currentTimeMillis());
                        ExampleDatabase.getDatabase().deltaDao().insert(d);
                        observableEmitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private static boolean persistData(PlainContact pc) {
        try {
            if (!TextUtils.isEmpty(pc.getName())) {
                Contact c = new Contact();
                c.setContactId(pc.getContactId());
                c.setName(pc.getName());
                c.setExternalId(pc.getExternalId());
                ExampleDatabase.getDatabase().contactDao().insertContact(c);
            }
            if (!TextUtils.isEmpty(pc.getEmail())) {
                Email e = new Email();
                e.setContactId(pc.getContactId());
                e.setValue(pc.getEmail());
                ExampleDatabase.getDatabase().emailDao().insertEmail(e);
            }
            if (!TextUtils.isEmpty(pc.getPhone())) {
                Phone p = new Phone();
                p.setContactId(pc.getContactId());
                p.setValue(pc.getPhone());
                ExampleDatabase.getDatabase().phoneDao().insertPhone(p);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String formatPhoneNumber(String phone) {
        return phone.replace("+", "").replace("-", "").replace(" ", "").trim();
    }

}
