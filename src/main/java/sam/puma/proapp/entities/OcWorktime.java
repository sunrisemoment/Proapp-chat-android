package sam.puma.proapp.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Anton on 6/27/2016.
 */
public class OcWorktime {
    private int workTimeId;
    private int workTeamId;
    private int workSourceId;
    private int userId;
    private int workKeywordId;
    private int objectKeywordId;
    private int quantity;
    private String startDateTime;
    private String endDateTime;
    private String workDuration;
    private int appraiserId;
    private String appraisal;
    private String appraiseDateTime;
    private int abortByUserId;
    private String abortDateTime;
    private String workTimeStatus;

    public String workKeyword;
    public String objectKeyword;
    public String userName;
    public String appraiserName;
    public String abortUserName;

    public OcWorktime(){

    }
    public OcWorktime(int id, int teamid,int worksourceid, int userid, int workid, int objectid, int quantity, String starttime,
                      String endTime, String duration, int appraiserId, String appraiseDateTime,
                      String appraisal, int abortUserId, String abortDateTime, String status){
        this.workTimeId = id;
        this.workTeamId = teamid;
        this.workSourceId = worksourceid;
        this.userId = userid;
        this.workKeywordId = workid;
        this.objectKeywordId = objectid;
        this.quantity = quantity;
        this.startDateTime = starttime;
        this.endDateTime = endTime;
        this.workDuration = duration;
        this.appraiserId = appraiserId;
        this.appraiseDateTime = appraiseDateTime;
        this.appraisal = appraisal;
        this.abortByUserId = abortUserId;
        this.abortDateTime = abortDateTime;
        this.workTimeStatus = status;
    }

    public String getWorkSentence(){
        return this.workKeyword+" "+this.objectKeyword+" "+this.quantity;
    }
    public int getWorkTimeId(){
        return this.workTimeId;
    }
    public int getWorkSourceId(){
        return this.workSourceId;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public String getStartDateTime(){
        return this.startDateTime;
    }
    public String getEndDateTime(){
        return this.endDateTime;
    }

    public String getDefaultTimeZone(String time){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = "";
        try {
            java.util.Date currentDate = sourceFormat.parse(time);
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
