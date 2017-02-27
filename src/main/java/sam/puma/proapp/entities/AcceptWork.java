package sam.puma.proapp.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Anton on 6/12/2016.
 */
public class AcceptWork {
    public int workaccept_id;
    public int workaccept_user_id;
    public int workoffer_id;
    public String workaccept_time;
    public int workaccept_quantity;
    public String workAccept;
    public String name;
    public AcceptWork(){

    }
    public AcceptWork(int id, int user_id, int offer_id, String time, int quantity, String workAccepts, String name){
        this.workaccept_id = id;
        this.workaccept_user_id = user_id;
        this.workoffer_id = offer_id;
        this.workaccept_time = time;
        this.workaccept_quantity = quantity;
        this.workAccept = workAccepts;
        this.name = name;
    }
    public AcceptWork(String accepts, String time){
        this.workAccept = accepts;
        this.workaccept_time = time;
    }

    public String getDefaultTimeZone(){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = "";
        try {
            java.util.Date currentDate = sourceFormat.parse(this.workaccept_time);
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
