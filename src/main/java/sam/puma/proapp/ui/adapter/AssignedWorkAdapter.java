package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.OcWorksource;

/**
 * Created by Anton on 6/22/2016.
 */
public class AssignedWorkAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<OcWorksource> allworks;
    int id;

    @SuppressWarnings("unchecked")
    public AssignedWorkAdapter(Activity context, ArrayList<OcWorksource> assignWorks) {
        super(context, R.layout.command_listview_row, assignWorks);
        this.context = context;
        this.allworks = assignWorks;
    }

    @Override
    public int getCount() {
        return this.allworks.size();
    }

    /**
     * get view cell for command table
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.command_listview_row, null);
        }
        if(allworks.get(position).worksource.trim().equals("section")) {
            TextView work_object_quantity = (TextView) row.findViewById(R.id.command_list_text);
            work_object_quantity.setText("Accepted Work");
            work_object_quantity.setBackgroundResource(R.drawable.popup_background_black);
            work_object_quantity.setTextColor(row.getResources().getColor(R.color.white));
            ImageView image = (ImageView)row.findViewById(R.id.command_list_imageview);
            image.setVisibility(View.INVISIBLE);
            row.setEnabled(false);
        } else if(allworks.get(position).worksource.trim().equals("start")) {
            TextView work_object_quantity = (TextView) row.findViewById(R.id.command_list_text);
            work_object_quantity.setText("Assigned Work");
            work_object_quantity.setBackgroundResource(R.drawable.popup_background_black);
            work_object_quantity.setTextColor(row.getResources().getColor(R.color.white));
            ImageView image = (ImageView)row.findViewById(R.id.command_list_imageview);
            image.setVisibility(View.INVISIBLE);
            row.setEnabled(false);
        } else {
            TextView work_object_quantity = (TextView) row.findViewById(R.id.command_list_text);
            work_object_quantity.setText(allworks.get(position).worksourceProviderName.split("_")[0] + ":" + allworks.get(position).getKeyWordSentence());
            work_object_quantity.setBackgroundResource(R.drawable.popup_background);
            ImageView image = (ImageView)row.findViewById(R.id.command_list_imageview);
            work_object_quantity.setTextColor(row.getResources().getColor(R.color.black87));
            image.setVisibility(View.VISIBLE);
            row.setEnabled(true);
        }
        return row;

    }


}
