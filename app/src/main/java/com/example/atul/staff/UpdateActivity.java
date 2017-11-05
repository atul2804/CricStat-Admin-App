package com.example.atul.staff;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    EditText playerid;
    Button updatebtn;

    ProgressBar progressBar;
    String id;
    TextView text;

    Button btn_update;

    ProgressDialog progressDialog,progressDialog1;

    EditText match0,match1,inng0,inng1,runs0,runs1,avg0,avg1;
    EditText sr0,sr1,half0,half1,cent0,cent1,high0,high1;

    String smatch0,smatch1,sinng0,sinng1,sruns0,sruns1,savg0,savg1;
    String ssr0,ssr1,shalf0,shalf1,scent0,scent1,shigh0,shigh1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);

     //   progressBar = (ProgressBar) findViewById(R.id.progress_bar);
     //   text = (TextView) findViewById(R.id.testtext);

        match0 = (EditText) findViewById(R.id.matches0);
        match1 = (EditText) findViewById(R.id.matches1);
        inng0 = (EditText) findViewById(R.id.innings0);
        inng1 = (EditText) findViewById(R.id.innings1);
        runs0 = (EditText) findViewById(R.id.runs0);
        runs1 = (EditText) findViewById(R.id.runs1);
        avg0 = (EditText) findViewById(R.id.avg0);
        avg1 = (EditText) findViewById(R.id.avg1);
        sr0 = (EditText) findViewById(R.id.sr0);
        sr1 = (EditText) findViewById(R.id.sr1);
        half0 = (EditText) findViewById(R.id.half0);
        half1 = (EditText) findViewById(R.id.half1);
        cent0 = (EditText) findViewById(R.id.century0);
        cent1 = (EditText) findViewById(R.id.century1);
        high0 = (EditText) findViewById(R.id.high0);
        high1 = (EditText) findViewById(R.id.high1);

        btn_update = (Button) findViewById(R.id.update);

        id = getIntent().getStringExtra("id");

        update();

        final String url1 = "https://cricstat.000webhostapp.com/php/updateplayer.php?id=" + id;

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smatch0 = match0.getText().toString();
                sinng0 = inng0.getText().toString();
                sruns0 = runs0.getText().toString();
                savg0 = avg0.getText().toString();
                ssr0 = sr0.getText().toString();
                shalf0 = half0.getText().toString();
                scent0 = cent0.getText().toString();
                shigh0 = high0.getText().toString();

                smatch1 = match1.getText().toString();
                sinng1 = inng1.getText().toString();
                sruns1 = runs1.getText().toString();
                savg1 = avg1.getText().toString();
                ssr1 = sr1.getText().toString();
                shalf1 = half1.getText().toString();
                scent1 = cent1.getText().toString();
                shigh1 = high1.getText().toString();

                new UpdateDataTask().execute(url1);

            }
        });
    }

    public void update(){
        String url = "https://cricstat.000webhostapp.com/php/Batstat.php?id=" + id;
        new GetUpdateTask().execute(url);
    }

    public class GetUpdateTask extends AsyncTask<String,Void,String >{

        @Override
        protected void onPreExecute() {
       //     progressBar.setVisibility(View.VISIBLE);
            progressDialog = new ProgressDialog(UpdateActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setTitle("Fetching his records ");
            progressDialog.setMessage("Almost done ...");
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

            if (s==null){
                Toast.makeText(UpdateActivity.this,"Fail",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONArray jarr = new JSONArray(s);
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject obj = jarr.optJSONObject(i);
                        if (obj.optInt("Type")==0){
                            match0.setText(obj.optString("Matches"));
                            inng0.setText(obj.optString("Innings"));
                            runs0.setText(obj.optString("Runs"));
                            avg0.setText(obj.optString("Avg"));
                            sr0.setText(obj.optString("SR"));
                            half0.setText(obj.optString("Half"));
                            cent0.setText(obj.optString("Century"));
                            high0.setText(obj.optString("High"));
                        }else{
                            match1.setText(obj.optString("Matches"));
                            inng1.setText(obj.optString("Innings"));
                            runs1.setText(obj.optString("Runs"));
                            avg1.setText(obj.optString("Avg"));
                            sr1.setText(obj.optString("SR"));
                            half1.setText(obj.optString("Half"));
                            cent1.setText(obj.optString("Century"));
                            high1.setText(obj.optString("High"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class UpdateDataTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            progressDialog1 = new ProgressDialog(UpdateActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog1.setTitle("Updating his records ...");
            progressDialog1.setMessage("Almost done ...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("match0",smatch0));
            nameValuePairs.add(new BasicNameValuePair("inng0",sinng0));
            nameValuePairs.add(new BasicNameValuePair("runs0",sruns0));
            nameValuePairs.add(new BasicNameValuePair("avg0",savg0));
            nameValuePairs.add(new BasicNameValuePair("sr0",ssr0));
            nameValuePairs.add(new BasicNameValuePair("half0",shalf0));
            nameValuePairs.add(new BasicNameValuePair("cent0",scent0));
            nameValuePairs.add(new BasicNameValuePair("high0",shigh0));
            nameValuePairs.add(new BasicNameValuePair("type0","0"));

            nameValuePairs.add(new BasicNameValuePair("match1",smatch1));
            nameValuePairs.add(new BasicNameValuePair("inng1",sinng1));
            nameValuePairs.add(new BasicNameValuePair("runs1",sruns1));
            nameValuePairs.add(new BasicNameValuePair("avg1",savg1));
            nameValuePairs.add(new BasicNameValuePair("sr1",ssr1));
            nameValuePairs.add(new BasicNameValuePair("half1",shalf1));
            nameValuePairs.add(new BasicNameValuePair("cent1",scent1));
            nameValuePairs.add(new BasicNameValuePair("high1",shigh1));
            nameValuePairs.add(new BasicNameValuePair("type1","1"));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(strings[0]);

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

                String response = EntityUtils.toString(httpEntity);

                return response;

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog1.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

        //    text.setText(s);

            finish();
        }
    }
}
