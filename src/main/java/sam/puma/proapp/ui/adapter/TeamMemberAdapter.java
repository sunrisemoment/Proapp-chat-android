package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.TeamMember;

/**
 * Created by puma on 02.08.2016.
 */
public class TeamMemberAdapter extends ArrayAdapter{
    Activity context;
    ArrayList<TeamMember> members;



    @SuppressWarnings("unchecked")
    public TeamMemberAdapter(Activity context,ArrayList<TeamMember> arraymodel) {
        super(context, R.layout.team_member_listview_item, arraymodel);
        this.context = context;
        this.members = arraymodel;
    }

    @Override
    public int getCount() {
        return members.size();
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
            row = inflater.inflate(R.layout.team_member_listview_item, null);

        }
        TextView name = (TextView) row.findViewById(R.id.team_member_item_name);
        TextView privilege = (TextView) row.findViewById(R.id.team_member_item_position);
        name.setText(members.get(position).name.split("_")[0]+"@"+members.get(position).name.split("_")[1]);
        if (members.get(position).teamCreatorId == members.get(position).userId){
           privilege.setText(R.string.command_team_leader);
        } else if (members.get(position).teamLeaderId == members.get(position).userId){
            privilege.setText(R.string.command_assistant_team_leader);
        } else {
            privilege.setText("");
        }

        return row;

    }
}
