package sam.puma.proapp.xmpp;

import sam.puma.proapp.entities.Account;

public interface OnMessageAcknowledged {
	public void onMessageAcknowledged(Account account, String id);
}
