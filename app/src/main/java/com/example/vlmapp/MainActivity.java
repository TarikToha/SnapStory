package com.example.vlmapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * MainActivity that captures an image from the camera, sends it to Google Gemini API,
 * and displays a 100-word story generated based on the image content.
 */
public class MainActivity extends AppCompatActivity {

    private GenerativeModelFutures model;

    private ImageView imageView;
    private TextView llmText, geminiOut;
    private ProgressBar llmProgressBar;
    private Button cameraButton, nextButton;
    private String summary = "";

    /**
     * Initializes the UI and the Gemini model client.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GenerativeModel gm = new GenerativeModel("gemini-2.0-flash", BuildConfig.GEMINI_KEY);
        model = GenerativeModelFutures.from(gm);

        cameraButton = findViewById(R.id.cameraButton);
        imageView = findViewById(R.id.imageView);
        llmText = findViewById(R.id.llmText);
        llmProgressBar = findViewById(R.id.llmProgressBar);
        geminiOut = findViewById(R.id.geminiOut);
        nextButton = findViewById(R.id.nextButton);
    }

    /**
     * Launches the camera intent to capture a photo.
     */
    public void startCamera(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, 1);
    }

    /**
     * Handles the result from the camera activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");

            imageView.setImageBitmap(image);
            cameraButton.setVisibility(View.GONE);
            llmText.setVisibility(View.VISIBLE);
            llmProgressBar.setVisibility(View.VISIBLE);

            getStory(image);
            Toast.makeText(this, "Generating a story...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends the captured image to Gemini API and retrieves a generated story.
     */
    private void getStory(Bitmap image) {

        Content content = new Content.Builder()
                .addImage(image)
                .addText("Generate a story from the image in a few sentences, without introductory text.")
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Executor executor = Executors.newSingleThreadExecutor();
        Futures.addCallback(response, new FutureCallback<>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();

                runOnUiThread(() -> {
                    llmProgressBar.setVisibility(View.GONE);
                    geminiOut.setText(resultText);
                    cameraButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                });
                Log.d("Gemini", "Result: " + resultText);
                summary = resultText;
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e("Gemini", "Error: " + t.getMessage());
            }
        }, executor);
    }


    public void nextActivity(View view) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("summary", summary);
        startActivity(intent);
    }

}