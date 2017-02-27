package sam.puma.proapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import net.java.otr4j.session.SessionStatus;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import me.grantland.widget.AutofitTextView;
import sam.puma.proapp.Config;
import sam.puma.proapp.R;
import sam.puma.proapp.crypto.axolotl.AxolotlService;
import sam.puma.proapp.entities.AcceptWork;
import sam.puma.proapp.entities.Account;
import sam.puma.proapp.entities.AssignWork;
import sam.puma.proapp.entities.Contact;
import sam.puma.proapp.entities.Conversation;
import sam.puma.proapp.entities.DownloadableFile;
import sam.puma.proapp.entities.Message;
import sam.puma.proapp.entities.MucOptions;
import sam.puma.proapp.entities.OcWorkroot;
import sam.puma.proapp.entities.OcWorksource;
import sam.puma.proapp.entities.OcWorktime;
import sam.puma.proapp.entities.OfferWork;
import sam.puma.proapp.entities.Presence;
import sam.puma.proapp.entities.Report;
import sam.puma.proapp.entities.TeamMember;
import sam.puma.proapp.entities.Transferable;
import sam.puma.proapp.entities.TransferablePlaceholder;
import sam.puma.proapp.http.HttpDownloadConnection;
import sam.puma.proapp.httpclient.ObjectKey;
import sam.puma.proapp.httpclient.Team;
import sam.puma.proapp.httpclient.WebClient;
import sam.puma.proapp.httpclient.WorkKey;
import sam.puma.proapp.persistance.JobDB;
import sam.puma.proapp.services.MessageArchiveService;
import sam.puma.proapp.services.XmppConnectionService;
import sam.puma.proapp.ui.adapter.ActiveWorkAdapter;
import sam.puma.proapp.ui.adapter.AssignAdapter;
import sam.puma.proapp.ui.adapter.AssignHistoryExpandableAdapter;
import sam.puma.proapp.ui.adapter.AssignedWorkAdapter;
import sam.puma.proapp.ui.adapter.MemberAdapter;
import sam.puma.proapp.ui.adapter.MessageAdapter;
import sam.puma.proapp.ui.adapter.MessageAdapter.OnContactPictureClicked;
import sam.puma.proapp.ui.adapter.MessageAdapter.OnContactPictureLongClicked;
import sam.puma.proapp.ui.adapter.ParentLevelListAdapter;
import sam.puma.proapp.ui.adapter.ProductivityAdapter;
import sam.puma.proapp.ui.adapter.ProductivityDetailAdapter;
import sam.puma.proapp.ui.adapter.ReportAdapter;
import sam.puma.proapp.ui.adapter.TeamMemberAdapter;
import sam.puma.proapp.ui.adapter.TeamMemberCheckableAdapter;
import sam.puma.proapp.utils.GeoHelper;
import sam.puma.proapp.utils.UIHelper;
import sam.puma.proapp.xmpp.XmppConnection;
import sam.puma.proapp.xmpp.chatstate.ChatState;
import sam.puma.proapp.xmpp.jid.InvalidJidException;
import sam.puma.proapp.xmpp.jid.Jid;

public class ConversationFragment extends Fragment implements EditMessage.KeyboardListener {
	/**
	 * anton inserted code for define all custom views and variables.
	  *start*/
	public String teamJid; //current selected teamJid;
	private ListView mActionListView; //when green button click, first displayed listview.
	private ListView mWorkListView;
	private ListView mObjectListView;
	private ListView mQuantityListView;
	private ListView mAddJobListView;	//when add Plus button click , displayd list view.
	private ArrayList<String> actions; //Actions for mActionListView.
	public ArrayList<String> works; //work list array for mActionListView.
	public ArrayList<String> objects; //object list array for mObjectListView.
	private ArrayList<String> quantities;//quantities list array for mQuantityListView.
	private ArrayList<String> addList; //contents are "add work", "add object".
	private ImageView refreshImage;
	public int currentWork_id;//current selected work id.
	public int currentObject_id; //current selected object id.
	public int currentQuantity;//current selected Quantity
	public int currentAppraiser_id;//current Appraiser id.
	public String currentAppraisal;
	public String status;//current selected action.
	public Team currentTeamInformation;
	public String myjid;
	public int user_id;
	public int user_group_id;
	public int currentTeamCreator_id;
	public int currentTeamId;
	public int company_id;
	private ArrayList<String> currentcommands; //displayed string in top command bar.
	private CommandAdapter actionAdapter; //for mActionListView;
	private CommandAdapter workAdapter;		//for mWorkListView;
	private CommandAdapter objectAdapter;	//for mObjectListView;
	private CommandAdapter quantityAdapter; //for mQuantityListView;
	private CommandAdapter addListAdapter;  //for mAddJobListView;
	private boolean addStatus = false;  //add status = 0; else = 1
	private ImageButton addCommandButton; //when add keyword button click , displayed right plus button.
	private ImageButton mGreenbutton;  //in left bottom "+" sign button;
	public JobDB job;					//for get and insert the action , works , object into local table.
	public  boolean isCommand = false; // current message is whether command or not.
	private LinearLayout commandLayout;	//containning mActionListViwe, mWrokListView, mObjectListView , mAddJobListView;
	public  int selectedItemId;			//for delete keyword from table , current selected keyword id.
	private Button backButton;			//when select the keyword , for turn back button.
	private TextView topBarTextView;	//when keywords are selected , displayed on top textbar.
	private LinearLayout addJobLayout;	//contain the "add keyword" layout.
	private boolean _isTagMode = false;	//
	private boolean _isquantitiy = false;//for separate current message is default quantity or added quantity.
	private ArrayList<WorkKey>  workList;//get Workkey object from response data.
	private ArrayList<ObjectKey> objectList;//get objectkey object from response data.
	public KProgressHUD kpHUD;

	private ArrayList<String> offers;
	private OfferAdapter offerAdapter;
	private AcceptWorkAdapter acceptAdapter;
	private ListView offersListView;
	private RelativeLayout offer_layout;
	private RelativeLayout editMessageLayout;
	private RelativeLayout bottomBarLayout;
	/**For Bottom bar tabs*/
	private ImageView actionbutton;
	private ImageView findaJobbutton;
	private ImageView completedJobButton;
	private ImageView reportWorksButton;
	private ImageView userLocation;
	private ImageView teamButton;
	private ImageView productivityButton;

	private ArrayList<OfferWork> offerworks;
	private ArrayList<OcWorkroot> offeredWorks;
	private ArrayList<OcWorkroot> historyOffers;
	private String inputExpiryTime;
	private String inputAcceptQuantity;
	public int badgeNumber;
	private RelativeLayout badgeLayout;
	private AutofitTextView badgeTextView;

	private ExpandableListView acceptHistoryListView;
	private Button offeredWorkButton;
	private Button acceptHistoryButton;
	public boolean isAccept = false;
	public HashMap<OcWorkroot, List<OcWorksource>> childMap;
	public HashMap<OcWorksource, List<OcWorktime>> childChildMap;
	public ParentLevelListAdapter historyAdapter;  /** 3 Level listview adapter */
	public ListView assignMemberListview;
	public AssignedWorkAdapter assignAdapter;
	public ArrayList<TeamMember> teamMembers;
	public MemberAdapter adapter;
	public ListView assignedWorkListView;
	private ArrayList<OcWorksource> toStartAssigns;
	private ArrayList<OcWorksource> toStartAccepts;
	private ArrayList<OcWorksource> toStartAllworks;
	/** For assign functions */
	public boolean isSelectedAssignList = false;
	private int assignedUserId;

	private ExpandableListView assignCompletedWorkList;
	private ExpandableListView assignUncompletedWorkList;
	private ArrayList<OcWorksource> assignCompletedWorks;
	private ArrayList<OcWorksource> assignUncompletedWorks;
	private RelativeLayout assignHistoryLayout;
	private int worksourceId;
	private LinearLayout assignedWorkLayout;
	private TextView mTextView;

	private Button assignCompletedWorkHistoryButton;
	private Button assignIncompletedWorkHistoryButton;
	public  boolean filter = true;

	public ArrayList<OcWorksource> myIncompletedWorks;
	public ArrayList<OcWorksource> myCompletedWorks;
	public HashMap<OcWorksource, List<OcWorktime>> completedAssignSubHistory;
	public HashMap<OcWorksource, List<OcWorktime>> incompletedAssignSubHistory;
	public AssignHistoryExpandableAdapter completedAssignAdapter;
	public AssignHistoryExpandableAdapter incompletedAssignAdapter;

	private ArrayList<OcWorktime> activeWorks;
	private ArrayList<OcWorktime> finishedWorks;
 	private ActiveWorkAdapter activeWorkAdapter;

	private ArrayList<AssignWork> startedAssignWorks;
	private AssignAdapter startedWorkAdapter;

	private String currentWorkKey;
	private String currentObjectKey;

	private OcWorktime currentOcWorktime;

	private RelativeLayout assignBadgeLayout;
	private TextView assignBadgeText;
/***for report function******/
	private RelativeLayout report_layout;
	public ArrayList<Report> reportCompleteWorks;
	public ArrayList<Report> reportIncompleteWorks;
	public ArrayList<Report> reportAbortWorks;
	private ListView reportListView;
	private ReportAdapter reportAdapter;
	private Button reportCompleteButton;
	private Button reportIncompleteButton;
	private Button reportAbortButton;
	private ArrayList<Report> reportTempList;

	/** for productivity function******/
	private RelativeLayout productivityLayout;
	private ListView productivitySummaryListView;
	private ListView productivityDetailListView;
	private Button productivitySummaryButton;
	private Button productivityDetailButton;
	private ArrayList<Report> summaryProdcutivities;
	private ArrayList<Report> detailProductivities;
	private int reportListViewStatus = 0;
	private ProductivityAdapter productivityAdapter;
	private ProductivityDetailAdapter productivityDetailAdapter;
	private ArrayList<Report> subSummaryProductivities;
	private ArrayList<Report> subDetailProductivities;
	private boolean filterStatus = false;
	/** for Team Member function**/
	private ImageButton addTeamMemberButton;
	private ImageButton removeTeamMemberButton;
	private ImageButton appointAssistantTeamLeaderButton;
	private ListView teamMemberListView;
	private RelativeLayout teamMemberLayout;

	private ArrayList<TeamMember> currentTeamMembers;
	private TeamMemberAdapter teamMemberAdapter;
	private String selectedUserJidToRemove;
	private TeamMemberCheckableAdapter teamMemberCheckableAdapter;
	private String teamMemberListViewStatus;
	private ArrayList<TeamMember> removeMembers;
	private TeamMember teamLeaber;

	private BroadcastReceiver mItemViewClickReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String status = intent.getStringExtra("status");
			String value = intent.getStringExtra("value");
			if (getActivity() != null) {
				if (intent.getAction().equals("ITEM_SELECTED_INTENT")) {
					if (status.equals("sort")) {
						if (!reportCompleteWorks.isEmpty())
							reportCompleteWorks.remove(0);
						if (!reportIncompleteWorks.isEmpty())
							reportIncompleteWorks.remove(0);
						if (!reportAbortWorks.isEmpty())
							reportAbortWorks.remove(0);
						if (filterStatus) {
							Collections.sort(reportCompleteWorks, new CustomComparator(value));
							Collections.sort(reportIncompleteWorks, new CustomComparator(value));
							Collections.sort(reportAbortWorks, new CustomComparator(value));
							filterStatus = false;
						} else {
							Collections.sort(reportCompleteWorks, new DecCustomComparator(value));
							Collections.sort(reportIncompleteWorks, new DecCustomComparator(value));
							Collections.sort(reportAbortWorks, new DecCustomComparator(value));
							filterStatus = true;
						}
						reportCompleteWorks.add(0, new Report("", "", "", "", "", 0));
						reportIncompleteWorks.add(0, new Report("", "", "", "", "", 0));
						reportAbortWorks.add(0, new Report("", "", "", "", "", 0));
						if (reportListView != null)
							reportAdapter.notifyDataSetChanged();
					} else {
						reportTempList.clear();
						switch (reportListViewStatus) {
							case 0:
								reportTempList = filterReporsts(reportCompleteWorks, status, value);
								break;
							case 1:
								reportTempList = filterReporsts(reportIncompleteWorks, status, value);
								break;
							case 2:
								reportTempList = filterReporsts(reportAbortWorks, status, value);
								break;
							default:
								break;
						}
						reportTempList.add(0, new Report("name", "", "", "", "", 0));
						reportAdapter = new ReportAdapter(activity, reportTempList);
						if (reportListView != null)
							reportListView.setAdapter(reportAdapter);
					}
				} else if (intent.getAction().equals("PRODUCTIVITY_DETAIL_ITEM_SELECTED")) {
						if (status.equals("sort")) {
							if (detailProductivities != null) {
								if (!detailProductivities.isEmpty())
									detailProductivities.remove(0);
								if (filterStatus) {
									Collections.sort(detailProductivities, new CustomComparator(value));
									filterStatus = false;
								} else {
									Collections.sort(detailProductivities, new DecCustomComparator(value));
									filterStatus = true;
								}
								detailProductivities.add(0, new Report("", "", "", "", "", 0));
								if (productivityDetailListView != null)
									productivityDetailAdapter.notifyDataSetChanged();
							}
						} else {
							subDetailProductivities = new ArrayList<>();
							if (detailProductivities != null) {
								subDetailProductivities = filterReporsts(detailProductivities, status, value);
								subDetailProductivities.add(0, new Report("name", "", "", "", "", 0));
								productivityDetailAdapter = new ProductivityDetailAdapter(activity, subDetailProductivities);
								if (productivityDetailListView != null)
									productivityDetailListView.setAdapter(productivityDetailAdapter);
							}
						}
				} else if (intent.getAction().equals("PRODUCTIVITY_SUMMARY_ITEM_SELECTED")) {
					if (status.equals("sort")) {
						if (summaryProdcutivities != null) {
							if (!summaryProdcutivities.isEmpty())
								summaryProdcutivities.remove(0);
							if (filterStatus) {
								Collections.sort(summaryProdcutivities, new CustomComparator(value));
								filterStatus = false;
							} else {
								Collections.sort(summaryProdcutivities, new DecCustomComparator(value));
								filterStatus = true;
							}
							summaryProdcutivities.add(0, new Report("", "", "", "", "", 0));
							if (productivitySummaryListView != null) {
								productivityAdapter.notifyDataSetChanged();
							}
						}
					} else {
						subSummaryProductivities = new ArrayList<>();
						if (summaryProdcutivities != null) {
							subSummaryProductivities = filterReporsts(summaryProdcutivities, status, value);
							subSummaryProductivities.add(0, new Report("name", "", "", "", "", 0));
							productivityAdapter = new ProductivityAdapter(activity, subSummaryProductivities);
							if (productivitySummaryListView != null)
								productivitySummaryListView.setAdapter(productivityAdapter);
						}
					}
				}
			}
		}
	};
	/******************end*********************/
	protected Conversation conversation;
	private OnClickListener leaveMuc = new OnClickListener() {

		@Override
		public void onClick(View v) {
			activity.endConversation(conversation);
		}
	};

	private OnClickListener joinMuc = new OnClickListener() {

		@Override
		public void onClick(View v) {
			activity.xmppConnectionService.joinMuc(conversation);
		}
	};

	private OnClickListener enterPassword = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MucOptions muc = conversation.getMucOptions();
			String password = muc.getPassword();
			if (password == null) {
				password = "";
			}
			activity.quickPasswordEdit(password, new XmppActivity.OnValueEdited() {

				@Override
				public void onValueEdited(String value) {
					activity.xmppConnectionService.providePasswordForMuc(
							conversation, value);
				}
			});
		}
	};

	protected ListView messagesView;
	final protected List<Message> messageList = new ArrayList<>();
	protected MessageAdapter messageListAdapter;
	private EditMessage mEditMessage;
	private ImageButton mSendButton;
	private RelativeLayout snackbar;
	private TextView snackbarMessage;
	private TextView snackbarAction;
	private boolean messagesLoaded = true;
	private Toast messageLoaderToast;

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		private int getIndexOf(String uuid, List<Message> messages) {
			if (uuid == null) {
				return messages.size() - 1;
			}

			for(int i = 0; i < messages.size(); ++i) {
				if (uuid.equals(messages.get(i).getUuid())) {
					return i;
				} else {
					Message next = messages.get(i);
					while(next != null && next.wasMergedIntoPrevious()) {
						if (uuid.equals(next.getUuid())) {
							return i;
						}
						next = next.next();
					}

				}
			}
			return 0;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
							 int visibleItemCount, int totalItemCount) {
			synchronized (ConversationFragment.this.messageList) {
				if (firstVisibleItem < 5 && messagesLoaded && messageList.size() > 0) {
					long timestamp;
					if (messageList.get(0).getType() == Message.TYPE_STATUS && messageList.size() >= 2) {
						timestamp = messageList.get(1).getTimeSent();
					} else {
						timestamp = messageList.get(0).getTimeSent();
					}
					messagesLoaded = false;
					activity.xmppConnectionService.loadMoreMessages(conversation, timestamp, new XmppConnectionService.OnMoreMessagesLoaded() {
						@Override
						public void onMoreMessagesLoaded(final int c, Conversation conversation) {
							if (ConversationFragment.this.conversation != conversation) {
								return;
							}
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									final int oldPosition = messagesView.getFirstVisiblePosition();
									final Message message;
									if (oldPosition < messageList.size()) {
										message = messageList.get(oldPosition);
									}  else {
										message = null;
									}
									String uuid = message != null ? message.getUuid() : null;
									View v = messagesView.getChildAt(0);
									final int pxOffset = (v == null) ? 0 : v.getTop();
									ConversationFragment.this.conversation.populateWithMessages(ConversationFragment.this.messageList);
									updateStatusMessages();
									messageListAdapter.notifyDataSetChanged();
									int pos = getIndexOf(uuid,messageList);
									messagesView.setSelectionFromTop(pos, pxOffset);
									messagesLoaded = true;
									if (messageLoaderToast != null) {
										messageLoaderToast.cancel();
									}
								}
							});
						}

						@Override
						public void informUser(final int resId) {

							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (messageLoaderToast != null) {
										messageLoaderToast.cancel();
									}
									if (ConversationFragment.this.conversation != conversation) {
										return;
									}
									messageLoaderToast = Toast.makeText(activity, resId, Toast.LENGTH_LONG);
									messageLoaderToast.show();
								}
							});

						}
					});

				}
			}
		}
	};
	private final int KEYCHAIN_UNLOCK_NOT_REQUIRED = 0;
	private final int KEYCHAIN_UNLOCK_REQUIRED = 1;
	private final int KEYCHAIN_UNLOCK_PENDING = 2;
	private int keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
	protected OnClickListener clickToDecryptListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (keychainUnlock == KEYCHAIN_UNLOCK_REQUIRED
					&& activity.hasPgp() && !conversation.getAccount().getPgpDecryptionService().isRunning()) {
				keychainUnlock = KEYCHAIN_UNLOCK_PENDING;
				updateSnackBar(conversation);
				Message message = getLastPgpDecryptableMessage();
				if (message != null) {
					activity.xmppConnectionService.getPgpEngine().decrypt(message, new UiCallback<Message>() {
						@Override
						public void success(Message object) {
							conversation.getAccount().getPgpDecryptionService().onKeychainUnlocked();
							keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
						}

						@Override
						public void error(int errorCode, Message object) {
							keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
						}

						@Override
						public void userInputRequried(PendingIntent pi, Message object) {
							try {
								activity.startIntentSenderForResult(pi.getIntentSender(),
										ConversationActivity.REQUEST_DECRYPT_PGP, null, 0, 0, 0);
							} catch (SendIntentException e) {
								keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
								updatePgpMessages();
							}
						}
					});
				}
			} else {
				keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
				updatePgpMessages();
			}
		}
	};
	protected OnClickListener clickToVerify = new OnClickListener() {

		@Override
		public void onClick(View v) {
			activity.verifyOtrSessionDialog(conversation, v);
		}
	};
	private OnEditorActionListener mEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEND) {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isFullscreenMode()) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
				if(addStatus){
					return false;
				}
				/**
				 * anton's code for event of keyboard send button click
				 */
				if(_isquantitiy) {
					if(mEditMessage.length() == 0){
						return false;
					} else {
						currentcommands.add("Quantity " + mEditMessage.getText().toString());
						mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
						_isquantitiy = false;
						currentQuantity = Integer.valueOf(mEditMessage.getText().toString()).intValue();
					}
				}
				//after sent message then all of the view have to set init.
				topBarTextView.setText("");
				mEditMessage.setEnabled(true);
				messagesView.setAlpha(1.0f);
				mGreenbutton.setImageResource(R.drawable.ic_add_white_24dp);
				sendMessage();
				hideTagList(10);
				_isTagMode = false;
				backButton.setVisibility(View.GONE);
				return true;
			} else {
				return false;
			}
		}
	};
	private OnClickListener mSendButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			/**
			 * after sendbutton pressed all of the view must be return in initial status.
			 * current message is default quantity or not ,
			 *start anton's code*/
			if(_isquantitiy) {
				if(mEditMessage.length() == 0){
					return;
				} else {
					currentcommands.add("Quantity " + mEditMessage.getText().toString());
					mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
					_isquantitiy = false;
					currentQuantity = Integer.valueOf(mEditMessage.getText().toString()).intValue();
				}
			}
			//after sent message then all of the view have to set init.
			topBarTextView.setText("");
			mEditMessage.setEnabled(true);
			messagesView.setAlpha(1.0f);
			mGreenbutton.setImageResource(R.drawable.ic_add_white_24dp); //set + sign in greenbutton image;
			/*********************************************************/
			Object tag = v.getTag();
			if (tag instanceof SendButtonAction) {
				SendButtonAction action = (SendButtonAction) tag;
				switch (action) {
					case TAKE_PHOTO:
						activity.attachFile(ConversationActivity.ATTACHMENT_CHOICE_TAKE_PHOTO);
						break;
					case SEND_LOCATION:
						activity.attachFile(ConversationActivity.ATTACHMENT_CHOICE_LOCATION);
						break;
					case RECORD_VOICE:
						activity.attachFile(ConversationActivity.ATTACHMENT_CHOICE_RECORD_VOICE);
						break;
					case CHOOSE_PICTURE:
						activity.attachFile(ConversationActivity.ATTACHMENT_CHOICE_CHOOSE_IMAGE);
						break;
					case CANCEL:
						if (conversation != null) {
							if (conversation.getCorrectingMessage() != null) {
								conversation.setCorrectingMessage(null);
								mEditMessage.getEditableText().clear();
							}
							if (conversation.getMode() == Conversation.MODE_MULTI) {
								conversation.setNextCounterpart(null);
							}
							updateChatMsgHint();
							updateSendButton();
						}
						break;
					default:
						sendMessage();
				}
			} else {
				sendMessage();
			}
			/**
			 * after sendbutton pressed all of the view must be return in initial status.
			 *start anton's code*/
			hideTagList(10);
			_isTagMode = false;
			backButton.setVisibility(View.GONE); //after sent add command message ,back button must be disappear
			/****************end********************/
		}
	};
	private OnClickListener clickToMuc = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ConferenceDetailsActivity.class);
			intent.setAction(ConferenceDetailsActivity.ACTION_VIEW_MUC);
			intent.putExtra("uuid", conversation.getUuid());
			startActivity(intent);
		}
	};
	private ConversationActivity activity;
	private Message selectedMessage;
	/**
	 *start anton's code
	 * produce message string to send from current commands stack user selected through
	 * @return message string to send
	 */
	public String getMessageToSend() {

		if (currentcommands == null)
			return  null;

		if (currentcommands.size() == 0) {
			return  null;
		}

		StringBuilder buffer = new StringBuilder();
		for (String command :
				currentcommands) {
			buffer.append(command + " ");
		}

		return buffer.toString().trim();
	}
/*********************end************************/
	public void setMessagesLoaded() {
		this.messagesLoaded = true;
	}

	private void sendMessage() {

		final String body = currentcommands.isEmpty() ? mEditMessage.getText().toString() : getMessageToSend();
		currentcommands.clear();
		if (body.length() == 0 || this.conversation == null) {
			return;
		}

		saveKeywordToBackend();

		final Message message;

		if (conversation.getCorrectingMessage() == null || isCommand ) {
			message = new Message(conversation, body, conversation.getNextEncryption());
			/**
			 * separate message is command or not?
			 */
			message.command = isCommand;

			if(!addStatus)
			isCommand = false;
			if (conversation.getMode() == Conversation.MODE_MULTI) {
				if (conversation.getNextCounterpart() != null) {
					message.setCounterpart(conversation.getNextCounterpart());
					message.setType(Message.TYPE_PRIVATE);
				}
			}
		} else {
			message = conversation.getCorrectingMessage();
			/**
			 * differ whether message is command or not?
			 */
			message.command = isCommand;

			if(!addStatus)
			isCommand = false;
			message.setBody(body);
			message.setEdited(message.getUuid());
			message.setUuid(UUID.randomUUID().toString());
			conversation.setCorrectingMessage(null);
		}
		/*********************************************/
		switch (conversation.getNextEncryption()) {
			case Message.ENCRYPTION_OTR:
				sendOtrMessage(message);
				break;
			case Message.ENCRYPTION_PGP:
				sendPgpMessage(message);
				break;
			case Message.ENCRYPTION_AXOLOTL:
				if(!activity.trustKeysIfNeeded(ConversationActivity.REQUEST_TRUST_KEYS_TEXT)) {
					sendAxolotlMessage(message);
				}
				break;
			default:
				sendPlainTextMessage(message);
		}
	}

	public void updateChatMsgHint() {
		final boolean multi = conversation.getMode() == Conversation.MODE_MULTI;
		if (conversation.getCorrectingMessage() != null) {
			this.mEditMessage.setHint(R.string.send_corrected_message);
		} else if (multi && conversation.getNextCounterpart() != null) {
			this.mEditMessage.setHint(getString(
					R.string.send_private_message_to,
					conversation.getNextCounterpart().getResourcepart()));
		} else if (multi && !conversation.getMucOptions().participating()) {
			this.mEditMessage.setHint(R.string.you_are_not_participating);
		} else {
			switch (conversation.getNextEncryption()) {
				case Message.ENCRYPTION_NONE:
					mEditMessage
							.setHint(getString(R.string.send_unencrypted_message));
					break;
				case Message.ENCRYPTION_OTR:
					mEditMessage.setHint(getString(R.string.send_otr_message));
					break;
				case Message.ENCRYPTION_AXOLOTL:
					AxolotlService axolotlService = conversation.getAccount().getAxolotlService();
					if (axolotlService != null && axolotlService.trustedSessionVerified(conversation)) {
						mEditMessage.setHint(getString(R.string.send_omemo_x509_message));
					} else {
						mEditMessage.setHint(getString(R.string.send_omemo_message));
					}
					break;
				case Message.ENCRYPTION_PGP:
					mEditMessage.setHint(getString(R.string.send_pgp_message));
					break;
				default:
					break;
			}
			getActivity().invalidateOptionsMenu();
		}
	}

	public void setupIme() {
		if (activity == null) {
			return;
		} else if (activity.usingEnterKey() && activity.enterIsSend()) {
			mEditMessage.setInputType(mEditMessage.getInputType() & (~InputType.TYPE_TEXT_FLAG_MULTI_LINE));
			mEditMessage.setInputType(mEditMessage.getInputType() & (~InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE));
		} else if (activity.usingEnterKey()) {
			mEditMessage.setInputType(mEditMessage.getInputType() | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			mEditMessage.setInputType(mEditMessage.getInputType() & (~InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE));
		} else {
			mEditMessage.setInputType(mEditMessage.getInputType() | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			mEditMessage.setInputType(mEditMessage.getInputType() | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

/**
 * anton's code
 * for connect the server and send request for team list.
 * all of the ListView initialize.
 */
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mItemViewClickReceiver, new IntentFilter("ITEM_SELECTED_INTENT"));
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mItemViewClickReceiver, new IntentFilter("PRODUCTIVITY_DETAIL_ITEM_SELECTED"));
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mItemViewClickReceiver, new IntentFilter("PRODUCTIVITY_SUMMARY_ITEM_SELECTED"));
		/*******************************/
		final View view = inflater.inflate(R.layout.fragment_conversation, container, false);
		view.setOnClickListener(null);
		mEditMessage = (EditMessage) view.findViewById(R.id.textinput);
		mEditMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (activity != null) {
					activity.hideConversationsOverview();
				}
			}
		});
		mEditMessage.setOnEditorActionListener(mEditorActionListener);

		mSendButton = (ImageButton) view.findViewById(R.id.textSendButton);
		mSendButton.setOnClickListener(this.mSendButtonListener);

		snackbar = (RelativeLayout) view.findViewById(R.id.snackbar);
		snackbarMessage = (TextView) view.findViewById(R.id.snackbar_message);
		snackbarAction = (TextView) view.findViewById(R.id.snackbar_action);

		messagesView = (ListView) view.findViewById(R.id.messages_view);
		messagesView.setOnScrollListener(mOnScrollListener);
		messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		messageListAdapter = new MessageAdapter((ConversationActivity) getActivity(), this.messageList);
		messageListAdapter.setOnContactPictureClicked(new OnContactPictureClicked() {
			@Override
			public void onContactPictureClicked(Message message) {
				if (message.getStatus() <= Message.STATUS_RECEIVED) {
					if (message.getConversation().getMode() == Conversation.MODE_MULTI) {
						if (message.getCounterpart() != null) {
							String user = message.getCounterpart().isBareJid() ? message.getCounterpart().toString() : message.getCounterpart().getResourcepart();
							if (!message.getConversation().getMucOptions().isUserInRoom(user)) {
								Toast.makeText(activity, activity.getString(R.string.user_has_left_conference, user), Toast.LENGTH_SHORT).show();
							}
							highlightInConference(user);
						}
					} else {
						activity.switchToContactDetails(message.getContact(), message.getAxolotlFingerprint());
					}
				} else {
					Account account = message.getConversation().getAccount();
					Intent intent = new Intent(activity, EditAccountActivity.class);
					intent.putExtra("jid", account.getJid().toBareJid().toString());
					intent.putExtra("fingerprint", message.getAxolotlFingerprint());
					startActivity(intent);
				}
			}
		});
		messageListAdapter
				.setOnContactPictureLongClicked(new OnContactPictureLongClicked() {

					@Override
					public void onContactPictureLongClicked(Message message) {
						if (message.getStatus() <= Message.STATUS_RECEIVED) {
							if (message.getConversation().getMode() == Conversation.MODE_MULTI) {
								if (message.getCounterpart() != null) {
									String user = message.getCounterpart().getResourcepart();
									if (user != null) {
										if (message.getConversation().getMucOptions().isUserInRoom(user)) {
											privateMessageWith(message.getCounterpart());
										} else {
											Toast.makeText(activity, activity.getString(R.string.user_has_left_conference, user), Toast.LENGTH_SHORT).show();
										}
									}
								}
							}
						} else {
							activity.showQrCode();
						}
					}
				});
		messagesView.setAdapter(messageListAdapter);

		registerForContextMenu(messagesView);

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		synchronized (this.messageList) {
			super.onCreateContextMenu(menu, v, menuInfo);
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			this.selectedMessage = this.messageList.get(acmi.position);
			populateContextMenu(menu);
		}
	}

	private void populateContextMenu(ContextMenu menu) {
		final Message m = this.selectedMessage;
		final Transferable t = m.getTransferable();
		Message relevantForCorrection = m;
		while(relevantForCorrection.mergeable(relevantForCorrection.next())) {
			relevantForCorrection = relevantForCorrection.next();
		}
		if (m.getType() != Message.TYPE_STATUS) {
			activity.getMenuInflater().inflate(R.menu.message_context, menu);
			menu.setHeaderTitle(R.string.message_options);
			MenuItem copyText = menu.findItem(R.id.copy_text);
			MenuItem retryDecryption = menu.findItem(R.id.retry_decryption);
			MenuItem correctMessage = menu.findItem(R.id.correct_message);
			MenuItem shareWith = menu.findItem(R.id.share_with);
			MenuItem sendAgain = menu.findItem(R.id.send_again);
			MenuItem copyUrl = menu.findItem(R.id.copy_url);
			MenuItem downloadFile = menu.findItem(R.id.download_file);
			MenuItem cancelTransmission = menu.findItem(R.id.cancel_transmission);
			MenuItem assignFilter = menu.findItem(R.id.assign_filter);
			if ((m.getType() == Message.TYPE_TEXT || m.getType() == Message.TYPE_PRIVATE)
					&& t == null
					&& !GeoHelper.isGeoUri(m.getBody())
					&& m.treatAsDownloadable() != Message.Decision.MUST) {
				copyText.setVisible(true);
			}
			if (m.getEncryption() == Message.ENCRYPTION_DECRYPTION_FAILED) {
				retryDecryption.setVisible(true);
			}
			if (relevantForCorrection.getType() == Message.TYPE_TEXT
					&& relevantForCorrection.isLastCorrectableMessage()) {
				correctMessage.setVisible(true);
			}
			if ((m.getType() != Message.TYPE_TEXT
					&& m.getType() != Message.TYPE_PRIVATE
					&& t == null)
					|| (GeoHelper.isGeoUri(m.getBody()))) {
				shareWith.setVisible(true);
			}
			if (m.getStatus() == Message.STATUS_SEND_FAILED) {
				sendAgain.setVisible(true);
			}
			if (m.hasFileOnRemoteHost()
					|| GeoHelper.isGeoUri(m.getBody())
					|| m.treatAsDownloadable() == Message.Decision.MUST
					|| (t != null && t instanceof HttpDownloadConnection)) {
				copyUrl.setVisible(true);
			}
			if ((m.getType() == Message.TYPE_TEXT && t == null && m.treatAsDownloadable() != Message.Decision.NEVER)
					|| (m.isFileOrImage() && t instanceof TransferablePlaceholder && m.hasFileOnRemoteHost())){
				downloadFile.setVisible(true);
				downloadFile.setTitle(activity.getString(R.string.download_x_file,UIHelper.getFileDescriptionString(activity, m)));
			}
			if ((t != null && !(t instanceof TransferablePlaceholder))
					|| (m.isFileOrImage() && (m.getStatus() == Message.STATUS_WAITING
					|| m.getStatus() == Message.STATUS_OFFERED))) {
				cancelTransmission.setVisible(true);
			}

		}
	}

	public void getTeamJobs(final String teamjid){
		kpHUD = KProgressHUD.create(getActivity())
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setDetailsLabel("Loading data")
				.setCancellable(true)
				.setAnimationSpeed(1)
				.setDimAmount(0.5f);

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				kpHUD.show();
			}
		});
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put(WebClient.WORKTEAM_JABBER_ID,teamjid);
		client.post("http://54.169.8.76/proapp/OcWorkteams/getteamid", params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				workList.clear();
				works.clear();
				objectList.clear();
				objects.clear();
				try {
					JSONObject responseJsonObject = new JSONObject(responseString);
					switch (responseJsonObject.getInt(WebClient.STATUS)){
						case 100:
							String dataString = responseJsonObject.getString(WebClient.DATA);
							JSONObject dataObject = new JSONObject(dataString);
							String teamString = dataObject.getString(WebClient.OCWORKTEAM);
							String objectKeywordString = dataObject.getString(WebClient.OCWORKTEAMOBJECTKEYWORD);
							String workKeywordString = dataObject.getString(WebClient.OCWORKTEAMWORKKEYWORD);
							JSONObject teamObject = new JSONObject(teamString);
							JSONArray objectArray = new JSONArray(objectKeywordString);
							JSONArray workArray = new JSONArray(workKeywordString);
							currentTeamInformation = new Team(teamObject.getInt(WebClient.WORKTEAM_ID),
									teamObject.getString(WebClient.WORKTEAM_NAME),
									teamObject.getInt(WebClient.WORKTEAM_CREATOR_ID),
									teamObject.getString(WebClient.WORKTEAM_JABBER_ID));
							currentTeamId = teamObject.getInt(WebClient.WORKTEAM_ID);
							currentTeamCreator_id = teamObject.getInt(WebClient.WORKTEAM_CREATOR_ID);

							for(int i = 0 ; i < objectArray.length() ; i++) {
								JSONObject objectobj = objectArray.getJSONObject(i);
								if (objectobj.getBoolean(WebClient.ISACTIVE)) {
									objects.add(objectobj.getString(WebClient.OBJECT_NAME));
									objectList.add(new ObjectKey(
											objectobj.getInt(WebClient.OBJECT_KEYWORD_ID),
											objectobj.getString(WebClient.OBJECT_NAME),
											objectobj.getInt(WebClient.WORKTEAM_ID),
											objectobj.getString(WebClient.CREATETIME),
											objectobj.getString(WebClient.DELETETIME),
											objectobj.getInt(WebClient.OBJECT_CREATOR_ID),
											objectobj.getInt(WebClient.DELETE_USER_ID),
											objectobj.getBoolean(WebClient.ISACTIVE)));
								}
							}

							for(int i = 0 ; i < workArray.length() ; i++) {
								JSONObject workobj = workArray.getJSONObject(i);
								if (workobj.getBoolean(WebClient.ISACTIVE)) {
									works.add(workobj.getString(WebClient.WORK_NAME));
									workList.add(new WorkKey(
											workobj.getInt(WebClient.WORK_KEYWORD_ID),
											workobj.getString(WebClient.WORK_NAME),
											workobj.getInt(WebClient.WORKTEAM_ID),
											workobj.getString(WebClient.CREATETIME),
											workobj.getString(WebClient.DELETETIME),
											workobj.getInt(WebClient.WORK_CREATOR_USER_ID),
											workobj.getInt(WebClient.DELETE_USER_ID),
											workobj.getBoolean(WebClient.ISACTIVE)));
								}
							}
							if(!works.isEmpty()){
								works.add(0,"Works");workList.add(0, new WorkKey());
							}
							joinTeam(currentTeamInformation.workteam_jid,myjid);
							break;
						case 400:
							works.clear();
							objects.clear();
							workList.clear();
							objectList.clear();
							break;
						default:
							return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				kpHUD.dismiss();
				formatListView();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences prefs = getActivity().getSharedPreferences("team", Context.MODE_PRIVATE);
		myjid = prefs.getString("myjid", null);
		if(conversation != null) {
			if (conversation.getMode() == Conversation.MODE_MULTI) {
				if (conversation.getJid().toString().contains("/")) {
					teamJid = conversation.getJid().toString().split("/")[0];
				} else {
					teamJid = conversation.getJid().toString();
				}
				joinTeam(teamJid, myjid);
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
//		if(getActivity() != null)
//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
	}

	public void getMyAccount(){
		SharedPreferences prefs = getActivity().getSharedPreferences("team", Context.MODE_PRIVATE);
		myjid = prefs.getString("myjid", null);
		if(myjid == null)return;
		final SharedPreferences.Editor pref = getActivity().getSharedPreferences("team", Context.MODE_PRIVATE).edit();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put(WebClient.JABBER_ID,myjid);
		String url = "http://54.169.8.76/proapp/OcUsers/get_my_id";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				kpHUD.dismiss();
				Toast.makeText(getActivity(),"no connection",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

				try {
					JSONObject responseObject = new JSONObject(responseString);
					int status = responseObject.getInt(WebClient.STATUS);
					String dataString = responseObject.getString(WebClient.DATA);
					switch (status){
						case 100:
							JSONObject account = new JSONObject(dataString);
							user_id = account.getInt("user_id");
							company_id = account.getInt("company_id");
							user_group_id = account.getInt("user_group_id");
							pref.putInt("user_id", user_id);
							pref.putInt("company_id", company_id);
							pref.putInt("user_group_id",user_group_id);
							pref.commit();
							break;
						case 400:
							break;
						default:
							break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
		/*********************************************************/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.assign_filter : {
				switchToMyJobs(item);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.share_with:
				shareWith(selectedMessage);
				return true;
			case R.id.copy_text:
				copyText(selectedMessage);
				return true;
			case R.id.correct_message:
				correctMessage(selectedMessage);
				return true;
			case R.id.send_again:
				resendMessage(selectedMessage);
				return true;
			case R.id.copy_url:
				copyUrl(selectedMessage);
				return true;
			case R.id.download_file:
				downloadFile(selectedMessage);
				return true;
			case R.id.cancel_transmission:
				cancelTransmission(selectedMessage);
				return true;
			case R.id.retry_decryption:
				retryDecryption(selectedMessage);
				return true;
			case R.id.assign_filter:

				return true;
			default:
				return super.onContextItemSelected(item);

		}
	}

	private void shareWith(Message message) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		if (GeoHelper.isGeoUri(message.getBody())) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, message.getBody());
			shareIntent.setType("text/plain");
		} else {
			shareIntent.putExtra(Intent.EXTRA_STREAM,
					activity.xmppConnectionService.getFileBackend()
							.getJingleFileUri(message));
			shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			String mime = message.getMimeType();
			if (mime == null) {
				mime = "*/*";
			}
			shareIntent.setType(mime);
		}
		try {
			activity.startActivity(Intent.createChooser(shareIntent, getText(R.string.share_with)));
		} catch (ActivityNotFoundException e) {
			//This should happen only on faulty androids because normally chooser is always available
			Toast.makeText(activity,R.string.no_application_found_to_open_file,Toast.LENGTH_SHORT).show();
		}
	}

	private void copyText(Message message) {
		if (activity.copyTextToClipboard(message.getMergedBody(),
				R.string.message_text)) {
			Toast.makeText(activity, R.string.message_copied_to_clipboard,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void resendMessage(Message message) {
		if (message.getType() == Message.TYPE_FILE || message.getType() == Message.TYPE_IMAGE) {
			DownloadableFile file = activity.xmppConnectionService.getFileBackend().getFile(message);
			if (!file.exists()) {
				Toast.makeText(activity, R.string.file_deleted, Toast.LENGTH_SHORT).show();
				message.setTransferable(new TransferablePlaceholder(Transferable.STATUS_DELETED));
				return;
			}
		}
		activity.xmppConnectionService.resendFailedMessages(message);
	}

	private void copyUrl(Message message) {
		final String url;
		final int resId;
		if (GeoHelper.isGeoUri(message.getBody())) {
			resId = R.string.location;
			url = message.getBody();
		} else if (message.hasFileOnRemoteHost()) {
			resId = R.string.file_url;
			url = message.getFileParams().url.toString();
		} else {
			url = message.getBody().trim();
			resId = R.string.file_url;
		}
		if (activity.copyTextToClipboard(url, resId)) {
			Toast.makeText(activity, R.string.url_copied_to_clipboard,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void downloadFile(Message message) {
		activity.xmppConnectionService.getHttpConnectionManager()
				.createNewDownloadConnection(message, true);
	}

	private void cancelTransmission(Message message) {
		Transferable transferable = message.getTransferable();
		if (transferable != null) {
			transferable.cancel();
		} else {
			activity.xmppConnectionService.markMessage(message, Message.STATUS_SEND_FAILED);
		}
	}

	private void retryDecryption(Message message) {
		message.setEncryption(Message.ENCRYPTION_PGP);
		activity.xmppConnectionService.updateConversationUi();
		conversation.getAccount().getPgpDecryptionService().add(message);
	}

	protected void privateMessageWith(final Jid counterpart) {
		this.mEditMessage.setText("");
		this.conversation.setNextCounterpart(counterpart);
		updateChatMsgHint();
		updateSendButton();
	}

	private void correctMessage(Message message) {
		while(message.mergeable(message.next())) {
			message = message.next();
		}
		this.conversation.setCorrectingMessage(message);
		this.mEditMessage.getEditableText().clear();
		this.mEditMessage.getEditableText().append(message.getBody());

	}

	protected void highlightInConference(String nick) {
		String oldString = mEditMessage.getText().toString().trim();
		if (oldString.isEmpty() || mEditMessage.getSelectionStart() == 0) {
			mEditMessage.getText().insert(0, nick + ": ");
		} else {
			if (mEditMessage.getText().charAt(
					mEditMessage.getSelectionStart() - 1) != ' ') {
				nick = " " + nick;
			}
			mEditMessage.getText().insert(mEditMessage.getSelectionStart(),
					nick + " ");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		/**
		 * all of the views are initialized
		 */
		initAllViews();
		initAllValiables();
		setHasOptionsMenu(true);
		commandLayout.setVisibility(View.GONE);
		addCommandButton.setVisibility(View.GONE);
		addJobLayout.setVisibility(View.GONE);
		mGreenbutton.setImageResource(R.drawable.ic_add_white_24dp);
		hideTagList(10);
		formatListView();
		mAddJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
				addCommandButton.setVisibility(View.VISIBLE);
				addJobLayout.setVisibility(View.GONE);
				backButton.setVisibility(View.VISIBLE);
				/**
				 *if new work is selected ,mWorkListView displayed.
				 */
				if (addList.get(position).toString() == getString(R.string.command_new_work)) {
					formatListView();
					hideTagList(1);
					topBarTextView.append(getString(R.string.command_start)+ " ");
				} else if (addList.get(position).toString() == getString(R.string.command_new_object)) {
					formatListView();
					hideTagList(2);
					topBarTextView.append(getString(R.string.command_object));
				}
				assignedWorkListView.setVisibility(View.GONE);
				mEditMessage.setEnabled(true);
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mEditMessage, InputMethodManager.SHOW_IMPLICIT);
			}
		});

		mActionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				topBarTextView.setVisibility(View.VISIBLE);
				formatListView();
				backButton.setVisibility(View.VISIBLE);
				if (addStatus) {

				}else {

					if (actions.get(position).equals(getString(R.string.command_assign))) {
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = "Assign";
						teamMembers.clear();
						getTeamMembers();
//						hideTagList(5);//for the member listview
						addJobLayout.setVisibility(View.GONE);
					} else if (actions.get(position).equals(getString(R.string.command_start))) {
						//test assigned worklist;
						assignedWorkListView.setVisibility(View.VISIBLE);
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = "Start";
						addJobLayout.setVisibility(View.GONE);
						getToStartWorks();
						formatListView();
						hideTagList(1);
					} else if (actions.get(position).equals(getString(R.string.command_finish))) {
						assignedWorkListView.setVisibility(View.VISIBLE);
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = "Finish";
						getActivedJobs();
						addJobLayout.setVisibility(View.GONE);
						//					getStartedAssingWorks();
//						hideTagList(1);
					} else if (actions.get(position).equals(getString(R.string.command_offer))) {
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = "Offer";
						formatListView();
						hideTagList(1);
						addJobLayout.setVisibility(View.GONE);

					} else if (actions.get(position).equals(getString(R.string.command_abort))) {
						assignedWorkListView.setVisibility(View.VISIBLE);
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = "Abort";
						getActivedJobs();
//						hideTagList(1);
						addJobLayout.setVisibility(View.GONE);
					} else if (actions.get(position).equals(getString(R.string.command_appraise))) {
						assignedWorkListView.setVisibility(View.VISIBLE);
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = "Appraise";
						getFinishedWorks();
//						hideTagList(1);
						addJobLayout.setVisibility(View.GONE);
					} else {
						topBarTextView.append(actions.get(position).toString() + " ");
						currentcommands.add(actions.get(position).toString());
						status = actions.get(position).toString();
						formatListView();
						hideTagList(1);
						addJobLayout.setVisibility(View.GONE);
					}
					mActionListView.setVisibility(View.GONE);
				}
			}

		});

		assignMemberListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				topBarTextView.append(teamMembers.get(position).name.split("_")[0]+"@"+teamMembers.get(position).name.split("_")[1]+" ");
				currentcommands.add(teamMembers.get(position).name.split("_")[0]+"@"+teamMembers.get(position).name.split("_")[1]);
				assignedUserId = teamMembers.get(position).userId;
				formatListView();
				hideTagList(1);
			}
		});

		assignedWorkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				if (status.equals("Start")) {
					if(toStartAllworks.get(position).worksource.equals("start")||toStartAllworks.get(position).worksource.equals("section"))return;
					final EditText input = new EditText(getActivity());
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					final String startSetence = "Start "+ toStartAllworks.get(position).worksourceWorkKeyword+" "+ toStartAllworks.get(position).worksourceObjectKeyword+" "+getString(R.string.command_quantity)+" ";
					builder.setTitle(startSetence+"(max "+ toStartAllworks.get(position).worksourceQuantityLessStarted+")");
					builder.setView(input);
					input.setText(String.valueOf(toStartAllworks.get(position).worksourceQuantityLessStarted));
					input.setSelection(String.valueOf(toStartAllworks.get(position).worksourceQuantityLessStarted).length());
					builder.setNegativeButton(getString(R.string.command_cancel),null);
					builder.setPositiveButton(getString(R.string.command_send),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							inputAcceptQuantity = input.getText().toString();
							int startQuantity = Integer.parseInt(inputAcceptQuantity);

							if(startQuantity > toStartAllworks.get(position).worksourceQuantityLessStarted){
								Toast.makeText(getActivity(),getString(R.string.command_not_valid_quantity),Toast.LENGTH_SHORT).show();
							} else {
								startWork(toStartAllworks.get(position),startQuantity);
								mEditMessage.setText(startSetence+inputAcceptQuantity);
								currentcommands.add(toStartAllworks.get(position).worksourceWorkKeyword+" "+ toStartAllworks.get(position).worksourceObjectKeyword+" quantity "+inputAcceptQuantity);
								isCommand = true;
								isAccept = true;
								sendMessage();
								isAccept = false;
							}
						}
					});
					builder.create().show();
					hideTagList(10);


				}else if(status.equals("Finish")){
				}
			}
		});

		mActionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				if(actions.get(position).equals(getString(R.string.command_offer))){
					final EditText input = new EditText(getActivity());
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(getString(R.string.command_set_expired_time));
					builder.setView(input);
					builder.setNegativeButton(getString(R.string.command_cancel),null);
					input.setText(String.valueOf(OfferWork.defaultExpiryTime));
					builder.setPositiveButton(getString(R.string.command_save),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							inputExpiryTime = input.getText().toString();
							OfferWork.defaultExpiryTime = Integer.parseInt(inputExpiryTime);
							InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(input.getWindowToken(),0);
						}
					});
					builder.create().show();
				}
				return false;
			}
		});

		mWorkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				backButton.setVisibility(View.VISIBLE);
				if (addStatus) {

				} else {
					if(status.equals("Finish")) {
						topBarTextView.append(activeWorks.get(position).getWorkSentence());
						currentcommands.add(activeWorks.get(position).getWorkSentence());
						hideTagList(10);
						topBarTextView.setVisibility(View.VISIBLE);
						backButton.setVisibility(View.VISIBLE);
						mSendButton.setEnabled(true);
						isSelectedAssignList = true;
						finishWork(activeWorks.get(position),"Finish");
						mEditMessage.setText("Finish "+activeWorks.get(position).getWorkSentence());
						isCommand = true;
						sendMessage();
						isCommand = false;
						hideTagList(10);

					} else if (status.equals("Abort")){
						topBarTextView.append(activeWorks.get(position).getWorkSentence());
						currentcommands.add(activeWorks.get(position).getWorkSentence());
						hideTagList(10);
						topBarTextView.setVisibility(View.VISIBLE);
						backButton.setVisibility(View.VISIBLE);
						mSendButton.setEnabled(true);
						isSelectedAssignList = true;
						finishWork(activeWorks.get(position),"Abort");
						mEditMessage.setText(getString(R.string.command_abort)+" "+activeWorks.get(position).getWorkSentence());
						isCommand = true;
						sendMessage();
						isCommand = false;
						hideTagList(10);
					} else if (status.equals("Appraise")){
						quantities.clear();
						quantities.add("Very good");
						quantities.add("Good");
						quantities.add("Average");
						quantities.add("Poor");
						quantities.add("Very poor");
						quantityAdapter = new CommandAdapter(getActivity(), quantities);
						mQuantityListView.setAdapter(quantityAdapter);
						hideTagList(3);
						currentcommands.add(finishedWorks.get(position).getWorkSentence());
						topBarTextView.append(finishedWorks.get(position).getWorkSentence());
						currentOcWorktime = new OcWorktime();
						currentOcWorktime = finishedWorks.get(position);
					} else {
						formatListView(); //before display command listViews every views must be initialized.
						topBarTextView.append(works.get(position).toString() + " ");
						currentcommands.add(works.get(position).toString());
						currentWork_id = workList.get(position).id;
						currentWorkKey = workList.get(position).workname;
						formatListView();
						hideTagList(2);
						isSelectedAssignList = false;
					}

				}
			}
		});
		/***for delete a work from the worklist , long click workListView cell***/
		mWorkListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				if (addStatus) {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
					builder1.setMessage("Confirm delete?");
					builder1.setCancelable(true);
					selectedItemId = workList.get(position).id; //current selected cell id is inserted in selectedItemId;
					builder1.setPositiveButton(
							"Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									AsyncHttpClient client = new AsyncHttpClient();
									RequestParams params = new RequestParams();
									params.put(WebClient.WORK_KEYWORD_ID,selectedItemId);
									params.put(WebClient.USERID,user_id);
									String url = "http://54.169.8.76/proapp/OcWorkteamWorkKeywords/mobile_edit";
									client.post(url, params, new TextHttpResponseHandler() {
										@Override
										public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
											return;
										}
										@Override
										public void onSuccess(int statusCode, Header[] headers, String responseString) {
											getTeamJobs(teamJid);
										}
									});
										dialog.cancel();
								}
							});

					builder1.setNegativeButton(
							"No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();
				}

				return false;
			}
		});
		mObjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				backButton.setVisibility(View.VISIBLE);
				if (addStatus) {
					// TODO: working on the status add button is clicked
				} else {
					/**
					 *before if in action view selected Appraise, then quantities list is changed by appraise list.
					 */
					if (currentcommands.get(0).toString().contentEquals("Appraise")) {
						quantities.clear();
						quantities.add("Very good");
						quantities.add("Good");
						quantities.add("Average");
						quantities.add("Poor");
						quantities.add("Very poor");
						quantityAdapter = new CommandAdapter(getActivity(), quantities);
						mQuantityListView.setAdapter(quantityAdapter);
						hideTagList(3);
						currentcommands.add(objects.get(position).toString());
						topBarTextView.append(objects.get(position).toString() + " ");
						currentObject_id = objectList.get(position).id;
						currentObjectKey = objectList.get(position).objectname;
					} else {
						formatListView();
						hideTagList(3);
						currentcommands.add(objects.get(position).toString());
						topBarTextView.append(objects.get(position).toString() + " ");
						currentObject_id = objectList.get(position).id;
						currentObjectKey = objectList.get(position).objectname;
						if (quantities.isEmpty()) {
							quantities.add(getString(R.string.command_quantity)+" 1");
							quantities.add(getString(R.string.command_other_quantity));
							quantityAdapter = new CommandAdapter(getActivity(), quantities);
							mQuantityListView.setAdapter(quantityAdapter);
						}
					}
				}
			}
		});

		mObjectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				if (addStatus) {
					selectedItemId = objectList.get(position).id;

					AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
					builder1.setMessage("Are you sure?");
					builder1.setCancelable(true);

					builder1.setPositiveButton(
							"Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

									AsyncHttpClient client = new AsyncHttpClient();
									RequestParams params = new RequestParams();
									params.put(WebClient.OBJECT_KEYWORD_ID,selectedItemId);
									params.put(WebClient.USERID,user_id);
									String url = "http://54.169.8.76/proapp/OcWorkteamObjectKeywords/mobile_edit";
									client.post(url, params, new TextHttpResponseHandler() {
										@Override
										public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
											return;
										}

										@Override
										public void onSuccess(int statusCode, Header[] headers, String responseString) {
											getTeamJobs(teamJid);
										}
									});
									dialog.cancel();
								}
							});


					builder1.setNegativeButton(
							"No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();
				}

				return false;
			}
		});
		mQuantityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				backButton.setVisibility(View.VISIBLE);
				if (addStatus) {

				} else {
					hideTagList(10);
					topBarTextView.setVisibility(View.VISIBLE);
					backButton.setVisibility(View.VISIBLE);
					if (quantities.get(position).toString().equals(getString(R.string.command_other_quantity))) {
						topBarTextView.append(getString(R.string.command_quantity)+" ");
						mEditMessage.setEnabled(true);
						mEditMessage.setText("");
						currentcommands.add("");
						_isquantitiy = true;
						mEditMessage.setInputType(InputType.TYPE_CLASS_NUMBER);
						//automatically popup keyboard.
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(mEditMessage, InputMethodManager.SHOW_IMPLICIT);
					} else {
						if(status.equals("Appraise")) {
							currentAppraisal = quantities.get(position).toString();
							currentcommands.add(quantities.get(position).toString());
							finishWork(currentOcWorktime, status);
							isCommand = true;
							isAccept = true;
							sendMessage();
							isAccept = false;
							hideTagList(10);
						} else {
							currentAppraisal = quantities.get(position).toString();
							topBarTextView.append(quantities.get(position).toString());
							currentcommands.add(quantities.get(position).toString());
							mEditMessage.setEnabled(false);
							currentQuantity = 1;
							isCommand = true;
							mEditMessage.setText(topBarTextView.getText().toString());
							sendMessage();
							isCommand = false;
						}

					}
					mSendButton.setTag(SendButtonAction.TEXT);
					mSendButton.setImageResource(getSendButtonImageResource(SendButtonAction.TEXT, Presence.Status.OFFLINE));
					mSendButton.setEnabled(true);
				}
			}
		});

		mGreenbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				offer_layout.setVisibility(View.GONE);
				formatListView();
				mEditMessage.setText("");
				topBarTextView.setText("");
				topBarTextView.setVisibility(View.GONE);
				currentcommands.clear();
				addStatus = false;
				addCommandButton.setVisibility(View.GONE);
				offer_layout.setVisibility(View.GONE);//3
				/**when the green button is clicked , get team list.***/
				if (commandLayout.getVisibility() == View.GONE) {
					if (!works.isEmpty() && !objects.isEmpty()) {
						hideTagList(0);
					} else {
						hideTagList(10);
						addJobLayout.setVisibility(View.VISIBLE);
						refreshImage.setVisibility(View.VISIBLE);
					}
					commandLayout.setVisibility(View.VISIBLE);
					messagesView.setAlpha(0.4f);
					mActionListView.setAlpha(1.0f);
					mEditMessage.setEnabled(false);
					isCommand = true;
					_isTagMode = true;
					mGreenbutton.setImageResource(R.drawable.ic_action_cancel);
					if(user_id == currentTeamCreator_id||user_group_id == 1){
						addJobLayout.setVisibility(View.VISIBLE);
					}else{
						addJobLayout.setVisibility(View.GONE);
					}
				} else {
					hideTagList(10);
					topBarTextView.setVisibility(View.GONE);
					addJobLayout.setVisibility(View.GONE);
					mEditMessage.setEnabled(true);
					addCommandButton.setVisibility(View.GONE);
					mSendButton.setVisibility(View.VISIBLE);
					mSendButton.setEnabled(true);
					backButton.setVisibility(View.GONE);
					isCommand = false;
					_isTagMode = false;
					mGreenbutton.setImageResource(R.drawable.ic_add_white_24dp);
					mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
				}
				mSendButton.setTag(SendButtonAction.TEXT);
				mSendButton.setImageResource(getSendButtonImageResource(SendButtonAction.TEXT, Presence.Status.OFFLINE));
			}

		});

		addCommandButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mEditMessage.getText().toString().isEmpty()) {
					if (addStatus) {
						if (mActionListView.getVisibility() == View.VISIBLE) {
						} else if (mWorkListView.getVisibility() == View.VISIBLE) {
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put(WebClient.WORK_NAME,mEditMessage.getText().toString());
							params.put(WebClient.WORKTEAM_ID,currentTeamId);
							params.put(WebClient.WORK_CREATOR_USER_ID,user_id);
							client.post("http://54.169.8.76/proapp/OcWorkteamWorkKeywords/mobile_add", params, new TextHttpResponseHandler() {
								@Override
								public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
									String aa = "kj";
								}
								@Override
								public void onSuccess(int statusCode, Header[] headers, String responseString) {
											getTeamJobs(teamJid);
								}
							});
							String temp = mEditMessage.getText().toString();
							mEditMessage.setText(topBarTextView.getText().toString() + " " + temp);
						} else if (mObjectListView.getVisibility() == View.VISIBLE) {
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put(WebClient.OBJECT_NAME,mEditMessage.getText().toString());
							params.put(WebClient.WORKTEAM_ID,currentTeamId);
							params.put(WebClient.OBJECT_CREATOR_ID,user_id);
							client.post("http://54.169.8.76/proapp/OcWorkteamObjectKeywords/mobile_add", params, new TextHttpResponseHandler() {
								@Override
								public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
								}
								@Override
								public void onSuccess(int statusCode, Header[] headers, String responseString) {
									getTeamJobs(teamJid);
								}
							});
							String temp = mEditMessage.getText().toString();
							mEditMessage.setText(topBarTextView.getText().toString() + " " + temp);
						} else if (mQuantityListView.getVisibility() == View.VISIBLE) {
							String temp = mEditMessage.getText().toString();
							mEditMessage.setText(topBarTextView.getText().toString() + " " + temp);
						}
					}
				}
				_isTagMode = false;
				sendMessage();
				try {
					mEditMessage.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		addJobLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backButton.setVisibility(View.VISIBLE);
				topBarTextView.setText("");
				if (mAddJobListView.getVisibility() == View.VISIBLE) {
					mAddJobListView.setVisibility(View.GONE);
					addStatus = false;
					addCommandButton.setVisibility(View.GONE);
					if (!works.isEmpty() && !objects.isEmpty()) {
						hideTagList(0);
					} else {
						hideTagList(0);
						mActionListView.setVisibility(View.GONE);
					}
					formatListView();
				} else {
					if (!works.isEmpty() && !objects.isEmpty()){
						hideTagList(0);
					} else {
						hideTagList(0);
						mActionListView.setVisibility(View.GONE);
					}
					topBarTextView.append(getString(R.string.command_add)+" ");
					topBarTextView.setVisibility(View.VISIBLE);

					hideTagList(4);
					formatListView();
					addStatus = true;
				}
			}
		});

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditMessage.setEnabled(false);
				mSendButton.setEnabled(false);
				messagesView.setAlpha(0.4f);
				backButton.setVisibility(View.VISIBLE);
				if (addStatus) {
					mEditMessage.setEnabled(false);
					mEditMessage.setText("");
					if (topBarTextView.getText().toString().equals(getString(R.string.command_add) + " ")) {
						formatListView();
						if (!works.isEmpty() && !objects.isEmpty()) {
							hideTagList(0);
						} else {
							hideTagList(0);
							mActionListView.setVisibility(View.GONE);
						}
						addStatus = false;
						topBarTextView.setVisibility(View.GONE);
						backButton.setVisibility(View.GONE);
						mSendButton.setVisibility(View.VISIBLE);
						addCommandButton.setVisibility(View.GONE);
						topBarTextView.setText("");

					} else if (topBarTextView.getText().toString().isEmpty()) {

					} else {
						formatListView();
						mEditMessage.setEnabled(false);
						hideTagList(4);
						topBarTextView.setText(getString(R.string.command_add)+ " ");
					}

				} else {
					if (status.equals("Assign")) {
						int ii = 0;
						mEditMessage.setText("");
						topBarTextView.setText("");
						if (!currentcommands.isEmpty()) {
							currentcommands.remove(currentcommands.size() - 1);
							for (int i = 0; i <= currentcommands.size() - 1; i++) {
								topBarTextView.append(currentcommands.get(i).toString() + " ");
								ii++;
							}
						}
						formatListView();

						if (currentcommands.size() == 0) {
							topBarTextView.setVisibility(View.GONE);
							backButton.setVisibility(View.GONE);
							if (!works.isEmpty() && !objects.isEmpty()) {
								hideTagList(ii);
							} else {
								hideTagList(ii);
								mActionListView.setVisibility(View.GONE);
							}
						} else {
							if (currentcommands.get(0).toString().equals(getString(R.string.command_appraise)+" ")) {
								quantities.clear();
								quantities.add("very Good");
								quantities.add("Good");
								quantities.add("average");
								quantities.add("poor");
								quantities.add("very poor");
								quantityAdapter = new CommandAdapter(getActivity(), quantities);
								mQuantityListView.setAdapter(quantityAdapter);
							}
							if (ii == 3)
								_isquantitiy = false;   //if in step 4 , click back
							switch (ii) {
								case 0:
									hideTagList(0);
									break;
								case 1:
									hideTagList(5);
									break;
								case 2:
									hideTagList(1);
									break;
								case 3:
									hideTagList(2);
									break;
								case 4:
									hideTagList(3);
									break;
								default:
									break;
							}
						}
					}else if(status.equals("Finish")){
						int ii = 0;
						mEditMessage.setText("");
						topBarTextView.setText("");
						if (!currentcommands.isEmpty()) {
							currentcommands.remove(currentcommands.size() - 1);
							for (int i = 0; i <= currentcommands.size() - 1; i++) {
								topBarTextView.append(currentcommands.get(i).toString() + " ");
								ii++;
							}
						}
						hideTagList(ii);
						if(ii == 0)topBarTextView.setVisibility(View.GONE);
					} else if (status.equals("Appraise")){
						int ii = 0;
						mEditMessage.setText("");
						topBarTextView.setText("");
						if (!currentcommands.isEmpty()) {
							currentcommands.remove(currentcommands.size() - 1);
							for (int i = 0; i <= currentcommands.size() - 1; i++) {
								topBarTextView.append(currentcommands.get(i).toString() + " ");
								ii++;
							}
						}
						hideTagList(ii);
						if(ii == 0)topBarTextView.setVisibility(View.GONE);
					} else {
						int ii = 0;
						mEditMessage.setText("");
						topBarTextView.setText("");
						if (!currentcommands.isEmpty()) {
							currentcommands.remove(currentcommands.size() - 1);
							for (int i = 0; i <= currentcommands.size() - 1; i++) {
								topBarTextView.append(currentcommands.get(i).toString() + " ");
								ii++;
							}
						}
						formatListView();

						if (currentcommands.size() == 0) {
							topBarTextView.setVisibility(View.GONE);
							backButton.setVisibility(View.GONE);
							if (!works.isEmpty() && !objects.isEmpty()) {
								hideTagList(ii);
							} else {
								hideTagList(ii);
								mActionListView.setVisibility(View.GONE);
							}
						} else {
							if (currentcommands.get(0).toString().equals(getString(R.string.command_appraise))) {
								quantities.clear();
								quantities.add("very Good");
								quantities.add("Good");
								quantities.add("average");
								quantities.add("poor");
								quantities.add("very poor");
								quantityAdapter = new CommandAdapter(getActivity(), quantities);
								mQuantityListView.setAdapter(quantityAdapter);
							}
							if (ii == 3)
								_isquantitiy = false;   //if in step 4 , click back
							hideTagList(ii);
						}
					}

				}
			}
		});

		offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				mEditMessage.setText("");
				topBarTextView.setText("");

				final EditText input = new EditText(getActivity());
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				final String acceptSentence = getString(R.string.command_accept)+" "+offeredWorks.get(position).workKeyword+" "+offeredWorks.get(position).objectKeyword+" "+getString(R.string.command_quantity);
				builder.setTitle(acceptSentence+"(max "+offeredWorks.get(position).workrootRemainQuantity+")");
				builder.setView(input);
				input.setText(String.valueOf(offeredWorks.get(position).workrootRemainQuantity));
				input.setSelection(String.valueOf(offeredWorks.get(position).workrootRemainQuantity).length());
				builder.setNegativeButton(getString(R.string.command_cancel),null);
				builder.setPositiveButton(getString(R.string.command_accept),new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						inputAcceptQuantity = input.getText().toString();
						int acceptQuantity = Integer.parseInt(inputAcceptQuantity);

						if(acceptQuantity > offeredWorks.get(position).workrootRemainQuantity){
							Toast.makeText(getActivity(),getString(R.string.command_not_valid_quantity),Toast.LENGTH_SHORT).show();
						} else {
							AsyncHttpClient client = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put("workroot_id", offeredWorks.get(position).workrootId);
							params.put("quantity", offeredWorks.get(position).workrootRemainQuantity-acceptQuantity);
							params.put("acceptquantity",acceptQuantity);
							params.put("user_id",user_id);
							params.put("provider",offeredWorks.get(position).workrootUserId);
							params.put("teamid",currentTeamId);
							params.put("workkeyid",offeredWorks.get(position).workKeywordId);
							params.put("objectkeyid", offeredWorks.get(position).objectKeywordId);
							params.put(WebClient.STATUS,"Accept");
							String url = "http://54.169.8.76/proapp/OcWorksources/mobile_add";
							client.post(url, params, new TextHttpResponseHandler() {
								@Override
								public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

								}

								@Override
								public void onSuccess(int statusCode, Header[] headers, String responseString) {
									try {
										JSONObject object = new JSONObject(responseString);
										switch (object.getInt("status")){
											case 100:
												getWorkoffers();
												break;
											case 400:
												Toast.makeText(getActivity(),object.getString("data"),Toast.LENGTH_SHORT).show();
												break;
											default:
												break;
										}
									}catch (JSONException e){
										e.printStackTrace();
									}

								}
							});
							//Accept sentence add in message table
							mEditMessage.setText(acceptSentence+inputAcceptQuantity);
							isCommand = true;
							isAccept = true;
							sendMessage();
							isAccept = false;
						}
					}
				});
				builder.create().show();
				hideTagList(10);
				messagesView.setAlpha(1f);
				offer_layout.setVisibility(View.GONE);
			}
		});
		offersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Are you sure?");
				builder.setNegativeButton("No",null);
				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteWorkoffer(position);
					}
				});
				builder.create().show();
				return false;
			}
		});

		reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

			}
		});

		refreshImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getTeamJobs(teamJid);
//				getTeamMembers();
				hideTagList(10);

			}
		});
		/**
		 * Bottom bar tab's onClickListeners
		 */
		actionbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideTagList(10);
				offer_layout.setVisibility(View.GONE);
				assignHistoryLayout.setVisibility(View.GONE);
				bottombarSelectedStatus(v);
				report_layout.setVisibility(View.GONE);
				productivityLayout.setVisibility(View.GONE);
				teamMemberLayout.setVisibility(View.GONE);
			}
		});
		findaJobbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(offer_layout.getVisibility() != View.VISIBLE){
					offer_layout.setVisibility(View.VISIBLE);
					hideTagList(10);
					messagesView.setAlpha(0.2f);
					getWorkoffers();
					updateExpiredData();
					getAcceptWorks();
					offersListView.setVisibility(View.VISIBLE);
					acceptHistoryListView.setVisibility(View.INVISIBLE);
					offeredWorkButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
					acceptHistoryButton.setBackgroundColor(getResources().getColor(R.color.black54));
				} else {
					offer_layout.setVisibility(View.INVISIBLE);
					hideTagList(10);
				}
				assignHistoryLayout.setVisibility(View.GONE);
				bottombarSelectedStatus(v);
				report_layout.setVisibility(View.GONE);
				productivityLayout.setVisibility(View.GONE);
				teamMemberLayout.setVisibility(View.GONE);
			}
		});
		completedJobButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(assignHistoryLayout.getVisibility() != View.VISIBLE ) {
					hideTagList(10);
					assignHistoryLayout.setVisibility(View.VISIBLE);
					getCompletedAssignJobs();
					getIncompleteAssignJobs();
					assignCompletedWorkList.setVisibility(View.VISIBLE);
					assignUncompletedWorkList.setVisibility(View.GONE);
					assignCompletedWorkHistoryButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
					assignIncompletedWorkHistoryButton.setBackgroundColor(getResources().getColor(R.color.black54));
				} else {
					assignHistoryLayout.setVisibility(View.GONE);
				}
				offer_layout.setVisibility(View.GONE);
				bottombarSelectedStatus(v);
				report_layout.setVisibility(View.GONE);
				productivityLayout.setVisibility(View.GONE);
				teamMemberLayout.setVisibility(View.GONE);
			}

		});
		reportWorksButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(report_layout.getVisibility() == View.GONE) {
					reportListViewStatus = 0;
					reportAdapter = new ReportAdapter(getActivity(),reportCompleteWorks);
					reportListView.setAdapter(reportAdapter);
					bottombarSelectedStatus(v);
					report_layout.setVisibility(View.VISIBLE);
					getReportWorks();
				} else {
					report_layout.setVisibility(View.GONE);
				}
				productivityLayout.setVisibility(View.GONE);
				teamMemberLayout.setVisibility(View.GONE);
			}

		});
		teamButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideTagList(10);
				if(teamMemberLayout.getVisibility() == View.GONE){
					teamMemberLayout.setVisibility(View.VISIBLE);
					getAllTeamMembers();
					kpHUD.show();
				} else {
					teamMemberLayout.setVisibility(View.GONE);
				}
				offer_layout.setVisibility(View.GONE);
				assignHistoryLayout.setVisibility(View.GONE);
				bottombarSelectedStatus(v);
				report_layout.setVisibility(View.GONE);
				productivityLayout.setVisibility(View.GONE);
			}
		});
		userLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideTagList(10);
				offer_layout.setVisibility(View.GONE);
				assignHistoryLayout.setVisibility(View.GONE);
				bottombarSelectedStatus(v);
				report_layout.setVisibility(View.GONE);
				productivityLayout.setVisibility(View.GONE);
				teamMemberLayout.setVisibility(View.GONE);
			}
		});
		productivityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (productivityLayout.getVisibility() == View.GONE) {
					hideTagList(10);
					bottombarSelectedStatus(v);
					productivityLayout.setVisibility(View.VISIBLE);
					getProductivities();
				} else {
					productivityLayout.setVisibility(View.GONE);
				}
				report_layout.setVisibility(View.GONE);
				offer_layout.setVisibility(View.GONE);
				assignHistoryLayout.setVisibility(View.GONE);
				teamMemberLayout.setVisibility(View.GONE);
			}
		});

		/**assign function tabs*/
		assignCompletedWorkHistoryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				assignCompletedWorkList.setVisibility(View.VISIBLE);
				assignUncompletedWorkList.setVisibility(View.GONE);
				assignCompletedWorkHistoryButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				assignIncompletedWorkHistoryButton.setBackgroundColor(getResources().getColor(R.color.black54));
			}
		});

		assignIncompletedWorkHistoryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				assignCompletedWorkList.setVisibility(View.GONE);
				assignUncompletedWorkList.setVisibility(View.VISIBLE);
				assignCompletedWorkHistoryButton.setBackgroundColor(getResources().getColor(R.color.black54));
				assignIncompletedWorkHistoryButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
			}
		});

		/** for Report function 3 tabs***/
		reportCompleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportListViewStatus = 0;
				reportAdapter = new ReportAdapter(getActivity(),reportCompleteWorks);
				reportListView.setAdapter(reportAdapter);
				changeReportTabs(reportListViewStatus);

			}
		});
		reportIncompleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportListViewStatus = 1;
				reportAdapter = new ReportAdapter(getActivity(),reportIncompleteWorks);
				reportListView.setAdapter(reportAdapter);
				changeReportTabs(reportListViewStatus);
			}
		});
		reportAbortButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reportListViewStatus = 2;
				reportAdapter = new ReportAdapter(getActivity(),reportAbortWorks);
				reportListView.setAdapter(reportAdapter);
				changeReportTabs(reportListViewStatus);
			}
		});
		/** for productivity 2 tabs******/
		productivitySummaryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				productivitySummaryListView.setVisibility(View.VISIBLE);
				productivityDetailListView.setVisibility(View.INVISIBLE);
				productivitySummaryButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				productivityDetailButton.setBackgroundColor(getResources().getColor(R.color.black54));
				productivityAdapter = new ProductivityAdapter(getActivity(),summaryProdcutivities);
				productivitySummaryListView.setAdapter(productivityAdapter);
			}
		});
		productivityDetailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				productivityDetailListView.setVisibility(View.VISIBLE);
				productivitySummaryListView.setVisibility(View.INVISIBLE);
				productivitySummaryButton.setBackgroundColor(getResources().getColor(R.color.black54));
				productivityDetailButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				productivityDetailAdapter = new ProductivityDetailAdapter(getActivity(),detailProductivities);
				productivityDetailListView.setAdapter(productivityDetailAdapter);
			}
		});

		/** for team members list**/
		teamMemberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(teamMemberListView.getAdapter().equals(teamMemberAdapter)) {
					teamMemberAdapter.notifyDataSetChanged();
				} else {
					teamMemberCheckableAdapter.notifyDataSetChanged();

				}
			}
		});
	}

	private void changeReportTabs(int position){
		switch (position){
			case 0:
				reportCompleteButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				reportIncompleteButton.setBackgroundColor(getResources().getColor(R.color.black54));
				reportAbortButton.setBackgroundColor(getResources().getColor(R.color.black54));
				break;
			case 1:
				reportCompleteButton.setBackgroundColor(getResources().getColor(R.color.black54));
				reportIncompleteButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				reportAbortButton.setBackgroundColor(getResources().getColor(R.color.black54));
				break;
			case 2:
				reportCompleteButton.setBackgroundColor(getResources().getColor(R.color.black54));
				reportIncompleteButton.setBackgroundColor(getResources().getColor(R.color.black54));
				reportAbortButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				break;
			default:
				break;
		}
	}

	private boolean deleteWorkoffer(int position){

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("workoffer_id",offerworks.get(position).workoffer_id);
		client.post("http://54.169.8.76/proapp/OcWorkoffers/mobile_delete", params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				getWorkoffers();
			}
		});
		return true;
	}

	private void getWorkoffers(){
		kpHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put(WebClient.WORKTEAM_ID,currentTeamId);
		client.post("http://54.169.8.76/proapp/OcWorkroots/mobile_getOfferedWorks", params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				String response = responseString;
				kpHUD.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				offeredWorks.clear();
				try {
					JSONObject responseJsonObject = new JSONObject(responseString);
					switch (responseJsonObject.getInt(WebClient.STATUS)){
						case 100:
							String dataString = responseJsonObject.getString(WebClient.DATA);
							JSONArray objectArray = new JSONArray(dataString);
							for(int i = 0 ; i < objectArray.length() ; i++) {
								JSONObject objectobj = objectArray.getJSONObject(i);
								String offerstring = objectobj.getString("OcWorkroot");
								JSONObject offerobj = new JSONObject(offerstring);
								String userString = objectobj.getString("OcUser");
								JSONObject userObj = new JSONObject(userString);
								String workString = objectobj.getString("OcWorkteamWorkKeyword");
								JSONObject workObj = new JSONObject(workString);
								String objectString = objectobj.getString("OcWorkteamObjectKeyword");
								JSONObject objectObj = new JSONObject(objectString);
								offeredWorks.add(new OcWorkroot(offerobj.getInt("workroot_id"),
										offerobj.getString("workroot"),
										offerobj.getInt("workroot_user_id"),
										offerobj.getInt("workteam_id"),
										offerobj.getInt("work_keyword_id"),
										offerobj.getInt("object_keyword_id"),
										offerobj.getInt("workroot_quantity"),
										offerobj.getInt("workroot_less_worksource_quantity"),
										offerobj.getString("workroot_datetime"),
										offerobj.getInt("workroot_duration"),
										offerobj.getBoolean("isExpired"),
										workObj.getString("work_name"),
										objectObj.getString("object_name"),
										userObj.getString("username")));
							}
							break;
						case 400:
							offeredWorks.clear();
							break;
						default:
							return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				offerAdapter = new OfferAdapter(getActivity(),offeredWorks);
				offersListView.setAdapter(offerAdapter);
				kpHUD.dismiss();
			}
		});
	}

	public void updateExpiredData(){
		AsyncHttpClient client  = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("teamid",currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorkroots/update_expiredtime";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
			}
		});

		/**
		 * for offers list view and accept list view.
		 */
		offeredWorkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				offersListView.setVisibility(View.VISIBLE);
				acceptHistoryListView.setVisibility(View.INVISIBLE);
				offeredWorkButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));
				acceptHistoryButton.setBackgroundColor(getResources().getColor(R.color.black54));
			}
		});
		acceptHistoryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				offersListView.setVisibility(View.INVISIBLE);
				acceptHistoryListView.setVisibility(View.VISIBLE);
				offeredWorkButton.setBackgroundColor(getResources().getColor(R.color.black54));
				acceptHistoryButton.setBackgroundColor(getResources().getColor(R.color.tab_selected));

			}
		});
		acceptHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});

	}
	/**
	 * get Accept list
	 */

	public void getAcceptWorks(){
		kpHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("team_id",currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorkroots/mobile_getAllOfferedWorks";
		client.post(url,params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(getActivity(),"no response",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				historyOffers.clear();
				childChildMap.clear();
				childMap.clear();
				try {
					JSONObject responseJsonObject = new JSONObject(responseString);
					switch (responseJsonObject.getInt(WebClient.STATUS)){
						case 100:
							String dataString = responseJsonObject.getString(WebClient.DATA);
							JSONArray objectArray = new JSONArray(dataString);
							for(int i = 0 ; i < objectArray.length() ; i++) {
								JSONObject objectobj = objectArray.getJSONObject(i);
								String offerstring = objectobj.getString("OcWorkroot");
								JSONObject offerobj = new JSONObject(offerstring);
								String userString = objectobj.getString("OcUser");
								JSONObject userObj = new JSONObject(userString);
								String workString = objectobj.getString("OcWorkteamWorkKeyword");
								JSONObject workObj = new JSONObject(workString);
								String objectString = objectobj.getString("OcWorkteamObjectKeyword");
								JSONObject objectObj = new JSONObject(objectString);
								OcWorkroot workroot = new OcWorkroot(offerobj.getInt("workroot_id"),
										offerobj.getString("workroot"),
										offerobj.getInt("workroot_user_id"),
										offerobj.getInt("workteam_id"),
										offerobj.getInt("work_keyword_id"),
										offerobj.getInt("object_keyword_id"),
										offerobj.getInt("workroot_quantity"),
										offerobj.getInt("workroot_less_worksource_quantity"),
										offerobj.getString("workroot_datetime"),
										offerobj.getInt("workroot_duration"),
										offerobj.getBoolean("isExpired"),
										workObj.getString("work_name"),
										objectObj.getString("object_name"),
										userObj.getString("username"));
								historyOffers.add(workroot);
								List<OcWorksource> childListGroup = new ArrayList<OcWorksource>();
								String worksourceString = objectobj.getString("OcWorksource");
								JSONArray worksourceObjs = new JSONArray(worksourceString);
								for(int j = 0; j < worksourceObjs.length() ; j++){
									JSONObject obj = worksourceObjs.getJSONObject(j);
									String providerString = obj.getString("Provider");
									JSONObject provider = new JSONObject(providerString);
									String workerString = obj.getString("Worker");
									JSONObject worker = new JSONObject(workerString);
									String work = obj.getString("OcWorkteamWorkKeyword");
									JSONObject workkey = new JSONObject(work);
									String object = obj.getString("OcWorkteamObjectKeyword");
									JSONObject objectkey = new JSONObject(object);
									OcWorksource worksource = new OcWorksource(obj.getInt("worksource_id"),obj.getString("worksource"),
											obj.getInt("workteam_id"),obj.getInt("workroot_id"),obj.getInt("worksource_provider_id"),
											obj.getInt("worksource_worker_id"), obj.getString("worksource_create_datetime"),
											workkey.getString("work_name"), objectkey.getString("object_name"),
											obj.getInt("worksource_quantity"), obj.getInt("worksource_quantity_less_started"),
											obj.getInt("worksource_quantity_less_finished"),obj.getBoolean("worksource_isClosed"),
											obj.getString("worksource_close_datetime"),provider.getString("username"),
											worker.getString("username"),
											obj.getInt("work_keyword_id"),
											obj.getInt("object_keyword_id"));
									childListGroup.add(worksource);
									List<OcWorktime> worktimes = new ArrayList<OcWorktime>();
									String worktimeString = obj.getString("OcWorktime");
									JSONArray worktimeArray = new JSONArray(worktimeString);
									for(int k = 0 ; k < worktimeArray.length() ; k++){
										JSONObject worktime = worktimeArray.getJSONObject(k);
										OcWorktime completedwork = new OcWorktime(worktime.getInt("worktime_id"),
												worktime.getInt("workteam_id"),
												worktime.getInt("worksource_id"),
												worktime.getInt("user_id"),
												worktime.getInt("work_keyword_id"),
												worktime.getInt("object_keyword_id"),
												worktime.getInt("quantity"),
												worktime.getString("start_datetime"),
												worktime.getString("end_datetime"),
												worktime.getString("work_duration_minutes"),
												worktime.getInt("appraiser_id"),
												worktime.getString("appraise_datetime"),
												worktime.getString("appraisal"),
												worktime.getInt("abort_by_user_id"),
												worktime.getString("abort_datetime"),
												worktime.getString("worktime_status"));
										String worktimeUserString = worktime.getString("OcUser");
										if(worktimeUserString.equals("[]")) worktimeUserString = "{}";
										JSONObject user = new JSONObject(worktimeUserString);
										String worktimeworkkey = worktime.getString("OcWorkteamWorkKeyword");
										if(worktimeworkkey.equals("[]")) worktimeworkkey = "{}";
										JSONObject worktimework = new JSONObject(worktimeworkkey);
										String worktimeobjectkey = worktime.getString("OcWorkteamObjectKeyword");
										if(worktimeobjectkey.equals("[]"))worktimeobjectkey = "{}";
										JSONObject worktimeobject = new JSONObject(worktimeobjectkey);
										String appraiserString = worktime.getString("Appraiser");
										if(appraiserString.equals("[]")) appraiserString = "{}";
										JSONObject appraiser = new JSONObject(appraiserString);
										String abortUserString = worktime.getString("AbortUser");
										if(abortUserString.equals("[]")) abortUserString = "{}";
										JSONObject abortUser = new JSONObject(abortUserString);
										completedwork.workKeyword = worktimework.getString("work_name");
										completedwork.objectKeyword = worktimeobject.getString("object_name");
										completedwork.userName = user.getString("username");
										if(appraiserString.equals("{}")){} else {
											completedwork.appraiserName = appraiser.getString("username");}
										if(abortUserString.equals("{}")){}else{
											completedwork.abortUserName = abortUser.getString("username");}
										worktimes.add(completedwork);
										worktimes.add(completedwork);
									}
									childChildMap.put(worksource,worktimes);

								}
								childMap.put(workroot,childListGroup);
							}
							historyAdapter = new ParentLevelListAdapter(getActivity(),historyOffers,childMap,childChildMap);
							acceptHistoryListView.setAdapter(historyAdapter);
							break;
						case 400:
							historyAdapter = new ParentLevelListAdapter(getActivity(),historyOffers,childMap,childChildMap);
							historyAdapter.notifyDataSetChanged();
							break;
						default:
							return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e){
					e.printStackTrace();
				}
				kpHUD.dismiss();
			}
		});
	}

	public void getActivedJobs(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("team_id", currentTeamId);
		params.put("user_id", user_id);
		String url = "http://54.169.8.76/proapp/OcWorktimes/get_active_works";
		activeWorks.clear();
		kpHUD.show();
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(getActivity(),"no response",Toast.LENGTH_SHORT).show();
				kpHUD.dismiss();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				try {
					JSONObject response = new JSONObject(responseString);
					switch (response.getInt(WebClient.STATUS)){
						case 100:
							String data = response.getString("data");
							JSONArray dataArray = new JSONArray(data);
							for (int i = 0 ; i < dataArray.length() ; i++) {
								JSONObject obj = dataArray.getJSONObject(i);
								String workTimeString = obj.getString("OcWorktime");
								JSONObject workTimeObject = new JSONObject(workTimeString);
								OcWorktime activeWork = new OcWorktime(workTimeObject.getInt("worktime_id"),
										workTimeObject.getInt("workteam_id"),
										workTimeObject.getInt("worksource_id"),
										workTimeObject.getInt("user_id"),
										workTimeObject.getInt("work_keyword_id"),
										workTimeObject.getInt("object_keyword_id"),
										workTimeObject.getInt("quantity"),
										workTimeObject.getString("start_datetime"),
										workTimeObject.getString("end_datetime"),
										workTimeObject.getString("work_duration_minutes"),
										workTimeObject.getInt("appraiser_id"),
										workTimeObject.getString("appraise_datetime"),
										workTimeObject.getString("appraisal"),
										workTimeObject.getInt("abort_by_user_id"),
										workTimeObject.getString("abort_datetime"),
										workTimeObject.getString("worktime_status"));
								String userString = obj.getString("OcUser");
								JSONObject user = new JSONObject(userString);
								String workkey = obj.getString("OcWorkteamWorkKeyword");
								JSONObject work = new JSONObject(workkey);
								String objectkey = obj.getString("OcWorkteamObjectKeyword");
								JSONObject object = new JSONObject(objectkey);
								String appraiserString = obj.getString("Appraiser");
								JSONObject appraiser = new JSONObject(appraiserString);
								String abortUserString = obj.getString("AbortUser");
								JSONObject abortUser = new JSONObject(abortUserString);
								activeWork.workKeyword = work.getString("work_name");
								activeWork.objectKeyword = object.getString("object_name");
								activeWork.userName = user.getString("username");
								activeWork.appraiserName = appraiser.getString("username");
								activeWork.abortUserName = abortUser.getString("username");
								activeWorks.add(activeWork);
							}
							activeWorkAdapter = new ActiveWorkAdapter(getActivity(),activeWorks);
							mWorkListView.setAdapter(activeWorkAdapter);
							hideTagList(1);
							break;
						case 400:
							break;
						default :
							break;
					}
				}catch (JSONException e){
					e.printStackTrace();
				}
				kpHUD.dismiss();
			}
		});
	}

	public void getFinishedWorks(){
		kpHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("workteamid", currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorktimes/getfinishedWorks";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				kpHUD.dismiss();
				Toast.makeText(getActivity(),"connection error" , Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				finishedWorks.clear();
				try {
					JSONObject response = new JSONObject(responseString);
					switch (response.getInt(WebClient.STATUS)){
						case 100:
							String data = response.getString("data");
							JSONArray dataArray = new JSONArray(data);
							for (int i = 0 ; i < dataArray.length() ; i++) {
								JSONObject obj = dataArray.getJSONObject(i);
								String workTimeString = obj.getString("OcWorktime");
								JSONObject workTimeObject = new JSONObject(workTimeString);
								OcWorktime activeWork = new OcWorktime(workTimeObject.getInt("worktime_id"),
										workTimeObject.getInt("workteam_id"),
										workTimeObject.getInt("worksource_id"),
										workTimeObject.getInt("user_id"),
										workTimeObject.getInt("work_keyword_id"),
										workTimeObject.getInt("object_keyword_id"),
										workTimeObject.getInt("quantity"),
										workTimeObject.getString("start_datetime"),
										workTimeObject.getString("end_datetime"),
										workTimeObject.getString("work_duration_minutes"),
										workTimeObject.getInt("appraiser_id"),
										workTimeObject.getString("appraise_datetime"),
										workTimeObject.getString("appraisal"),
										workTimeObject.getInt("abort_by_user_id"),
										workTimeObject.getString("abort_datetime"),
										workTimeObject.getString("worktime_status"));
								String userString = obj.getString("OcUser");
								if(userString.equals("[]")) userString = "{}";
								JSONObject user = new JSONObject(userString);
								String workkey = obj.getString("OcWorkteamWorkKeyword");
								if(workkey.equals("[]")) workkey = "{}";
								JSONObject work = new JSONObject(workkey);
								String objectkey = obj.getString("OcWorkteamObjectKeyword");
								if(objectkey.equals("[]")) objectkey = "{}";
								JSONObject object = new JSONObject(objectkey);
								String appraiserString = obj.getString("Appraiser");
								if(appraiserString.equals("[]")) appraiserString = "{}";
								JSONObject appraiser = new JSONObject(appraiserString);
								String abortUserString = obj.getString("AbortUser");
								if(abortUserString.equals("[]")) abortUserString = "{}";
								JSONObject abortUser = new JSONObject(abortUserString);
								if(!workkey.equals("{}"))
								activeWork.workKeyword = work.getString("work_name");
								if(!objectkey.equals("{}"))
								activeWork.objectKeyword = object.getString("object_name");
								if(!userString.equals("{}"))
								activeWork.userName = user.getString("username");
								if(!appraiserString.equals("{}"))
								activeWork.appraiserName = appraiser.getString("username");
								if(!abortUser.equals("{}"))
								activeWork.abortUserName = abortUser.getString("username");
								finishedWorks.add(activeWork);
							}
							activeWorkAdapter = new ActiveWorkAdapter(getActivity(),finishedWorks);
							mWorkListView.setAdapter(activeWorkAdapter);
							hideTagList(1);
							break;
						case 400:
							break;
						default :
							break;
					}
				}catch (JSONException e){
					e.printStackTrace();
				}
				kpHUD.dismiss();
			}
		});
	}

	public void getToStartWorks(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("assigned_user_id",user_id);
		params.put("workteamid",currentTeamId);
		toStartAssigns.clear();
		toStartAccepts.clear();
		String url = "http://54.169.8.76/proapp/OcWorksources/getToStartWorks";
		kpHUD.show();
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(getActivity(),"connection error" , Toast.LENGTH_SHORT).show();
				kpHUD.dismiss();
				return;
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				toStartAssigns.clear();
				toStartAccepts.clear();
				try {
					JSONObject responseOBJ = new JSONObject(responseString);
					switch (responseOBJ.getInt("status")){
						case 100:
							String data = responseOBJ.getString("data");
							JSONArray arrayData = new JSONArray(data);
							for(int i = 0 ; i < arrayData.length() ; i++){
								JSONObject jobObj = arrayData.getJSONObject(i);
								String jobString = jobObj.getString("OcWorksource");
								JSONObject job = new JSONObject(jobString);
								String providerString = jobObj.getString("Provider");
								JSONObject provider = new JSONObject(providerString);
								String workerString = jobObj.getString("Worker");
								JSONObject worker = new JSONObject(workerString);
								String work = jobObj.getString("OcWorkteamWorkKeyword");
								JSONObject workkey = new JSONObject(work);
								String object = jobObj.getString("OcWorkteamObjectKeyword");
								JSONObject objectkey = new JSONObject(object);
								OcWorksource assignWork = new OcWorksource(job.getInt("worksource_id"),job.getString("worksource"),
										job.getInt("workteam_id"),job.getInt("workroot_id"),job.getInt("worksource_provider_id"),
										job.getInt("worksource_worker_id"), job.getString("worksource_create_datetime"),
										workkey.getString("work_name"), objectkey.getString("object_name"),
										job.getInt("worksource_quantity"), job.getInt("worksource_quantity_less_started"),
										job.getInt("worksource_quantity_less_finished"),job.getBoolean("worksource_isClosed"),
										job.getString("worksource_close_datetime"),provider.getString("username"),
										worker.getString("username"),
										job.getInt("work_keyword_id"),
										job.getInt("object_keyword_id"));
								if(assignWork.worksource.trim().equals("Assign")) {
									toStartAssigns.add(assignWork);
								} else {
									if(worker.getInt("user_id") == user_id)
										toStartAccepts.add(assignWork);
								}
							}
							toStartAllworks = collactionArrays(toStartAssigns,toStartAccepts);
							assignAdapter = new AssignedWorkAdapter(getActivity(), toStartAllworks);
									assignAdapter.notifyDataSetChanged();
									assignedWorkListView.setAdapter(assignAdapter);
							hideTagList(1);
							break;
						case 400:
							toStartAllworks = collactionArrays(toStartAssigns,toStartAccepts);
							assignAdapter = new AssignedWorkAdapter(getActivity(), toStartAllworks);
							assignAdapter.notifyDataSetChanged();
							assignedWorkListView.setAdapter(assignAdapter);
							hideTagList(1);
							break;
						default:
							break;
					}
				}catch (JSONException e){
					e.printStackTrace();
				}
				kpHUD.dismiss();

			}
		});
	}

	public ArrayList<OcWorksource> collactionArrays(ArrayList<OcWorksource> assignWorks, ArrayList<OcWorksource> acceptWorks){
		ArrayList<OcWorksource> works = new ArrayList<>();
		OcWorksource start = new OcWorksource();
		start.worksource = "start";
		if(!assignWorks.isEmpty()) {
			for (OcWorksource work : assignWorks) {
				if(work.worksourceWorkerId == user_id) {
					works.add(work);
				}
			}
			if(!works.isEmpty()) works.add(0,start);
		}
		OcWorksource section = new OcWorksource();
		section.worksource = "section";
		if(!acceptWorks.isEmpty()) {
			works.add(section);
			for (OcWorksource work : acceptWorks) {
				works.add(work);
			}
		}
		return works;
	}

	public void getCompletedAssignJobs(){
			kpHUD.show();
			assignCompletedWorks.clear();
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("workteamid",currentTeamId);
			String url = "http://54.169.8.76/proapp/OcWorksources/getcompletedworks";
			client.post(url, params, new TextHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String responseString) {
					assignCompletedWorks.clear();
					try {
						JSONObject responseOBJ = new JSONObject(responseString);
						switch (responseOBJ.getInt("status")){
							case 100:
								String data = responseOBJ.getString("data");
								JSONArray arrayData = new JSONArray(data);
								for(int i = 0 ; i < arrayData.length() ; i++){
									JSONObject jobObj = arrayData.getJSONObject(i);
									String jobString = jobObj.getString("OcWorksource");
									if(jobString.equals("[]"))jobString = "{}";
									JSONObject job = new JSONObject(jobString);
									String providerString = jobObj.getString("Provider");
									if(providerString.equals("[]")) providerString = "{}";
									JSONObject provider = new JSONObject(providerString);
									String workerString = jobObj.getString("Worker");
									if(workerString.equals("[]")) workerString = "{}";
									JSONObject worker = new JSONObject(workerString);
									String work = jobObj.getString("OcWorkteamWorkKeyword");
									if(work.equals("[]")) work = "{}";
									JSONObject workkey = new JSONObject(work);
									String object = jobObj.getString("OcWorkteamObjectKeyword");
									if(object.equals("[]")) object = "{}";
									JSONObject objectkey = new JSONObject(object);
									OcWorksource assignWork = new OcWorksource(job.getInt("worksource_id"),job.getString("worksource"),
											job.getInt("workteam_id"),job.getInt("workroot_id"),job.getInt("worksource_provider_id"),
											job.getInt("worksource_worker_id"), job.getString("worksource_create_datetime"),
											workkey.getString("work_name"), objectkey.getString("object_name"),
											job.getInt("worksource_quantity"), job.getInt("worksource_quantity_less_started"),
											job.getInt("worksource_quantity_less_finished"),job.getBoolean("worksource_isClosed"),
											job.getString("worksource_close_datetime"),provider.getString("username"),
											worker.getString("username"),
											job.getInt("work_keyword_id"),
											job.getInt("object_keyword_id"));
									assignCompletedWorks.add(assignWork);
									List<OcWorktime> childListGroup = new ArrayList<OcWorktime>();
									String worktimeString = jobObj.getString("OcWorktime");
									JSONArray worktimeArray = new JSONArray(worktimeString);
									for(int j = 0 ; j < worktimeArray.length() ; j++){
										JSONObject worktime = worktimeArray.getJSONObject(j);
										OcWorktime completedwork = new OcWorktime(worktime.getInt("worktime_id"),
												worktime.getInt("workteam_id"),
												worktime.getInt("worksource_id"),
												worktime.getInt("user_id"),
												worktime.getInt("work_keyword_id"),
												worktime.getInt("object_keyword_id"),
												worktime.getInt("quantity"),
												worktime.getString("start_datetime"),
												worktime.getString("end_datetime"),
												worktime.getString("work_duration_minutes"),
												worktime.getInt("appraiser_id"),
												worktime.getString("appraise_datetime"),
												worktime.getString("appraisal"),
												worktime.getInt("abort_by_user_id"),
												worktime.getString("abort_datetime"),
												worktime.getString("worktime_status"));
										String userString = worktime.getString("OcUser");
										if(userString.equals("[]")) userString = "{}";
										JSONObject user = new JSONObject(userString);
										String worktimeworkkey = worktime.getString("OcWorkteamWorkKeyword");
										if(worktimeworkkey.equals("[]")) worktimeworkkey = "{}";
										JSONObject worktimework = new JSONObject(worktimeworkkey);
										String worktimeobjectkey = worktime.getString("OcWorkteamObjectKeyword");
										if(worktimeobjectkey.equals("[]"))worktimeobjectkey = "{}";
										JSONObject worktimeobject = new JSONObject(worktimeobjectkey);
										String appraiserString = worktime.getString("Appraiser");
										if(appraiserString.equals("[]")) appraiserString = "{}";
										JSONObject appraiser = new JSONObject(appraiserString);
										String abortUserString = worktime.getString("AbortUser");
										if(abortUserString.equals("[]")) abortUserString = "{}";
										JSONObject abortUser = new JSONObject(abortUserString);
										completedwork.workKeyword = worktimework.getString("work_name");
										completedwork.objectKeyword = worktimeobject.getString("object_name");
										completedwork.userName = user.getString("username");
										if(appraiserString.equals("{}")){} else {
											completedwork.appraiserName = appraiser.getString("username");}
										if(abortUserString.equals("{}")){}else{
										completedwork.abortUserName = abortUser.getString("username");}
										childListGroup.add(completedwork);
										childListGroup.add(completedwork);
									}
									completedAssignSubHistory.put(assignWork,childListGroup);
								}
								completedAssignAdapter = new AssignHistoryExpandableAdapter(getActivity(),assignCompletedWorks,completedAssignSubHistory);
								assignCompletedWorkList.setAdapter(completedAssignAdapter);
								break;
							case 400:
								break;
							default:
								break;
						}
					}catch (JSONException e){
						e.printStackTrace();
					}
					kpHUD.dismiss();
				}
			});
	}

	public void getIncompleteAssignJobs(){
			kpHUD.show();
			assignCompletedWorks.clear();
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("workteamid",currentTeamId);
			params.put("assigned_user_id",user_id);
			String url = "http://54.169.8.76/proapp/OcWorksources/getInCompletedWorks";
			client.post(url, params, new TextHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String responseString) {
					assignUncompletedWorks.clear();
					try {
						JSONObject responseOBJ = new JSONObject(responseString);
						switch (responseOBJ.getInt("status")){
							case 100:
								String data = responseOBJ.getString("data");
								JSONArray arrayData = new JSONArray(data);
								for(int i = 0 ; i < arrayData.length() ; i++){
									JSONObject jobObj = arrayData.getJSONObject(i);
									String jobString = jobObj.getString("OcWorksource");
									if(jobString.equals("[]"))jobString = "{}";
									JSONObject job = new JSONObject(jobString);
									String providerString = jobObj.getString("Provider");
									if(providerString.equals("[]")) providerString = "{}";
									JSONObject provider = new JSONObject(providerString);
									String workerString = jobObj.getString("Worker");
									if(workerString.equals("[]")) workerString = "{}";
									JSONObject worker = new JSONObject(workerString);
									String work = jobObj.getString("OcWorkteamWorkKeyword");
									if(work.equals("[]")) work = "{}";
									JSONObject workkey = new JSONObject(work);
									String object = jobObj.getString("OcWorkteamObjectKeyword");
									if(object.equals("[]")) object = "{}";
									JSONObject objectkey = new JSONObject(object);
									OcWorksource assignWork = new OcWorksource(job.getInt("worksource_id"),job.getString("worksource"),
											job.getInt("workteam_id"),job.getInt("workroot_id"),job.getInt("worksource_provider_id"),
											job.getInt("worksource_worker_id"), job.getString("worksource_create_datetime"),
											workkey.getString("work_name"), objectkey.getString("object_name"),
											job.getInt("worksource_quantity"), job.getInt("worksource_quantity_less_started"),
											job.getInt("worksource_quantity_less_finished"),job.getBoolean("worksource_isClosed"),
											job.getString("worksource_close_datetime"),provider.getString("username"),
											worker.getString("username"),
											job.getInt("work_keyword_id"),
											job.getInt("object_keyword_id"));
									assignUncompletedWorks.add(assignWork);
									List<OcWorktime> childListGroup = new ArrayList<OcWorktime>();
									String worktimeString = jobObj.getString("OcWorktime");
									JSONArray worktimeArray = new JSONArray(worktimeString);
									for(int j = 0 ; j < worktimeArray.length() ; j++){
										JSONObject worktime = worktimeArray.getJSONObject(j);
										OcWorktime completedwork = new OcWorktime(worktime.getInt("worktime_id"),
												worktime.getInt("workteam_id"),
												worktime.getInt("worksource_id"),
												worktime.getInt("user_id"),
												worktime.getInt("work_keyword_id"),
												worktime.getInt("object_keyword_id"),
												worktime.getInt("quantity"),
												worktime.getString("start_datetime"),
												worktime.getString("end_datetime"),
												worktime.getString("work_duration_minutes"),
												worktime.getInt("appraiser_id"),
												worktime.getString("appraise_datetime"),
												worktime.getString("appraisal"),
												worktime.getInt("abort_by_user_id"),
												worktime.getString("abort_datetime"),
												worktime.getString("worktime_status"));
										String userString = worktime.getString("OcUser");
										if(userString.equals("[]")) userString = "{}";
										JSONObject user = new JSONObject(userString);
										String worktimeworkkey = worktime.getString("OcWorkteamWorkKeyword");
										if(worktimeworkkey.equals("[]")) worktimeworkkey = "{}";
										JSONObject worktimework = new JSONObject(worktimeworkkey);
										String worktimeobjectkey = worktime.getString("OcWorkteamObjectKeyword");
										if(worktimeobjectkey.equals("[]"))worktimeobjectkey = "{}";
										JSONObject worktimeobject = new JSONObject(worktimeobjectkey);
										String appraiserString = worktime.getString("Appraiser");
										if(appraiserString.equals("[]")) appraiserString = "{}";
										JSONObject appraiser = new JSONObject(appraiserString);
										String abortUserString = worktime.getString("AbortUser");
										if(abortUserString.equals("[]")) abortUserString = "{}";
										JSONObject abortUser = new JSONObject(abortUserString);
										completedwork.workKeyword = worktimework.getString("work_name");
										completedwork.objectKeyword = worktimeobject.getString("object_name");
										completedwork.userName = user.getString("username");
										if(appraiserString.equals("{}")){} else {
										completedwork.appraiserName = appraiser.getString("username");}
										if(abortUserString.equals("{}")){}else{
											completedwork.abortUserName = abortUser.getString("username");}
										childListGroup.add(completedwork);
										childListGroup.add(completedwork);
									}
									incompletedAssignSubHistory.put(assignWork,childListGroup);
								}
								incompletedAssignAdapter = new AssignHistoryExpandableAdapter(getActivity(),assignUncompletedWorks, incompletedAssignSubHistory);
								assignUncompletedWorkList.setAdapter(incompletedAssignAdapter);

								break;
							case 400:
								break;
							default:
								break;
						}
					}catch (JSONException e){
						e.printStackTrace();
					}
					kpHUD.dismiss();
				}
			});
	}

	public void getTeamMembers(){
		kpHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("workteam_id", currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorkteamMembers/mobile_getonline_members";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(getActivity(),"connection error" , Toast.LENGTH_SHORT).show();
				kpHUD.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				teamMembers.clear();
				try {
					JSONObject responseOBJ = new JSONObject(responseString);
					switch (responseOBJ.getInt("status")){
						case 100:
							String data = responseOBJ.getString("data");
							JSONArray arrayData = new JSONArray(data);
							for(int i = 0 ; i < arrayData.length() ; i++){
								JSONObject jobObj = arrayData.getJSONObject(i);
								String jobString = jobObj.getString("OcWorkteamMember");
								JSONObject job = new JSONObject(jobString);
								String userString = jobObj.getString("OcUser");
								JSONObject userObj = new JSONObject(userString);
								String teamString = jobObj.getString("OcWorkteam");
								JSONObject teamObj = new JSONObject(teamString);
								teamMembers.add(new TeamMember(userObj.getString("username"),userObj.getString("jabber_id"),job.getInt("user_id"),
										teamObj.getInt("workteam_id"),teamObj.getString("workteam_jabber_id"),teamObj.getString("workteam_name"),teamObj.getInt("creator_id"),teamObj.getInt("workteam_leader_id")));
							}
							adapter = new MemberAdapter(getActivity(),teamMembers);
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									assignMemberListview.setAdapter(adapter);
									hideTagList(5);
								}
							});
							break;
						case 400:
							break;
						default:
							break;
					}
				}catch (JSONException e){
					e.printStackTrace();
				}
				kpHUD.dismiss();
			}
		});
	}
	/**
	 * initialize all of keyword list from database and response data.
	 */
	public void formatListView(){
		actions.clear();

		if(user_id == currentTeamCreator_id||user_group_id == 1) {
//			actions.add("Accept");
			actions.add(getString(R.string.command_start));
			actions.add(getString(R.string.command_abort));
			actions.add(getString(R.string.command_finish));
			actions.add(getString(R.string.command_offer));
			actions.add(getString(R.string.command_assign));
			actions.add(getString(R.string.command_appraise));
		}else{
//			actions.add("Accept");
			actions.add(getString(R.string.command_start));
			actions.add(getString(R.string.command_abort));
			actions.add(getString(R.string.command_finish));
		}
		actionAdapter = new CommandAdapter(getActivity(),actions);
		actionAdapter.notifyDataSetChanged();
		mActionListView.setAdapter(actionAdapter);

		/****how to treat work and job****/
		workAdapter = new CommandAdapter(getActivity(),works);
		mWorkListView.setAdapter(workAdapter);
		workAdapter.notifyDataSetChanged();

		objectAdapter = new CommandAdapter(getActivity(), objects);
		mObjectListView.setAdapter(objectAdapter);
		objectAdapter.notifyDataSetChanged();
		/*********************************/
		quantities.clear();
		quantities.add(getString(R.string.command_quantity)+ " 1");
		quantities.add(getString(R.string.command_other_quantity));
		quantityAdapter = new CommandAdapter(getActivity(), quantities);
		quantityAdapter.notifyDataSetChanged();
		mQuantityListView.setAdapter(quantityAdapter);
		addList.clear();
		addList.add(getString(R.string.command_new_work));
		addList.add(getString(R.string.command_new_object));
		addListAdapter = new CommandAdapter(getActivity(), addList);
		mAddJobListView.setAdapter(addListAdapter);
	}

	/**
	 * each case list view attr setting.
	 * @param i
	 */
    public void hideTagList(int i){
		assignedWorkLayout.setVisibility(View.GONE);
		switch (i){
			case 0:// for action listview;
				commandLayout.setVisibility(View.VISIBLE);
				mAddJobListView.setVisibility(View.GONE);
				mActionListView.setVisibility(View.VISIBLE);
				mWorkListView.setVisibility(View.GONE);
				assignedWorkListView.setVisibility(View.GONE);
				assignMemberListview.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.GONE);
				mQuantityListView.setVisibility(View.GONE);
				addJobLayout.setVisibility(View.VISIBLE);
				backButton.setVisibility(View.GONE);
				mEditMessage.setEnabled(false);
				mSendButton.setEnabled(false);
				refreshImage.setVisibility(View.VISIBLE);
				/***User Role dependence***/
				if(user_id == currentTeamCreator_id||user_group_id == 1){
					addJobLayout.setVisibility(View.VISIBLE);
				}else{
					addJobLayout.setVisibility(View.GONE);
				}
				break;
			case 1://worklistview
				assignedWorkListView.setVisibility(View.GONE);
				if(status != null) {
					if (status.equals("Start")) {
						assignedWorkListView.setVisibility(View.VISIBLE);
						assignedWorkLayout.setVisibility(View.VISIBLE);
					} else {
						assignedWorkListView.setVisibility(View.GONE);
						assignedWorkLayout.setVisibility(View.GONE);
					}
				}
				commandLayout.setVisibility(View.VISIBLE);
				mAddJobListView.setVisibility(View.GONE);
				mActionListView.setVisibility(View.GONE);
				mWorkListView.setVisibility(View.VISIBLE);
				assignMemberListview.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.GONE);
				mQuantityListView.setVisibility(View.GONE);
				backButton.setVisibility(View.VISIBLE);
				mEditMessage.setEnabled(false);
				mSendButton.setEnabled(false);
				addJobLayout.setVisibility(View.GONE);
				refreshImage.setVisibility(View.GONE);
				break;
			case 2://object list view
				commandLayout.setVisibility(View.VISIBLE);
				mAddJobListView.setVisibility(View.GONE);
				mActionListView.setVisibility(View.GONE);
				assignedWorkListView.setVisibility(View.GONE);
				assignMemberListview.setVisibility(View.GONE);
				mWorkListView.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.VISIBLE);
				mQuantityListView.setVisibility(View.GONE);
				backButton.setVisibility(View.VISIBLE);
				mEditMessage.setEnabled(false);
				mSendButton.setEnabled(false);
				addJobLayout.setVisibility(View.GONE);
				refreshImage.setVisibility(View.GONE);
				break;
			case 3://quantity listview
				commandLayout.setVisibility(View.VISIBLE);
				mAddJobListView.setVisibility(View.GONE);
				mActionListView.setVisibility(View.GONE);
				assignedWorkListView.setVisibility(View.GONE);
				assignMemberListview.setVisibility(View.GONE);
				mWorkListView.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.GONE);
				mQuantityListView.setVisibility(View.VISIBLE);
				backButton.setVisibility(View.VISIBLE);
				mEditMessage.setEnabled(false);
				addJobLayout.setVisibility(View.GONE);
				refreshImage.setVisibility(View.GONE);
				break;
			case  4://for addjob listview
				commandLayout.setVisibility(View.VISIBLE);
				mAddJobListView.setVisibility(View.VISIBLE);
				mActionListView.setVisibility(View.GONE);
				assignedWorkListView.setVisibility(View.GONE);
				assignMemberListview.setVisibility(View.GONE);
				mWorkListView.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.GONE);
				mQuantityListView.setVisibility(View.GONE);
				backButton.setVisibility(View.VISIBLE);
				addJobLayout.setVisibility(View.GONE);
				mSendButton.setEnabled(false);
				mEditMessage.setEnabled(false);
				refreshImage.setVisibility(View.GONE);
				break;
			case  5://for member list view
				commandLayout.setVisibility(View.VISIBLE);
				mAddJobListView.setVisibility(View.GONE);
				mActionListView.setVisibility(View.GONE);
				assignedWorkListView.setVisibility(View.GONE);
				assignMemberListview.setVisibility(View.VISIBLE);
				mWorkListView.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.GONE);
				mQuantityListView.setVisibility(View.GONE);
				backButton.setVisibility(View.VISIBLE);
				addJobLayout.setVisibility(View.GONE);
				mSendButton.setEnabled(false);
				mEditMessage.setEnabled(false);
				refreshImage.setVisibility(View.GONE);
				break;
			default:
				commandLayout.setVisibility(View.GONE);
				mAddJobListView.setVisibility(View.GONE);
				mActionListView.setVisibility(View.GONE);
				assignedWorkListView.setVisibility(View.GONE);
				assignMemberListview.setVisibility(View.GONE);
				mWorkListView.setVisibility(View.GONE);
				mObjectListView.setVisibility(View.GONE);
				mQuantityListView.setVisibility(View.GONE);
				mEditMessage.setEnabled(true);
				backButton.setVisibility(View.GONE);
				topBarTextView.setVisibility(View.GONE);
				addCommandButton.setVisibility(View.GONE);
				addJobLayout.setVisibility(View.GONE);
				messagesView.setAlpha(1.0f);
				mGreenbutton.setImageResource(R.drawable.ic_add_white_24dp);
				refreshImage.setVisibility(View.GONE);
				assignHistoryLayout.setVisibility(View.GONE);
				report_layout.setVisibility(View.GONE);
				productivityLayout.setVisibility(View.GONE);
				break;
		}

	}
/*********************************end**********************************/

	@Override
	public void onStop() {
		super.onStop();
		if (this.conversation != null) {
			final String msg = mEditMessage.getText().toString();
			this.conversation.setNextMessage(msg);
			updateChatState(this.conversation, msg);
		}
		timer(false);
	}

	private void updateChatState(final Conversation conversation, final String msg) {
		ChatState state = msg.length() == 0 ? Config.DEFAULT_CHATSTATE : ChatState.PAUSED;
		Account.State status = conversation.getAccount().getStatus();
		if (status == Account.State.ONLINE && conversation.setOutgoingChatState(state)) {
			activity.xmppConnectionService.sendChatState(conversation);
		}
	}

	public void reInit(Conversation conversation) {
		if (conversation == null) {
			return;
		}
		/*******anton's code**********************/
		initAllValiables();
		initAllViews();
		works.clear();
		workList.clear();
		objects.clear();
		objectList.clear();
		currentcommands.clear();
		isCommand = false;
		if(conversation.getMode() == Conversation.MODE_MULTI) {
			if (conversation.getJid().toString().contains("/")) {
				teamJid = conversation.getJid().toString().split("/")[0];
			} else {
				teamJid = conversation.getJid().toString();
			}
//			joinTeam(teamJid);
			mGreenbutton.setVisibility(View.VISIBLE);
			getTeamJobs(teamJid);
			getMyAccount();
			hideTagList(10);
			bottomBarLayout.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			lp.addRule(RelativeLayout.ABOVE,R.id.bottom_bar_layout);
			editMessageLayout.setLayoutParams(lp);
			timer(true);
			updateExpiredData();

		}else{
			mGreenbutton.setVisibility(View.GONE);
			hideTagList(10);
			bottomBarLayout.setVisibility(View.GONE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			editMessageLayout.setLayoutParams(lp);
			offer_layout.setVisibility(View.GONE);
			timer(false);
		}
		/*****************************************/
		this.activity = (ConversationActivity) getActivity();
		setupIme();
		if (this.conversation != null) {
			final String msg = mEditMessage.getText().toString();
			this.conversation.setNextMessage(msg);
			if (this.conversation != conversation) {
				updateChatState(this.conversation, msg);
			}
			this.conversation.trim();
		}

		this.keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
		this.conversation = conversation;
		boolean canWrite = this.conversation.getMode() == Conversation.MODE_SINGLE || this.conversation.getMucOptions().participating();
		this.mEditMessage.setEnabled(canWrite);
		this.mSendButton.setEnabled(canWrite);
		this.mEditMessage.setKeyboardListener(null);
		this.mEditMessage.setText("");
		this.mEditMessage.append(this.conversation.getNextMessage());
		this.mEditMessage.setKeyboardListener(this);
		messageListAdapter.updatePreferences();
		this.messagesView.setAdapter(messageListAdapter);
		updateMessages();
		this.messagesLoaded = true;
		int size = this.messageList.size();
		if (size > 0) {
			messagesView.setSelection(size - 1);
		}
	}

	private OnClickListener mEnableAccountListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final Account account = conversation == null ? null : conversation.getAccount();
			if (account != null) {
				account.setOption(Account.OPTION_DISABLED, false);
				activity.xmppConnectionService.updateAccount(account);
			}
		}
	};

	private OnClickListener mUnblockClickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			v.post(new Runnable() {
				@Override
				public void run() {
					v.setVisibility(View.GONE);
				}
			});
			if (conversation.isDomainBlocked()) {
				BlockContactDialog.show(activity, activity.xmppConnectionService, conversation);
			} else {
				activity.unblockConversation(conversation);
			}
		}
	};

	private OnClickListener mAddBackClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Contact contact = conversation == null ? null : conversation.getContact();
			if (contact != null) {
				activity.xmppConnectionService.createContact(contact);
				activity.switchToContactDetails(contact);
			}
		}
	};

	private OnClickListener mAnswerSmpClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(activity, VerifyOTRActivity.class);
			intent.setAction(VerifyOTRActivity.ACTION_VERIFY_CONTACT);
			intent.putExtra("contact", conversation.getContact().getJid().toBareJid().toString());
			intent.putExtra(VerifyOTRActivity.EXTRA_ACCOUNT, conversation.getAccount().getJid().toBareJid().toString());
			intent.putExtra("mode", VerifyOTRActivity.MODE_ANSWER_QUESTION);
			startActivity(intent);
		}
	};

	private void updateSnackBar(final Conversation conversation) {
		final Account account = conversation.getAccount();
		final Contact contact = conversation.getContact();
		final int mode = conversation.getMode();
		if (account.getStatus() == Account.State.DISABLED) {
			showSnackbar(R.string.this_account_is_disabled, R.string.enable, this.mEnableAccountListener);
		} else if (conversation.isBlocked()) {
			showSnackbar(R.string.contact_blocked, R.string.unblock, this.mUnblockClickListener);
		} else if (!contact.showInRoster() && contact.getOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST)) {
			showSnackbar(R.string.contact_added_you, R.string.add_back, this.mAddBackClickListener);
		} else if (mode == Conversation.MODE_MULTI
				&& !conversation.getMucOptions().online()
				&& account.getStatus() == Account.State.ONLINE) {
			switch (conversation.getMucOptions().getError()) {
				case NICK_IN_USE:
					showSnackbar(R.string.nick_in_use, R.string.edit, clickToMuc);
					break;
				case NO_RESPONSE:
					showSnackbar(R.string.joining_conference, 0, null);
					break;
				case PASSWORD_REQUIRED:
					showSnackbar(R.string.conference_requires_password, R.string.enter_password, enterPassword);
					break;
				case BANNED:
					showSnackbar(R.string.conference_banned, R.string.leave, leaveMuc);
					break;
				case MEMBERS_ONLY:
					showSnackbar(R.string.conference_members_only, R.string.leave, leaveMuc);
					break;
				case KICKED:
					showSnackbar(R.string.conference_kicked, R.string.join, joinMuc);
					break;
				case UNKNOWN:
					showSnackbar(R.string.conference_unknown_error, R.string.join, joinMuc);
					break;
				case SHUTDOWN:
					showSnackbar(R.string.conference_shutdown, R.string.join, joinMuc);
					break;
				default:
					break;
			}
		} else if (keychainUnlock == KEYCHAIN_UNLOCK_REQUIRED) {
			showSnackbar(R.string.openpgp_messages_found, R.string.decrypt, clickToDecryptListener);
		} else if (mode == Conversation.MODE_SINGLE
				&& conversation.smpRequested()) {
			showSnackbar(R.string.smp_requested, R.string.verify, this.mAnswerSmpClickListener);
		} else if (mode == Conversation.MODE_SINGLE
				&& conversation.hasValidOtrSession()
				&& (conversation.getOtrSession().getSessionStatus() == SessionStatus.ENCRYPTED)
				&& (!conversation.isOtrFingerprintVerified())) {
			showSnackbar(R.string.unknown_otr_fingerprint, R.string.verify, clickToVerify);
		} else {
			hideSnackbar();
		}
	}

	public void updateMessages() {
		synchronized (this.messageList) {
			if (getView() == null) {
				return;
			}
			final ConversationActivity activity = (ConversationActivity) getActivity();
			if (this.conversation != null) {
				conversation.populateWithMessages(ConversationFragment.this.messageList);
				updatePgpMessages();
				updateSnackBar(conversation);
				updateStatusMessages();
				this.messageListAdapter.notifyDataSetChanged();
				updateChatMsgHint();
				if (!activity.isConversationsOverviewVisable() || !activity.isConversationsOverviewHideable()) {
					activity.sendReadMarkerIfNecessary(conversation);
				}
				this.updateSendButton();
			}
		}
	}

	public void updatePgpMessages() {
		if (keychainUnlock != KEYCHAIN_UNLOCK_PENDING) {
			if (getLastPgpDecryptableMessage() != null
					&& !conversation.getAccount().getPgpDecryptionService().isRunning()) {
				keychainUnlock = KEYCHAIN_UNLOCK_REQUIRED;
			} else {
				keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
			}
		}
	}

	@Nullable
	private Message getLastPgpDecryptableMessage() {
		for (final Message message : this.messageList) {
			if (message.getEncryption() == Message.ENCRYPTION_PGP
					&& (message.getStatus() == Message.STATUS_RECEIVED || message.getStatus() >= Message.STATUS_SEND)
					&& message.getTransferable() == null) {
				return message;
			}
		}
		return null;
	}

	private void messageSent() {
		int size = this.messageList.size();
		messagesView.setSelection(size - 1);
		mEditMessage.setText("");
		updateChatMsgHint();
	}

	public void setFocusOnInputField() {
		mEditMessage.requestFocus();
	}


	enum SendButtonAction {TEXT, TAKE_PHOTO, SEND_LOCATION, RECORD_VOICE, CANCEL, CHOOSE_PICTURE}

	private int getSendButtonImageResource(SendButtonAction action, Presence.Status status) {
		switch (action) {
			case TEXT:
				switch (status) {
					case CHAT:
					case ONLINE:
						return R.drawable.ic_send_text_online;
					case AWAY:
						return R.drawable.ic_send_text_away;
					case XA:
					case DND:
						return R.drawable.ic_send_text_dnd;
					default:
						return R.drawable.ic_send_text_offline;
				}
			case TAKE_PHOTO:
				switch (status) {
					case CHAT:
					case ONLINE:
						return R.drawable.ic_send_photo_online;
					case AWAY:
						return R.drawable.ic_send_photo_away;
					case XA:
					case DND:
						return R.drawable.ic_send_photo_dnd;
					default:
						return R.drawable.ic_send_photo_offline;
				}
			case RECORD_VOICE:
				switch (status) {
					case CHAT:
					case ONLINE:
						return R.drawable.ic_send_voice_online;
					case AWAY:
						return R.drawable.ic_send_voice_away;
					case XA:
					case DND:
						return R.drawable.ic_send_voice_dnd;
					default:
						return R.drawable.ic_send_voice_offline;
				}
			case SEND_LOCATION:
				switch (status) {
					case CHAT:
					case ONLINE:
						return R.drawable.ic_send_location_online;
					case AWAY:
						return R.drawable.ic_send_location_away;
					case XA:
					case DND:
						return R.drawable.ic_send_location_dnd;
					default:
						return R.drawable.ic_send_location_offline;
				}
			case CANCEL:
				switch (status) {
					case CHAT:
					case ONLINE:
						return R.drawable.ic_send_cancel_online;
					case AWAY:
						return R.drawable.ic_send_cancel_away;
					case XA:
					case DND:
						return R.drawable.ic_send_cancel_dnd;
					default:
						return R.drawable.ic_send_cancel_offline;
				}
			case CHOOSE_PICTURE:
				switch (status) {
					case CHAT:
					case ONLINE:
						return R.drawable.ic_send_picture_online;
					case AWAY:
						return R.drawable.ic_send_picture_away;
					case XA:
					case DND:
						return R.drawable.ic_send_picture_dnd;
					default:
						return R.drawable.ic_send_picture_offline;
				}
		}
		return R.drawable.ic_send_text_offline;
	}

	public void updateSendButton() {
		final Conversation c = this.conversation;
		final SendButtonAction action;
		final Presence.Status status;
		final String text = this.mEditMessage == null ? "" : this.mEditMessage.getText().toString();
		final boolean empty = text.length() == 0;
		final boolean conference = c.getMode() == Conversation.MODE_MULTI;
		if (c.getCorrectingMessage() != null && (empty || text.equals(c.getCorrectingMessage().getBody()))) {
			action = SendButtonAction.CANCEL;
		} else if (conference && !c.getAccount().httpUploadAvailable()) {
			if (empty && c.getNextCounterpart() != null) {
				action = SendButtonAction.CANCEL;
			} else {
				action = SendButtonAction.TEXT;
			}
		} else {
			if (empty) {
				if (conference && c.getNextCounterpart() != null) {
					action = SendButtonAction.CANCEL;
				} else {
					String setting = activity.getPreferences().getString("quick_action", "recent");
					if (!setting.equals("none") && UIHelper.receivedLocationQuestion(conversation.getLatestMessage())) {
						setting = "location";
					} else if (setting.equals("recent")) {
						setting = activity.getPreferences().getString("recently_used_quick_action", "text");
					}
					switch (setting) {
						case "photo":
							action = SendButtonAction.TAKE_PHOTO;
							break;
						case "location":
							action = SendButtonAction.SEND_LOCATION;
							break;
						case "voice":
							action = SendButtonAction.RECORD_VOICE;
							break;
						case "picture":
							action = SendButtonAction.CHOOSE_PICTURE;
							break;
						default:
							action = SendButtonAction.TEXT;
							break;
					}
				}
			} else {
				action = SendButtonAction.TEXT;
			}
		}
		if (activity.useSendButtonToIndicateStatus() && c != null
				&& c.getAccount().getStatus() == Account.State.ONLINE) {
			if (c.getMode() == Conversation.MODE_SINGLE) {
				status = c.getContact().getMostAvailableStatus();
			} else {
				status = c.getMucOptions().online() ? Presence.Status.ONLINE : Presence.Status.OFFLINE;
			}
		} else {
			status = Presence.Status.OFFLINE;
		}
		this.mSendButton.setTag(action);
		this.mSendButton.setImageResource(getSendButtonImageResource(action, status));
	}

	protected void updateStatusMessages() {
		synchronized (this.messageList) {
			if (showLoadMoreMessages(conversation)) {
				this.messageList.add(0, Message.createLoadMoreMessage(conversation));
			}
			if (conversation.getMode() == Conversation.MODE_SINGLE) {
				ChatState state = conversation.getIncomingChatState();
				if (state == ChatState.COMPOSING) {
					this.messageList.add(Message.createStatusMessage(conversation, getString(R.string.contact_is_typing, conversation.getName())));
				} else if (state == ChatState.PAUSED) {
					this.messageList.add(Message.createStatusMessage(conversation, getString(R.string.contact_has_stopped_typing, conversation.getName())));
				} else {
					for (int i = this.messageList.size() - 1; i >= 0; --i) {
						if (this.messageList.get(i).getStatus() == Message.STATUS_RECEIVED) {
							return;
						} else {
							if (this.messageList.get(i).getStatus() == Message.STATUS_SEND_DISPLAYED) {
								this.messageList.add(i + 1,
										Message.createStatusMessage(conversation, getString(R.string.contact_has_read_up_to_this_point, conversation.getName())));
								return;
							}
						}
					}
				}
			}
		}
	}

	private boolean showLoadMoreMessages(final Conversation c) {
		final boolean mam = hasMamSupport(c);
		final MessageArchiveService service = activity.xmppConnectionService.getMessageArchiveService();
		return mam && (c.getLastClearHistory() != 0  || (c.countMessages() == 0 && c.hasMessagesLeftOnServer()  && !service.queryInProgress(c)));
	}

	private boolean hasMamSupport(final Conversation c) {
		if (c.getMode() == Conversation.MODE_SINGLE) {
			final XmppConnection connection = c.getAccount().getXmppConnection();
			return connection != null && connection.getFeatures().mam();
		} else {
			return c.getMucOptions().mamSupport();
		}
	}

	protected void showSnackbar(final int message, final int action, final OnClickListener clickListener) {
		snackbar.setVisibility(View.VISIBLE);
		snackbar.setOnClickListener(null);
		snackbarMessage.setText(message);
		snackbarMessage.setOnClickListener(null);
		snackbarAction.setVisibility(clickListener == null ? View.GONE : View.VISIBLE);
		if (action != 0) {
			snackbarAction.setText(action);
		}
		snackbarAction.setOnClickListener(clickListener);
	}

	protected void hideSnackbar() {
		snackbar.setVisibility(View.GONE);
	}

	protected void sendPlainTextMessage(Message message) {
		ConversationActivity activity = (ConversationActivity) getActivity();
		activity.xmppConnectionService.sendMessage(message);
		messageSent();
	}

	protected void sendPgpMessage(final Message message) {
		final ConversationActivity activity = (ConversationActivity) getActivity();
		final XmppConnectionService xmppService = activity.xmppConnectionService;
		final Contact contact = message.getConversation().getContact();
		if (!activity.hasPgp()) {
			activity.showInstallPgpDialog();
			return;
		}
		if (conversation.getAccount().getPgpSignature() == null) {
			activity.announcePgp(conversation.getAccount(), conversation);
			return;
		}
		if (conversation.getMode() == Conversation.MODE_SINGLE) {
			if (contact.getPgpKeyId() != 0) {
				xmppService.getPgpEngine().hasKey(contact,
						new UiCallback<Contact>() {

							@Override
							public void userInputRequried(PendingIntent pi,
														  Contact contact) {
								activity.runIntent(
										pi,
										ConversationActivity.REQUEST_ENCRYPT_MESSAGE);
							}

							@Override
							public void success(Contact contact) {
								messageSent();
								activity.encryptTextMessage(message);
							}

							@Override
							public void error(int error, Contact contact) {
								System.out.println();
							}
						});

			} else {
				showNoPGPKeyDialog(false,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								conversation
										.setNextEncryption(Message.ENCRYPTION_NONE);
								xmppService.databaseBackend
										.updateConversation(conversation);
								message.setEncryption(Message.ENCRYPTION_NONE);
								xmppService.sendMessage(message);
								messageSent();
							}
						});
			}
		} else {
			if (conversation.getMucOptions().pgpKeysInUse()) {
				if (!conversation.getMucOptions().everybodyHasKeys()) {
					Toast warning = Toast
							.makeText(getActivity(),
									R.string.missing_public_keys,
									Toast.LENGTH_LONG);
					warning.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					warning.show();
				}
				activity.encryptTextMessage(message);
				messageSent();
			} else {
				showNoPGPKeyDialog(true,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								conversation
										.setNextEncryption(Message.ENCRYPTION_NONE);
								message.setEncryption(Message.ENCRYPTION_NONE);
								xmppService.databaseBackend
										.updateConversation(conversation);
								xmppService.sendMessage(message);
								messageSent();
							}
						});
			}
		}
	}

	public void showNoPGPKeyDialog(boolean plural,
								   DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIconAttribute(android.R.attr.alertDialogIcon);
		if (plural) {
			builder.setTitle(getString(R.string.no_pgp_keys));
			builder.setMessage(getText(R.string.contacts_have_no_pgp_keys));
		} else {
			builder.setTitle(getString(R.string.no_pgp_key));
			builder.setMessage(getText(R.string.contact_has_no_pgp_key));
		}
		builder.setNegativeButton(getString(R.string.cancel), null);
		builder.setPositiveButton(getString(R.string.send_unencrypted),
				listener);
		builder.create().show();
	}

	protected void sendAxolotlMessage(final Message message) {
		final ConversationActivity activity = (ConversationActivity) getActivity();
		final XmppConnectionService xmppService = activity.xmppConnectionService;
		xmppService.sendMessage(message);
		messageSent();
	}

	protected void sendOtrMessage(final Message message) {
		final ConversationActivity activity = (ConversationActivity) getActivity();
		final XmppConnectionService xmppService = activity.xmppConnectionService;
		activity.selectPresence(message.getConversation(),
				new XmppActivity.OnPresenceSelected() {

					@Override
					public void onPresenceSelected() {
						message.setCounterpart(conversation.getNextCounterpart());
						xmppService.sendMessage(message);
						messageSent();
					}
				});
	}

	public void appendText(String text) {
		if (text == null) {
			return;
		}
		String previous = this.mEditMessage.getText().toString();
		if (previous.length() != 0 && !previous.endsWith(" ")) {
			text = " " + text;
		}
		this.mEditMessage.append(text);
	}

	@Override
	public boolean onEnterPressed() {
		if (activity.enterIsSend()) {
			sendMessage();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onTypingStarted() {
		Account.State status = conversation.getAccount().getStatus();
		if (status == Account.State.ONLINE && conversation.setOutgoingChatState(ChatState.COMPOSING)) {
			activity.xmppConnectionService.sendChatState(conversation);
		}
		activity.hideConversationsOverview();
		updateSendButton();
	}

	@Override
	public void onTypingStopped() {
		Account.State status = conversation.getAccount().getStatus();
		if (status == Account.State.ONLINE && conversation.setOutgoingChatState(ChatState.PAUSED)) {
			activity.xmppConnectionService.sendChatState(conversation);
		}
	}

	@Override
	public void onTextDeleted() {
		Account.State status = conversation.getAccount().getStatus();
		if (status == Account.State.ONLINE && conversation.setOutgoingChatState(Config.DEFAULT_CHATSTATE)) {
			activity.xmppConnectionService.sendChatState(conversation);
		}
		updateSendButton();
	}

	@Override
	public void onTextChanged() {
		if (conversation != null && conversation.getCorrectingMessage() != null) {
			updateSendButton();
		}
	}

	private int completionIndex = 0;
	private int lastCompletionLength = 0;
	private String incomplete;
	private int lastCompletionCursor;
	private boolean firstWord = false;

	@Override
	public boolean onTabPressed(boolean repeated) {
		if (conversation == null || conversation.getMode() == Conversation.MODE_SINGLE) {
			return false;
		}
		if (repeated) {
			completionIndex++;
		} else {
			lastCompletionLength = 0;
			completionIndex = 0;
			final String content = mEditMessage.getText().toString();
			lastCompletionCursor = mEditMessage.getSelectionEnd();
			int start = lastCompletionCursor > 0 ? content.lastIndexOf(" ",lastCompletionCursor-1) + 1 : 0;
			firstWord = start == 0;
			incomplete = content.substring(start,lastCompletionCursor);
		}
		List<String> completions = new ArrayList<>();
		for(MucOptions.User user : conversation.getMucOptions().getUsers()) {
			if (user.getName().startsWith(incomplete)) {
				completions.add(user.getName()+(firstWord ? ": " : " "));
			}
		}
		Collections.sort(completions);
		if (completions.size() > completionIndex) {
			String completion = completions.get(completionIndex).substring(incomplete.length());
			mEditMessage.getEditableText().delete(lastCompletionCursor,lastCompletionCursor + lastCompletionLength);
			mEditMessage.getEditableText().insert(lastCompletionCursor, completion);
			lastCompletionLength = completion.length();
		} else {
			completionIndex = -1;
			mEditMessage.getEditableText().delete(lastCompletionCursor,lastCompletionCursor + lastCompletionLength);
			lastCompletionLength = 0;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
	                                final Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ConversationActivity.REQUEST_DECRYPT_PGP) {
				activity.getSelectedConversation().getAccount().getPgpDecryptionService().onKeychainUnlocked();
				keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
				updatePgpMessages();
			} else if (requestCode == ConversationActivity.REQUEST_TRUST_KEYS_TEXT) {
				final String body = mEditMessage.getText().toString();
				Message message = new Message(conversation, body, conversation.getNextEncryption());
				sendAxolotlMessage(message);
			} else if (requestCode == ConversationActivity.REQUEST_TRUST_KEYS_MENU) {
				int choice = data.getIntExtra("choice", ConversationActivity.ATTACHMENT_CHOICE_INVALID);
				activity.selectPresenceToAttachFile(choice, conversation.getNextEncryption());
			}
		} else {
			if (requestCode == ConversationActivity.REQUEST_DECRYPT_PGP) {
				keychainUnlock = KEYCHAIN_UNLOCK_NOT_REQUIRED;
				updatePgpMessages();
			}
		}
	}

	/**
	 * for keyword list Adapter
	 */
	class CommandAdapter extends ArrayAdapter {

        Activity context;
		ArrayList<String> commandlist;


        @SuppressWarnings("unchecked")
		CommandAdapter(Activity context,ArrayList<String> arraymodel) {
            super(context, R.layout.command_listview_row, arraymodel);
            this.context = context;
			this.commandlist = arraymodel;
        }

		@Override
		public int getCount() {
			return commandlist.size();
		}

		/**
		 * get view cell for command table
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.command_listview_row, null);

            }
			TextView command = (TextView) row.findViewById(R.id.command_list_text);
			if(commandlist.get(position).trim().equals("Works")){
				command.setText(commandlist.get(position).toString());
				command.setBackgroundResource(R.drawable.popup_background_black);
				command.setTextColor(row.getResources().getColor(R.color.white));
				ImageView image = (ImageView)row.findViewById(R.id.command_list_imageview);
				image.setVisibility(View.INVISIBLE);
				row.setEnabled(false);
				row.setOnClickListener(null);
//				row.setClickable(false);
				return row;
			} else {
				command.setText(commandlist.get(position).toString());
				ImageView image = (ImageView)row.findViewById(R.id.command_list_imageview);
				image.setVisibility(View.VISIBLE);
				command.setBackgroundResource(R.drawable.popup_background);
				command.setTextColor(row.getResources().getColor(R.color.black87));
				row.setEnabled(true);
//				row.setClickable(true);
				return row;
			}
   		 }
	}
	class OfferAdapter extends ArrayAdapter {

		Activity context;
		ArrayList<OcWorkroot> offers;

		@SuppressWarnings("unchecked")
		OfferAdapter(Activity context,ArrayList<OcWorkroot> arraymodel) {
			super(context, R.layout.offer_listview_item, arraymodel);
			this.context = context;
			this.offers = arraymodel;
		}

		@Override
		public int getCount() {
			return offers.size();
		}

		/**
		 * get view cell for command table
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.offer_listview_item, null);

			}
			row.setVisibility(View.VISIBLE);
			TextView work_object_quantity = (TextView) row.findViewById(R.id.work_offer_work_object);
			work_object_quantity.setText(offers.get(position).workKeyword+" "+offers.get(position).objectKeyword+" "+offers.get(position).workrootRemainQuantity);

			TextView expire_time = (TextView) row.findViewById(R.id.work_offer_expire_time);
			if(offers.get(position).getExpiryTime() == 1000){
				expire_time.setText("Expired");
				row.setVisibility(View.INVISIBLE);
				return row;
			} else {
				row.setVisibility(View.VISIBLE);
				expire_time.setText(offers.get(position).getExpiryTime() + " minutes left");
			}
			TextView nameTextView = (TextView)row.findViewById(R.id.accept_user_name);
			if(offers.get(position).userName.contains("_"))
				nameTextView.setText(offers.get(position).userName.split("_")[0]);
			/**
			 * avatar image set part;
			 */
			ImageView avatar = (ImageView)row.findViewById(R.id.offer_avatar_imageview);
			ConversationActivity activity ;
			activity = (ConversationActivity) getActivity();
			avatar.setImageBitmap(activity.avatarService().get(conversation.getContact(),activity.getPixel(32)));
			return row;

		}
	}

	class AcceptWorkAdapter extends ArrayAdapter {

		Activity context;
		ArrayList<AcceptWork> accepts;

		@SuppressWarnings("unchecked")
		AcceptWorkAdapter(Activity context,ArrayList<AcceptWork> arraymodel) {
			super(context, R.layout.offer_listview_item, arraymodel);
			this.context = context;
			this.accepts = arraymodel;
		}

		@Override
		public int getCount() {
			return accepts.size();
		}

		/**
		 * get view cell for command table
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				row = inflater.inflate(R.layout.offer_listview_item, null);
			}
			TextView work_object_quantity = (TextView) row.findViewById(R.id.work_offer_work_object);
			work_object_quantity.setText(accepts.get(position).workAccept);

			TextView accept_time = (TextView) row.findViewById(R.id.work_offer_expire_time);
			accept_time.setText(accepts.get(position).workaccept_time.toString());

			TextView username = (TextView)row.findViewById(R.id.accept_user_name);
			username.setText(accepts.get(position).name);
			ImageView image = (ImageView)row.findViewById(R.id.offer_avatar_imageview);
			image.setVisibility(View.GONE);
			return row;

		}
	}
	public void showBadge(int badgeNumber, int  assignbadgeNumber){
		//if badge is true then offerBadge will show and false assignBadge will be shown

			if(badgeNumber <= 0){
				badgeLayout.setVisibility(View.GONE);
			} else {
				badgeLayout.setVisibility(View.VISIBLE);
			}

			if(assignbadgeNumber <= 0){
				assignBadgeLayout.setVisibility(View.GONE);
			} else {
				assignBadgeLayout.setVisibility(View.VISIBLE);
			}
	}
	public void updateBadge(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("team_id",currentTeamId);
		params.put("user_id" , user_id);
		String url = "http://54.169.8.76/proapp/OcWorkroots/getOffersNumber";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				try {
					JSONObject badge = new JSONObject(responseString);
					badge.getInt("offer");
					badge.getInt("assign");
					showBadge(badge.getInt("offer"), badge.getInt("assign"));
					badgeTextView.setText(String.valueOf(badge.getInt("offer")));
					assignBadgeText.setText(String.valueOf(badge.getInt("assign")));
				}catch (JSONException e){
					e.printStackTrace();
				}
			}
		});
	}
	public void timer(boolean yon) {
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				if(getActivity()!=null) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							updateBadge();
						}
					});
				}
			}
		};
		Timer timer = new Timer();
		if (!yon) {
			timer.cancel();
		} else {
			timer.scheduleAtFixedRate(doAsynchronousTask, 5000, 5000);
		}
	}
	public void timerForUpdateOffers(boolean yon){
		TimerTask Task = new TimerTask() {
			@Override
			public void run() {
				if(getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							updateExpiredData();
						}
					});
				}
			}
		};
		Timer timer = new Timer();
		if(!yon){
			timer.cancel();
		} else {
			timer.scheduleAtFixedRate(Task,5,20000);
		}
	}

	private void saveKeywordToBackend(){
		/**
		 * anton's code
		 * for working report , we have to register command sentence structure in to working table.
		 */
		if(!isAccept) {
			if ((!addStatus) && isCommand) {
				if(isSelectedAssignList){
					AsyncHttpClient client = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					params.put("workassign_id", worksourceId);
					String url = "http://54.169.8.76/proapp/OcWorkassigns/mobile_started";
					client.post(url, params, new TextHttpResponseHandler() {
						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

						}

						@Override
						public void onSuccess(int statusCode, Header[] headers, String responseString) {
						}
					});
				} else {
					if (status.equals("Offer")) {
						AsyncHttpClient client = new AsyncHttpClient();
						RequestParams params = new RequestParams();
						params.put(WebClient.WORKTEAM_ID, currentTeamId);
						params.put(WebClient.USERID, user_id);
						params.put(WebClient.WORKKEYWORDID, currentWork_id);
						params.put(WebClient.OBJECTKEYWORDID, currentObject_id);
						params.put(WebClient.QUANTITY, currentQuantity);
						params.put(WebClient.STATUS, status);
						params.put("duration", OfferWork.defaultExpiryTime);
						String url = "http://54.169.8.76/proapp/OcWorkroots/mobile_add";
						client.post(url, params, new TextHttpResponseHandler() {
							@Override
							public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
							}

							@Override
							public void onSuccess(int statusCode, Header[] headers, String responseString) {

							}
						});
					} else if(status.equals("Assign")){
						AsyncHttpClient client = new AsyncHttpClient();
						RequestParams params = new RequestParams();

						params.put(WebClient.STATUS, status);
						params.put(WebClient.WORKTEAM_ID, currentTeamId);
						params.put(WebClient.USERID, user_id);
						params.put(WebClient.WORKKEYWORDID, currentWork_id);
						params.put("workkey", currentWorkKey);
						params.put(WebClient.OBJECTKEYWORDID, currentObject_id);
						params.put("objectkey", currentObjectKey);
						params.put(WebClient.QUANTITY, currentQuantity);
						params.put("assigned_user_id", assignedUserId);
						params.put("isAssign", isSelectedAssignList);
						params.put("workroot_id",0);
						String url = "http://54.169.8.76/proapp/OcWorksources/mobile_assign";
						client.post(url, params, new TextHttpResponseHandler() {
							@Override
							public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
								return;
							}

							@Override
							public void onSuccess(int statusCode, Header[] headers, String responseString) {

							}
						});
					} else if(status.equals("Start")){
						AsyncHttpClient client = new AsyncHttpClient();
						RequestParams params = new RequestParams();
						params.put("startQuantity", currentQuantity);
						params.put("workteamid", currentTeamId);
						params.put("worksourceid",0);
						params.put("userid", user_id);
						params.put("workkeyid",currentWork_id);
						params.put("objectkeyid", currentObject_id);
						params.put("remainQuantity",currentQuantity);
						String url = "http://54.169.8.76/proapp/OcWorktimes/startWork";
						client.post(url, params, new TextHttpResponseHandler() {
							@Override
							public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

							}

							@Override
							public void onSuccess(int statusCode, Header[] headers, String responseString) {

							}
						});
					} else {
					}
				}
				currentAppraisal = "";
				isSelectedAssignList = false;
			}
		}
	}

	public  void startWork(OcWorksource worksource, int quantity){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("startQuantity", quantity);
		params.put("workteamid", currentTeamId);
		params.put("worksourceid",worksource.worksourceId);
		params.put("userid", user_id);
		params.put("workkeyid",worksource.worksourceWorkKeywordId );
		params.put("objectkeyid", worksource.worksourceObjectKeywordId);
		params.put("remainQuantity",worksource.worksourceQuantityLessStarted-quantity);
		String url = "http://54.169.8.76/proapp/OcWorktimes/startWork";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

			}
		});
	}

	public  void finishWork(OcWorktime worktime, String action){
		AsyncHttpClient client  = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("worktimeid", worktime.getWorkTimeId());
		params.put("action", action);
		params.put("userid", user_id);
		params.put("appraisal", currentAppraisal);
		params.put("worksourceId",worktime.getWorkSourceId());
		params.put("finishedQuantity",worktime.getQuantity());
		String url = "http://54.169.8.76/proapp/OcWorktimes/finishWork";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
			}
		});
	}

	public void bottombarSelectedStatus(View view){
		switch (view.getId()){
			case R.id.action_button:
				view.setBackgroundResource(R.color.black54);
				break;
			case R.id.findajob_button:
				view.setBackgroundResource(R.color.black54);
				break;
			case R.id.jobcomplete_button:
				view.setBackgroundResource(R.color.black54);
				break;
			case R.id.jobsincomplete_button:
				view.setBackgroundResource(R.color.black54);
				break;
			case R.id.workteam_button:
				view.setBackgroundResource(R.color.black54);
				break;
			case R.id.userlocation_button:
				view.setBackgroundResource(R.color.black54);
				break;
			case R.id.productivity_button:
				view.setBackgroundResource(R.color.black54);
				break;
			default:
				break;
		}
	}

	public void getReportWorks(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("teamid",currentTeamId);
		kpHUD.show();
		String url = "http://54.169.8.76/proapp/OcWorktimes/reportallworks";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				reportCompleteWorks.clear();
				reportIncompleteWorks.clear();
				reportAbortWorks.clear();
				try{
					JSONObject responseObject = new JSONObject(responseString);
					switch (responseObject.getInt("status")){
						case 100:
							String dataString = responseObject.getString("data");
							JSONArray dataArray = new JSONArray(dataString);
							for(int i = 0 ; i < dataArray.length() ; i++ ){
								JSONObject data = dataArray.getJSONObject(i);
								String worktimeString = data.getString("OcWorktime");
								if(worktimeString.equals("[]"))worktimeString = "{}";
								JSONObject worktime = new JSONObject(worktimeString);
								String userString = data.getString("OcUser");
								if(userString.equals("[]"))userString = "{}";
								JSONObject user = new JSONObject(userString);
								String workkeyString = data.getString("OcWorkteamWorkKeyword");
								if(workkeyString.equals("[]"))workkeyString = "{}";
								JSONObject workkey = new JSONObject(workkeyString);
								String objectkeyString = data.getString("OcWorkteamObjectKeyword");
								if(objectkeyString.equals("[]"))objectkeyString = "{}";
								JSONObject objectkey = new JSONObject(objectkeyString);
								Report report = new Report(user.getString("username"),
										worktime.getString("start_datetime"),
										worktime.getString("end_datetime"),
										workkey.getString("work_name"),
										objectkey.getString("object_name"),
										worktime.getInt("quantity"));
								report.setAbort(worktime.getString("abort_datetime"));
								report.workerId = user.getInt("user_id");
								if(worktime.getBoolean("isFinished")){
									if(worktime.getString("abort_datetime").equals("null")){
										reportCompleteWorks.add(report);
									} else {
										reportAbortWorks.add(report);
									}
								}else{
									reportIncompleteWorks.add(report);
								}
							}
							break;
						case 400:
							reportCompleteWorks.clear();
							reportIncompleteWorks.clear();
							reportAbortWorks.clear();
							break;
						default:
							break;
					}
					reportCompleteWorks.add(0,new Report("name","start","finish","work","object",0));
					reportIncompleteWorks.add(0,new Report("name","start","finish","work","object",0));
					reportAbortWorks.add(0,new Report("name","start","finish","work","object",0));
					reportAdapter = new ReportAdapter(getActivity(),reportCompleteWorks);
					reportListView.setAdapter(reportAdapter);
				}catch (JSONException e){
					e.printStackTrace();
				}
				kpHUD.dismiss();
			}
		});
	}

	public void getProductivities(){
		kpHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("team_id",currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorktimes/productivityWorks";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
					summaryProdcutivities.clear();
					detailProductivities.clear();
				try{
					JSONObject responseObject = new JSONObject(responseString);
					switch (responseObject.getInt("status")){
						case 100:
							String dataString = responseObject.getString("data");
							JSONArray dataArray = new JSONArray(dataString);
							for(int i = 0 ; i < dataArray.length() ; i++ ){
								JSONObject data = dataArray.getJSONObject(i);
								String summary = data.getString("summary");
								JSONObject summaryObj = new JSONObject(summary);
								String worktimeString = summaryObj.getString("OcWorktime");
								if(worktimeString.equals("[]"))worktimeString = "{}";
								JSONObject worktime = new JSONObject(worktimeString);
								String userString = summaryObj.getString("OcUser");
								if(userString.equals("[]"))userString = "{}";
								JSONObject user = new JSONObject(userString);
								String workkeyString = summaryObj.getString("OcWorkteamWorkKeyword");
								if(workkeyString.equals("[]"))workkeyString = "{}";
								JSONObject workkey = new JSONObject(workkeyString);
								String objectkeyString = summaryObj.getString("OcWorkteamObjectKeyword");
								if(objectkeyString.equals("[]"))objectkeyString = "{}";
								JSONObject objectkey = new JSONObject(objectkeyString);
								Report report = new Report(user.getString("username"),
										worktime.getString("start_datetime"),
										worktime.getString("end_datetime"),
										workkey.getString("work_name"),
										objectkey.getString("object_name"),
										worktime.getInt("quantity"));
								report.workerId = user.getInt("user_id");
								Report subreport = new Report();
								String substring = data.getString("sub");
								JSONArray sub = new JSONArray(substring);
								for( int j = 0 ; j < sub.length() ; j++){
									JSONObject subObject = sub.getJSONObject(j);
									String subworktimeString = subObject.getString("OcWorktime");
									if(worktimeString.equals("[]"))worktimeString = "{}";
									JSONObject subworktime = new JSONObject(subworktimeString);
									String subuserString = subObject.getString("OcUser");
									if(userString.equals("[]"))userString = "{}";
									JSONObject subuser = new JSONObject(subuserString);
									String subworkkeyString = subObject.getString("OcWorkteamWorkKeyword");
									if(workkeyString.equals("[]"))workkeyString = "{}";
									JSONObject subworkkey = new JSONObject(subworkkeyString);
									String subobjectkeyString = subObject.getString("OcWorkteamObjectKeyword");
									if(objectkeyString.equals("[]"))objectkeyString = "{}";
									JSONObject subobjectkey = new JSONObject(subobjectkeyString);
									subreport = new Report(subuser.getString("username"),
											subworktime.getString("start_datetime"),
											subworktime.getString("end_datetime"),
											subworkkey.getString("work_name"),
											subobjectkey.getString("object_name"),
											subworktime.getInt("quantity"));
									subreport.workerId = subuser.getInt("user_id");
									report.reports.add(subreport);
									detailProductivities.add(subreport);
								}
								summaryProdcutivities.add(report);
							}
							break;
						case 400:
							summaryProdcutivities.clear();
							break;
						default:
							break;
					}
					summaryProdcutivities.add(0,new Report("name","start","finish","work","object",0));
					productivityAdapter = new ProductivityAdapter(getActivity(),summaryProdcutivities);
					productivitySummaryListView.setAdapter(productivityAdapter);

					detailProductivities.add(0, new Report("","","","","",0));
					productivityDetailAdapter = new ProductivityDetailAdapter(getActivity(), detailProductivities);
					productivityDetailListView.setAdapter(productivityDetailAdapter);
					kpHUD.dismiss();
				}catch (JSONException e){
					e.printStackTrace();
				}
			}
		});
	}

	public class CustomComparator implements Comparator<Report> {
		String	status = "";
		public CustomComparator(String status){
			this.status = status;
		}
		@Override
		public int compare(Report ob1, Report ob2) {
			int compare = 0;
			if(status.equals("name")){
				compare = ob1.getName().compareTo(ob2.getName());
			} else if(status.equals("start")) {
				compare = ob1.getStartTime().compareTo(ob2.getStartTime());
			} else if(status.equals("end")) {
				compare = ob1.getEndTime().compareTo(ob2.getEndTime());
			} else if(status.equals("work")) {
				compare = ob1.getWork().compareTo(ob2.getWork());
			} else if(status.equals("object")) {
				compare = ob1.getObject().compareTo(ob2.getObject());
			}
			return compare;
		}
	}

	public class DecCustomComparator implements Comparator<Report> {
		String	status = "";
		public DecCustomComparator(String status){
			this.status = status;
		}
		@Override
		public int compare(Report ob1, Report ob2) {
			int compare = 0;
			if(status.equals("name")){
				compare = ob2.getName().compareTo(ob1.getName());
			} else if(status.equals("start")) {
				compare = ob2.getStartTime().compareTo(ob1.getStartTime());
			} else if(status.equals("end")) {
				compare = ob2.getEndTime().compareTo(ob1.getEndTime());
			} else if(status.equals("work")) {
				compare = ob2.getWork().compareTo(ob1.getWork());
			} else if(status.equals("object")) {
				compare = ob2.getObject().compareTo(ob1.getObject());
			}
			return compare;
		}
	}

	public ArrayList<Report> filterReporsts(ArrayList<Report> reports, String selector, String source){
		ArrayList<Report> filteredArrayList = new ArrayList<>();
		for(Report item : reports){
			if(selector.equals("name")){
				if(item.getName().equals(source)){
					filteredArrayList.add(item);
				}
			} else if(selector.equals("work")) {
				if(item.getWork().equals(source)){
					filteredArrayList.add(item);
				}
			} else if(selector.equals("object")) {
				if(item.getObject().equals(source)){
					filteredArrayList.add(item);
				}
			}
		}
		return filteredArrayList;
	}

	private void initAllValiables(){
		workList = new ArrayList<>();
		objectList = new ArrayList<>();
		actions = new ArrayList<>();
		works = new ArrayList<>();
		objects = new ArrayList<>();
		currentcommands = new ArrayList<>();
		addList = new ArrayList<>();
		quantities = new ArrayList<>();
		offerworks = new ArrayList<>();
		offeredWorks = new ArrayList<>();
		historyOffers = new ArrayList<>();
		childMap = new HashMap<OcWorkroot, List<OcWorksource>>();
		childChildMap = new HashMap<OcWorksource,List<OcWorktime>>();
		toStartAssigns = new ArrayList<OcWorksource>();
		toStartAccepts = new ArrayList<OcWorksource>();
		toStartAllworks = new ArrayList<OcWorksource>();
		assignCompletedWorks = new ArrayList<>();
		assignUncompletedWorks = new ArrayList<>();
		teamMembers =new ArrayList<TeamMember>();
		myCompletedWorks = new ArrayList<>();
		myIncompletedWorks = new ArrayList<>();
		completedAssignSubHistory = new HashMap<>();
		incompletedAssignSubHistory = new HashMap<>();
		activeWorks = new ArrayList<OcWorktime>();
		finishedWorks = new ArrayList<>();
		startedAssignWorks = new ArrayList<>();
		reportAbortWorks = new ArrayList<>();
		reportCompleteWorks = new ArrayList<>();
		reportIncompleteWorks = new ArrayList<>();
		summaryProdcutivities = new ArrayList<>();
		detailProductivities = new ArrayList<>();
		subDetailProductivities = new ArrayList<>();
		subSummaryProductivities = new ArrayList<>();
		reportTempList = new ArrayList<>();
		teamMemberListViewStatus = "";
		teamLeaber = new TeamMember();
		removeMembers = new ArrayList<>();
	}

	private void initAllViews(){
		assignedWorkLayout = (LinearLayout)getActivity().findViewById(R.id.assigned_work_layout);
		offersListView = (ListView)getActivity().findViewById(R.id.offer_listView);//for offers list view in find job button.
		offer_layout = (RelativeLayout)getActivity().findViewById(R.id.offer_layout);
		editMessageLayout = (RelativeLayout)getActivity().findViewById(R.id.textsend);
		bottomBarLayout = (RelativeLayout)getActivity().findViewById(R.id.bottom_bar_layout);
		actionbutton = (ImageView)getActivity().findViewById(R.id.action_button);
		findaJobbutton = (ImageView)getActivity().findViewById(R.id.findajob_button);
		completedJobButton = (ImageView)getActivity().findViewById(R.id.jobcomplete_button);
		reportWorksButton = (ImageView)getActivity().findViewById(R.id.jobsincomplete_button);
		userLocation = (ImageView)getActivity().findViewById(R.id.userlocation_button);
		teamButton = (ImageView)getActivity().findViewById(R.id.workteam_button);
		productivityButton = (ImageView)getActivity().findViewById(R.id.productivity_button);
		badgeLayout = (RelativeLayout)getActivity().findViewById(R.id.badge_layout);
		badgeTextView = (AutofitTextView)getActivity().findViewById(R.id.badge_textview);
		acceptHistoryListView = (ExpandableListView) getActivity().findViewById(R.id.accept_histroty_listview);
		offeredWorkButton = (Button)getActivity().findViewById(R.id.offered_work_button);
		mGreenbutton = (ImageButton)getActivity().findViewById(R.id.green_commandbutton);
		/**For Assign funtion views*/
		acceptHistoryButton = (Button)getActivity().findViewById(R.id.work_history_button);
		assignMemberListview = (ListView)getActivity().findViewById(R.id.assign_member_listview);
		assignedWorkListView = (ListView)getActivity().findViewById(R.id.assigned_work_listview);
		assignCompletedWorkList = (ExpandableListView) getActivity().findViewById(R.id.assign_completed_listview);
		assignUncompletedWorkList = (ExpandableListView)getActivity().findViewById(R.id.assign_uncompleted_listview);
		assignHistoryLayout = (RelativeLayout) getActivity().findViewById(R.id.assign_history_laytout);
		assignCompletedWorkHistoryButton = (Button)getActivity().findViewById(R.id.assign_completed_history_button);
		assignIncompletedWorkHistoryButton = (Button)getActivity().findViewById(R.id.assign_incompleted_history_button);
		assignBadgeLayout = (RelativeLayout) getActivity().findViewById(R.id.assign_badge_layout);
		assignBadgeText = (TextView) getActivity().findViewById(R.id.assign_badge_textview);
		/**For Report function Views**********************************************/
		report_layout = (RelativeLayout) getActivity().findViewById(R.id.report_layout);
		reportListView = (ListView) getActivity().findViewById(R.id.report_listview);
		reportCompleteButton = (Button)getActivity().findViewById(R.id.report_completed_tab);
		reportIncompleteButton = (Button)getActivity().findViewById(R.id.report_incompleted_tab);
		reportAbortButton = (Button)getActivity().findViewById(R.id.report_abort_tab);
		/**For SummaryProdcutivity function views**********/
		productivityLayout = (RelativeLayout)getActivity().findViewById(R.id.productivity_layout);
		productivitySummaryListView = (ListView)getActivity().findViewById(R.id.productivity_summary_listview);
		productivitySummaryButton = (Button)getActivity().findViewById(R.id.productivity_summary_button);
		productivityDetailListView = (ListView)getActivity().findViewById(R.id.productivity_detail_listview);
		productivityDetailButton = (Button)getActivity().findViewById(R.id.productivity_detail_tab);

		badgeLayout.setVisibility(View.GONE);

		commandLayout = (LinearLayout)getActivity().findViewById(R.id.command_list_layout);
		mGreenbutton = (ImageButton)getActivity().findViewById(R.id.green_commandbutton);
		addCommandButton = (ImageButton)getActivity().findViewById(R.id.addCommandButton);
		backButton = (Button)getActivity().findViewById(R.id.command_backbutton);
		mActionListView = (ListView)getActivity().findViewById(R.id.action_listview);
		mWorkListView = (ListView)getActivity().findViewById(R.id.work_listview);
		mObjectListView = (ListView)getActivity().findViewById(R.id.object_listview);
		mQuantityListView = (ListView)getActivity().findViewById(R.id.quantity_listview);
		mAddJobListView = (ListView)getActivity().findViewById(R.id.start_listview);
		topBarTextView = (TextView)getActivity().findViewById(R.id.add_action_catalog_textview);
		addJobLayout = (LinearLayout)getActivity().findViewById(R.id.job_add_layout);
		refreshImage = (ImageView)getActivity().findViewById(R.id.refresh_image);

		/**team Members function views**/
		teamMemberLayout = (RelativeLayout)getActivity().findViewById(R.id.team_member_laytout);
		teamMemberListView = (ListView)getActivity().findViewById(R.id.team_member_listview);
		addTeamMemberButton = (ImageButton)getActivity().findViewById(R.id.team_member_add_tab);
		removeTeamMemberButton = (ImageButton)getActivity().findViewById(R.id.team_member_remove_tab);
		appointAssistantTeamLeaderButton = (ImageButton)getActivity().findViewById(R.id.team_member_leader_tab);
		teamMemberListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setOnclickTeamViews();

	}

	private void switchToMyJobs(MenuItem item){
		if (filter) {
			myCompletedWorks.clear();
			myIncompletedWorks.clear();
			for (OcWorksource work : assignCompletedWorks) {
				if (work.worksourceWorkerId == user_id) {
					myCompletedWorks.add(work);
				}
			}
			completedAssignAdapter = new AssignHistoryExpandableAdapter(getActivity(), myCompletedWorks,completedAssignSubHistory);
			assignCompletedWorkList.setAdapter(completedAssignAdapter);

			for (OcWorksource work : assignUncompletedWorks) {
				if (work.worksourceWorkerId == user_id) {
					myIncompletedWorks.add(work);
				}
			}
			incompletedAssignAdapter = new AssignHistoryExpandableAdapter(getActivity(), myIncompletedWorks, incompletedAssignSubHistory);
			assignUncompletedWorkList.setAdapter(incompletedAssignAdapter);

			//for filtering productivities
			ArrayList<Report> filteredSummaryProductivities = new ArrayList<>();
			ArrayList<Report> filteredDetailProductivities = new ArrayList<>();
			for(Report report : subSummaryProductivities){
				if(report.workerId == user_id)
				filteredSummaryProductivities.add(report);
			}
			filteredSummaryProductivities.add(0,new Report("","","","","",0));
			ProductivityAdapter summaryAdapter = new ProductivityAdapter(getActivity(), filteredSummaryProductivities);
			productivitySummaryListView.setAdapter(summaryAdapter);
			for(Report report : subDetailProductivities){
				if(report.workerId == user_id)
				filteredDetailProductivities.add(report);
			}
			filteredDetailProductivities.add(0,new Report("","","","","",0));
			ProductivityDetailAdapter detailAdapter = new ProductivityDetailAdapter(getActivity(), filteredDetailProductivities);
			productivityDetailListView.setAdapter(detailAdapter);

			ArrayList<Report> complete = new ArrayList<>();
			for(Report report : reportCompleteWorks){
				if(report.workerId == user_id)
					complete.add(report);
			}
			complete.add(0,new Report("","","","","",0));
			ArrayList<Report> incomplete = new ArrayList<>();
			for(Report report : reportIncompleteWorks){
				if(report.workerId == user_id)
					incomplete.add(report);
			}
			incomplete.add(0,new Report("","","","","",0));
			ArrayList<Report> abort = new ArrayList<>();
			for(Report report : reportAbortWorks){
				if(report.workerId == user_id)
					abort.add(report);
			}
			abort.add(0,new Report("","","","","",0));
			switch (reportListViewStatus){
				case 0:
					ReportAdapter adapter0 = new ReportAdapter(getActivity(), complete);
					reportListView.setAdapter(adapter0);
					break;
				case 1:
					ReportAdapter adapter1 = new ReportAdapter(getActivity(), incomplete);
					reportListView.setAdapter(adapter1);
					break;
				case 2:
					ReportAdapter adapter2 = new ReportAdapter(getActivity(), abort);
					reportListView.setAdapter(adapter2);
					break;
				default:
					break;
			}
			filter = false;
			item.setIcon(R.drawable.turnoff);
		} else {
			incompletedAssignAdapter = new AssignHistoryExpandableAdapter(getActivity(), assignUncompletedWorks, incompletedAssignSubHistory);
			assignUncompletedWorkList.setAdapter(incompletedAssignAdapter);
			completedAssignAdapter = new AssignHistoryExpandableAdapter(getActivity(), assignCompletedWorks,completedAssignSubHistory);
			assignCompletedWorkList.setAdapter(completedAssignAdapter);
			filter = true;
			item.setIcon(R.drawable.turnon);
			switch (reportListViewStatus){
				case 0:
					ReportAdapter adapter0 = new ReportAdapter(getActivity(), reportCompleteWorks);
					reportListView.setAdapter(adapter0);
					break;
				case 1:
					ReportAdapter adapter1 = new ReportAdapter(getActivity(), reportIncompleteWorks);
					reportListView.setAdapter(adapter1);
					break;
				case 2:
					ReportAdapter adapter2 = new ReportAdapter(getActivity(), reportAbortWorks);
					reportListView.setAdapter(adapter2);
					break;
				default:
					break;
			}
			productivityDetailListView.setAdapter(productivityDetailAdapter);
			productivitySummaryListView.setAdapter(productivityAdapter);
		}
	}

	public void leaveTeam(String teamJid, String userJid){
		if(conversation != null) {
			/**anton's code**/
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put(WebClient.JABBER_ID,userJid);
			params.put(WebClient.WORKTEAM_JABBER_ID, teamJid);
			String url = "http://54.169.8.76/proapp/OcWorkteamMembers/mobile_leaveteam";
			client.post(url, params, new TextHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String responseString) {
//					getTeamJobs(currentTeamInformation.workteam_jid);
					getAllTeamMembers();
				}
			});
		}
	}

	public void joinTeam(String teamJid, String userJid){
		/***teamlist view in startConversation*/
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put(WebClient.WORKTEAM_JABBER_ID, teamJid);
		params.put(WebClient.JABBER_ID, userJid);
		String url = "http://54.169.8.76/proapp/OcWorkteams/mobile_jointeam";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				try {
					JSONObject responseObject = new JSONObject(responseString);
					switch (responseObject.getInt("status")){
						case 100:
							break;
						case 400:
							break;
						default:
							break;
					}
					getAllTeamMembers();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void getAllTeamMembers(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("workteam_id", currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorkteamMembers/mobile_getonline_members";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(getActivity(),"connection error" , Toast.LENGTH_SHORT).show();
				kpHUD.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				teamMembers.clear();
				try {
					JSONObject responseOBJ = new JSONObject(responseString);
					switch (responseOBJ.getInt("status")){
						case 100:
							String data = responseOBJ.getString("data");
							JSONArray arrayData = new JSONArray(data);
							for(int i = 0 ; i < arrayData.length() ; i++){
								JSONObject jobObj = arrayData.getJSONObject(i);
								String jobString = jobObj.getString("OcWorkteamMember");
								JSONObject job = new JSONObject(jobString);
								String userString = jobObj.getString("OcUser");
								JSONObject userObj = new JSONObject(userString);
								String teamString = jobObj.getString("OcWorkteam");
								JSONObject teamObj = new JSONObject(teamString);
								teamMembers.add(new TeamMember(userObj.getString("username"),userObj.getString("jabber_id"),job.getInt("user_id"),
										teamObj.getInt("workteam_id"),teamObj.getString("workteam_jabber_id"),teamObj.getString("workteam_name"),teamObj.getInt("creator_id"),teamObj.getInt("workteam_leader_id")));
							}
							if(getActivity()!= null) {
								teamMemberAdapter = new TeamMemberAdapter(getActivity(), teamMembers);
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										teamMemberListView.setAdapter(teamMemberAdapter);
									}
								});
							}
							break;
						case 400:
							break;
						default:
							break;
					}
				}catch (JSONException e){
					e.printStackTrace();
				}
				kpHUD.dismiss();
			}
		});
	}

	private void setOnclickTeamViews(){
		addTeamMemberButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText input = new EditText(getActivity());
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setView(input);
				builder.setNegativeButton(getString(R.string.command_cancel),null);
				builder.setPositiveButton(getString(R.string.command_save),new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String inputName = input.getText().toString();
						if (inputName.contains("@")){
							String JidString = inputName.split("@")[0]+"_"+inputName.split("@")[1]+"@"+"54.169.8.76";
							joinTeam(currentTeamInformation.workteam_jid,JidString);

							try {
								Jid jid = Jid.fromString(JidString);
								activity.xmppConnectionService.invite(conversation,jid);
							} catch (InvalidJidException e) {
								e.printStackTrace();
							}
							kpHUD.show();
						} else {
							Toast.makeText(getActivity(),"Wrong Worker ID",Toast.LENGTH_SHORT).show();
						}
					}
				});
				builder.create().show();

			}
		});
		removeTeamMemberButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				teamMemberListViewStatus = "remove";
				if(teamMemberAdapter != null) {
					if (teamMemberListView.getAdapter().equals(teamMemberAdapter)) {
						teamMemberCheckableAdapter = new TeamMemberCheckableAdapter(getActivity(), teamMembers, teamMemberListViewStatus);
						teamMemberListView.setAdapter(teamMemberCheckableAdapter);
						removeTeamMemberButton.setImageResource(R.drawable.white_check);
					} else {
						removeMembers.clear();
						for (TeamMember member : teamMembers) {
							if (member.isChecked)
								removeMembers.add(member);
						}

						if (!removeMembers.isEmpty()) {
							for (TeamMember member : removeMembers) {
								leaveTeam(currentTeamInformation.workteam_jid, member.jid);
								abortForDeletedUser(member.userId);
							}
							kpHUD.show();
						}
						teamMemberAdapter = new TeamMemberAdapter(getActivity(), teamMembers);
						teamMemberListView.setAdapter(teamMemberAdapter);
						removeTeamMemberButton.setImageResource(R.drawable.userminus);
					}
					appointAssistantTeamLeaderButton.setImageResource(R.drawable.userup);
				}
			}
		});

		appointAssistantTeamLeaderButton.setOnClickListener(new OnClickListener() {
			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View v) {
				teamMemberListViewStatus = "setTeamLeader";
				if(teamMemberAdapter != null) {
					if (teamMemberListView.getAdapter().equals(teamMemberAdapter)) {
						teamMemberCheckableAdapter = new TeamMemberCheckableAdapter(getActivity(), teamMembers, teamMemberListViewStatus);
						teamMemberListView.setAdapter(teamMemberCheckableAdapter);
						appointAssistantTeamLeaderButton.setImageResource(R.drawable.white_check);
					} else {
						for (TeamMember member : teamMembers) {
							if (member.isChecked) {
								teamLeaber = (member);
							}
						}
						if (teamLeaber.userId != 0) {
							if (!teamLeaber.jid.isEmpty()) {
								setCurrentTeamLeader(teamLeaber.userId);
								mEditMessage.setText(teamLeaber.name+" is appointed Team Leader");
								kpHUD.show();
							}
						}
						teamMemberAdapter = new TeamMemberAdapter(getActivity(), teamMembers);
						teamMemberListView.setAdapter(teamMemberAdapter);
						appointAssistantTeamLeaderButton.setImageResource(R.drawable.userup);
					}
					removeTeamMemberButton.setImageResource(R.drawable.userminus);
				}
			}
		});
	}

	public View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	public void setCurrentTeamLeader(int userId){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("team_id", currentTeamId);
		params.put("user_id", userId);
		String url = "http://54.169.8.76/proapp/OcWorkteams/setTeamLeader";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				getAllTeamMembers();
			}
		});
	}

	public  void abortForDeletedUser(int userId){
		AsyncHttpClient client  = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		params.put("team_id" , currentTeamId);
		String url = "http://54.169.8.76/proapp/OcWorktimes/abortfordeleteduser";
		client.post(url, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

			}
		});
	}
}
