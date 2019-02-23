package com.example.win.asynctastdemoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnNext;
    String TAG="Main Activity";
    ListView listView;
    List<Contacts> contactsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext = (Button) findViewById(R.id.button);
        listView=(ListView) findViewById(R.id.listView);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new YourAsyncTask(MainActivity.this).execute("Felix IT");
            }
        });


        List<String> language=new ArrayList<>();
        language.add("C++");
        language.add("Java");
        language.add("Kotlin");
        language.add("Swift");
        language.add("JS");
        language.add("Python");


        //Spinner and AutoCompleteTextView

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, language);



// Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);




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
                    Contacts contacts=new Contacts();

                    JSONObject jsonObject=arrObj.getJSONObject(item);

                    contacts.setId(jsonObject.getString("id"));
                    contacts.setName(jsonObject.getString("name"));
                    contacts.setEmail(jsonObject.getString("email"));
                    contacts.setAddress(jsonObject.getString("address"));
                    contacts.setGender(jsonObject.getString("gender"));

                  //  String name=jsonObject.getString("name");

                    JSONObject phoneObj=jsonObject.getJSONObject("phone");
                   // String number=phoneObj.getString("mobile");


                    contacts.setMobile(phoneObj.getString("mobile"));
                    contacts.setHome(phoneObj.getString("home"));
                    contacts.setOffice(phoneObj.getString("office"));

                    //Toast.makeText(MainActivity.this, name+" "+number, Toast.LENGTH_SHORT).show();
                    contactsList.add(contacts);
                }

            CustomAdaptor customAdaptor=new CustomAdaptor();
                listView.setAdapter(customAdaptor);



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



    class CustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return contactsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.list_item, null);

            //ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
            TextView textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
            TextView textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);

            TextView textViewNumber = (TextView) view.findViewById(R.id.textViewNumber);



            textViewName.setText(contactsList.get(position).getName());
            textViewEmail.setText(contactsList.get(position).getEmail());
            textViewAddress.setText(contactsList.get(position).getAddress());

            textViewNumber.setText(contactsList.get(position).getMobile());





            return view;
        }
    }
}
