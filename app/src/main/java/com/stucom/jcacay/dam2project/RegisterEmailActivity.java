package com.stucom.jcacay.dam2project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.stucom.jcacay.dam2project.model.Player;
import com.stucom.jcacay.dam2project.model.PlayerList;

import java.util.HashMap;
import java.util.Map;

public class RegisterEmailActivity extends AppCompatActivity {
    EditText edEmail;
    Button btnRegEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        edEmail = findViewById(R.id.regEmail);
        btnRegEmail = findViewById(R.id.btnRegEmail);
        btnRegEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEmail();
            }
        });
    }

    protected void postEmail() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Intent intent = new Intent(RegisterEmailActivity.this, RegisterCodeActivity.class);
        String URL = "https://api.flx.cat/dam2game/register";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        intent.putExtra("regEmail", edEmail.getText().toString());
                        startActivity(intent);
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
                params.put("email", edEmail.getText().toString());
                return params;
            }
        };
        queue.add(request);
    }
}
