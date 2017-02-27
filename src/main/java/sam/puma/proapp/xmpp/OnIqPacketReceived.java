package sam.puma.proapp.xmpp;

import sam.puma.proapp.entities.Account;
import sam.puma.proapp.xmpp.stanzas.IqPacket;

public interface OnIqPacketReceived extends PacketReceived {
	public void onIqPacketReceived(Account account, IqPacket packet);
}
