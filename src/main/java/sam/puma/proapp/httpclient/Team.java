package sam.puma.proapp.httpclient;

/**
 * Created by Anton on 4/14/2016.
 */
public class Team {

    public int creator_id;
    public String name ;
    public String workteam_jid;
    public int id ;

    public Team( int id,String name, int creator_id,String workteam_jid){
        this.id = id;
        this.name = name;
        this.workteam_jid = workteam_jid;
        this.creator_id = creator_id;
    }

}
