package sam.puma.proapp.httpclient;
import android.app.Application;

public class ProappApplication extends Application {
	
	private static WebClient mClient;

	public ProappApplication() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * for single tone
	 * @return
	 */
	public static WebClient sharedClient() {
		if (mClient == null) {
			mClient = new WebClient();
		}
		return mClient;
	}
}
