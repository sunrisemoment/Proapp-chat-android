package sam.puma.proapp.entities;

/**
 * Created by Anton on 6/22/2016.
 */
public class TeamMember {
    public String name;
    public String jid;
    public int userId;
    public int teamID;
    public String teamJid;
    public String teamName;
    public int teamCreatorId;
    public int teamLeaderId;
    public boolean isChecked = false;
    public TeamMember(String name,String jid,int userid,int teamID,String teamJid,String teamname, int creator, int leader){
        this.name = name;
        this.jid = jid;
        this.userId = userid;
        this.teamID = teamID;
        this.teamJid = teamJid;
        this.teamName = teamname;
        this.teamCreatorId = creator;
        this.teamLeaderId = leader;
    }

    public TeamMember(){

    }
}
