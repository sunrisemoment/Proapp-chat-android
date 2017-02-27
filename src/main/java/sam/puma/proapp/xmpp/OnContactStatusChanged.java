package sam.puma.proapp.xmpp;

import sam.puma.proapp.entities.Contact;

public interface OnContactStatusChanged {
	public void onContactStatusChanged(final Contact contact, final boolean online);
}
