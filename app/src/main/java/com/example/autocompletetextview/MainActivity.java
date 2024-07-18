package com.example.autocompletetextview;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PHP_SCRIPT_URL = "http://lesterintheclouds.com/crud-android/read.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        // Fetch data from PHP script
        fetchData(autoCompleteTextView);
    }

    private void fetchData(final AutoCompleteTextView autoCompleteTextView) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, PHP_SCRIPT_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> dataList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                // Assuming JSON structure is {"id":"1","data":"example"}
                                String data = response.getJSONObject(i).getString("data");
                                dataList.add(data);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, dataList);
                            autoCompleteTextView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "JSON Exception: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching data: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }
}
