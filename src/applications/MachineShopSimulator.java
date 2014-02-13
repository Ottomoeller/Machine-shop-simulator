/** machine shop simulation */

package applications;

import utilities.MyInputStream; 
import dataStructures.LinkedQueue;
import exceptions.MyInputException;

public class MachineShopSimulator {
    // These Error messages because the test assume they will be here.  
    public static final String minimumMachineOrJobError = "number of machines and jobs must be >= 1";
    public static final String invalidNumberOrTimeError = "bad machine number or task time";
    public static final String minimumChangeOverError = "change-over time must be >= 0";
    public static final String minimumTaskError = "each job must have >= 1 task";
    
    public static void main(String[] args) {
        largeTime = Integer.MAX_VALUE;
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        Job.setTimeNow(0);
        Machine.inputData(); // get machine and job data
        Machine.startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        Machine.outputStatistics(); // output machine wait times
    }

    // data members of MachineShopSimulator
    private static EventList eList; // pointer to event list

    private static int largeTime; // all machines finish before this


    public static EventList getEventList(){
        return eList;
    }   
    
    public static void setEventList(EventList event){
        eList = event;
    }

    public static int getLargeTime(){
        return largeTime;
    }


    /** process all jobs to completion */
    static void simulate() {
        while (Machine.getNumJobs() > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            Job.setTimeNow(eList.nextEventTime(nextToFinish));
            // change job on machine nextToFinish
            Job theJob = Machine.changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !Job.moveToNextMachine(theJob))
                Machine.decrementNumJobs();
        }
    }

}
