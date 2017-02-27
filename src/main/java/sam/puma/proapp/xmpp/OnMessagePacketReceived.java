package sam.puma.proapp.xmpp;

import sam.puma.proapp.entities.Account;
import sam.puma.proapp.xmpp.stanzas.MessagePacket;

public interface OnMessagePacketReceived extends PacketReceived {
	public void onMessagePacketReceived(Account account, MessagePacket packet);
}
