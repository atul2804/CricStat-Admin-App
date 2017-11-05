package com.example.atul.staff;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul on 28-10-2017.
 */

public class BowlUpdate extends AppCompatActivity {

    String id;
    TextView text;

    Button btn_update;

    ProgressDialog progressDialog,progressDialog1;

    EditText match0,match1,inng0,inng1,wick0,wick1,avg0,avg1;
    EditText eco0,eco1,best0,best1;

    String smatch0,smatch1,sinng0,sinng1,swick0,swick1,savg0,savg1;
    String seco0,seco1,sbest0,sbest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bowl_update);

     //   text = (TextView) findViewById(R.id.testtext);

        match0 = (EditText) findViewById(R.id.matches0);
        match1 = (EditText) findViewById(R.id.matches1);
        inng0 = (EditText) findViewById(R.id.innings0);
        inng1 = (EditText) findViewById(R.id.innings1);
        wick0 = (EditText) findViewById(R.id.wickets0);
        wick1 = (EditText) findViewById(R.id.wickets1);
        avg0 = (EditText) findViewById(R.id.avg0);
        avg1 = (EditText) findViewById(R.id.avg1);
        eco0 = (EditText) findViewById(R.id.eco0);
        eco1 = (EditText) findViewById(R.id.eco1);
        best0 = (EditText) findViewById(R.id.best0);
        best1 = (EditText) findViewById(R.id.best1);

        btn_update = (Button) findViewById(R.id.update);

        id = getIntent().getStringExtra("id");

        update();

        final String url1 = "https://cricstat.000webhostapp.com/php/bowlupdate.php?id=" + id;

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smatch0 = match0.getText().toString();
                sinng0 = inng0.getText().toString();
                swick0 = wick0.getText().toString();
                savg0 = avg0.getText().toString();
                seco0 = eco0.getText().toString();
                sbest0 = best0.getText().toString();

                smatch1 = match1.getText().toString();
                sinng1 = inng1.getText().toString();
                swick1 = wick1.getText().toString();
                savg1 = avg1.getText().toString();
                seco1 = eco1.getText().toString();
                sbest1 = best1.getText().toString();

                new UpdateDataTask().execute(url1);

            }
        });
    }

    public void update(){
        String url = "https://cricstat.000webhostapp.com/php/Bowlstat.php?id=" + id;
        new GetUpdateTask().execute(url);
    }

    public class GetUpdateTask extends AsyncTask<String,Void,String > {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(BowlUpdate.this,R.style.AppTheme_Dark_Dialog);
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
                Toast.makeText(BowlUpdate.this,"Fail",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONArray jarr = new JSONArray(s);
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject obj = jarr.optJSONObject(i);
                        if (obj.optInt("Type")==0){
                            match0.setText(obj.optString("Matches"));
                            inng0.setText(obj.optString("Innings"));
                            wick0.setText(obj.optString("Wickets"));
                            avg0.setText(obj.optString("Avg"));
                            eco0.setText(obj.optString("Eco"));
                            best0.setText(obj.optString("Best"));
                        }else{
                            match1.setText(obj.optString("Matches"));
                            inng1.setText(obj.optString("Innings"));
                            wick1.setText(obj.optString("Wickets"));
                            avg1.setText(obj.optString("Avg"));
                            eco1.setText(obj.optString("Eco"));
                            best1.setText(obj.optString("Best"));
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
            progressDialog1 = new ProgressDialog(BowlUpdate.this,R.style.AppTheme_Dark_Dialog);
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
            nameValuePairs.add(new BasicNameValuePair("runs0",swick0));
            nameValuePairs.add(new BasicNameValuePair("avg0",savg0));
            nameValuePairs.add(new BasicNameValuePair("sr0",seco0));
            nameValuePairs.add(new BasicNameValuePair("high0",sbest0));
            nameValuePairs.add(new BasicNameValuePair("type0","0"));

            nameValuePairs.add(new BasicNameValuePair("match1",smatch1));
            nameValuePairs.add(new BasicNameValuePair("inng1",sinng1));
            nameValuePairs.add(new BasicNameValuePair("runs1",swick1));
            nameValuePairs.add(new BasicNameValuePair("avg1",savg1));
            nameValuePairs.add(new BasicNameValuePair("sr1",seco1));
            nameValuePairs.add(new BasicNameValuePair("high1",sbest1));
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

       //     text.setText(s);

            finish();
        }
    }
}
