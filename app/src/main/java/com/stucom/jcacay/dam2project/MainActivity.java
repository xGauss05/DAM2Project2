package com.stucom.jcacay.dam2project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stucom.jcacay.dam2project.model.Player;
import com.stucom.jcacay.dam2project.model.Token;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String token = "", email = "";
    Token tokenObject;

    private static final int[] BUTTONS_ID = new int[]{
            R.id.btnPlay, R.id.btnRanking, R.id.btnSettings, R.id.btnAbout
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("asd", "onCreate() Main Activity");
        setContentView(R.layout.activity_main);
        Intent intentRegister = getIntent();
        token = intentRegister.getStringExtra("token");
        email = intentRegister.getStringExtra("email");

        if (token == null) {
            Intent intent = new Intent(MainActivity.this, RegisterEmailActivity.class);
            startActivity(intent);
        }
        for (int id : BUTTONS_ID) {
            Button button = findViewById(id);
            button.setOnClickListener(this);
        }
        tokenObject = new Token();
        tokenObject.setData(token);
        tokenObject.saveToPrefs(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asd", "onResume() Main Activity");
        tokenObject.loadFromPrefs(this);
        token = tokenObject.getData();
    }

    @Override
    public void onPause() {
        Log.d("asd", "onPause() Main Activity");
        tokenObject.setData(token);
        tokenObject.saveToPrefs(this);
        super.onPause();
    }

    public void saveToPrefs(Context context) {
        SharedPreferences shPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shPrefs.edit();
        prefsEditor.putString("token", token);
        prefsEditor.putString("email", email);
        prefsEditor.apply();
    }

    public void loadFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.token = prefs.getString("token", "");
        this.email = prefs.getString("email", "");
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnPlay:
                intent = new Intent(MainActivity.this, PlayActivity.class);
                break;
            case R.id.btnRanking:

                intent = new Intent(MainActivity.this, RankingActivity.class);
                break;
            case R.id.btnSettings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                break;
            case R.id.btnAbout:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;
        }
        if (intent == null) {
            return;
        }
        intent.putExtra("token", tokenObject.getData());
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
