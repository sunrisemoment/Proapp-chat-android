package sam.puma.proapp.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Anton on 7/1/2016.
 */
public class OcWorkroot {
    public int workrootId;
    public String workroot;
    public int workrootUserId;
    public int workteamId;
    public int workKeywordId;
    public int objectKeywordId;
    public int workrootQuantity;
    public int workrootRemainQuantity;
    public String workrootDateTime;
    public int duration;
    public boolean isExpired;
    public String workKeyword;
    public String objectKeyword;
    public String userName;
    public long expiryTime;

    public OcWorkroot(){

    }
    public OcWorkroot(int workroot_id,String workroot,int workroot_user_id,int workteam_id,int workkeyword_id,int objectkeyword_id,int workroot_quantity, int workrootremainquantity, String workrootdatetime, int duration, boolean expired,String work, String object,String userName){
        this.workrootId = workroot_id;
        this.workroot = workroot;
        this.workrootUserId = workroot_user_id;
        this.workteamId = workteam_id;
        this.workKeywordId =workkeyword_id;
        this.objectKeywordId = objectkeyword_id;
        this.workrootQuantity = workroot_quantity;
        this.workrootRemainQuantity = workrootremainquantity;
        this.workrootDateTime = workrootdatetime;
        this.duration = duration;
        this.isExpired = expired;
        this.workKeyword = work;
        this.objectKeyword = object;
        this.userName = userName;
    }
    public long getExpiryTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            java.util.Date date = dateFormat.parse(this.workrootDateTime);
            java.util.Date newDate = new java.util.Date();
            String currentDateString = dateFormat.format(newDate);
            java.util.Date currentDate = dateFormat.parse(currentDateString);
            this.expiryTime = this.printDifference(date,currentDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if((this.duration-this.expiryTime > 0)&&this.expiryTime != 1000){
            return this.duration-this.expiryTime;
        }else{
            return 1000;
        }
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public long printDifference(java.util.Date startDate, java.util.Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedDays <=0){
            if(elapsedHours <=0){
                if(elapsedMinutes>=0){
                    return elapsedMinutes;
                } else {
                    return 1000;
                }
            } else {
                return 1000;
            }
        }else{
            return 1000;
        }
    }

    public String getDefaultTimeZone(){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = "";
        try {
            java.util.Date currentDate = sourceFormat.parse(this.workrootDateTime);
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
