package simulation;

import java.util.*;

public class ManipulateQueue {
    
    // queue's length
    public static int queueLength = 0;
    
    // get evenQ[num]
    public static Event getElementOfQueue(Queue<Event> eventQ, int num) {
        Event event = new Event(-1, -1d, -1, -1);
        int i = 0;
        for(Event e : eventQ) {
            if(i == num) {
                event.eventType = e.eventType;
                event.startTime = e.startTime;
                event.busName = e.busName;
                event.stationName = e.stationName;
                break;
            }
            i++;
        }
        return event;
    }
    // set evenQ[num] = event
    public static Queue<Event> setElementOfQueue(Queue<Event> eventQ, int num, Event event) {
        int i = 0;
        Queue<Event> eventCopy = new LinkedList<Event>();
        for(Event e : eventQ) {
            if(i == num) {
                eventCopy.offer(event);
            }
            else {
                eventCopy.offer(e);
            }
            i++;
        }
        return eventCopy;
    }
    
    // bubble sort by startTime (ASC), then by eventType (ASC)
    public static Queue<Event> bubbleSort(Queue<Event> eventQ) {
        queueLength = 0;
        // get eventQ's length
        for(Event e : eventQ) {
            queueLength++;
        }
        for(int i = 0; i < queueLength-1; i++) {
            int _flag = 0;
            Event _event = null;
            for(int j = 0; j < queueLength-1; j++) {
                if(getElementOfQueue(eventQ, j).startTime > getElementOfQueue(eventQ, j+1).startTime) {
                    _event = getElementOfQueue(eventQ, j+1);
                    eventQ = setElementOfQueue(eventQ, j+1, getElementOfQueue(eventQ, j));
                    eventQ = setElementOfQueue(eventQ, j, _event);
                    _flag = 1;
                }
                else if(getElementOfQueue(eventQ, j).startTime == getElementOfQueue(eventQ, j+1).startTime) {
                    if(getElementOfQueue(eventQ, j).eventType > getElementOfQueue(eventQ, j+1).eventType) {
                        _event = getElementOfQueue(eventQ, j+1);
                        eventQ = setElementOfQueue(eventQ, j+1, getElementOfQueue(eventQ, j));
                        eventQ = setElementOfQueue(eventQ, j, _event);
                        _flag = 1;
                    }
                }
            }
            if(_flag == 0) {
                // _flag == 0 means no order change in the queue, sort ends
                break;
            }
        }
        return eventQ;
    }
    
    // Stop buses which are driving on the road(eventType == 1)
    public static Queue<Event> stopDrive(Queue<Event> eventQ) {
        Queue<Event> eventCopy = new LinkedList<Event>();
        for(Event e : eventQ) {
            if(e.eventType == 1) {
                eventCopy.offer(new Event(e.eventType, e.startTime+Initialization.boardingTime, e.busName, e.stationName));
            }
            else {
                eventCopy.offer(e);
            }
        }
        return ManipulateQueue.bubbleSort(eventCopy);
    }
    
    // to fix buses' nextArrivalTime which is confused by stopDrive method
    public static Bus[] fixBusArrivalTime(Queue<Event> eventQ, Bus[] bus) {
        for(Event e : eventQ) {
            if(e.eventType == 1) {
                bus[e.busName].nextArrivalTime = e.startTime;
            }
        }
        return bus;
    }
    
}
