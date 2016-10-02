package com.myhackathonproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class DetailsActivity extends AppCompatActivity {

    private DetailsActivity detailsActivity;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detailsActivity = this;

        Intent intent = getIntent();
        String searchKey = intent.getStringExtra("SEARCH_KEY");
        Log.i("searchKey: ", searchKey);
        Details itemDetails = new Details();
        itemDetails.setKey(searchKey);
        itemDetails.execute();
    }

    private class Details extends AsyncTask<Void, Void, JSONObject> {

        private RequestQueue queue;
        private String BASE_URL = "http://foodie.fwd.wf";
        private String URL;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        private String key;

        @Override
        protected JSONObject doInBackground(Void... params) {

            URL = BASE_URL + "/foodieapi?key=" + getKey();
            detailsActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress = new ProgressDialog(detailsActivity);
                    progress.setTitle("");
                    progress.setMessage("Fetching information...");
                    progress.setCancelable(false);
                    progress.show();
                }
            });

            queue = Volley.newRequestQueue(detailsActivity);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Volley Response", "Response: " + response.toString());
                            detailsActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                }
                            });

                            try {
                                setDetails(response);
                            } catch (JSONException e) {
                                Log.i("JSONException: ", ""+e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.i("VolleyError: ", ""+error.getMessage());
                            detailsActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                }
                            });
                            detailsActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Could not fetch information !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

            queue.add(jsObjRequest);
            return null;
        }

        public void setDetails(JSONObject response) throws JSONException {
            String imgURL = response.getString("imgURL");
            try {
                InputStream in = new java.net.URL(imgURL).openStream();
                Bitmap bmp = BitmapFactory.decodeStream(in);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bmp);

                String name = (String) response.get("name");
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(name);

                if(response.has("origin")) {
                    String placeOfOrigin = response.getString("origin");
                    TextView origin = (TextView) findViewById(R.id.origin);
                    origin.setText(placeOfOrigin);
                }
                if(response.has("ingredients")) {
                    String ingredientsStr = response.getString("ingredients");
                    TextView ingredients = (TextView) findViewById(R.id.ingredients);
                    ingredients.setText(ingredientsStr);
                }

            } catch (Exception e) {
                Log.e("Error", ""+e.getMessage());
            }
        }
    };
}
