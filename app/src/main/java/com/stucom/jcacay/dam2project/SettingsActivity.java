package com.stucom.jcacay.dam2project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stucom.jcacay.dam2project.model.Player;
import com.stucom.jcacay.dam2project.model.Ranking;
import com.stucom.jcacay.dam2project.model.Token;
import com.stucom.jcacay.dam2project.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edName, edEmail;
    ImageView imAvatar;
    Uri photoURI;
    Player player;
    Token token;
    Bitmap bitmap;
    Button btnDeleteAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d("asd", "onCreate() Settings Activity");
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edEmail.setEnabled(false);
        btnDeleteAcc = findViewById(R.id.btnDeleteAcc);
        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UnregisterActivity.class);
                startActivity(intent);
            }
        });
        imAvatar = findViewById(R.id.imAvatar);
        findViewById(R.id.btnGallery).setOnClickListener(this);
        findViewById(R.id.btnCamera).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
        player = new Player();
        token = new Token();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asd", "onResume() Settings Activity");
        token.loadFromPrefs(this);
        player.loadFromPrefs(this);
        edEmail.setText(player.getEmail());
        
        if (player.getImage() != null) {
            Picasso.get().load(player.getImage()).into(imAvatar);
        } else {
            setAvatarImage(player.getImage(), false);
        }

        if (player.getName().equalsIgnoreCase("") || player.getName() == null) {
            edName.setText("user");
        } else {
            edName.setText(player.getName());
        }
    }

    @Override
    public void onPause() {
        Log.d("asd", "onPause() Settings Activity");
        player.setName(edName.getText().toString());
        player.setEmail(edEmail.getText().toString());
        player.saveToPrefs(this);
        updateUser();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                deleteAvatar();
                break;
            case R.id.btnCamera:
                getAvatarFromCamera();
                break;
            case R.id.btnGallery:
                getAvatarFromGallery();
                break;
        }
    }

    protected void getUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.flx.cat/dam2game/user/?token=" + token.getData();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd", response);
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        player = user.getData();
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

    protected void updateUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.flx.cat/dam2game/user";
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("asd updateUser", response);
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
                params.put("name", player.getName());
                bitmap = ((BitmapDrawable) imAvatar.getDrawable()).getBitmap();
                params.put("image", encodeImage(bitmap));
                return params;
            }
        };
        queue.add(request);
    }

    private static final int AVATAR_FROM_GALLERY = 1;
    private static final int AVATAR_FROM_CAMERA = 2;

    public void deleteAvatar() {
        setAvatarImage(null, true);
    }

    public void getAvatarFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_FROM_GALLERY);
    }

    public void getAvatarFromCamera() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photo = new File(storageDir, "photo.jpg");
        try {
            boolean ok = photo.createNewFile();
            if (ok) {
                Log.d("asd", "Overwriting image");
            }
        } catch (IOException e) {
            Log.e("asd", "Error creating image file " + photo);
            return;
        }
        Log.d("asd", "Writing photo to " + photo);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoURI = FileProvider.getUriForFile(this, "com.stucom.jcacay.fileProvider", photo);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, AVATAR_FROM_CAMERA);
        } catch (IllegalArgumentException e) {
            Log.e("asd", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == AVATAR_FROM_GALLERY) {
            photoURI = data.getData();
        }
        String avatar = (photoURI == null) ? null : photoURI.toString();
        setAvatarImage(avatar, true);
    }

    public void setAvatarImage(String avatar, boolean saveToSharedPreferences) {
        Log.d("asd", "Avatar = " + avatar);
        if (avatar == null) {
            imAvatar.setImageResource(R.drawable.unknown);
        } else {
            Uri uri = Uri.parse(avatar);
            Log.d("asd Uri", uri.toString());
            imAvatar.setImageURI(uri);
        }
        if (!saveToSharedPreferences) {
            return;
        }

        player.setImage(avatar);
        player.saveToPrefs(this);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedString;
    }
}
