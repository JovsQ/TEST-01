package com.jovsq.stitch;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LEFT_IMAGE_REQUEST_CODE = 1;
    private static final int RIGHT_IMAGE_REQUEST_CODE = 2;
    private static final String TAG = "STITCHING";

    private ImageView left, right, merged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews(){
        left = (ImageView)findViewById(R.id.iv_left);
        right = (ImageView)findViewById(R.id.iv_right);
        merged = (ImageView)findViewById(R.id.iv_merged);

        left.setOnClickListener(this);
        right.setOnClickListener(this);
        merged.setOnClickListener(this);
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

    private void getBitmap01(Intent data){
        // Let's read picked image data - its URI
        Uri pickedImage = data.getData();
        Log.d(TAG, "URI" + pickedImage);
        // Let's read picked image path using content resolver
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        Log.d(TAG, "image path: " + pickedImage);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        Log.d(TAG,"bitmap: " + bitmap);
        // Do something with the bitmap


        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "on activity result");
        if (resultCode == RESULT_OK && data != null){
            Log.d(TAG, "result ok");

            ImageView view = requestCode == LEFT_IMAGE_REQUEST_CODE ? left : right;

            PicassoSingleton.getInstance().getPicasso().load(data.getData()).fit().into(view, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Success");
                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "error: " + e.getMessage());
                }
            });

//            if (requestCode == LEFT_IMAGE_REQUEST_CODE) {
//                Log.d(TAG, "set image");
//
//                left.setImageBitmap(bitmap);
//            } else if (requestCode == RIGHT_IMAGE_REQUEST_CODE) {
//                right.setImageBitmap(bitmap);
//            }
        }
    }

    private void mergeImage(){
        Bitmap leftBitmap = ((BitmapDrawable)left.getDrawable()).getBitmap();
        Bitmap rightBitmap = ((BitmapDrawable)right.getDrawable()).getBitmap();
        combineImage(leftBitmap, rightBitmap);
    }

    private void combineImage(Bitmap leftBitmap, Bitmap rightBitmap){
        Bitmap bitmapCombined = null;

        int width, height = 0;

        if(leftBitmap.getWidth() > rightBitmap.getWidth()) {
            width = leftBitmap.getWidth() + rightBitmap.getWidth();
            height = leftBitmap.getHeight();
        } else {
            width = rightBitmap.getWidth() + rightBitmap.getWidth();
            height = leftBitmap.getHeight();
        }

        bitmapCombined = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(bitmapCombined);

        comboImage.drawBitmap(leftBitmap, 0f, 0f, null);
        comboImage.drawBitmap(rightBitmap, leftBitmap.getWidth(), 0f, null);

        String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

        File direct = new File(Environment.getExternalStorageDirectory() + "/stitch");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/stitch/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/stitch/"), tmpImg);
        if (file.exists()) {
            file.delete();
        }


        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bitmapCombined.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch(IOException e) {
          Log.e(TAG, "problem combining images", e);
        }

        merged.setImageBitmap(bitmapCombined);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_left:
                getImage(LEFT_IMAGE_REQUEST_CODE);
                break;
            case R.id.iv_right:
                getImage(RIGHT_IMAGE_REQUEST_CODE);
                break;
            case R.id.iv_merged:
                mergeImage();
                break;
        }
    }
}
