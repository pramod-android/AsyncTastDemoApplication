package com.example.win.asynctastdemoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnNext;

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

            String str=args[0];
            // do background work here
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return str;
        }

        protected void onPostExecute(String result) {
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
