package com.dodhev.dmooretaskmanger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Escrawn on 25/09/2017.
 */

public class login extends AppCompatActivity {

    private String username;
    private String password;

    private String LOGIN_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


    }


    public void goSignUp(View v) {

        Intent newActivity = new Intent(login.this, SignUp.class);
        startActivity(newActivity);
    }

    public void getUserInput() {
        TextView txUsername = (TextView) findViewById(R.id.username);
        username = txUsername.getText().toString();
        TextView txtPassword = (TextView) findViewById(R.id.password);
        password = txtPassword.getText().toString();
    }

    public void updateLoginUrl() {

    }

    public void LogIn(View v) {

        getUserInput();
        UserAsyncTask user = new UserAsyncTask();

        user.execute();




    }

    private void updateUI(User user) {


        TextView textView = (TextView) findViewById(R.id.logueado);


        try {
            if (user.getId() > 0) {
                Log.d("CODIGO DE USUARIO", user.getId() + "");
                Intent tasksActivity = new Intent(login.this,MainActivity.class);
                int userID = user.getId();
                tasksActivity.putExtra("userID",userID);
                startActivity(tasksActivity);
            } else {
                textView.setText("NO SE HA PODIDO CONECTAR");
            }
        } catch (NullPointerException e) {
            Log.d("NO ENCONTRADO", "NO ENCONTRADO");
            textView.setText("No se ha encontrado el usuario");
        }
    }


    private class UserAsyncTask extends AsyncTask<URL, Void, User> {


        @Override
        protected User doInBackground(URL... params) {

            getUserInput();
            updateLoginUrl();
            URL url = createUrl(LOGIN_URL);
            User user = new User();
            String jsonResponse = "";


            try {
                jsonResponse = makeHttpRequest(url);
                Log.d("JSONRESPONSE", jsonResponse + "-------------------");
                user = extractUserFromJson(jsonResponse);
                Log.d("LLAMADO DOINBACKGROUND", "++++++++++++++++++++++++++++++++++++++");


            } catch (IOException e) {
                Log.d("Problema", "Problema con makeHttpRequest");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSONEXCEPTION", "jsonexception");
            }


            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            updateUI(user);

        }

        private URL createUrl(String stringUrl) {

            URL url = null;

            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                return null;
            }

            return url;
        }

        private String readFromStream(InputStream inputStream) throws IOException {

            StringBuilder output = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            Log.d("Read From stream", "Method called");
            return output.toString();
        }


        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            Log.d("MakeHttpRequest", "Method called");

            if (url == null) {
                Log.d("URL_NULL", "URL IS NULL IN METHOD makeHttpRequest");
                return jsonResponse;
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                //TODO
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            Log.d("JSONRESPONSE+++", jsonResponse + "´´´´´´´´´´´´´´´´");
            return jsonResponse;
        }

        private User extractUserFromJson(String userJson) throws JSONException {

            User user = new User();


            if (TextUtils.isEmpty(userJson)) {
                return null;
            }

            int idUser = -1;
            JSONObject featureObject = new JSONObject(userJson);

            try {


                int code = featureObject.getInt("ErrorCode");


                if (code == 0) {
                    Log.d("LOGIN", "DATOS CORRECTOS");
                    JSONObject dataObject = featureObject.getJSONObject("Data");
                    idUser = dataObject.getInt("Id");

                    Log.d("ID DEL USUARIO", idUser + "");

                    user.setId(idUser);
                    user.setUserName(username);


                } else {
                    Log.d("LOGIN", "ERROR AL LOGUEAR");
                    return null;
                }

            } catch (JSONException e) {
                Log.d("Parsing JSON", "Hubo un problema al intentar conseguir los datos");
            }
            return user;
        }


    }
}
