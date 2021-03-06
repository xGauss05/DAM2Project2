package com.stucom.jcacay.dam2project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.stucom.jcacay.dam2project.model.Token;

import java.util.HashMap;
import java.util.Map;

public class RegisterCodeActivity extends AppCompatActivity {
    EditText edCode;
    Button btnRegCode;
    String regEmail;
    Token token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);
        regEmail = getIntent().getStringExtra("regEmail");
        edCode = findViewById(R.id.regCode);
        btnRegCode = findViewById(R.id.btnRegCode);
        btnRegCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        token = new Token();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("asd", "onPause() RegisterCode Activity");
        token.saveToPrefs(this);
        super.onPause();
    }

    /**
     * Peticion a la API para registrar el usuario con codigo incluido
     */
    protected void registerUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://api.flx.cat/dam2game/register";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        Gson gson = new Gson();
                        token = gson.fromJson(response, Token.class);
                        Intent intent = new Intent(RegisterCodeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asd", "ERROR: " + error.getMessage());
                        Context context = getApplicationContext();
                        CharSequence text = "ERROR CODE !";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", regEmail);
                params.put("verify", edCode.getText().toString());
                return params;
            }
        };
        queue.add(request);
    }
}
