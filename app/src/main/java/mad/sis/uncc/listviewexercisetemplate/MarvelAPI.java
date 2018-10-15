package mad.sis.uncc.listviewexercisetemplate;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MarvelAPI extends AppCompatActivity {
    //you should fill those two keys as you receive them from the API website
    String APIKey = "";
    String privateKey = "";

    //parameters
    String seriesNameQuery = null;
    String year = null;
    EditText seriesText = null;
    EditText yearText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marvel_api);
        setTitle("Marvel API App");
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
                    GetSeriesAPI getSeriesAPI = new GetSeriesAPI();
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
}
