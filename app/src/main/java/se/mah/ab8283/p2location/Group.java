package se.mah.ab8283.p2location;

/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */

public class Group {
    String name = "";
    int members = 0;

    public Group(String name, int members){
        this.name = name;
        this.members = members;
    }



    public String getName(){
        return name;
    }

    public int getMembers(){
        return members;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setMembers(int members){
        this.members = members;
    }
}
