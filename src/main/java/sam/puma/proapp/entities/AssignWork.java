package sam.puma.proapp.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Anton on 6/22/2016.
 */
public class AssignWork {
    public String assign_sentence;
    public int assignWorkId;
    public int assignUserId;
    public int assignedUserId;
    public String assignUserName;
    public String assignedUserName;
    public String assignWork;
    public String assignObject;
    public int assignQuantity;
    public String assignedTime;
    public boolean isStarted;
    public boolean isFinished;
    public String startTime;
    public String finishTime;

    public AssignWork(){

    }

    public AssignWork(int assignworkid,int assignUserid,int assigneduserid,String assingwork,String assingObject,int quantity,String assignedTime,boolean isStarted){
        this.assignWorkId = assignworkid;
        this.assignUserId = assignUserid;
        this.assignedUserId = assigneduserid;
        this.assignWork = assingwork;
        this.assignObject = assingObject;
        this.assignedTime = assignedTime;
        this.isStarted = isStarted;
        this.assignQuantity = quantity;

    }
    public AssignWork(String work,String assignObject , int quantity){
        this.assignWork = work;
        this.assignObject = assignObject;
        this.assignQuantity = quantity;
    }
    public String getWorkSentence(){
        return this.assignWork+" "+this.assignObject+" "+this.assignQuantity;
    }
    public String getDefaultTimeZone(){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = "";
        try {
            java.util.Date currentDate = sourceFormat.parse(this.assignedTime);
            TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
            SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            destFormat.setTimeZone(tz);
            result = destFormat.format(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
