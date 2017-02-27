package sam.puma.proapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sam.puma.proapp.Config;
import sam.puma.proapp.R;
import sam.puma.proapp.entities.Account;
import sam.puma.proapp.ui.ManageAccountActivity;
import sam.puma.proapp.ui.XmppActivity;
import sam.puma.proapp.ui.widget.Switch;

public class AccountAdapter extends ArrayAdapter<Account> {

	private XmppActivity activity;

	public AccountAdapter(XmppActivity activity, List<Account> objects) {
		super(activity, 0, objects);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Account account = getItem(position);
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.account_row, parent, false);
		}
		TextView jid = (TextView) view.findViewById(R.id.account_jid);
		if (Config.DOMAIN_LOCK != null) {
			/**
			 * anton's code for list of account.
			 */
			if(account.getJid().getLocalpart().contains("_")){
				jid.setText(account.getJid().getLocalpart().split("_")[0]);
			} else {
				jid.setText(account.getJid().getLocalpart().split("@")[0]);
			}
		} else {
			if(account.getJid().toBareJid().toString().contains("_")){
				jid.setText(account.getJid().toBareJid().toString().split("_")[0]);
			} else {
				jid.setText(account.getJid().toBareJid().toString().split("@")[0]);
			}
		}
		/***************************************************************/
		TextView statusView = (TextView) view.findViewById(R.id.account_status);
		ImageView imageView = (ImageView) view.findViewById(R.id.account_image);
		imageView.setImageBitmap(activity.avatarService().get(account, activity.getPixel(48)));
		statusView.setText(getContext().getString(account.getStatus().getReadableId()));
		switch (account.getStatus()) {
			case ONLINE:
				statusView.setTextColor(activity.getOnlineColor());
				break;
			case DISABLED:
			case CONNECTING:
				statusView.setTextColor(activity.getSecondaryTextColor());
				break;
			default:
				statusView.setTextColor(activity.getWarningTextColor());
				break;
		}
		final Switch tglAccountState = (Switch) view.findViewById(R.id.tgl_account_status);
		final boolean isDisabled = (account.getStatus() == Account.State.DISABLED);
		tglAccountState.setChecked(!isDisabled,false);
		tglAccountState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b == isDisabled && activity instanceof ManageAccountActivity) {
					((ManageAccountActivity) activity).onClickTglAccountState(account,b);
				}
			}
		});
		return view;
	}
}