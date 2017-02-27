package sam.puma.proapp.httpclient;

/**
 * Created by Anton on 4/15/2016.
 */
public class WorkKey {

    public int id;
    public String workname;
    public int workteam_id ;
    public String createTime;
    public String deleteTime;
    public int creator_id;
    public int deleter_id;
    public boolean isActive;
    public WorkKey(){

    }
    public WorkKey(int id, String name, int team_id,String create_time,String delete_time,int creator_id,int deleter_id,boolean isActive){
        this.id = id;
        this.workname = name;
        this.workteam_id = team_id;
        this.createTime = create_time;
        this.deleteTime = delete_time;
        this.creator_id = creator_id;
        this.deleter_id = deleter_id;
        this.isActive = isActive;
    }
}
