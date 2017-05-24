package adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongnd.screenenglish.R;

import java.util.List;

import databases.Subject;

/**
 * Created by DongND on 5/18/2017.
 */

public class DialogAdapter extends ArrayAdapter<Subject>{

    public Activity context;
    public List<Subject> list;
    public int layoutId;

    public DialogAdapter(Activity context, int layoutId, List<Subject> list){
        super(context, layoutId, list);
        this.context=context;
        this.list=list;
        this.layoutId=layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.item_dialog, parent,  false);

        Subject subject=list.get(position);
        Bitmap bitmap= BitmapFactory.decodeByteArray(subject.getImg(), 0, subject.getImg().length);
        TextView title=(TextView)view.findViewById(R.id.item_dialog_text);
        ImageView img=(ImageView)view.findViewById(R.id.item_dialog_img);

        title.setText(subject.getSb_title());
        img.setImageBitmap(bitmap);

        return view;
    }
}
