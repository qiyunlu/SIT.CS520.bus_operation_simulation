package simulation;

public class Event {
    
    /*
     * event type
     * -1 : initial value, means nothing.
     * 0 : a person comes to a station.
     * 1 : a bus arrives a station
     * 2 : one person boards the bus
     * 3 : monitor event, for a period of time to output the running state
     */
    public int eventType = -1;
    // the time this event occurs
    public double startTime = -1d;
    // bus name
    public int busName = -1;
    // station name
    public int stationName = -1;
    
    // constructor
    public Event(int eventType, double startTime, int busName, int stationName) {
        this.eventType = eventType;
        this.startTime = T.keepDecimalPlaces(startTime, 3);
        this.busName = busName;
        this.stationName = stationName;
    }
    
}
