package simulation;

import java.io.*;
import java.text.*;
import java.util.*;

public class Output {
    
    /*
     * copy from "http://blog.csdn.net/malik76/article/details/6408726/"
     * to add content(String) in route(a file)
     */
    public static void export(String route, String content) {
        FileWriter writer = null;
        try {
            // open a new writer. "true" means we add things behind.
            writer = new FileWriter(route, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // add event0 in Detailed output.txt
    public static void addEvent0InD(Event event, double currentTime, Station[] station) {
        final String dO = Main.detailedOutput;
        export(dO, T.timePoint(currentTime)+"\n");
        export(dO, "One person comes to No."+event.stationName+" station.\n");
        export(dO, "Now there are "+station[event.stationName].peopleNum+" people at this station.\n");
        export(dO, "--------------------------------------------------\n");
    }
    // add event1 in Detailed output.txt
    public static void addEvent1InD(Event event, double currentTime) {
        final String dO = Main.detailedOutput;
        export(dO, T.timePoint(currentTime)+"\n");
        export(dO, "No."+event.busName+" bus arrives No."+event.stationName+" station.\n");
        export(dO, "--------------------------------------------------\n");
    }
    // add event10 in Detailed output.txt
    public static void addEvent10InD(Event event, double currentTime) {
        final String dO = Main.detailedOutput;
        export(dO, T.timePoint(currentTime)+"\n");
        export(dO, "No people at No."+event.stationName+" station.\n");
        export(dO, "No."+event.busName+" bus leaves.\n");
        export(dO, "--------------------------------------------------\n");
    }
    // add event20 in Detailed output.txt
    public static void addEvent20InD(Event event, double currentTime) {
        final String dO = Main.detailedOutput;
        export(dO, T.timePoint(currentTime)+"\n");
        export(dO, "All people at No."+event.stationName+" station board No."+event.busName+" bus.\n");
        export(dO, "Bus leaves.\n");
        export(dO, "--------------------------------------------------\n");
    }
    // add event21 in Detailed output.txt
    public static void addEvent21InD(Event event, double currentTime, Station[] station) {
        final String dO = Main.detailedOutput;
        export(dO, T.timePoint(currentTime)+"\n");
        export(dO, "One person is boarding No."+event.busName+" bus at No."+event.stationName+" station.\n");
        export(dO, "Still have "+station[event.stationName].peopleNum+" people at this station.\n");
        export(dO, "--------------------------------------------------\n");
    }
    
    // For a period of time to add buses' and stations' state in Monitor output.txt
    public static void addBSStateInM(double currentTime, Bus[] bus, Station[] station, int[][] record) {
        final String mO = Main.monitorOutput;
        export(mO, "\n============================== Monitor Output ==============================\n");
        export(mO, T.timePoint(currentTime)+"\n");
        // bus state
        export(mO, "==================== Bus ====================\n");
        for(int i = 0; i < Initialization.busNum; i++) {
            export(mO, "No."+i+" bus\n");
            if(bus[i].busState == 0) {
                double _nextArrTime = T.keepDecimalPlaces(bus[i].nextArrivalTime - currentTime, 3);
                export(mO, "Bus is driving from No."+bus[i].lastStop+" station to No."+bus[i].nextStop+" station.\n");
                export(mO, "After "+_nextArrTime+" seconds the bus will arrive next station.\n");
            }
            else if(bus[i].busState == 1) {
                export(mO, "Bus is stop at No."+bus[i].nextStop+" station.\n");
                export(mO, "People is boarding the bus.\n");
            }
            export(mO, "--------------------------------------------------\n");
        }
        // station state
        export(mO, "==================== Station ====================\n");
        for(int i = 0; i < Initialization.stationNum; i++) {
            export(mO, "No."+i+" station\n");
            export(mO, "The average size of a waiting queue: " + T.keepDecimalPlaces((double)record[i][0]/(double)record[i][1], 3)+"\n");
            export(mO, "The maximum size of a waiting queue: " + record[i][2]+"\n");
            export(mO, "The minimum size of a waiting queue: " + record[i][3]+"\n");
            export(mO, "--------------------------------------------------\n");
        }
        // draw a simple graphic
        export(mO, "==================== Graphic ====================\n");
        drawGraphics(bus, station);
        export(mO, "======================================================================\n\n");
    }
    // draw a simple graphic of bus and station state in Monitor output.txt
    public static void drawGraphics(Bus[] bus, Station[] station) {
        final String mO = Main.monitorOutput;
        
        // draw the road
        export(mO, "Station   ");
        for(int i = 0; i < Initialization.stationNum; i++) {
            export(mO, new DecimalFormat("000").format(i)+"-----");
        }
        export(mO, "000\n");
        // people at every station
        export(mO, "People    ");
        for(int i = 0; i < Initialization.stationNum; i++) {
            export(mO, new DecimalFormat("000").format(station[i].peopleNum)+"     ");
        }
        export(mO, new DecimalFormat("000").format(station[0].peopleNum)+"\n\n");
        // print buses situation
        for(int i = 0; i < Initialization.busNum; i++) {
            export(mO, "Bus "+new DecimalFormat("000").format(i)+"   ");
            if(bus[i].busState == 0) {
                int _t = bus[i].lastStop;
                for(int j = 0; j < Initialization.stationNum; j++) {
                    if(j == _t) {
                        export(mO, " |  ==> ");
                    }
                    else {
                        export(mO, " |      ");
                    }
                }
                export(mO, " | \n");
            }
            else {
                int _t = bus[i].nextStop;
                for(int j = 0; j < Initialization.stationNum; j++) {
                    if(j == _t) {
                        export(mO, "STP     ");
                    }
                    else {
                        export(mO, " |      ");
                    }
                }
                export(mO, " | \n");
            }
        }
    }
    
    
    public static void printByList(int[][] record, Queue<String> printList) {
        for(String PL : printList) {
            export(PL, "\n============================== Simulation Complete ==============================\n");
            for(int i = 0; i < Initialization.stationNum; i++) {
                export(PL, "No."+i+" station\n");
                export(PL, "The average size of a waiting queue: " + T.keepDecimalPlaces((double)record[i][0]/(double)record[i][1], 3)+"\n");
                export(PL, "The maximum size of a waiting queue: " + record[i][2]+"\n");
                export(PL, "The minimum size of a waiting queue: " + record[i][3]+"\n");
                export(PL, "--------------------------------------------------\n");
            }
            export(PL, "======================================================================\n");
        }
    }
        
}
