package mad.sis.uncc.listviewexercisetemplate;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;

public class SeriesJsonParser {


    static public ArrayList<Series> parseJsonString(String jstring)
    {
        ArrayList <Series> srList = null;
        try
        {
            JSONObject root = new JSONObject(jstring);
            //get the Series list
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return srList;
    }
}
