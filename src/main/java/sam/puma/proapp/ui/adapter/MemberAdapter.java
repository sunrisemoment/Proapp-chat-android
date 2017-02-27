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
 * Created by Anton on 6/22/2016.
 */
public class MemberAdapter extends ArrayAdapter{
    Activity context;
    ArrayList<TeamMember> members;


    @SuppressWarnings("unchecked")
    public MemberAdapter(Activity context,ArrayList<TeamMember> arraymodel) {
        super(context, R.layout.command_listview_row, arraymodel);
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
            row = inflater.inflate(R.layout.command_listview_row, null);

        }
        TextView command = (TextView) row.findViewById(R.id.command_list_text);
        command.setText(members.get(position).name.split("_")[0]+"@"+members.get(position).name.split("_")[1]);

        return row;

    }
}
