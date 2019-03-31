package com.pc.oitdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

public class RetrieveActivity extends AppCompatActivity {

    private EditText username;
    private TextView display;
    private Button retrieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        display = findViewById(R.id.displaytv);
        retrieve = findViewById(R.id.retrievebtn);
        username = findViewById(R.id.username);

        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveLogin(RetrieveActivity.this).execute(username.getText().toString());
            }
        });
    }

    public static class RetrieveLogin extends AsyncTask<String,Void,String> {

        private WeakReference<RetrieveActivity> retrieveActivityWeakReference;


        RetrieveLogin(RetrieveActivity retrieveActivity) {
            retrieveActivityWeakReference = new WeakReference<>(retrieveActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String uname = strings[0];


            String link="http://192.168.64.2/OITDemo.php";

            try{

                String data  = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(uname, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                Log.d(TAG, "doInBackground: connection created");

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();
                //Log.d(TAG, "doInBackground: writer");
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;



                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                Log.d(TAG, "doInBackground: " + sb.toString());

                wr.close();
                reader.close();
                return sb.toString();
            } catch(Exception e){
                Log.d(TAG, "doInBackground: " + e.getMessage());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            RetrieveActivity retrieveActivity = retrieveActivityWeakReference.get();

            retrieveActivity.display.setText(s);
        }
    }
}
