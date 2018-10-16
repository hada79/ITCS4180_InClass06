package mad.sis.uncc.listviewexercisetemplate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class GetSeriesAPI extends AsyncTask <String,Integer,ArrayList> {

    ProgressBar progressBar;
    IData iData;

    public GetSeriesAPI(IData iData, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.iData = iData;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        progressBar.setVisibility(View.GONE);
        iData.handleData(arrayList);
        super.onPostExecute(arrayList);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
        super.onProgressUpdate(values);
    }

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

                JSONObject root = new JSONObject(response);
                JSONObject data = root.getJSONObject("data");

                JSONArray series = data.getJSONArray("results");

                for (int i = 0; i < series.length(); i++) {

                    JSONObject seriesJson = series.getJSONObject(i);
                    Series s = new Series();
                    s.id = seriesJson.getString("id");
                    s.name = seriesJson.getString("title");
                    s.description = seriesJson.getString("description");

                    JSONObject thumbnails = seriesJson.getJSONObject("thumbnail");
                    s.imgUrl = thumbnails.getString("path") + "." + thumbnails.getString("extension");

                    // URL
                    JSONArray urls = seriesJson.getJSONArray("urls");
                    JSONObject urlsJson = urls.getJSONObject(0);
                    s.url = urlsJson.getString("url");

                    // CHARACTERS
                    ArrayList<SrCharacter> chrList = new ArrayList<SrCharacter>();
                    JSONObject charactersJson = seriesJson.getJSONObject("characters");
                    JSONArray items = charactersJson.getJSONArray("items");

                    for (int j = 0; j < items.length(); j++) {
                        JSONObject itemsJson = items.getJSONObject(j);
                        SrCharacter c = new SrCharacter();

                        //c.description = charactersJson.getString("description");
                        c.name = itemsJson.getString("name");
                        //c.imgUrl = charactersJson.getString("imgUrl");

                        chrList.add(c);
                    }

                    s.characters = chrList;
                    srList.add(s);

                    int progress = (int) ((i+1) * 100.0f / series.length());
                    publishProgress(progress);
                }
            }

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
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

    public static interface IData {
        public void handleData(ArrayList<Series> data);
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
