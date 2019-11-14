package com.example.cloneclassifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getName();

    private ImageClassifier classifier;
    Uri Image;
    Uri cropImage;

    private ImageView imageView;
    private TextView text_rank1;
    private TextView text_rank2;
    private TextView text_rank3;
    private TextView text_timeCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        InitializeView();

        Intent imgIntent = getIntent();
        Image = imgIntent.getExtras().getParcelable("image");
        cropImage = imgIntent.getExtras().getParcelable("cropImage");
        imageView.setImageURI(cropImage);

        // Load Model Preference
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String modelName = pref.getString("modelName", "");

        try {
            classifier = new ImageClassifier(this, modelName);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize an image classifier.");
        }

        String[] textResult = classifyImage();
        text_rank1.setText(textResult[1]);
        text_rank2.setText(textResult[2]);
        text_rank3.setText(textResult[3]);
        text_timeCost.setText(textResult[0]);
    }

    private void InitializeView() {
        imageView = (ImageView) findViewById(R.id.resultImage);
        text_rank1 = (TextView) findViewById(R.id.text_rank1);
        text_rank2 = (TextView) findViewById(R.id.text_rank2);
        text_rank3 = (TextView) findViewById(R.id.text_rank3);
        text_timeCost = (TextView) findViewById(R.id.text_timeCost);
    }

    private String[] classifyImage() {
        if (classifier == null) {
            Toast.makeText(this, "Uninitialized Classifier.", Toast.LENGTH_SHORT).show();
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        String textToShow = classifier.classifyFrame(bitmap);
        bitmap.recycle();
//        Toast.makeText(this, textToShow, Toast.LENGTH_SHORT).show();
        String[] textResult = textToShow.split("\n");    // [0] : 실행시간, [1]~[3] : rank

        return textResult;
    }
}
