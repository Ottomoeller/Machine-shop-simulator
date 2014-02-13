
package applications;

import dataStructures.LinkedQueue;

public class Job {

    private LinkedQueue taskQ; // this job's tasks
    private int length; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int id; // job identifier

    // constructor
    public Job(int theId) {
        id = theId;
        taskQ = new LinkedQueue();
        // length and arrivalTime have default value 0
    }

    // other methods
    public void addTask(int theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).getTime();
        length += theTime;
        return theTime;
    }

    static boolean moveToNextMachine(Job theJob) {
        if (theJob.taskQ.isEmpty()) {// no next task
            System.out.println("Job " + theJob.id + " has completed at "
                    + MachineShopSimulator.getTimeNow() + " Total wait was " + (MachineShopSimulator.getTimeNow() - theJob.length));
            return false;
        } else {// theJob has a next task
            // get machine for next task
            int p = ((Task) theJob.taskQ.getFrontElement()).getMachine();
            // put on machine p's wait queue
            Machine.getMachine()[p].getJobQ().put(theJob);
            theJob.arrivalTime = MachineShopSimulator.getTimeNow();
            // if p idle, schedule immediately
            isIdle(p);
            return true;
        }
    }

    public static void isIdle(int p){
        if (MachineShopSimulator.getEventList().nextEventTime(p) == MachineShopSimulator.getLargeTime()) {// machine is idle
            Machine.changeState(p);
        }
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

}
