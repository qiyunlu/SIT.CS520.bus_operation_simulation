package simulation;

public class Bus {
    
    // bus name
    public int busName = -1;
    // last stop
    public int lastStop = -1;
    // next stop
    public int nextStop = -1;
    // the time bus left the last stop
    public double nextArrivalTime = -1d;
    /* 
     * bus state
     * -1 : initial value, means nothing.
     * 0 : bus is on the way.
     * 1 : bus is at station.
     */
    public int busState = -1;
    
    // constructor
    public Bus(int busName, int lastStop, int nextStop, double nextArrivalTime, int busState) {
        this.busName = busName;
        this.lastStop = lastStop;
        this.nextStop = nextStop;
        this.nextArrivalTime = T.keepDecimalPlaces(nextArrivalTime, 3);
        this.busState = busState;
    }
    
}
