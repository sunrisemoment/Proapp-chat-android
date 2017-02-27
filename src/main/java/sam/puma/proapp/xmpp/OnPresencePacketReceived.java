package sam.puma.proapp.xmpp;

import sam.puma.proapp.entities.Account;
import sam.puma.proapp.xmpp.stanzas.PresencePacket;

public interface OnPresencePacketReceived extends PacketReceived {
	public void onPresencePacketReceived(Account account, PresencePacket packet);
}
