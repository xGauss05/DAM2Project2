package com.stucom.jcacay.dam2project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.stucom.jcacay.dam2project.model.PlayerList;
import com.stucom.jcacay.dam2project.model.Token;

import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("asd", "onCreate() Ranking Activity");
        setContentView(R.layout.activity_ranking);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        token = new Token();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asd", "onResume() Ranking Activity");
        token.loadFromPrefs(this);
        downloadUsers();
    }

    @Override
    public void onPause() {
        Log.d("asd", "onPause() Ranking Activity");
        token.saveToPrefs(this);
        super.onPause();
    }

    protected void downloadUsers() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.flx.cat/dam2game/ranking/?token=" + token.getData();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        Gson gson = new Gson();
                        PlayerList playerList = gson.fromJson(response, PlayerList.class);
                        //for (Player player : playerList.getData()) {
                        //    Log.d("asd", "Player " + player.getName());
                        //}
                        PlayerAdapter adapter = new PlayerAdapter(playerList.getData());
                        recyclerView.setAdapter(adapter);
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

    class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPlayerImage;
            TextView tvPlayerName;
            TextView tvPlayerScore;

            ViewHolder(View view) {
                super(view);
                ivPlayerImage = view.findViewById(R.id.ivPlayerImage);
                tvPlayerName = view.findViewById(R.id.tvPlayerName);
                tvPlayerScore = view.findViewById(R.id.tvPlayerScore);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Player player = players.get(position);
                        Intent intent = new Intent(RankingActivity.this, PlayerDetailActivity.class);
                        intent.putExtra("id", player.getId());
                        startActivity(intent);
                    }
                });
            }
        }

        private List<Player> players;

        PlayerAdapter(List<Player> players) {
            super();
            this.players = players;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("asd", "onCreateViewHolder()");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d("asd", "onBindViewHolder() : " + position);
            Collections.sort(players);
            Player player = players.get(position);
            holder.tvPlayerName.setText(player.getName());
            holder.tvPlayerScore.setText((Integer.toString(player.getTotalScore())));
            Picasso.get().load(player.getImage()).into(holder.ivPlayerImage);
        }

        @Override
        public int getItemCount() {
            return players.size();
        }
    }
}
