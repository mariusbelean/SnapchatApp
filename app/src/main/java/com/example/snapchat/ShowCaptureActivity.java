package com.example.snapchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.snapchat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ShowCaptureActivity extends AppCompatActivity {

    String Uid;
    Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);

        Bundle extras = getIntent().getExtras();
        //assert extras != null;
        byte[] b = extras.getByteArray("capture");

        if(b!=null){
            ImageView image = findViewById(R.id.imageCaptured);

            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            rotateBitmap =  rotate (decodeBitmap);

            image.setImageBitmap(rotateBitmap);
        }

        Uid = FirebaseAuth.getInstance().getUid();

        Button mStory = findViewById(R.id.story);
        mStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStories();
            }
        });

    }

    private void saveToStories() {
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("story");
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dataToUpload = baos.toByteArray();
        UploadTask uploadTask = filePath.putBytes(dataToUpload);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> imageUrl = taskSnapshot.getStorage().getDownloadUrl(); //need to check if this works!!!!!!!!!

                Long currentTimeStamp = System.currentTimeMillis();
                Long endTimeStamp = currentTimeStamp + (24*60*60*1000);

                Map<String, Object> mapToUpload = new HashMap<>();
                mapToUpload.put("imageUrl", imageUrl.toString());
                mapToUpload.put("timestampBeg", currentTimeStamp);
                mapToUpload.put("timestampEnd", endTimeStamp);

                userStoryDb.child(key).setValue(mapToUpload);

                finish();
                return;

            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });
    }

    private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodeBitmap, 0, 0, width, height, matrix, true);
    }
}
