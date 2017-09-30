package com.dodhev.dmooretaskmanger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.widget.ListView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private String DMOORE_REQUEST_URL;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TaskAsyncTask task = new TaskAsyncTask();
        task.execute();


        Log.d("On Create", "Method called");
    }

    private int getUserId() {
        Intent tasksActivity = getIntent();
        int userID = tasksActivity.getIntExtra("userID", 0);

        return userID;
    }

    private void updateUi(ArrayList<Task> task) {

        ListView lv = (ListView) findViewById(R.id.listView);
        final TaskAdapter adapter = new TaskAdapter(this, task);

        lv.setAdapter(adapter);

        Log.d("update Ui", "Method called");

    }

    public void updateRequestUrl() {

      
    }

    private class TaskAsyncTask extends AsyncTask<URL, Void, ArrayList<Task>> {


        @Override
        protected ArrayList<Task> doInBackground(URL... urls) {

            updateRequestUrl();
            URL url = createUrl(DMOORE_REQUEST_URL);
            ArrayList<Task> tasks = new ArrayList<Task>();
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpRequest(url);
                Log.d("URL", url.getAuthority());
                tasks = extractFeatureFromJson(jsonResponse);
            } catch (IOException e) {
                Log.d("Problema", "Problema con makeHttpRequest");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.d("Do in background", "Method called");
            return tasks;
        }


        @Override
        protected void onPostExecute(ArrayList<Task> tasks) {
            super.onPostExecute(tasks);
            updateUi(tasks);

        }

        //Creamos la URL a partir del string
        private URL createUrl(String stringUrl) {

            URL url = null;

            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error creating url", exception);
                return null;
            }
            Log.d("CreateUrl", "Method called");
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

            if (url == null) {
                Log.d("URL_NULL", "URL IS NULL IN METHOD makeHttpRequest");
                return jsonResponse;

            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("11111", "11111111");
                urlConnection.setRequestMethod("GET");
                Log.d("222222", "2222");
                urlConnection.setReadTimeout(10000);
                Log.d("333333", "33333");
                urlConnection.setConnectTimeout(15000);
                Log.d("444444", "444444");
                urlConnection.connect();
                Log.d("5555555", "5555555");
                Log.d("asdasdasd", urlConnection.getResponseMessage());

                inputStream = urlConnection.getInputStream();
                Log.d("6666666", "666666666");

                jsonResponse = readFromStream(inputStream);
                Log.d("JsonResponse", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            } catch (IOException e) {
                Log.d("IOEXCEPTION",e.toString());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            Log.d("JsonResponse",jsonResponse);
            Log.d("MakeHttpResponse", "Method called");

            return jsonResponse;
        }

        private ArrayList<Task> extractFeatureFromJson(String taskJson) throws JSONException {

            //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

            if (TextUtils.isEmpty(taskJson)) {
                return null;
            }

            ArrayList<Task> tasks = new ArrayList<>();
            JSONObject featureObject = new JSONObject(taskJson);

            try {


                JSONArray data = featureObject.getJSONArray("Data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject tasksOb = data.getJSONObject(i);
                    int id = tasksOb.getInt("Id");
                    String name = tasksOb.getString("Name");
                    Log.d("NAME",name);
                    String desc = tasksOb.getString("Desc");
                    Log.d("Description",desc);
                    Task task = new Task(id, name, desc);
                    tasks.add(task);

                }



            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the taks JSON results", e);
            }
            Log.d("ExtractFeatureFromJson", "Method called");
            return tasks;
        }


    }
}