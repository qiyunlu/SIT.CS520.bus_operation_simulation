package simulation;

public class T {
    // this tool class is for method and format that hard to classify
    
    // num keeps N decimal places
    public static double keepDecimalPlaces(double num, int N) {
        return (double) (Math.round(num * Math.pow(10, N)) / Math.pow(10, N));
    }
    
    // double to int
    public static int doubleToInt(double num) {
        return (new Double(num)).intValue();
    }
    
    //change running seconds to normal time
    public static String timePoint(double time) {
        int day = 0;
        int hour = 0;
        int minute = 0;
        double second = 0;
        int t = 0;
        
        // calculate seconds
        t = doubleToInt(time);
        second = keepDecimalPlaces(time - t + t % 60, 3);
        t = t / 60;
        // calculate minutes
        minute = t % 60;
        t = t / 60;
        // calculate hours
        hour = t % 24;
        t = t / 24;
        // days
        day = t;
        
        return "Time : "+hour+":"+minute+":"+second+"  day "+day;
    }
    
    
    
    
    
    
    
    
    
}
