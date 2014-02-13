package applications;

import utilities.MyInputStream;
import dataStructures.LinkedQueue;
import exceptions.MyInputException;

public class Machine {

    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine
    private static Machine[] machine; // array of machines
    private static int numMachines; // number of machines
    public static final String minimumMachineOrJobError = "number of machines and jobs must be >= 1";
    public static final String invalidNumberOrTimeError = "bad machine number or task time";
    public static final String minimumChangeOverError = "change-over time must be >= 0";
    public static final String minimumTaskError = "each job must have >= 1 task";
    private static int numJobs; // number of jobs


    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */

    static Job changeState(int theMachine) {// Task on theMachine has finished,
        // schedule next one.
        Job lastJob;
        if (machine[theMachine].activeJob == null) {// in idle or change-over
            // state
            lastJob = null;
            // wait over, ready for new job
            if (machine[theMachine].jobQ.isEmpty()) // no waiting job
                MachineShopSimulator.getEventList().setFinishTime(theMachine, MachineShopSimulator.getLargeTime());
            else {// take job off the queue and work on it
                machine[theMachine].activeJob = (Job) machine[theMachine].jobQ.remove();
                machine[theMachine].totalWait += Job.getTimeNow() - machine[theMachine].activeJob.getArrivalTime();
                machine[theMachine].numTasks++;
                int t = machine[theMachine].activeJob.removeNextTask();
                MachineShopSimulator.getEventList().setFinishTime(theMachine, Job.getTimeNow() + t);
            }
        } else {// task has just finished on machine[theMachine]
            // schedule change-over time
            lastJob = machine[theMachine].activeJob;
            machine[theMachine].activeJob = null;
            MachineShopSimulator.getEventList().setFinishTime(theMachine, Job.getTimeNow() + machine[theMachine].changeTime);
        }

        return lastJob;
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + Job.getTimeNow());
        for (int p = 1; p <= numMachines; p++) {
            System.out.println("Machine " + p + " completed "
                    + machine[p].numTasks + " tasks");
            System.out.println("The total wait time was "
                    + machine[p].totalWait);
            System.out.println();
        }
    }

    /** input machine shop data */
    static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();
        machineJobInput(keyboard);
        changeOverInput(keyboard);
        jobInput(keyboard);
    }

    public static void machineJobInput(MyInputStream keyboard){
        System.out.println("Enter number of machines and jobs");
        numMachines = keyboard.readInteger();
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1){
            throw new MyInputException(minimumMachineOrJobError);
        }

        // create event and machine queues
        MachineShopSimulator.setEventList(new EventList(numMachines, MachineShopSimulator.getLargeTime()));
        machine = new Machine[numMachines+1];
        for (int i = 1; i <= numMachines; i++){
            machine[i] = new Machine();
        }
    }

    public static void changeOverInput(MyInputStream keyboard){
        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= numMachines; j++) {
            int ct = keyboard.readInteger();
            if (ct < 0)
                throw new MyInputException(minimumChangeOverError);
            machine[j].changeTime = ct;
        }
    }


    public static void jobInput(MyInputStream keyboard){
        // input the jobs
        Job theJob;
        for (int i = 1; i <= numJobs; i++) {
            System.out.println("Enter number of tasks for job " + i);
            int tasks = keyboard.readInteger(); // number of tasks
            int firstMachine = 0; // machine for first task
            if (tasks < 1){
                throw new MyInputException(minimumTaskError);
            }

            // create the job
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            for (int j = 1; j <= tasks; j++) {// get tasks for job i
                int theMachine = keyboard.readInteger();
                int theTaskTime = keyboard.readInteger();
                if (theMachine < 1 || theMachine > numMachines || theTaskTime < 1){
                    throw new MyInputException(invalidNumberOrTimeError);
                }
                if (j == 1){
                    firstMachine = theMachine; // job's first machine
                }
                theJob.addTask(theMachine, theTaskTime); // add to
            } // task queue
            machine[firstMachine].jobQ.put(theJob);
        }
    }

    /** load first jobs onto each machine */
    static void startShop() {
        for (int p = 1; p <= numMachines; p++){
            changeState(p);
        }
    }

    public static Machine[] getMachine(){
        return machine;
    }

    public Machine() {
        jobQ = new LinkedQueue();
    }

    public LinkedQueue getJobQ(){
        return jobQ;
    }


    public static int getNumJobs(){
        return numJobs;
    }

    public static void decrementNumJobs(){
        numJobs--;
    }
}
