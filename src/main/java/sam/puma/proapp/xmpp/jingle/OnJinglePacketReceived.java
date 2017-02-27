package sam.puma.proapp.xmpp.jingle;

import sam.puma.proapp.entities.Account;
import sam.puma.proapp.xmpp.PacketReceived;
import sam.puma.proapp.xmpp.jingle.stanzas.JinglePacket;

public interface OnJinglePacketReceived extends PacketReceived {
	void onJinglePacketReceived(Account account, JinglePacket packet);
}
