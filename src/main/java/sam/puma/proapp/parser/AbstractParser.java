package sam.puma.proapp.parser;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sam.puma.proapp.entities.Account;
import sam.puma.proapp.entities.Contact;
import sam.puma.proapp.services.XmppConnectionService;
import sam.puma.proapp.xml.Element;
import sam.puma.proapp.xmpp.jid.Jid;
import sam.puma.proapp.xmpp.stanzas.AbstractStanza;

public abstract class AbstractParser {

	protected XmppConnectionService mXmppConnectionService;

	protected AbstractParser(XmppConnectionService service) {
		this.mXmppConnectionService = service;
	}

	public static Long getTimestamp(Element element, Long defaultValue) {
		Element delay = element.findChild("delay","urn:xmpp:delay");
		if (delay != null) {
			String stamp = delay.getAttribute("stamp");
			if (stamp != null) {
				try {
					return AbstractParser.parseTimestamp(delay.getAttribute("stamp")).getTime();
				} catch (ParseException e) {
					return defaultValue;
				}
			}
		}
		return defaultValue;
	}

	protected long getTimestamp(Element packet) {
		return getTimestamp(packet,System.currentTimeMillis());
	}

	public static Date parseTimestamp(String timestamp) throws ParseException {
		timestamp = timestamp.replace("Z", "+0000");
		SimpleDateFormat dateFormat;
		timestamp = timestamp.substring(0,19)+timestamp.substring(timestamp.length() -5,timestamp.length());
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",Locale.US);
		return dateFormat.parse(timestamp);
	}

	protected void updateLastseen(final AbstractStanza packet, final Account account, final boolean presenceOverwrite) {
		updateLastseen(getTimestamp(packet), account, packet.getFrom(), presenceOverwrite);
	}

	protected void updateLastseen(long timestamp, final Account account, final Jid from, final boolean presenceOverwrite) {
		final String presence = from == null || from.isBareJid() ? "" : from.getResourcepart();
		final Contact contact = account.getRoster().getContact(from);
		if (timestamp >= contact.lastseen.time) {
			contact.lastseen.time = timestamp;
			if (!presence.isEmpty() && presenceOverwrite) {
				contact.lastseen.presence = presence;
			}
		}
	}

	protected String avatarData(Element items) {
		Element item = items.findChild("item");
		if (item == null) {
			return null;
		}
		return item.findChildContent("data", "urn:xmpp:avatar:data");
	}
}
