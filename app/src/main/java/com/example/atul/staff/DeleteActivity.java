package com.example.atul.staff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteActivity extends AppCompatActivity {

    EditText playerid;
    Button delete,update,bowl_update,add_news;

    ProgressBar progressBar;
    String id;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        playerid = (EditText) findViewById(R.id.input_playerid);
        delete = (Button) findViewById(R.id.delete_btn);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        update = (Button) findViewById(R.id.update_btn);
        add_news = (Button) findViewById(R.id.insert_news);

        bowl_update = (Button) findViewById(R.id.update_bowl);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = playerid.getText().toString();
                if (id.length() == 0){
                    playerid.setError("Empty Field");
                }else{
                    deleting();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = playerid.getText().toString();
                if (id.length() == 0){
                    playerid.setError("Empty Field");
                }else{
                    Intent intent = new Intent(DeleteActivity.this, UpdateActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });

        bowl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = playerid.getText().toString();
                if (id.length() == 0){
                    playerid.setError("Empty Field");
                }else{
                    Intent it = new Intent(DeleteActivity.this, BowlUpdate.class);
                    it.putExtra("id",id);
                    startActivity(it);
                }
            }
        });

        add_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(DeleteActivity.this, InsertNews.class);
                startActivity(inte);
            }
        });
    }

    public void deleting(){
        String url = "https://cricstat.000webhostapp.com/php/deleteplayer.php?id=" + id;
        new DeleteTask().execute(url);
    }

    public class DeleteTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DeleteActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setTitle("Deleting his records ...");
            progressDialog.setMessage("Almost done ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(param[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(
                            new InputStreamReader(
                                    urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),s ,Toast.LENGTH_LONG).show();
        }
    }
}
