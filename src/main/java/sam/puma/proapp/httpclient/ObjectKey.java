package sam.puma.proapp.httpclient;

/**
 * Created by Anton on 4/15/2016.
 */
public class ObjectKey {

    public int id;
    public String objectname ;
    public int workteam_id;
    public String create_time;
    public String delete_time;
    public int create_user_id;
    public int delete_user_id;
    public boolean isActive;
    public ObjectKey(int id, String name, int team_id, String create_time, String delete_time , int create_user_id, int delete_user_id , boolean isActive){
        this.id = id;
        this.objectname = name;
        this.workteam_id = team_id;
        this.create_time = create_time;
        this.delete_time = delete_time;
        this.create_user_id = create_user_id;
        this.delete_user_id = delete_user_id;
        this.isActive = isActive;
    }
    public ObjectKey(){

    }
}
