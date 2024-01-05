package com.example.joosulsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.joosulsa.databinding.ActivityTestBinding;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestActivity extends AppCompatActivity {
    private static final String FLASK_SERVER_URL = "http://192.168.219.51:5000/upload_image";
    private ActivityTestBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bitmap bitmap = getIntent().getParcelableExtra("TestImg");
        Log.d("이미지왔어",bitmap.toString());
        String base64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG,100);
        Log.d("이미지변환",base64Image.toString());
        uploadImageToServer(base64Image);

    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat,quality,byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
    }

    private void uploadImageToServer(String base64Image){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("image",base64Image);
            Log.d("담았냐?",jsonBody.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                FLASK_SERVER_URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            Toast.makeText(TestActivity.this, "서버응답" + result, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(TestActivity.this, "서버오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(this).add(request);
    }







}