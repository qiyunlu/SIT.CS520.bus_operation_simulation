package simulation;

import java.io.*;
import java.util.*;

public class Main {
    
    // start time
    final static double startTime = 0d;
    // calculate the time simulation ends and keep three decimal places
    final static double endTime = T.keepDecimalPlaces(startTime + Initialization.simulatingTime, 3);
    // set currentTime in simulation
    static double currentTime = startTime;
    
    // the place we store program's output
    final static String detailedOutput = "./output/Detailed output.txt";
    final static String monitorOutput = "./output/Monitor output.txt";
    
    // event queue
    public static Queue<Event> eventQ = new LinkedList<Event>();
    // bus array
    public static Bus[] bus = new Bus[Initialization.busNum];
    // station array
    public static Station[] station = new Station[Initialization.stationNum];
    /* 
     * record array : in order to record every station's max, min, average waiting people
     * record[n][0] : No.n station's total coming people
     * record[n][1] : Times buses came to No.n station
     * record[n][2] : No.n station's max waiting people
     * record[n][3] : No.n station's min waiting people
     */
    public static int[][] record = new int[Initialization.stationNum][4];
    
    
    // start running
    public static void main(String[] args) {
        
        // create files which we store output
        try{
            OutputStream f01 = new FileOutputStream(detailedOutput);
            OutputStream f02 = new FileOutputStream(monitorOutput);
            f01.close();
            f02.close();
        }catch(Exception e){
            System.out.println(e);
        }
        
        // initialize bus array
        for(int i = 0; i < Initialization.busNum; i++) {
            bus[i] = new Bus(i, -1, -1, -1d, -1);
        }
        // initialize station array
        for(int i = 0; i < Initialization.stationNum; i++) {
            station[i] = new Station(i, 0);
        }
        // initialize record array
        for(int i = 0; i < Initialization.stationNum; i++) {
            for(int j = 0; j < 4; j++) {
                if(j == 3) {
                    // initialize min waiting people
                    record[i][j] = T.doubleToInt(Initialization.meanArrivalRate * Initialization.simulatingTime);
                }
                else {
                    // initialize others
                    record[i][j] = 0;
                }
            }
        }
        
        // initialize the map
        // generate one person event for each stop
        for(int i = 0; i < Initialization.stationNum; i++) {
            eventQ.offer(new Event(0, startTime, -1, i));
        }
        // buses distributed uniformly along the route
        double _piece = T.keepDecimalPlaces((double)Initialization.stationNum / (double)Initialization.busNum, 3);
        for(int i = 0; i < Initialization.busNum; i++) {
            double _occurTime = T.keepDecimalPlaces((Math.round(i*_piece+0.499d)-i*_piece)*Initialization.driveTime+currentTime, 3);
            int _stationName = (int) Math.round(i*_piece+0.499d);
            // the bus comes to No.0 station after drove a circle
            if(_stationName == Initialization.stationNum) {
                _stationName = 0;
            }
            eventQ.offer(new Event(1, _occurTime, i, _stationName));
            // update bus state
            bus[i].nextStop = _stationName;
            bus[i].nextArrivalTime = _occurTime;
        }
        // add a monitor to the queue
        eventQ.offer(new Event(3, startTime+Initialization.outputTime, -1, -1));
        // sort eventQ order by startTime ASC, then by eventType (ASC)
        eventQ = ManipulateQueue.bubbleSort(eventQ);
        
        /*
         * stopTime is used for remain the distance between the adjacent buses
         * while stopTime == currentTime, means there are people boarding buses somewhere at this moment
         * while people boards somewhere, all other buses should stop and wait except buses which are also being boarded by people
         */
        double stopTime = -1d;
        
        
        
        // start simulating
        // loop until bus operation simulation ends
        while (currentTime < endTime) {
            // first element of eventQ has smaller(or equal) startTime than any other. So this element starts first.
            Event event = eventQ.poll();
            /*
             * sleep until the time the event occurs
             * if we delete this "try/catch" code, our program will run in highest speed.
             */
            /*
            try {
                Thread.sleep((long)((event.startTime-currentTime)*1000));
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
            */
            currentTime = event.startTime;
            
            // identify what's the type of the event
            if(event.eventType == 0) {
                // a person comes to a station
                station[event.stationName].peopleNum++;
                // record event0 in Detailed output.txt
                Output.addEvent0InD(event, currentTime, station);
                // calculate next arrival and push this new event into event queue
                double _interArrivalTime = RandomArrivalTime.interArrivalTime(Initialization.meanArrivalRate);
                eventQ.offer(new Event(0, currentTime+_interArrivalTime, -1, event.stationName));
                // sort eventQ order by startTime ASC, then order by eventType ASC
                eventQ = ManipulateQueue.bubbleSort(eventQ);
            }
            else if(event.eventType == 1) {
                // a bus arrives a station.
                // update bus's state
                int _busName = event.busName;
                bus[_busName].busState = 1;
                bus[_busName].nextArrivalTime = currentTime;
                // record event1 in Detailed output.txt
                Output.addEvent1InD(event, currentTime);
                // record the state of this station
                int _stationName = event.stationName;
                record[_stationName][0] += station[_stationName].peopleNum;
                record[_stationName][1]++;
                if(record[_stationName][2] < station[_stationName].peopleNum) {
                    record[_stationName][2] = station[_stationName].peopleNum;
                }
                if(record[_stationName][3] > station[_stationName].peopleNum) {
                    record[_stationName][3] = station[_stationName].peopleNum;
                }
                
                if(station[_stationName].peopleNum == 0) {
                    // no people, go next station.
                    // record event10 in Detailed output.txt
                    Output.addEvent10InD(event, currentTime);
                    // push new event into event queue
                    int _nextStationName = _stationName+1;
                    if(_nextStationName == Initialization.stationNum) {
                        _nextStationName = 0;
                    }
                    eventQ.offer(new Event(1, currentTime+Initialization.driveTime, event.busName, _nextStationName));
                    eventQ = ManipulateQueue.bubbleSort(eventQ);
                    //update bus's state
                    bus[_busName].lastStop = event.stationName;
                    bus[_busName].nextStop = _nextStationName;
                    bus[_busName].nextArrivalTime = currentTime+Initialization.driveTime;
                    bus[_busName].busState = 0;
                }
                else {
                    // push boarding event to queue.
                    eventQ.offer(new Event(2, currentTime, event.busName, event.stationName));
                    eventQ = ManipulateQueue.bubbleSort(eventQ);
                }
            }
            else if(event.eventType == 2) {
                // update bus's state
                int _busName = event.busName;
                bus[_busName].busState = 1;
                bus[_busName].nextArrivalTime = currentTime;
                
                if(station[event.stationName].peopleNum == 0) {
                    // all people board. Bus goes next station.
                    // record event20 in Detailed output.txt
                    Output.addEvent20InD(event, currentTime);
                    // push new event into event queue
                    int _nextStationName = event.stationName+1;
                    if(_nextStationName == Initialization.stationNum) {
                        _nextStationName = 0;
                    }
                    
                    double _addedTime = currentTime;
                    
                    ///* delete this section of code will make distance between the adjacent buses not the same
                    if(stopTime == currentTime) {
                        // people are boarding buses somewhere
                        // this _addedTime will be added one boarding time
                        _addedTime += Initialization.boardingTime;
                    }
                    //*/
                    
                    eventQ.offer(new Event(1, _addedTime+Initialization.driveTime, event.busName, _nextStationName));
                    eventQ = ManipulateQueue.bubbleSort(eventQ);
                    // update bus's state
                    bus[_busName].lastStop = event.stationName;
                    bus[_busName].nextStop = _nextStationName;
                    bus[_busName].nextArrivalTime = _addedTime+Initialization.driveTime;
                    bus[_busName].busState = 0;
                }
                else {
                    // people are boarding.
                    station[event.stationName].peopleNum--;
                    
                    ///* delete this section of code will make distance between the adjacent buses not the same
                    // other buses should wait except being boarded ones
                    if(stopTime != currentTime) {
                        // a stopped bus cannot be stopped again
                        eventQ = ManipulateQueue.stopDrive(eventQ);
                        bus = ManipulateQueue.fixBusArrivalTime(eventQ, bus);
                    }
                    // mark that at this moment people are boarding buses somewhere
                    stopTime = currentTime;
                    //*/
                    
                    // record event21 in Detailed output.txt
                    Output.addEvent21InD(event, currentTime, station);
                    // push boarding event to queue.
                    eventQ.offer(new Event(2, currentTime+Initialization.boardingTime, event.busName, event.stationName));
                    eventQ = ManipulateQueue.bubbleSort(eventQ);
                }
            }
            else if(event.eventType == 3) {
                // For a period of time to add buses' and stations' state in Monitor output.txt
                Output.addBSStateInM(currentTime, bus, station, record);
                // add new monitor to event queue
                eventQ.offer(new Event(3, currentTime+Initialization.outputTime, -1, -1));
                eventQ = ManipulateQueue.bubbleSort(eventQ);
            }
        }
        
        // Simulation completes. Print out every station's record
        Queue<String> printList = new LinkedList<String>();
        // add files which you want to add record to
        printList.offer(detailedOutput);
        printList.offer(monitorOutput);
        Output.printByList(record, printList);
        // terminal output
        System.out.println("\n============================== Simulation Complete ==============================");
        for(int i = 0; i < Initialization.stationNum; i++) {
            System.out.println("No."+i+" station");
            System.out.println("The average size of a waiting queue: " + T.keepDecimalPlaces((double)record[i][0]/(double)record[i][1], 3));
            System.out.println("The maximum size of a waiting queue: " + record[i][2]);
            System.out.println("The minimum size of a waiting queue: " + record[i][3]);
            System.out.println("--------------------------------------------------");
        }
        System.out.println("======================================================================");
    }
    
}
