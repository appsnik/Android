package co.appsnik.chuck;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Chuck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getChuckFact(View view) {
        getRamdomJoke();
    }

    public void getRamdomJoke() {
        new DownloadTask() {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getString("type").equals("success")) {
                            JSONObject jsonValue = json.getJSONObject("value");
                            String joke = jsonValue.getString("joke");
                            TextView textbox = (TextView) findViewById(R.id.joke_text_box);
                            textbox.setText(joke);
                        }
                    } catch (JSONException e) {
                        Log.i(TAG, "Failed to parse json object.");
                        e.printStackTrace();
                    }
                }
            }
        }.execute("http://api.icndb.com/jokes/random/");
    }
/*
    public void getJokeCount() {
        new DownloadTask().execute("http://api.icndb.com/categories");
    }
*/
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection conn = null;
            InputStream stream = null;

            try {
                URL url = new URL(urls[0]);
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.connect();
                stream = conn.getInputStream();
                return convertStreamToString(stream);
            } catch (IOException e) {
                Log.i(TAG, "Failed to download url: " + urls[0]);
                e.printStackTrace();
                return null;
            } finally {
                if (stream != null) {
                    try {
                        Log.i(TAG, "Closing stream");
                        stream.close();
                    } catch (IOException e) {
                    }
                }
                if (conn != null) {
                    Log.i(TAG, "Disconnecting");
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, null != result ? result : "(null)");
        }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
}
