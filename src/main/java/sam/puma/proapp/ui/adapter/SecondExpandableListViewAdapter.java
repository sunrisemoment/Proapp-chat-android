package sam.puma.proapp.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.OcWorksource;
import sam.puma.proapp.entities.OcWorktime;

/**
 * Created by Anton on 7/5/2016.
 */
public class SecondExpandableListViewAdapter extends BaseExpandableListAdapter{
//    private static SecondExpandableListViewAdapter ourInstance = new SecondExpandableListViewAdapter();
//
//    public static SecondExpandableListViewAdapter getInstance() {
//        return ourInstance;
//    }

    private Context context;
    private List<OcWorksource> listGroup;
    private HashMap<OcWorksource, List<OcWorktime>> listChild;

    private SecondExpandableListViewAdapter() {
    }

    public SecondExpandableListViewAdapter(Context context, List<OcWorksource> listGroup,
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
        try {
            return listChild.get(listGroup.get(groupPosition)).size();
        } catch (Exception e) {
            return 0;
        }


    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        OcWorksource worksource = (OcWorksource) getGroup(groupPosition);
        List<OcWorktime> worktimes = new ArrayList<>();
        worktimes = listChild.get(worksource);
        return worktimes.get(childPosition);
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
        return true;
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
        assignedwork.setText("Accept "+listGroup.get(groupPosition).getOfferdWorkSentence());
        assignUser.setText(listGroup.get(groupPosition).worksourceProviderName.split("_")[0]+"  >>  "+listGroup.get(groupPosition).worksourceWorkerName.split("_")[0]);
        assignedTime.setText(listGroup.get(groupPosition).worksourceCreateTime);
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
        float scale = row.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (30*scale + 0.5f);
        row.setPaddingRelative(dpAsPixels,0,0,0);
        if(childPosition%2 == 1){
            assignedwork.setText("Finish " + assignWork.getWorkSentence());
            assignedTime.setText(assignWork.getDefaultTimeZone(assignWork.getEndDateTime()));
            if(assignWork.getEndDateTime().equals("null")){
                row.setVisibility(View.GONE);
                assignUser.setText(assignWork.userName.split("_")[0]);
                return row;
            } else {
                assignUser.setText(assignWork.userName.split("_")[0]);
                row.setVisibility(View.VISIBLE);
                return row;
            }
        } else {
            assignedwork.setText("Start " + assignWork.getWorkSentence());
            assignedTime.setText(assignWork.getDefaultTimeZone(assignWork.getStartDateTime()));
            assignUser.setText(assignWork.userName.split("_")[0]);
            row.setVisibility(View.VISIBLE);
            return row;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        Log.d("SecondLevelAdapter", "Unregistering observer");
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

}
