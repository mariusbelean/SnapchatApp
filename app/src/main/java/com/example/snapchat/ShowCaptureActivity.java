package com.example.snapchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.snapchat.R;

public class ShowCaptureActivity extends AppCompatActivity {

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

            Bitmap rotateBitmap =  rotate (decodeBitmap);

            image.setImageBitmap(rotateBitmap);
        }

    }

    private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodeBitmap, 0, 0, width, height, matrix, true);
    }
}
