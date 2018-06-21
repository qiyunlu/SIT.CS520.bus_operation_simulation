package simulation;

public class Initialization {
    
    // The number of buses
    public static final int busNum = 5;
    // The number of bus stops
    public static final int stationNum = 15;
    // The time driving between two contiguous stops(seconds)
    public static final double driveTime = 5d *60d;
    // Mean arrival rate of people at one station(persons/second)
    public static final double meanArrivalRate = 5d /60d;
    // The time one people boards the bus(seconds)
    public static final double boardingTime = 2d;
    // How long will us run this simulation(seconds)
    public static final double simulatingTime = 8d *60d*60d;
    // How much time we will output the state of bus once(seconds)
    public static final double outputTime = 30d *60d;
    
}
