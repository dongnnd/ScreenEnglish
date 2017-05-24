package adapter;

/**
 * Created by DongND on 5/1/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongnd.screenenglish.R;

import java.util.ArrayList;

/**
 * Created by mukesh on 18/5/15.
 */
public class NotificationAdapter extends BaseAdapter {

    Context context;
    ArrayList<NotificationItem> nsList;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> nslList) {
        this.context = context;
        this.nsList = nslList;
    }

    @Override
    public int getCount() {
        return nsList.size();
    }

    @Override
    public Object getItem(int position) {
        return nsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.item_notification, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_notifi_title);
        TextView textContent=(TextView)rowView.findViewById(R.id.item_notifi_content);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_notifi_img);

        NotificationItem m = nsList.get(position);
        txtTitle.setText(m.getTitle());
        textContent.setText(m.getContent());
        if(m != null && m.getImg() !=null) {

            imageView.setImageBitmap(m.getImg());
        }else{

        }
        return rowView;

    };
}