package com.stucom.jcacay.dam2project;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stucom.jcacay.dam2project.model.Player;
import com.stucom.jcacay.dam2project.model.Token;

import java.util.HashMap;
import java.util.Map;

public class PlayActivity extends AppCompatActivity {
    Token token;
    Player player;
    EditText edLevel, edScore;
    Button btnScore;
    TextView tvAlertScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        token = new Token();
        player = new Player();

        edLevel = findViewById(R.id.edLevel);
        edScore = findViewById(R.id.edScore);

        edLevel.setInputType(InputType.TYPE_CLASS_NUMBER);
        edLevel.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });

        edScore.setInputType(InputType.TYPE_CLASS_NUMBER);
        edScore.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

        tvAlertScore = findViewById(R.id.tvAlertScore);
        tvAlertScore.setVisibility(View.INVISIBLE);

        btnScore = findViewById(R.id.btnScore);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postScore();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("asd", "onResume() PlayActivity");
        super.onResume();
        player.loadFromPrefs(this);
        token.loadFromPrefs(this);
    }

    @Override
    protected void onPause() {
        Log.d("asd", "onPause() PlayActivity");
        super.onPause();
    }

    protected void postScore() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://api.flx.cat/dam2game/user/score";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        tvAlertScore.setVisibility(View.VISIBLE);
                        CountDownTimer timer = new CountDownTimer(2000, 1000) {
                            public void onTick(long asd) {
                            }
                            public void onFinish() {
                                tvAlertScore.setVisibility(View.INVISIBLE);
                            }
                        }.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asd", "ERROR: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token.getData());
                params.put("level", edLevel.getText().toString());
                params.put("score", edScore.getText().toString());
                return params;
            }
        };
        queue.add(request);
    }
}

