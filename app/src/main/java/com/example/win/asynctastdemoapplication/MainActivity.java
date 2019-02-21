package com.example.win.asynctastdemoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    Button btnNext;
    String TAG="Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext = (Button) findViewById(R.id.button);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new YourAsyncTask(MainActivity.this).execute("Felix IT");
            }
        });
    }

    private class YourAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        public YourAsyncTask(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Doing something, please wait.");
            dialog.show();
        }

        protected String doInBackground(String... args) {


            String url = "http://api.androidhive.info/contacts/";
            String jsonStr = "";
            try {
                // Making a request to url and getting response
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                jsonStr = EntityUtils.toString(response.getEntity());
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }




//            String str=args[0];
//            // do background work here
//            try {
//                Thread.sleep(4000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//

            return jsonStr;
        }

        protected void onPostExecute(String result) {


            try {
                JSONObject mainObj=new JSONObject(result);

                JSONArray arrObj=mainObj.getJSONArray("contacts");

                for(int item=0;item<arrObj.length();item++){

                    JSONObject jsonObject=arrObj.getJSONObject(item);

                    String name=jsonObject.getString("name");

                    JSONObject phoneObj=jsonObject.getJSONObject("phone");
                    String number=phoneObj.getString("mobile");


                    Toast.makeText(MainActivity.this, name+" "+number, Toast.LENGTH_SHORT).show();

                }





            } catch (JSONException e) {
                e.printStackTrace();
            }


            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//            startActivity(intent);
        }
    }


    Boolean isDoubbleBackPressed = false;

    @Override
    public void onBackPressed() {
        if (isDoubbleBackPressed) {
            super.onBackPressed();
        }
        isDoubbleBackPressed = true;
        Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isDoubbleBackPressed = false;
            }
        }, 3000);
    }
}
