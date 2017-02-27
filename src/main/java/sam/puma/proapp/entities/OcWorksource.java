package sam.puma.proapp.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Anton on 7/1/2016.
 */
public class OcWorksource {
    public int worksourceId;
    public String worksource;
    public int workteamId;
    public int workrootId;
    public int worksourceProviderId;
    public int worksourceWorkerId;
    public String worksourceCreateTime;
    public String worksourceWorkKeyword;
    public int worksourceWorkKeywordId;
    public String worksourceObjectKeyword;
    public int worksourceObjectKeywordId;
    public int worksourceQuantity;
    public int worksourceQuantityLessStarted;
    public int worksourceQuantityLessFinished;
    public boolean worksourceIsCloased;
    public String worksourceCloasedDateTime;
    public String worksourceProviderName;
    public String worksourceWorkerName;

    public OcWorksource(){

    }

    public OcWorksource(int id,String worksource,int teamid, int rootid,int providerid,int workerid,
                        String createTime, String workKeyword, String objectKeyword, int quantity,
                        int startedQuantity, int finishedQuantity, boolean isCloased, String cloasedDateTime,
                        String providerName, String workerName, int workid, int objectid){
        this.worksourceId = id;
        this.worksource = worksource;
        this.workteamId = teamid;
        this.workrootId = rootid;
        this.worksourceProviderId = providerid;
        this.worksourceWorkerId = workerid;
        this.worksourceCreateTime = createTime;
        this.worksourceWorkKeyword = workKeyword;
        this.worksourceObjectKeyword = objectKeyword;
        this.worksourceQuantity = quantity;
        this.worksourceQuantityLessStarted = startedQuantity;
        this.worksourceQuantityLessFinished = finishedQuantity;
        this.worksourceIsCloased = isCloased;
        this.worksourceCloasedDateTime = cloasedDateTime;
        this.worksourceProviderName = providerName;
        this.worksourceWorkerName = workerName;
        this.worksourceWorkKeywordId = workid;
        this.worksourceObjectKeywordId = objectid;
    }

    public String getKeyWordSentence(){
        return this.worksourceWorkKeyword+" "+this.worksourceObjectKeyword+" "+this.worksourceQuantityLessStarted;
    }
    public String getOfferdWorkSentence(){
        return this.worksourceWorkKeyword+" "+this.worksourceObjectKeyword+" "+this.worksourceQuantity;
    }

    public String getDefaultTimeZone(){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = "";
        try {
            java.util.Date currentDate = sourceFormat.parse(this.worksourceCreateTime);
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
