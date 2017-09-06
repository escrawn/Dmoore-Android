package com.dodhev.dmooretaskmanger;

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

    private static final String DMOORE_REQUEST_URL = "http://alejandronaranjodev.com/DmooreWS.asmx/GetTaskSUserJSON";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskAsyncTask task = new TaskAsyncTask();
        task.execute();



    }



    private void updateUi(ArrayList<Task> task) {

        ListView lv = (ListView) findViewById(R.id.listView);

        final TaskAdapter adapter = new TaskAdapter(this,task);

        lv.setAdapter(adapter);

    }

    private class TaskAsyncTask extends AsyncTask<URL, Void, ArrayList<Task>> {


        @Override
        protected ArrayList<Task> doInBackground(URL... urls) {

            URL url = createUrl(DMOORE_REQUEST_URL);

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                //Handle exception
            }

            ArrayList<Task> tasks = extractFeatureFromJson(jsonResponse);

            return tasks;
        }

        @Override
        protected void onPostExecute(ArrayList<Task> tasks) {

            if(tasks == null){
                return;
            }
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
            return output.toString();
        }

        private String makeHttpRequest(URL url) throws IOException {

            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            if (url == null) {
                return jsonResponse;
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private ArrayList<Task> extractFeatureFromJson(String taskJson) {

            if (TextUtils.isEmpty(taskJson)) {
                return null;
            }
            try {
                JSONArray featureArray = new JSONArray(taskJson);


                // If there are results in the features array
                if (featureArray.length() > 0) {

                    // Extract out the first feature
                    JSONObject firstFeature = featureArray.getJSONObject(0);
                    ArrayList<Task> tasks = new ArrayList<Task>();

                    for (int i = 0; i < featureArray.length(); i++) {
                        JSONObject taskObject = featureArray.getJSONObject(i);
                        int id = taskObject.getInt("Id");
                        String name = taskObject.getString("Name");
                        String desc = taskObject.getString("Desc");
                        Task task = new Task(id, name, desc);
                        tasks.add(task);

                    }


                    return tasks;
                }


            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }



    }
}