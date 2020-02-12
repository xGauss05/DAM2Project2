package com.stucom.jcacay.dam2project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stucom.jcacay.dam2project.model.Token;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String token, email;
    Token tokenObject;

    private static final int[] BUTTONS_ID = new int[]{
            R.id.btnPlay, R.id.btnRanking, R.id.btnSettings, R.id.btnAbout
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("asd", "onCreate() Main Activity");
        setContentView(R.layout.activity_main);
        for (int id : BUTTONS_ID) {
            Button button = findViewById(id);
            button.setOnClickListener(this);
        }
        tokenObject = new Token();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asd", "onResume() Main Activity");
        tokenObject.loadFromPrefs(this);
        token = tokenObject.getData();
        System.out.println("asd onResume() Token: " + token);
    }

    @Override
    public void onPause() {
        Log.d("asd", "onPause() Main Activity");
        tokenObject.setData(token);
        tokenObject.saveToPrefs(this);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        Log.d("asd", "asdasd"+token);
        switch (view.getId()) {
            case R.id.btnPlay:
                intent = new Intent(MainActivity.this, PlayActivity.class);
                break;
            case R.id.btnRanking:
                if (token.equalsIgnoreCase("")) {
                    intent = new Intent(MainActivity.this, RegisterEmailActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, RankingActivity.class);
                }
                break;
            case R.id.btnSettings:
                if (token.equalsIgnoreCase("")) {
                    intent = new Intent(MainActivity.this, RegisterEmailActivity.class);

                } else {
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                }
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
