package com.arit.demo.localstorage;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.arit.demo.localstorage.model.User;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private String displayname;
    private boolean safemode;

    @BindView(R.id.tvShowPreference)
    TextView tvShowPref;

    @BindView(R.id.tvShowRealmData)
    TextView tvShowRealmData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("displayname", "John Doe");
        editor.putBoolean("safemode", true);
        editor.apply();*/
    }

    @OnClick(R.id.btnGetPreference)
    public void showPref(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        this.displayname = pref.getString("displayname", "default value");
        this.tvShowPref.setText(this.displayname);

       // this.safemode = pref.getBoolean("safemode", false);
    }

    @OnClick(R.id.btnRealmData)
    public void showRealmData(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).findFirst();
                tvShowRealmData.setText(user.getFirstname());
            }
        });
    }

}
