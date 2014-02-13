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

    static Job changeState(int theMachine) {
        Job lastJob;
        if (machine[theMachine].activeJob == null) {
            // state
            lastJob = null;
            workOnJob(theMachine);
        } else {
            lastJob = machine[theMachine].activeJob;
            machine[theMachine].activeJob = null;
            MachineShopSimulator.getEventList().setFinishTime(theMachine, Job.getTimeNow() + machine[theMachine].changeTime);
        }
        return lastJob;
    }
    
    private static void workOnJob(int theMachine){
        if (machine[theMachine].jobQ.isEmpty()) 
            MachineShopSimulator.getEventList().setFinishTime(theMachine, MachineShopSimulator.getLargeTime());
        else {
            machine[theMachine].activeJob = (Job) machine[theMachine].jobQ.remove();
            machine[theMachine].totalWait += Job.getTimeNow() - machine[theMachine].activeJob.getArrivalTime();
            machine[theMachine].numTasks++;
            int t = machine[theMachine].activeJob.removeNextTask();
            MachineShopSimulator.getEventList().setFinishTime(theMachine, Job.getTimeNow() + t);
        }
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


        MachineShopSimulator.setEventList(new EventList(numMachines, MachineShopSimulator.getLargeTime()));
        machine = new Machine[numMachines+1];
        for (int i = 1; i <= numMachines; i++){
            machine[i] = new Machine();
        }
    }

    public static void changeOverInput(MyInputStream keyboard){
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= numMachines; j++) {
            int ct = keyboard.readInteger();
            if (ct < 0)
                throw new MyInputException(minimumChangeOverError);
            machine[j].changeTime = ct;
        }
    }


    public static void jobInput(MyInputStream keyboard){
        Job theJob;
        for (int i = 1; i <= numJobs; i++) {
            System.out.println("Enter number of tasks for job " + i);
            int tasks = keyboard.readInteger();
            if (tasks < 1){
                throw new MyInputException(minimumTaskError);
            }
            
            theJob = new Job(i);
            createTask(keyboard, tasks, theJob);
        }
    }
    
    private static void createTask(MyInputStream keyboard, int tasks, Job theJob){
        System.out.println("Enter the tasks (machine, time)"
                + " in process order");
        int firstMachine = 0; 
        for (int j = 1; j <= tasks; j++) {
            int theMachine = keyboard.readInteger();
            int theTaskTime = keyboard.readInteger();
            numOrTimeErrorCheck(theMachine, theTaskTime);
            if (j == 1){
                firstMachine = theMachine; 
            }
            theJob.addTask(theMachine, theTaskTime);
        }
        machine[firstMachine].jobQ.put(theJob);
    }

    private static void numOrTimeErrorCheck(int theMachine, int theTaskTime){
        if (theMachine < 1 || theMachine > numMachines || theTaskTime < 1){
            throw new MyInputException(invalidNumberOrTimeError);
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
