package com.example.atul.staff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends Activity {

    ImageView logo;
    EditText emailtext,passwordtext;
    Button btnlogin;

    String email,password;
    ProgressBar progressBar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailtext = (EditText) findViewById(R.id.input_email);
        passwordtext = (EditText) findViewById(R.id.input_password);
        btnlogin = (Button) findViewById(R.id.btn_login);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validitate();
            }
        });
    }

    public void validitate(){
        email = emailtext.getText().toString();
        password = passwordtext.getText().toString();
        int c=0;

        if (email.isEmpty()){
            emailtext.setError("Enter a valid email address");
        }else{
            c++;
        }

        if (password.isEmpty()){
            passwordtext.setError("Enter the password");
        }else{
            c++;
        }
        if (c == 2){
            login(email,password);
        }
    }

    public void login(String email,String password){
        String url = "https://cricstat.000webhostapp.com/php/login.php";
        new LoginTask().execute(url);
    }

    public class LoginTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignInActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setTitle("Authenticating ...");
            progressDialog.setMessage("Almost done ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("uname",email));
            nameValuePairs.add(new BasicNameValuePair("pass",password));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(param[0]);

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
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

            if (s.equalsIgnoreCase("Success")) {
                Intent intent = new Intent(SignInActivity.this, DeleteActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
