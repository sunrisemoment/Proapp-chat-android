package sam.puma.proapp.httpclient;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebClient {
/**
 * interface for receive data
 */
	public interface WebClientListener {
		public void onFinished(String response);
		public void onFailed(int status, String error);
	}
	private WebClientListener listener;

	public void setListener(WebClientListener listener) {
		this.listener = listener;
	}

	/************/

	private Context context;

	public WebClient(Context ctx) {
		context = ctx;
		myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 1000);
		HttpConnectionParams.setSoTimeout(myParams, 1000);
		httpClient = new DefaultHttpClient(myParams);
	}
	public WebClient() {
		myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 1000);
		HttpConnectionParams.setSoTimeout(myParams, 1000);
		httpClient = new DefaultHttpClient(myParams);
	}

	/**
	 * Table's Name
	 */
	public static final String OCWORKTEAM = "OcWorkteam";

	public static final String OCUSER = "OcUser";
	public static final String JABBER_ID = "jabber_id";
	public static final String COMPANY_ID = "company_id";
	public static final String COMPANY1_JABBER_CODE = "company_jabber_code";

	public static final String WORKTEAM_ID = "workteam_id";
	public static final String WORKTEAM_JABBER_ID = "workteam_jabber_id";
	public static final String WORKTEAM_NAME = "workteam_name";
	public static final String WORKTEAM_CREATOR_ID = "creator_id";
	public static final String COMPANY_JABBER_CODE = "company_jabber_code";

	public static final String OBJECT_KEYWORD_ID = "workteam_object_keyword_id";
	public static final String OBJECT_NAME = "object_name";
	public static final String OBJECT_CREATOR_ID = "creator_id";
	public static final String OCWORKTEAMOBJECTKEYWORD = "OcWorkteamObjectKeyword";

	public static final String WORK_KEYWORD_ID = "workteam_work_keyword_id";
	public static final String WORK_NAME = "work_name";
	public static final String WORK_CREATOR_USER_ID = "creator_user_id";
	public static final String OCWORKTEAMWORKKEYWORD = "OcWorkteamWorkKeyword";

	public static final String CREATETIME = "create_datetime";
	public static final String DELETETIME = "delete_datetime";
	public static final String DELETE_USER_ID = "delete_user_id";
	public static final String ISACTIVE = "isActive";

	public static final String STATUS = "status";
	public static final String DATA = "data";

	public static final String USERID = "user_id";

	public static final String WORKKEYWORDID = "work_keyword_id";
	public static final String OBJECTKEYWORDID = "object_keyword_id";
	public static final String QUANTITY = "quantity";
	public static final String WORKDURATIONMINUTES = "work_duration_minutes";
	public static final String APPRAISER_ID = "appraiser_id";
	public static final String APPRAISAL = "appraisal";
	public static final String ABORTBYUSERID = "abort_by_user_id";

	HttpParams myParams;
	DefaultHttpClient httpClient;

	public void getMyAccount(final String jid) throws Throwable {
		String url = "http://54.169.8.76/proapp/OcUsers/get_my_id";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WebClient.JABBER_ID,jid));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}



	/**
	 * get TeamList from web server {mobile_index action of teams controller }
	 * @throws Throwable
	 */
	public void getTeamID(String teamJid) throws Throwable {

		String url = "http://54.169.8.76/proapp/OcWorkteams/getteamid";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WebClient.WORKTEAM_JABBER_ID,teamJid));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}
			}
		}).execute(postMethod);
	}

	/**
	 * get team's work and team's object from the web client.
	 * @param team_id
	 * @throws Throwable
	 */
	public void getWorks(final int team_id) throws Throwable {
		String url = "http://54.169.8.76/proapp/OcWorkteamWorkKeywords/getworks";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WebClient.WORKTEAM_ID,String.valueOf(team_id).toString()));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}

	public void getObjects( int team_id) throws Throwable {
		String url = "http://54.169.8.76/proapp/OcWorkteamObjectKeywords/getobjects";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WORKTEAM_ID,String.valueOf(team_id).toString()));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}

	/**
	 * Add work name to worktable.
	 * @param name
	 * @param user_id
	 * @param team_id
	 * @throws Throwable
	 */
	public void addwork(String name , int team_id ,int user_id) throws Throwable {
		String url = "http://54.169.8.76/proapp/OcWorkteamWorkKeywords/mobile_add";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WebClient.WORK_NAME,name));
		postParameters.add(new BasicNameValuePair(WebClient.WORKTEAM_ID,String.valueOf(team_id)));
		postParameters.add(new BasicNameValuePair(WebClient.WORK_CREATOR_USER_ID,String.valueOf(user_id)));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){
				}
			}
		}).execute(postMethod);
	}

	/**
	 *
	 * @param name
	 * @param team_id
	 * @param user_id
	 * @throws Throwable
	 */
	public void addobject(String name , int team_id,int user_id) throws Throwable {
		String url = "http://54.169.8.76/proapp/OcWorkteamObjectKeywords/mobile_add";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WebClient.OBJECT_NAME,name));
		postParameters.add(new BasicNameValuePair(WebClient.WORKTEAM_ID,String.valueOf(team_id)));
		postParameters.add(new BasicNameValuePair(WebClient.OBJECT_CREATOR_ID,String.valueOf(user_id)));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}

	/**
	 * delet job keyword from work table, object table.
	 * @param id
	 * @param whichkey
	 * @throws Throwable
	 */

	/**
	 * add team to team table.
	 * @param name
	 * @throws Throwable
	 */
	public void addWorkTeam(String name,String teamJid ,int user_id ,String company_jid) throws Throwable {
		String url = "http://54.169.8.76/proapp/ocworkteams/mobile_add";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WORKTEAM_JABBER_ID,teamJid));
		postParameters.add(new BasicNameValuePair(WORKTEAM_CREATOR_ID,String.valueOf(user_id).toString()));
		postParameters.add(new BasicNameValuePair(WORKTEAM_NAME,name));
		postParameters.add(new BasicNameValuePair(COMPANY_JABBER_CODE,company_jid));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}


	public void deleteWork (int id) throws Throwable {
		String url = "http://54.169.8.76/proapp/OcWorkteamWorkKeywords/mobile_delete";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WORK_KEYWORD_ID,String.valueOf(id).toString()));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}
	public void deleteObject (int id)throws Throwable{
		String url = "http://54.169.8.76/proapp/OcWorkteamObjectKeywords/mobile_delete";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(OBJECT_KEYWORD_ID,String.valueOf(id).toString()));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}

	public void joinWorkTeam (int user_id,int team_id)throws Throwable{
		String url = "http://54.169.8.76/proapp/OcWorkTeamMembers/mobile_add";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(USERID,String.valueOf(user_id).toString()));
		postParameters.add(new BasicNameValuePair(WORKTEAM_ID,String.valueOf(team_id).toString()));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}


	public void addworkreport(int workteam_id,
							  int user_id,
							  int work_keyword_id,
							  int object_keyword_id,
							  int quantity ,
							  int work_duration_minutes,
							  int appraiser_id,
							  String appraisal ,
							  int abort_by_user_id,
							  String status
	)throws Throwable{
		String url = "http://54.169.8.76/proapp/OcWorktimes/mobile_add";
		HttpPost postMethod = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(WORKTEAM_ID,String.valueOf(workteam_id).toString()));
		postParameters.add(new BasicNameValuePair(USERID,String.valueOf(user_id).toString()));
		postParameters.add(new BasicNameValuePair(WORKKEYWORDID,String.valueOf(work_keyword_id).toString()));
		postParameters.add(new BasicNameValuePair(OBJECTKEYWORDID,String.valueOf(object_keyword_id).toString()));
		postParameters.add(new BasicNameValuePair(QUANTITY,String.valueOf(quantity).toString()));
		postParameters.add(new BasicNameValuePair(WORKDURATIONMINUTES,String.valueOf(work_duration_minutes).toString()));
		postParameters.add(new BasicNameValuePair(APPRAISER_ID,String.valueOf(appraiser_id).toString()));
		postParameters.add(new BasicNameValuePair(APPRAISAL,appraisal));
		postParameters.add(new BasicNameValuePair(ABORTBYUSERID,String.valueOf(abort_by_user_id).toString()));
		postParameters.add(new BasicNameValuePair(STATUS,status));

		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		postMethod.setEntity(formEntity);
		new KhmHttpConnect(httpClient, new KhmHttpConnect.OnCompleteListener() {
			@Override
			public void onComplete(String response, Exception e) {
				if(listener == null){
					return;
				}
				if(e != null){
					e.printStackTrace();return;
				}
				try {
					JSONObject jsonObject = new JSONObject(response);
					switch (jsonObject.getInt(STATUS)) {
						case 100:
							listener.onFinished(jsonObject.getString(DATA));
							break;
						case 200:
							listener.onFailed(200, jsonObject.getString(DATA));
							break;
						case 400:
							listener.onFailed(400,jsonObject.getString(DATA));
							break;
					}
				} catch (JSONException e1){

				}

			}
		}).execute(postMethod);
	}

	private void makePostRequest() {


	}
}
