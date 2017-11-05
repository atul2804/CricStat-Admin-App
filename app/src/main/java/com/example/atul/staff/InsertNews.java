package com.example.atul.staff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsertNews extends AppCompatActivity {

    EditText title,author,date,article;
    Button submit;

    String mtitle,mauthor,mdate,marticle;
    ProgressDialog progressDialog;

    String ServerURL = "https://cricstat.000webhostapp.com/php/article.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_news);

        title =  (EditText) findViewById(R.id.yourtitle);
        article = (EditText) findViewById(R.id.yourarticle);
        author = (EditText) findViewById(R.id.yourauthor);
        date = (EditText) findViewById(R.id.yourdate);

    //    getActionBar().setTitle("Your Article here");

        final String exam = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        date.setText(exam);

        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     Toast.makeText(getApplicationContext(), exam,Toast.LENGTH_SHORT).show();
                getdata();
            }
        });

    }

    public void getdata(){
        mtitle = title.getText().toString();
        mauthor = author.getText().toString();
        marticle = article.getText().toString();
        mdate = date.getText().toString();

        if (mtitle.length() ==0){
        //    Toast.makeText(getApplicationContext(),"Please fill all details",Toast.LENGTH_SHORT).show();
            title.setError("Field required");
        }else if (mdate.length()==0){
            date.setError("Required !");
        }else if (marticle.length()==0){
            article.setError("Required !!");
        }else if (mauthor.length()==0){
            author.setError("Required !!");
        }else{
            new SendPostReqAsyncTask().execute();
        }
    }

    public class SendPostReqAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(InsertNews.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setTitle("Inserting your article ...");
            progressDialog.setMessage("Almosst done ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("title",mtitle));
            nameValuePairs.add(new BasicNameValuePair("author",mauthor));
            nameValuePairs.add(new BasicNameValuePair("article",marticle));
            nameValuePairs.add(new BasicNameValuePair("date",mdate));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(ServerURL);

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

            //    int response = httpEntity.getContent();
                String response = EntityUtils.toString(httpEntity);

                return response;

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s.equalsIgnoreCase("Data Submit Successfully")){
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
