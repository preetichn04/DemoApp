package com.pc.oitdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

public class SubmitActivity extends AppCompatActivity {

    private EditText firstname, lastname, username;   //the textfields
    private TextView display;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.username);

        display = findViewById(R.id.uname);

        submit = findViewById(R.id.submitbtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubmitLogin(SubmitActivity.this).execute(firstname.getText().toString(),lastname.getText().toString(), username.getText().toString());
            }
        });
    }


    public static class SubmitLogin extends AsyncTask<String,Void,String>{

        private WeakReference<SubmitActivity> submitActivityWeakReference;

        SubmitLogin(SubmitActivity submitActivity) {
            submitActivityWeakReference = new WeakReference<>(submitActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... voids) {
            String fname = voids[0];
            String lname = voids[1];
            String uname = voids[2];


            String link="http://192.168.64.2/OITDemoInsert.php";

            try{

                String data  = URLEncoder.encode("first_name", "UTF-8") + "=" +
                        URLEncoder.encode(fname, "UTF-8");
                data += "&" + URLEncoder.encode("last_name", "UTF-8") + "=" +
                        URLEncoder.encode(lname, "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "=" +
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

            SubmitActivity submitActivity = submitActivityWeakReference.get();

            Toast.makeText(submitActivity, s, Toast.LENGTH_LONG).show();

            Intent i = new Intent(submitActivity, RetrieveActivity.class);
            submitActivity.startActivity(i);
        }
    }

}
