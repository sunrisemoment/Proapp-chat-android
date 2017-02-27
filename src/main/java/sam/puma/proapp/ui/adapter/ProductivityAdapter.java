package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.Report;

/**
 * Created by puma on 17.07.2016.
 */
public class ProductivityAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<Report> reportworks =new ArrayList<Report>();

    @SuppressWarnings("unchecked")
    public ProductivityAdapter(Activity context, ArrayList<Report> arraymodel) {
        super(context, R.layout.productivity_summary_header , arraymodel);
        this.context = context;
        this.reportworks = arraymodel;
    }

    @Override
    public int getCount() {
        return reportworks.size();
    }

    /**
     * get view cell for command table
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(position == 0) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.productivity_summary_header, null);//name layout define.
            LinearLayout namelayout = (LinearLayout)row.findViewById(R.id.productivity_header_username);
            LinearLayout workLayout = (LinearLayout)row.findViewById(R.id.productivity_header_work);
            LinearLayout objectLayout = (LinearLayout)row.findViewById(R.id.productivity_header_object);
            LinearLayout productivity = (LinearLayout)row.findViewById(R.id.productivity_header_productivity);
            namelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort", "name");
                }
            });
            workLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort", "work");
                }
            });
            objectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort", "object");
                }
            });
            productivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else {
            if (row == null||row.findViewById(R.id.productivity_summary_name) == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.productivity_summary_item, null);
            }
            TextView nameText = (TextView) row.findViewById(R.id.productivity_summary_name);
            TextView workText = (TextView) row.findViewById(R.id.productivity_summary_work);
            TextView objectText = (TextView) row.findViewById(R.id.productivity_summary_object);
            TextView productivity = (TextView) row.findViewById(R.id.productivity_summary_productivity);

            nameText.setText(reportworks.get(position).getName().split("_")[0]);
            workText.setText(reportworks.get(position).getWork());
            objectText.setText(reportworks.get(position).getObject());
            productivity.setText(String.valueOf(reportworks.get(position).getSubProductivity()));
            nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("name", reportworks.get(position).getName());
                }
            });
            workText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("work", reportworks.get(position).getWork());
                }
            });
            objectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("object", reportworks.get(position).getObject());
                }
            });
        }
        return row;

    }

    public void broadCastIntent(String status, String value){
        Intent itemIntent = new Intent();
        itemIntent.setAction("PRODUCTIVITY_SUMMARY_ITEM_SELECTED");
        itemIntent.putExtra("status", status);
        itemIntent.putExtra("value" , value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(itemIntent);
    }
}

