package sam.puma.proapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.OcWorkroot;
import sam.puma.proapp.entities.OcWorksource;
import sam.puma.proapp.entities.OcWorktime;
import sam.puma.proapp.ui.widget.CustomExpandableListView;

/**
 * Created by Anton on 7/5/2016.
 */
public class ParentLevelListAdapter extends BaseExpandableListAdapter {
    private static ParentLevelListAdapter ourInstance = new ParentLevelListAdapter();

    public static ParentLevelListAdapter getInstance() {
        return ourInstance;
    }

    private Context context;
    private List<OcWorkroot> listGroup;
    private HashMap<OcWorkroot, List<OcWorksource>> listChild;
    private HashMap<OcWorksource, List<OcWorktime>> listChildChild;

    private ParentLevelListAdapter() {
    }

    public ParentLevelListAdapter(Context context, List<OcWorkroot> listGroup,
                                     HashMap<OcWorkroot, List<OcWorksource>> listChild,HashMap<OcWorksource, List<OcWorktime>> listChildChild) {
        this.context = context;
        this.listGroup = listGroup;
        this.listChild = listChild;
        this.listChildChild = listChildChild;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        int size = listChild.get(listGroup.get(groupPosition)).size();
        return 1;
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

        OcWorkroot parentNode = (OcWorkroot) getGroup(groupPosition);
        List<OcWorksource> childSources = listChild.get(parentNode);
        String name = "";
        String names = "";
        for (OcWorksource source : childSources){
            if(!name.equals(source.worksourceWorkerName)){
                if(name.equals("")) {
                    names += source.worksourceWorkerName;
                } else {
                    names += source.worksourceWorkerName + ",";
                }
            }
            name = source.worksourceWorkerName;
        }

        row.setVisibility(View.VISIBLE);
        TextView work_object_quantity = (TextView) row.findViewById(R.id.work_offer_work_object);
        work_object_quantity.setText("Offer "+listGroup.get(groupPosition).workKeyword+" "+listGroup.get(groupPosition).objectKeyword+" "+listGroup.get(groupPosition).workrootQuantity);

        TextView expire_time = (TextView) row.findViewById(R.id.work_offer_expire_time);
        expire_time.setText(listGroup.get(groupPosition).getDefaultTimeZone());

        TextView username = (TextView)row.findViewById(R.id.accept_user_name);
        if(listGroup.get(groupPosition).userName.contains("_"))
            if(!names.equals("")) {
                username.setText(listGroup.get(groupPosition).userName.split("_")[0] + " >> " + names.split("_")[0]);
            } else {
                username.setText(listGroup.get(groupPosition).userName.split("_")[0]);
            }
        ImageView image = (ImageView)row.findViewById(R.id.offer_avatar_imageview);

        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {



        final CustomExpandableListView secondLevelExpListView = new CustomExpandableListView(this.context);
        OcWorkroot parentNode = (OcWorkroot) getGroup(groupPosition);
        List<OcWorksource> childSources = listChild.get(parentNode);
        secondLevelExpListView.setAdapter(new SecondExpandableListViewAdapter(context, childSources , listChildChild));
        secondLevelExpListView.setGroupIndicator(null);
        secondLevelExpListView.setDivider(null);
//        secondLevelExpListView.setSelectedGroup(groupPosition);
        return secondLevelExpListView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
