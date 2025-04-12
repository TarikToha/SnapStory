package com.example.vlmapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/" + "gemini-2.0-flash-exp-image-generation:generateContent?key=" + BuildConfig.GEMINI_KEY;
    private final ArrayList<String> prompts = new ArrayList<>();

    private Button genButton, backButton;
    private ProgressBar llmProgressBar;
    private TextView genText;
    private ImageView genView;

    private int promptIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        genButton = findViewById(R.id.genButton);
        llmProgressBar = findViewById(R.id.llmProgressBar);
        genView = findViewById(R.id.genView);
        genText = findViewById(R.id.genText);
        backButton = findViewById(R.id.backButton);

        String receivedPrompt = getIntent().getStringExtra("summary");

        String summary;
        if (receivedPrompt != null) {
            Log.d("Prompt Received", receivedPrompt);
            summary = receivedPrompt;
        } else {
            summary = "The morning sun streams through the large window, casting a soft glow on the minimalist living room. A cat, curled up on the rug, watches the world go by outside. Inside, a television sits atop a sleek, low cabinet, its screen displaying a hypnotic checkerboard pattern. The room is peaceful, a serene sanctuary from the outside world. The muted tones and clean lines create a sense of calm and order, inviting relaxation and contemplation. It's a space designed for quiet moments and simple pleasures";
        }
        genText.setText(summary);


        String[] sentences = summary.split("(?<=[.!?])\\s+");
        for (String sentence : sentences) {
            String prompt = sentence.trim();
            Log.d("Prompt", prompt);
            prompts.add(prompt);
        }

        genButton.setVisibility(View.VISIBLE);

    }

    private JSONObject makeJSONBody(String prompt) throws JSONException {
        JSONObject part = new JSONObject().put("text", "Draw this scene: " + prompt);
        JSONArray parts = new JSONArray().put(part);
        JSONObject content = new JSONObject().put("parts", parts);
        JSONArray contents = new JSONArray().put(content);

        JSONObject generationConfig = new JSONObject().put("responseModalities", new JSONArray().put("Text").put("Image"));


        return new JSONObject().put("contents", contents).put("generationConfig", generationConfig);
    }


    private void generateImageFromPrompt(String prompt) {
        try {

            JSONObject requestBody = makeJSONBody(prompt);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GEMINI_URL, requestBody, response -> {
                try {

                    JSONArray partsArray = response.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts");

                    for (int i = 0; i < partsArray.length(); i++) {
                        JSONObject partObj = partsArray.getJSONObject(i);

                        if (partObj.has("text")) {
                            String resultText = partObj.getString("text");
                            genText.setText(resultText);
                        }

                        if (partObj.has("inlineData")) {
                            JSONObject inline = partObj.getJSONObject("inlineData");
                            String base64 = inline.getString("data");
                            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
                            Bitmap image = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                            genView.setImageBitmap(image);
                            genText.setText(prompt);
                            llmProgressBar.setVisibility(View.GONE);
                            genButton.setVisibility(View.VISIBLE);
                            backButton.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    Log.e("Gemini", "Parsing Error: " + e.getMessage());
                    genText.setText("Parsing Error: " + e.getMessage());
                }
            }, error -> {
                String errMsg = "Unknown error";

                if (error instanceof com.android.volley.TimeoutError) {
                    errMsg = "Request timeout.";
                } else if (error instanceof com.android.volley.NoConnectionError) {
                    errMsg = "No network connection.";
                } else if (error instanceof com.android.volley.AuthFailureError) {
                    errMsg = "Authentication failed.";
                } else if (error instanceof com.android.volley.ServerError) {
                    errMsg = "Server error.";
                } else if (error instanceof com.android.volley.NetworkError) {
                    errMsg = "Network error.";
                } else if (error instanceof com.android.volley.ParseError) {
                    errMsg = "Parse error.";
                }

                Log.e("Gemini", "Volley Error: " + errMsg);

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String body = new String(error.networkResponse.data);
                    Log.e("Gemini", "Error response body: " + body);
                }

                genText.setText("Volley Error: " + errMsg);
                llmProgressBar.setVisibility(View.GONE);
            });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
            genText.setText("JSONException: " + e.getMessage());
        }
    }

    public void generateImageFromPrompt(View view) {
        String prompt = prompts.get(promptIndex % prompts.size());
        generateImageFromPrompt(prompt);
        genButton.setVisibility(View.GONE);
        llmProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Generating a scene...", Toast.LENGTH_SHORT).show();
        promptIndex++;
    }

    public void backToMainActivity(View view) {
        finish();
    }
}