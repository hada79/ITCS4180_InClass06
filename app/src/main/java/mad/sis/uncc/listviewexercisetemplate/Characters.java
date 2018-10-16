package mad.sis.uncc.listviewexercisetemplate;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Characters extends Activity {

    Series series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        if(getIntent() != null && getIntent().getExtras() != null) {
            series = (Series) getIntent().getExtras().getSerializable(MarvelAPI.SERIES_KEY);

            TextView title = (TextView) findViewById(R.id.charactersTitle);
            title.setText(series.name);

            ListView characterListView = (ListView) findViewById(R.id.characterListView);
            ArrayAdapter<SrCharacter> adapter = new ArrayAdapter<SrCharacter>(this, android.R.layout.simple_list_item_1, android.R.id.text1, series.characters);

            characterListView.setAdapter(adapter);

        }
    }

}
