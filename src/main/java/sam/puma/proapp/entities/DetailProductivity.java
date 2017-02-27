package sam.puma.proapp.entities;

/**
 * Created by puma on 15.07.2016.
 */
public class DetailProductivity {
    private String user;
    private String start;
    private String work;
    private String object;
    private int quantity;
    private int productivity;

    public DetailProductivity(){}

    public DetailProductivity(String name, String start, String work, String object, int quantity, int productivity){
        this.user  = name;
        this.start = start;
        this.work = work;
        this.object = object;
        this.quantity = quantity;
        this.productivity = productivity;
    }

    public String getUser(){return this.user;}

    public String getStart(){return this.start;}

    public String getWork(){return  this.work;}

    public String getObject(){return this.object;}

    public int getQuantity(){return this.quantity;}

    public int getProductivity(){return this.productivity;}

}
