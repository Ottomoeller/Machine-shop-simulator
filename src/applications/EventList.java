package applications;

public class EventList {
    public static final String minimumMachineNumError = "number of machines must be >= 1";

    int[] finishTime; // finish time array

    // constructor
    public EventList(int theNumMachines, int theLargeTime) {
        if (theNumMachines < 1)
            throw new IllegalArgumentException(minimumMachineNumError);
        finishTime = new int[theNumMachines + 1];

        for (int i = 1; i <= theNumMachines; i++)// all machines are idle, initialize with // large finish time
            finishTime[i] = theLargeTime;
    }

    /** @return machine for next event */
    public int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int p = 1;
        int t = finishTime[1];
        for (int i = 2; i < finishTime.length; i++)
            if (finishTime[i] < t) {// i finishes earlier
                p = i;
                t = finishTime[i];
            }
        return p;
    }

    public int nextEventTime(int theMachine) {
        return finishTime[theMachine];
    }

    public void setFinishTime(int theMachine, int theTime) {
        finishTime[theMachine] = theTime;
    }
}
