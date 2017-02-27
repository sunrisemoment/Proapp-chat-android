package sam.puma.proapp.xmpp;

import sam.puma.proapp.entities.Account;

public interface OnStatusChanged {
	public void onStatusChanged(Account account);
}
