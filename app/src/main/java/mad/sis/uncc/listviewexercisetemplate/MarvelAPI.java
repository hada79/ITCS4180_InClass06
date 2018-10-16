package mad.sis.uncc.listviewexercisetemplate;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MarvelAPI extends AppCompatActivity implements GetSeriesAPI.IData {
    //you should fill those two keys as you receive them from the API website
    String APIKey = "e920f543e78ad8a2fdeea76c09e14bea";
    String privateKey = "4ae8ae5d54d69ec2083832129f3a98b228569c41";
    public static String SERIES_KEY = "SERIES";

    //parameters
    String seriesNameQuery = null;
    String year = null;
    EditText seriesText = null;
    EditText yearText = null;

    public ProgressBar progressBar;
    public ArrayList<Series> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marvel_api);
        setTitle("Marvel API App");

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setMax(10);

        //get button
        Button button =  (Button) findViewById(R.id.seriesbutton);

        //EeditTexts
        seriesText = (EditText) findViewById(R.id.seriesNameText);
        yearText = (EditText) findViewById(R.id.yearText);

        //Check Internet Connectivity

        if(!isConnected())
        {
            Toast toast = Toast.makeText(this,"No Internet detected, please check your Internet connectivity!", Toast.LENGTH_LONG);
            toast.show();
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input
                if(seriesText.getText() == null || yearText.getText() == null) {
                    Toast toast =Toast.makeText(MarvelAPI.this, "Empty series name query or empty start year field!", Toast.LENGTH_LONG);
                    toast.show();

                }
                else {
                    seriesNameQuery = seriesText.getText().toString();
                    year = yearText.getText().toString();

                    GetSeriesAPI getSeriesAPI = new GetSeriesAPI(MarvelAPI.this, progressBar);
                    getSeriesAPI.execute(seriesNameQuery,year,APIKey,privateKey);
                }

            }
        });


    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;

    }

    @Override
    public void handleData(ArrayList<Series> data) {
        if (data.size() > 0) {
            Log.d("demo", data.toString());
            this.series = data;

            ListView listView = (ListView)findViewById(R.id.seriesListView);
            SeriesAdapter adapter = new SeriesAdapter(MarvelAPI.this, R.layout.list_item, series);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MarvelAPI.this, Characters.class);
                    Log.d("demo", "" + position);
                    Log.d("demo", "" + series.get(position).toString());
                    i.putExtra(SERIES_KEY, series.get(position));
                    startActivity(i);
                }
            });
        } else {
            Toast.makeText(MarvelAPI.this, "No series found", Toast.LENGTH_SHORT).show();

        }
    }
}
