package com.stucom.jcacay.dam2project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class UnregisterActivity extends AppCompatActivity {
    Player player;
    Token token;
    Button btnDeleteY, btnDeleteN, btnFullDeleteY, btnFullDeleteN;
    TextView tvAskFullUnregister;
    boolean fullDelete = false, deleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregister);

        token = new Token();
        player = new Player();

        assignComponents();

        tvAskFullUnregister.setVisibility(View.INVISIBLE);
        btnFullDeleteY.setVisibility(View.INVISIBLE);
        btnFullDeleteN.setVisibility(View.INVISIBLE);

        btnDeleteY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFullDeleteY.setVisibility(View.VISIBLE);
                btnFullDeleteN.setVisibility(View.VISIBLE);
                tvAskFullUnregister.setVisibility(View.VISIBLE);
            }
        });

        btnDeleteN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainActivity();
            }
        });

        btnFullDeleteY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullDelete = true;
                deleted = true;
                unregisterUser();
                returnToMainActivity();

            }
        });

        btnFullDeleteN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullDelete = false;
                deleted = true;
                unregisterUser();
                returnToMainActivity();
            }
        });
    }

    protected void returnToMainActivity() {
        Intent intent = new Intent(UnregisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    protected void assignComponents() {
        btnDeleteY = findViewById(R.id.btnDeleteY);
        btnDeleteN = findViewById(R.id.btnDeleteN);
        btnFullDeleteN = findViewById(R.id.btnFullDeleteN);
        btnFullDeleteY = findViewById(R.id.btnFullDeleteY);
        tvAskFullUnregister = findViewById(R.id.tvAskFullUnregister);
    }

    protected void unregisterUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://api.flx.cat/dam2game/unregister";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
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
                params.put("must_delete", String.valueOf(fullDelete));
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("asd", "onResume() Unregister Activity");
        fullDelete = false;
        deleted = false;
        player.loadFromPrefs(this);
        token.loadFromPrefs(this);
    }

    @Override
    protected void onPause() {
        Log.d("asd", "onPause() Unregister Activity");
        if (deleted) {
            player = new Player();
            token = new Token();
        }
        player.saveToPrefs(this);
        token.saveToPrefs(this);

        super.onPause();
    }


}