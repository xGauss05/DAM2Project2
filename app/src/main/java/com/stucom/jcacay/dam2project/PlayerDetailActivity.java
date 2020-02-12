package com.stucom.jcacay.dam2project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stucom.jcacay.dam2project.model.Player;
import com.stucom.jcacay.dam2project.model.PlayerDetail;
import com.stucom.jcacay.dam2project.model.Token;

public class PlayerDetailActivity extends AppCompatActivity {
    TextView tvPlayerNameDet;
    ImageView ivPlayerImgDet;
    Button btnMessage;
    EditText edMessage;
    Player player;
    Token token;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);
        tvPlayerNameDet = findViewById(R.id.tvPlayerNameDet);
        ivPlayerImgDet = findViewById(R.id.ivPlayerImgDet);
        btnMessage = findViewById(R.id.btnMessage);
        edMessage = findViewById(R.id.edMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        id = getIntent().getIntExtra("id", 0);
        token = new Token();
    }

    protected void sendMessage() {
        super.onResume();
        token.loadFromPrefs(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.flx.cat/dam2game/message/" + id + "?token=" + token.getData() + "&text=" + edMessage.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        // mostrar mensaje enviado correcto
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asd", "ERROR: " + error.getMessage());
                    }
                }
        );
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        token.loadFromPrefs(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.flx.cat/dam2game/user/" + id + "?token=" + token.getData();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        Gson gson = new Gson();
                        PlayerDetail detail = gson.fromJson(response, PlayerDetail.class);
                        player = detail.getPlayer();
                        tvPlayerNameDet.setText(player.getName());
                        Picasso.get().load(player.getImage()).into(ivPlayerImgDet);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asd", "ERROR: " + error.getMessage());
                    }
                }
        );
        queue.add(request);
    }
}
