package sam.puma.proapp.xmpp.stanzas.streammgmt;

import sam.puma.proapp.xmpp.stanzas.AbstractStanza;

public class AckPacket extends AbstractStanza {

	public AckPacket(int sequence, int smVersion) {
		super("a");
		this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
		this.setAttribute("h", Integer.toString(sequence));
	}

}
