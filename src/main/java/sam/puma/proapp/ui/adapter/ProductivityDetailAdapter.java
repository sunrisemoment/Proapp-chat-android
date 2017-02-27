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
 * Created by puma on 18.07.2016.
 */
public class ProductivityDetailAdapter extends ArrayAdapter {
    Activity context;
    private ArrayList<Report> detailProductivitys =new ArrayList<>();

    @SuppressWarnings("unchecked")
    public ProductivityDetailAdapter(Activity context, ArrayList<Report> arraymodel) {
        super(context, 0 , arraymodel);
        this.context = context;
        this.detailProductivitys = arraymodel;
    }

    @Override
    public int getCount() {
        return detailProductivitys.size();
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
            row = inflater.inflate(R.layout.productivity_detail_header, null);
            LinearLayout namelayout = (LinearLayout)row.findViewById(R.id.productivity_detail_name_layout);
            LinearLayout startLayout = (LinearLayout)row.findViewById(R.id.productivity_detail_start_layout);
            LinearLayout workLayout = (LinearLayout)row.findViewById(R.id.productivity_detail_work_layout);
            LinearLayout objectLayout = (LinearLayout)row.findViewById(R.id.productivity_detail_object_layout);
            LinearLayout productivityLayout = (LinearLayout)row.findViewById(R.id.productivity_detail_productivity_layout);

            namelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort","name");
                }
            });
            startLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort","start");
                }
            });
            workLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort","work");
                }
            });
            objectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort","object");
                }
            });
            productivityLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("sort","productivity");
                }
            });
        } else {
            if (row == null||(row.findViewById(R.id.productivity_detail_name) == null)) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.productivity_detail_item, null);
            }
            TextView nameText = (TextView) row.findViewById(R.id.productivity_detail_name);
            TextView startText = (TextView) row.findViewById(R.id.productivity_detail_start);
            TextView workText = (TextView) row.findViewById(R.id.productivity_detail_work);
            TextView objectText = (TextView) row.findViewById(R.id.productivity_detail_object);
            TextView productivityText = (TextView) row.findViewById(R.id.productivity_detail_productivity);
            if(detailProductivitys.get(position).getName().contains("_"))
            nameText.setText(detailProductivitys.get(position).getName().split("_")[0]);
            startText.setText(timeParse(detailProductivitys.get(position).getStartTime()));
            workText.setText(detailProductivitys.get(position).getWork());
            objectText.setText(detailProductivitys.get(position).getObject());
            productivityText.setText(String.valueOf(detailProductivitys.get(position).getProductivity()));

            nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("name",detailProductivitys.get(position).getName());
                }
            });
            startText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            workText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("work",detailProductivitys.get(position).getWork());
                }
            });
            objectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    broadCastIntent("object",detailProductivitys.get(position).getObject());
                }
            });
            productivityText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return row;

    }

    public void broadCastIntent(String status, String value){
        Intent itemIntent = new Intent();
        itemIntent.setAction("PRODUCTIVITY_DETAIL_ITEM_SELECTED");
        itemIntent.putExtra("status", status);
        itemIntent.putExtra("value" , value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(itemIntent);
    }

    public String timeParse(String time){
        String year = time.split("-")[0].substring(2,4);
        String month = time.split("-")[1];
        String day = time.split("-")[2].split(" ")[0];
        String hour = time.split("-")[2].split(" ")[1].split(":")[0];
        String min = time.split("-")[2].split(" ")[1].split(":")[1];
        return year+"-"+month+"-"+day+" "+hour+"-"+min;
    }
}
