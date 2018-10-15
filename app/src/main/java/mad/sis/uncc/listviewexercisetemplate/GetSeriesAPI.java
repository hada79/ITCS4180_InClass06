package mad.sis.uncc.listviewexercisetemplate;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class GetSeriesAPI extends AsyncTask <String,Integer,ArrayList> {

    @Override
    protected ArrayList<Series> doInBackground(String... strings) {
        //limit the response to 25 series no more
        long time = System.currentTimeMillis();
        String hash = generateHash(Long.toString(time),strings[2],strings[3]);
        Log.d("Test","Hash is "+hash);

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String response = null;
        ArrayList<Series> srList = new ArrayList<Series>();
        try {
            String urlString = "https://gateway.marvel.com:443/v1/public/series?titleStartsWith="+ URLEncoder.encode(strings[0].toLowerCase(),"UTF-8")+
                    "&startYear="+strings[1]+"&limit=25&ts="+time+"&apikey="+URLEncoder.encode(strings[2].toLowerCase(),"UTF-8")+"&hash="+URLEncoder.encode(hash.toLowerCase(),"UTF-8");
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();


            }
            else
            {
                Log.d("Test", "The code is "+code);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("test","the response is "+response);
        return srList;

    }
    //the code is taken from https://github.com/Karumi/MarvelApiClientAndroid/tree/master/MarvelApiClient/src
    private String generateHash(String timestamp, String publicKey, String privateKey)
    {
        try {
            String value = timestamp + privateKey + publicKey;
            MessageDigest md5Encoder = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5Encoder.digest(value.getBytes());

            StringBuilder md5 = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; ++i) {
                md5.append(Integer.toHexString((md5Bytes[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return md5.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
