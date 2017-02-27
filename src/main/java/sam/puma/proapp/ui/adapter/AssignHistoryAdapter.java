package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.AssignWork;

/**
 * Created by Anton on 6/23/2016.
 */
public class AssignHistoryAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<AssignWork> assignedWorks;

    @SuppressWarnings("unchecked")
    public AssignHistoryAdapter(Activity context, ArrayList<AssignWork> arraymodel) {
        super(context, R.layout.offer_listview_item, arraymodel);
        this.context = context;
        this.assignedWorks = arraymodel;
    }

    @Override
    public int getCount() {
        return assignedWorks.size();
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
            row = inflater.inflate(R.layout.offer_listview_item, null);
        }
        TextView work_object_quantity = (TextView) row.findViewById(R.id.work_offer_work_object);
        work_object_quantity.setText(assignedWorks.get(position).getWorkSentence());
        TextView datetime = (TextView)row.findViewById(R.id.work_offer_expire_time);
        if(assignedWorks.get(position).isFinished){
            datetime.setText("Assigned"+assignedWorks.get(position).assignedTime+" assigned : "+assignedWorks.get(position).startTime+"~"+assignedWorks.get(position).finishTime);
        } else {
            datetime.setText(assignedWorks.get(position).assignedTime+" assigned : "+assignedWorks.get(position).startTime+" started");
        }

        TextView assignUser = (TextView)row.findViewById(R.id.accept_user_name);
        assignUser.setText("from "+assignedWorks.get(position).assignUserName.split("_")[0]+" to "+assignedWorks.get(position).assignedUserName.split("_")[0]);


        return row;

    }

}
