package com.arit.demo.localstorage;

import android.app.Application;
import android.content.SharedPreferences;

import com.arit.demo.localstorage.model.User;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    private RealmAsyncTask task;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("displayname", "John Doe");
        editor.putBoolean("safemode", true);
        editor.apply();

        Realm.init(this);

        RealmConfiguration config  = new RealmConfiguration.Builder()
                                        .name("Local.realm").deleteRealmIfMigrationNeeded()
                                        .build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();

        //User user = realm.createObject(User.class); // Managed Object
        final User user = new User(); // UnManaged Object
        user.setFirstname("John");
        user.setLastname("Doe");

        /*realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(user);
            }
        });*/

        // 1) กำลัง process อยู่แต่ว่ามีสายเข้า ทำให้ process ค้าง เลยต้องทำการเคลียร์ของ async ทิ้ง มี call back 2ตัว onsuccess,onerror ทำอะไรต่อ
        task = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(user);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // do something when finished task
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // do something when finished task
            }
        });

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // 2) Kill task
        Realm.getDefaultInstance().close();
        this.task.cancel();
    }



}
