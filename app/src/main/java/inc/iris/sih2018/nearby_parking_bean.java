package inc.iris.sih2018;

/**
 * Created by lappy on 3/26/2018.
 */

public class nearby_parking_bean {
    public String name;
    public String distance;
    public int slots;
    public int cost;

     public nearby_parking_bean(String name,String distance,int  slots,int cost)
     {
        this.name=name;
        this.distance=distance;
        this.slots=slots;
        this.cost=cost;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getDistance()
    {
        return distance;
    }
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    public int getSlots()
    {
        return slots;
    }
    public void setSlots(int slots)
    {
        this.slots = slots;
    }
    public int getCost()
    {
        return cost;
    }
    public void setCost(int cost)
    {
        this.cost = cost;
    }



}
