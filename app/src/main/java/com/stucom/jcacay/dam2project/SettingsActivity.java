package com.stucom.jcacay.dam2project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.stucom.jcacay.dam2project.model.Player;

import java.io.File;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edEmail;
    ImageView imAvatar;
    Uri photoURI;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d("asd", "onCreate() Settings Activity");
        // Capture needed layout views
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        imAvatar = findViewById(R.id.imAvatar);
        // All buttons to this class (see implements in the class' declaration)
        findViewById(R.id.btnGallery).setOnClickListener(this);
        findViewById(R.id.btnCamera).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
        // Instantiate player object
        player = new Player();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asd", "onResume() Settings Activity");
        // Load player info from SharedPrefs
        player.loadFromPrefs(this);
        edName.setText(player.getName());
        edEmail.setText(player.getEmail());
        setAvatarImage(player.getImage(), false);
    }

    @Override
    public void onPause() {
        // Save player info from SharedPrefs (save changes on name and email only)
        Log.d("asd", "onPause() Settings Activity");
        player.setName(edName.getText().toString());
        player.setEmail(edEmail.getText().toString());
        player.saveToPrefs(this);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        // All buttons come here, so we decide based on their ids
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

    // Needed for onActivityResult()
    private static final int AVATAR_FROM_GALLERY = 1;
    private static final int AVATAR_FROM_CAMERA = 2;

    public void deleteAvatar() {
        // In this case simply clear the image by pasing null
        setAvatarImage(null, true);
    }

    public void getAvatarFromGallery() {
        // Call the Open Document intent searching for images
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_FROM_GALLERY);
    }

    public void getAvatarFromCamera() {
        // Prepare for storage (see FileProvider background documentation)
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Always this path
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
        // Pass the photo path to the Intent and start it
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
        // Skip cancels & errors
        if (resultCode != RESULT_OK) return;

        if (requestCode == AVATAR_FROM_GALLERY) {
            // coming from gallery, the URI is in the intent's data
            photoURI = data.getData();
        }
        // if camera, no action needed, as we set the URI when the intent was created

        // now set the avatar
        String avatar = (photoURI == null) ? null : photoURI.toString();
        setAvatarImage(avatar, true);
    }

    public void setAvatarImage(String avatar, boolean saveToSharedPreferences) {
        Log.d("asd", "Avatar = " + avatar);
        if (avatar == null) {
            // if null, set the default "unknown" avatar picture
            imAvatar.setImageResource(R.drawable.unknown);
        } else {
            // the URI must be valid, so we set it to the ImageView
            Uri uri = Uri.parse(avatar);
            imAvatar.setImageURI(uri);
        }
        if (!saveToSharedPreferences) return;
        // comply if a save to prefs was requested
        player.setImage(avatar);
        player.saveToPrefs(this);
    }
}
