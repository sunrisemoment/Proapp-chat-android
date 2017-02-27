package sam.puma.proapp.httpclient;

/**
 * Created by Anton on 4/20/2016.
 */
public class Working {
    private int id;
    private int work_id;
    private int team_id;
    private int target_id;
    private String work_end;
    private String work_start;
    private String work_duation;
    private int appraiser_id;
    private int quantity;
    private String quality;
    private String status;

    public Working(int id, int w_id, int t_id, int tar_id , String w_end, String w_start, String w_d,int appraiser_id, int quantity, String quality,String status){
        this.id = id;
        this.work_id = w_id;
        this.team_id = t_id;
        this.target_id = tar_id;
        this.work_end = w_end;
        this.work_start = w_start;
        this.work_duation = w_d;
        this.appraiser_id = appraiser_id;
        this.quality = quality;
        this.quantity = quantity;
        this.status  = status;
    }
}
