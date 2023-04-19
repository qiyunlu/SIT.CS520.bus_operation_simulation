# SIT CS520 Bus Operation Simulation

## Archive Note
This project is no longer actively maintained. The latest version of the project is v0.1.0-alpha, which was last updated on Jun 22, 2018.

### Simulate a Bus Service

### Three Kinds of Events
##### 1. person: A person arrives in the queue at a bus stop
Action:After a random (exponentially-distributed inter-arrival) time, another person is scheduled to arrive in the queue
##### 2. arrival: A bus arrives at a bus stop
Action: If there is no one in the queue, the bus proceeds to the next stop, and the event of its arrival there is generated; otherwise, the event to be generated (at present time!) is the first person in the queue boarding the bus;
##### 3. boarder: A person boards the bus
Action: The length of the queue diminishes by 1; If the queue is now empty, the bus proceeds to the next stop, otherwise the next passenger boards the bus

### Assumptions
1. It takes everyone the same time to enter the bus
2. As many people (on average) exit the bus as enter it, and the time to exit the bus is negligible. Consequence: we do not consider the exit event in our model
3. The stops are equally spaced in a circle
4. The buses may not pass one another

### The Event Record
This is an entry in the event structure. It contains
1. The time of the event
2. The type of the event
3. The rest of the information, which is event-dependent: the name (number) of the stop, the number of the bus, etc.

### For the beginning, assume there are
* 15 bus stops
* 5 buses
* The time to drive between any two contiguous stops is 5 minutes
* The passenger’s mean arrival rate at each stop is 5 persons/min
* The boarding time is 2 seconds for each passenger
* The total simulation time is 8 hours.

### Start with the buses distributed uniformly along the route (by generating appropriate arrivalevents) and generating one person event for each stop.
### Feel free to change the simulation parameters.
