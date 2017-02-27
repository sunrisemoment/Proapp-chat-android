package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.SummaryProdcutivity;

/**
 * Created by puma on 15.07.2016.
 */
public class ProductivitySummaryAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<SummaryProdcutivity> summaryProdcutivities;

    @SuppressWarnings("unchecked")
    public ProductivitySummaryAdapter(Activity context, ArrayList<SummaryProdcutivity> arraymodel) {
        super(context, R.layout.productivity_summary_item, arraymodel);
        this.context = context;
        this.summaryProdcutivities = arraymodel;
    }

    @Override
    public int getCount() {
        return summaryProdcutivities.size();
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

        if(position == 0) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.productivity_summary_header, null);
        } else {
            if (row == null||row.equals(R.layout.report_listview_header)) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.productivity_summary_item, null);
            }
            TextView nameText = (TextView) row.findViewById(R.id.productivity_summary_name);
            TextView starttimeText = (TextView) row.findViewById(R.id.productivity_summary_work);
            TextView finishTimeText = (TextView) row.findViewById(R.id.productivity_summary_object);
            TextView workText = (TextView) row.findViewById(R.id.productivity_summary_productivity);


            nameText.setText(summaryProdcutivities.get(position).getName());
            starttimeText.setText(summaryProdcutivities.get(position).getWork());
            finishTimeText.setText(summaryProdcutivities.get(position).getObject());
            workText.setText(summaryProdcutivities.get(position).getWork());
        }
        return row;

    }
}
