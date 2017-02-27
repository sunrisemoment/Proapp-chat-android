package sam.puma.proapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import sam.puma.proapp.R;

/**
 * Created by Anton on 3/27/2016.
 */
public class ActivityAddJobs extends Activity{
    private EditText mAddJobEditText;
    private EditText mAddActionEditText1;
    private EditText mAddActionEditText2;
    private Button   addJobButton;
    private LinearLayout actionAddLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        mAddActionEditText1 = (EditText)findViewById(R.id.action_1);
        mAddActionEditText2 = (EditText)findViewById(R.id.action_2);
        mAddJobEditText = (EditText)findViewById(R.id.job_title_text);
        addJobButton = (Button)findViewById(R.id.add_action_button);
        actionAddLayout = (LinearLayout)findViewById(R.id.action_add_layout);

    }
void onAddActionButtonClick(View v){
    EditText edittext = new EditText(this);
    edittext.setHint("Action name");
    actionAddLayout.addView(edittext);
}
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }


}
