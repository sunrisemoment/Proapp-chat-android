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
import sam.puma.proapp.entities.AcceptWork;
import sam.puma.proapp.entities.OcWorkroot;
import sam.puma.proapp.entities.OcWorksource;
import sam.puma.proapp.entities.OfferWork;

/**
 * Created by Anton on 6/20/2016.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static ExpandableListViewAdapter ourInstance = new ExpandableListViewAdapter();

    public static ExpandableListViewAdapter getInstance() {
        return ourInstance;
    }

    private Context context;
    private List<OcWorkroot> listGroup;
    private HashMap<OcWorkroot, List<OcWorksource>> listChild;

    private ExpandableListViewAdapter() {
    }

    public ExpandableListViewAdapter(Context context, List<OcWorkroot> listGroup,
                                   HashMap<OcWorkroot, List<OcWorksource>> listChild) {
        this.context = context;
        this.listGroup = listGroup;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
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
        row.setVisibility(View.VISIBLE);
        TextView work_object_quantity = (TextView) row.findViewById(R.id.work_offer_work_object);
        work_object_quantity.setText("Offer "+listGroup.get(groupPosition).workKeyword+" "+listGroup.get(groupPosition).objectKeyword+" "+listGroup.get(groupPosition).workrootQuantity);

        TextView expire_time = (TextView) row.findViewById(R.id.work_offer_expire_time);
        expire_time.setText(listGroup.get(groupPosition).workrootDateTime);

        TextView username = (TextView)row.findViewById(R.id.accept_user_name);
        if(listGroup.get(groupPosition).userName.contains("_"))
        username.setText(listGroup.get(groupPosition).userName.split("_")[0]);
        ImageView image = (ImageView)row.findViewById(R.id.offer_avatar_imageview);
//        image.setVisibility(View.GONE);
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
        OcWorksource accept = (OcWorksource)getChild(groupPosition,childPosition);

        TextView work_object_quantity = (TextView) row.findViewById(R.id.work_offer_work_object);
        work_object_quantity.setText("Accept "+accept.worksourceWorkKeyword+" "+accept.worksourceObjectKeyword+" "+accept.worksourceQuantity);

        TextView accept_time = (TextView) row.findViewById(R.id.work_offer_expire_time);
        accept_time.setText(accept.worksourceCreateTime);

        TextView username = (TextView)row.findViewById(R.id.accept_user_name);
        if(accept.worksourceWorkerName.contains("_"))
        username.setText(accept.worksourceWorkerName.split("_")[0]);
        ImageView image = (ImageView)row.findViewById(R.id.offer_avatar_imageview);
        image.setVisibility(View.GONE);
        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
