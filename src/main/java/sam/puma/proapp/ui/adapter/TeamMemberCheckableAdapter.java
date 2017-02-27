package sam.puma.proapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sam.puma.proapp.R;
import sam.puma.proapp.entities.TeamMember;

/**
 * Created by puma on 02.08.2016.
 */
public class TeamMemberCheckableAdapter extends ArrayAdapter{
    Activity context;
    ArrayList<TeamMember> members;
    String status;
    View mLastView;


    @SuppressWarnings("unchecked")
    public TeamMemberCheckableAdapter(Activity context,ArrayList<TeamMember> arraymodel, String status) {
        super(context, R.layout.team_member_checkable_item, arraymodel);
        this.context = context;
        this.members = arraymodel;
        this.status = status;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.team_member_checkable_item, null);

        }
        TextView name = (TextView) row.findViewById(R.id.team_member_check_name);
        TextView privilege = (TextView) row.findViewById(R.id.team_member_check_position);
        name.setText(members.get(position).name.split("_")[0]+"@"+members.get(position).name.split("_")[1]);
        if (members.get(position).teamCreatorId == members.get(position).userId){
            privilege.setText(R.string.command_team_leader);
        } else if (members.get(position).teamLeaderId == members.get(position).userId){
            privilege.setText(R.string.command_assistant_team_leader);
        } else {
            privilege.setText("");
        }
        final CheckBox checkBox = (CheckBox)row.findViewById(R.id.team_member_check);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("remove")) {
                    if (members.get(position).isChecked) {
                        members.get(position).isChecked = false;
                    } else {
                        members.get(position).isChecked = true;
                    }
                    checkBox.setChecked(members.get(position).isChecked);
                } else if (status.equals("setTeamLeader")) {
//                    if (members.get(position).isChecked) {
//                        members.get(position).isChecked = false;
//                    } else {
//                        members.get(position).isChecked = true;
//                    }
//                    checkBox.setChecked(members.get(position).isChecked);
                    if (mLastView != null)
                        deselect(mLastView);
                    select(v);
                    mLastView = v;
                    for(TeamMember member : members){
                        member.isChecked = false;
                    }
                    members.get(position).isChecked = true;
                    checkBox.setChecked(members.get(position).isChecked);
                }
            }
        });
        return row;
    }

    private void select(View v) {
        LinearLayout parent = (LinearLayout) (v.getParent());
        CheckBox checkBox = (CheckBox)parent.findViewById(R.id.team_member_check);
        checkBox.setChecked(true);
    }

    private void deselect(View v) {
        LinearLayout parent = (LinearLayout) (v.getParent());
        CheckBox checkBox = (CheckBox)parent.findViewById(R.id.team_member_check);
        checkBox.setChecked(false);
    }
}
