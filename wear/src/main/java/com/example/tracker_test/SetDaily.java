package com.example.tracker_test;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import java.util.HashMap;
import java.util.Map;

public class SetDaily extends WearableActivity {

    Button minus, plus, submit;
    TextView dailyGoal;

    int counter = 10000;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog pd;

    String ServerURL = "http://10.230.6.2/android/insert.php" ;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_daily);


        minus = (Button) findViewById(R.id.dailyMinus);
        plus = (Button) findViewById(R.id.dailyPlus);
        submit = (Button) findViewById(R.id.submit);
        dailyGoal = (TextView) findViewById(R.id.dailyNumber);
        builder = new AlertDialog.Builder(SetDaily.this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter = counter - 10;
                dailyGoal.setText(Integer.toString(counter));

            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter = counter + 10;
                dailyGoal.setText(Integer.toString(counter));

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.clear();
                final String getGoal = dailyGoal.getText().toString();
                int goalInt = Integer.parseInt(getGoal);
                editor.putInt("key_goal_daily", goalInt);
                editor.commit();
                Toast.makeText(SetDaily.this, "Daily Goal is set!", Toast.LENGTH_SHORT).show();
                //new JsonTask().execute("http://10.230.6.2/android/getdata.php");




                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                builder.setTitle("Server Response");
                                builder.setMessage("Response:" + response);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SetDaily.this,"Error",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();

                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("first","Daily Goal");//the first column is the $POST[]in the insert.php data
                        params.put("second",getGoal);
                        return params;
                    }
                };

                MySingleton.getInstance(SetDaily.this).addTorequestue(stringRequest);




                /*Intent intent = new Intent(SetDaily.this, MainActivity.class);
                startActivity(intent);*/

            }
        });





    }//end of onCreate method




    //get data from database
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SetDaily.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                JSONObject json = new JSONObject(String.valueOf(buffer));

                JSONArray cast = json.getJSONArray("result");

                for (int i = 0; i < cast.length(); ++i) {
                    JSONObject rec = cast.getJSONObject(i);
                    System.out.println(rec);
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }// end of async caller*/





}
