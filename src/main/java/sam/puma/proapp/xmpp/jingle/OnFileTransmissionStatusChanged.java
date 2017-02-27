package sam.puma.proapp.xmpp.jingle;

import sam.puma.proapp.entities.DownloadableFile;

public interface OnFileTransmissionStatusChanged {
	void onFileTransmitted(DownloadableFile file);

	void onFileTransferAborted();
}
