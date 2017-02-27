package sam.puma.proapp.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Anton on 6/12/2016.
 */
public class OfferWork {
    public int workoffer_id;
    public int workoffer_user_id;
    public int workteam_id;
    public String workoffer_work;
    public String workoffer_object;
    public String workoffer_quantity;
    public String workoffer_datetime;
    public int workoffer_duration;
    public boolean isExpired;
    public long expiryTime;
    public int totalOffers;
    public String userName;
    public static int defaultExpiryTime = 15;

    public OfferWork(){

    }
    public OfferWork(int workoffer_id,
                     int workoffer_user_id,
                     int workteam_id,
                     String workoffer_work,
                     String workoffer_object,
                     String workoffer_quantity,
                     String workoffer_datetime,
                     int workoffer_duration,
                     boolean isexpeired){
        this.workoffer_id = workoffer_id;
        this.workoffer_user_id = workoffer_user_id;
        this.workteam_id = workteam_id;
        this.workoffer_work = workoffer_work;
        this.workoffer_object = workoffer_object;
        this.workoffer_quantity = workoffer_quantity;
        this.workoffer_datetime = workoffer_datetime;
        this.workoffer_duration = workoffer_duration;
        this.isExpired = isexpeired;

    }
    public OfferWork(String work, String object, String quantity, String time){
        this.workoffer_work = work;
        this.workoffer_object = object;
        this.workoffer_quantity = quantity;
        this.workoffer_datetime = time;
    }

    public long getExpiryTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            java.util.Date date = dateFormat.parse(this.workoffer_datetime);
            java.util.Date newDate = new java.util.Date();
            String currentDateString = dateFormat.format(newDate);
            java.util.Date currentDate = dateFormat.parse(currentDateString);
            this.expiryTime = this.printDifference(date,currentDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if((this.workoffer_duration-this.expiryTime > 0)&&this.expiryTime != 1000){
            return this.workoffer_duration-this.expiryTime;
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

//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays,
//                elapsedHours, elapsedMinutes, elapsedSeconds);
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
            java.util.Date currentDate = sourceFormat.parse(this.workoffer_datetime);
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
