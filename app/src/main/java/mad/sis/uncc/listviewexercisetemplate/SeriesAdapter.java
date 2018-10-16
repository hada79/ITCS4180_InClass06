package mad.sis.uncc.listviewexercisetemplate;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

    public class SeriesAdapter extends ArrayAdapter<Series>{

    ArrayList<Series> series;
    public SeriesAdapter(Context context, int resource, ArrayList<Series> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Series series = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);
            viewHolder.urlView = (TextView) convertView.findViewById(R.id.urlView);
            viewHolder.descriptionView = (TextView) convertView.findViewById(R.id.descriptionView);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set the data from the email object
        viewHolder.nameView.setText(series.name);
        viewHolder.urlView.setText(series.url);
        viewHolder.descriptionView.setText(series.description);

        Picasso.get().load(series.imgUrl).into(viewHolder.imageView);

        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView nameView;
        ImageView imageView;
        TextView urlView;
        TextView descriptionView;
    }

}
