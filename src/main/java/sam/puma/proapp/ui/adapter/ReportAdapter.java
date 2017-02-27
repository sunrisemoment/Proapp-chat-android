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
 * Created by puma on 14.07.2016.
 */
public class ReportAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<Report> reportworks;

    @SuppressWarnings("unchecked")
    public ReportAdapter(Activity context, ArrayList<Report> arraymodel) {
        super(context, 0 , arraymodel);
        this.context = context;
        this.reportworks = new ArrayList<>();
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
            row = inflater.inflate(R.layout.report_listview_header, null);
            LinearLayout namelayout = (LinearLayout)row.findViewById(R.id.report_header_name_layout);
            LinearLayout startLayout= (LinearLayout)row.findViewById(R.id.report_header_start_layout);
            LinearLayout endLayout = (LinearLayout)row.findViewById(R.id.report_header_end_layout);
            LinearLayout workLayout = (LinearLayout)row.findViewById(R.id.report_header_work_layout);
            LinearLayout objectLayout = (LinearLayout)row.findViewById(R.id.report_header_object_layout);
            namelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort", "name");
                }
            });
            startLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort", "start");
                }
            });
            endLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort", "end");
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

        } else {
            if (row == null||row.findViewById(R.id.report_complete_username) == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.report_listview_row, null);
            }
            TextView nameText = (TextView) row.findViewById(R.id.report_complete_username);
            TextView starttimeText = (TextView) row.findViewById(R.id.report_complete_start);
            TextView finishTimeText = (TextView) row.findViewById(R.id.report_complete_finish);
            TextView workText = (TextView) row.findViewById(R.id.report_complete_work);
            TextView objectText = (TextView) row.findViewById(R.id.report_complete_object);
            TextView quantityText = (TextView) row.findViewById(R.id.report_complete_quantity);
            nameText.setText(reportworks.get(position).getName().split("_")[0]);
            starttimeText.setText(reportworks.get(position).getDefaultTimeZone(reportworks.get(position).getStartTime()));
            if (reportworks.get(position).getEndTime().equals("null")&&reportworks.get(position).getAbort().equals("null")){
                finishTimeText.setText("");
            } else if(reportworks.get(position).getEndTime().equals("null")&&!reportworks.get(position).getAbort().equals("null")){
                finishTimeText.setText(reportworks.get(position).getDefaultTimeZone(reportworks.get(position).getAbort()));
            } else {
                finishTimeText.setText(reportworks.get(position).getDefaultTimeZone(reportworks.get(position).getEndTime()));
            }
            workText.setText(reportworks.get(position).getWork());
            objectText.setText(reportworks.get(position).getObject());
            quantityText.setText(String.valueOf(reportworks.get(position).getQuantity()));
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
        itemIntent.setAction("ITEM_SELECTED_INTENT");
        itemIntent.putExtra("status", status);
        itemIntent.putExtra("value" , value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(itemIntent);
    }
}
