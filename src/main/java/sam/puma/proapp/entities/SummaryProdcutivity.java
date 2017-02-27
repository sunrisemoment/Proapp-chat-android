package sam.puma.proapp.entities;

import java.util.ArrayList;

/**
 * Created by puma on 15.07.2016.
 */
public class SummaryProdcutivity {
    private String name;
    private String work;
    private String object;
    private float productivity;
    private ArrayList<OcWorktime> worktimes;

    public SummaryProdcutivity(){}

    public SummaryProdcutivity(String name, String work, String object, float quantity){
        this.name = name;
        this.work= work;
        this.object = object;
        this.productivity = quantity;
    }

    public String getName(){return this.name;}

    public String getWork(){return this.work;}

    public String getObject(){return this.object;}

    public float getProductivity(){return this.productivity;}

    public void setWorktimes(ArrayList<OcWorktime> worktimes){
        this.worktimes = worktimes;
    }
    public void setProductivity(){
        int sum = 0;
        int n = 0;
        for(OcWorktime worktime : worktimes){
            sum += worktime.getQuantity();
            n++;
        }
        this.productivity =(float)sum/n;
    }
}
