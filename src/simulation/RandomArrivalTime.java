package simulation;

public class RandomArrivalTime {
    
    public static double interArrivalTime(double lamda) {
        int m = 65536;
        double Z = (-1/lamda)*Math.log((Math.random()*m+1)/m);
        // return the time(seconds) that next person will arrive
        return T.keepDecimalPlaces(Z, 3);
    }
    
}
