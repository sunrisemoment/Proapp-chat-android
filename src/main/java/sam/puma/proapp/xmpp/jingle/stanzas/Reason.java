package sam.puma.proapp.xmpp.jingle.stanzas;

import sam.puma.proapp.xml.Element;

public class Reason extends Element {
	private Reason(String name) {
		super(name);
	}

	public Reason() {
		super("reason");
	}
}
