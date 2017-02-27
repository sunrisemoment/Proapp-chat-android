package sam.puma.proapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.OcWorksource;
import sam.puma.proapp.entities.OcWorktime;


/**
 * Created by Anton on 6/26/2016.
 */
public class AssignHistoryExpandableAdapter extends BaseExpandableListAdapter {

    private static AssignHistoryExpandableAdapter ourInstance = new AssignHistoryExpandableAdapter();

    public static AssignHistoryExpandableAdapter getInstance() {
        return ourInstance;
    }

    private Context context;
    private List<OcWorksource> listGroup;
    private HashMap<OcWorksource, List<OcWorktime>> listChild;

    private AssignHistoryExpandableAdapter() {
    }

    public AssignHistoryExpandableAdapter(Context context, List<OcWorksource> listGroup,
                                          HashMap<OcWorksource, List<OcWorktime>> listChild) {
        this.context = context;
        this.listGroup = listGroup;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        int si = listGroup.size();
        return si;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.offer_listview_item, null);
        }
        TextView assignedwork = (TextView)row.findViewById(R.id.work_offer_work_object);
        TextView assignedTime = (TextView)row.findViewById(R.id.work_offer_expire_time);
        TextView assignUser = (TextView)row.findViewById(R.id.accept_user_name);
        if(listGroup.get(groupPosition).worksourceIsCloased){
            assignedwork.setText("Assign "+listGroup.get(groupPosition).getOfferdWorkSentence()+" (Aborted)");
        } else {
            assignedwork.setText("Assign "+listGroup.get(groupPosition).getOfferdWorkSentence());
        }
        assignUser.setText(listGroup.get(groupPosition).worksourceProviderName.split("_")[0]+"  >>  "+listGroup.get(groupPosition).worksourceWorkerName.split("_")[0]);
        assignedTime.setText(listGroup.get(groupPosition).getDefaultTimeZone());
        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.offer_listview_item, null);
        }
        OcWorktime assignWork = (OcWorktime)getChild(groupPosition,childPosition);
        TextView assignedwork = (TextView)row.findViewById(R.id.work_offer_work_object);
        TextView assignedTime = (TextView)row.findViewById(R.id.work_offer_expire_time);
        TextView assignUser = (TextView)row.findViewById(R.id.accept_user_name);
        if(childPosition%2 == 1){
            assignedwork.setText("Finish " + assignWork.getWorkSentence());
            assignedTime.setText(assignWork.getDefaultTimeZone(assignWork.getDefaultTimeZone(assignWork.getEndDateTime())));
            assignUser.setText(assignWork.userName.split("_")[0]);
            if(assignWork.getEndDateTime().equals("null")){
                row.setVisibility(View.GONE);
                return row;
            } else {
                row.setVisibility(View.VISIBLE);
                return row;
            }
        } else {
            assignedwork.setText("Start " + assignWork.getWorkSentence());
            assignedTime.setText(assignWork.getDefaultTimeZone(assignWork.getDefaultTimeZone(assignWork.getEndDateTime())));
            row.setVisibility(View.VISIBLE);
            assignUser.setText(assignWork.userName.split("_")[0]);
            return row;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}