package com.jovsq.stitch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LEFT_IMAGE_REQUEST_CODE = 1;
    private static final int RIGHT_IMAGE_REQUEST_CODE = 2;
    private static final String TAG = "JOVS";

    private ImageView left, right, merged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews(){
        left = findViewById(R.id.iv_left);
        right = findViewById(R.id.iv_right);
        merged = findViewById(R.id.iv_merged);

        left.setOnClickListener(this);
        right.setOnClickListener(this);
    }

    public Bitmap combineImage(){
        return null;
    }

    private void getImage(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == LEFT_IMAGE_REQUEST_CODE) {

       }
       else if (requestCode == RIGHT_IMAGE_REQUEST_CODE) {

       }
    }

    @Override
    public void onClick(View v) {
        if (v == left) {
            Log.d(TAG, "clicked left");
        } else if (v == right) {
            Log.d(TAG, "clicked right ");
        }
    }
}
