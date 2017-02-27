package sam.puma.proapp.entities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by puma on 13.07.2016.
 */
public class Report {
    private String name;
    private String startTime;
    private String endTime;
    private String work;
    private String object;
    private String abort;
    private int quantity;
    public OcWorkroot workRoot;
    public OcWorksource workSource;
    public OcWorktime worktime;
    public int workerId;

    public ArrayList<Report> reports;

    public Report(){
        this.reports = new ArrayList<>();
        this.workRoot = new OcWorkroot();
        this.workSource = new OcWorksource();
    }

    public Report(String name, String start, String end, String work, String object, int quantity){
        this.name = name;
        this.startTime = start;
        this.endTime = end;
        this.work = work;
        this.object = object;
        this.quantity = quantity;
        this.reports = new ArrayList<>();
    }

    public void setAbort(String abort){
        this.abort = abort;
    }
    public String getAbort(){
        return this.abort;
    }

    public String getName(){ return this.name; }

    public String getStartTime(){return this.startTime;}

    public String getEndTime(){return  this.endTime;}

    public String getWork(){return this.work;}

    public String getObject(){return this.object;}

    public int getQuantity(){return this.quantity;}

    public float getProductivity(){

        float timeDurtion = 0;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            if(endTime.equals("null")){
                return 0;
            }
            java.util.Date start = dateFormat.parse(this.startTime);
            java.util.Date end = dateFormat.parse(this.endTime);
            timeDurtion = this.printDifference(start,end);

        }catch (ParseException e){
            e.printStackTrace();
        }
        if(timeDurtion <= 0.0001){
            return 999999;
        }
        return (float)roundTwoDecimals(this.quantity/timeDurtion);
    }

    public float getSubProductivity(){
        float totalProductivity = 0;
        for (Report item : this.reports) {
            totalProductivity += item.getProductivity();
        }
        return totalProductivity/(float)this.reports.size();
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public float printDifference(java.util.Date startDate, java.util.Date endDate){

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
        return (float)elapsedHours+(float)elapsedMinutes/(float)60;
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
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
