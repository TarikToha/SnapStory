package com.example.vlmapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private GenerativeModelFutures model;

    private ImageView imageView;
    private TextView llmText;
    private ProgressBar llmProgressBar;
    private TextView geminiOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GenerativeModel gm = new GenerativeModel("gemini-2.0-flash", BuildConfig.GEMINI_KEY);
        model = GenerativeModelFutures.from(gm);

        imageView = findViewById(R.id.imageView);
        llmText = findViewById(R.id.llmText);
        llmProgressBar = findViewById(R.id.llmProgressBar);
        geminiOut = findViewById(R.id.geminiOut);

    }

    public void startCamera(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");

            imageView.setImageBitmap(image);
            llmText.setVisibility(View.VISIBLE);
            llmProgressBar.setVisibility(View.VISIBLE);

            getStory(image);
        }
    }

    private void getStory(Bitmap image) {

        Content content = new Content.Builder()
                .addImage(image)
                .addText("Generate a story from the image in 100 words")
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
                });
                Log.d("Gemini", "Result: " + resultText);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Log.e("Gemini", "Error: " + t.getMessage());
            }
        }, executor);

    }

}