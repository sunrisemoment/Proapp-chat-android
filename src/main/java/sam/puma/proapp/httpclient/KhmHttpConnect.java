package sam.puma.proapp.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

/**
 * UsingExampleForPostMethod:
 * HttpPost searchRequest = new HttpPost( new URI(SEARCH_URI) );
 * List<NameValuePair> parameters = new ArrayList<NameValuePair>();
 * parameters.add(new BasicNameValuePair("appid","YahooDemo"));
 * parameters.add(new BasicNameValuePair("query","searchquery"));
 * searchRequest.setEntity(new UrlEncodedFormEntity(parameters));
 * KhmHttpConnect task = new KhmHttpConnect(... ...);
 * task.execute(searchRequest);
 * 
 * */
public class KhmHttpConnect extends AsyncTask<HttpUriRequest, Void, String> { 
	    public static final String HTTP_RESPONSE = "khm_httpResponse"; 
	    private OnCompleteListener mListener;
	    private HttpClient mClient;
	    private Exception ex;

	    public interface OnCompleteListener{
			void onComplete(String response, Exception e);
		}
	    
	    public KhmHttpConnect(OnCompleteListener listener) { 
	        mListener = listener;
	        mClient = new DefaultHttpClient();
	    }
	    public KhmHttpConnect(HttpClient client, OnCompleteListener listener) { 
	        mClient = client;
	        mListener = listener;
	    }
	    
	    @Override 
	    protected String doInBackground(HttpUriRequest... params) { 
	        try{ 
	            HttpUriRequest request = params[0]; 
	            HttpResponse serverResponse = mClient.execute(request); 
	            BasicResponseHandler handler = new BasicResponseHandler(); 
	            String response = handler.handleResponse(serverResponse);
	            ex = null;
	            return response;
	            /*@return value in doInBackground is passed as param to onPostExecute.
	            This is defined in AsyncTask<HttpUriRequest, Void, String> as AsyncTask's generic types
	            */
	        } catch (Exception e) {
	        	ex = e;
	            return null; 
	        } 
	    }
	    
	    @Override 
	    protected void onPostExecute(String result) {

		    mListener.onComplete(result, ex);
	    }
	    
	    public void setOnComplete(OnCompleteListener listener){
	    	mListener = listener;
	    }
	}
	
