package com.general.code;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Author: zml
 * Date  : 2019/1/4 - 16:33
 **/
public class ChoosePictureActivity extends Activity implements View.OnClickListener{
    private static final String TAG = ChoosePictureActivity.class.getSimpleName();

    ImageView chosenImageView;
    Button choosePicture;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_picture);
        chosenImageView = findViewById(R.id.chosen_picture_iv);
        choosePicture = findViewById(R.id.choose_picture_button);
        choosePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if (resultCode == RESULT_OK){
            Uri imageUri = intent.getData();
            Display currentDisplay = getWindowManager().getDefaultDisplay();
            int dw = currentDisplay.getWidth();
            int dh = currentDisplay.getHeight()/2-100;

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}






















