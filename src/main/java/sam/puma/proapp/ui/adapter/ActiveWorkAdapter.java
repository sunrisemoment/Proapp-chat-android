package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.OcWorktime;

/**
 * Created by Anton on 6/28/2016.
 */
public class ActiveWorkAdapter extends ArrayAdapter{
    Activity context;
    ArrayList<OcWorktime> activeWorks;

    @SuppressWarnings("unchecked")
    public ActiveWorkAdapter(Activity context, ArrayList<OcWorktime> arraymodel) {
        super(context, R.layout.command_listview_row, arraymodel);
        this.context = context;
        this.activeWorks = arraymodel;
    }

    @Override
    public int getCount() {
        return activeWorks.size();
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
        TextView work_object_quantity = (TextView) row.findViewById(R.id.command_list_text);
        work_object_quantity.setText(activeWorks.get(position).getWorkSentence());

        return row;

    }
}
