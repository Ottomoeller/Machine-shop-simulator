package applications;

import dataStructures.LinkedQueue;

public class Machine {

    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine
    private static Machine[] machine; // array of machines

    // constructor
    public static Machine[] getMachine(){
        return machine;
    }
    
    public static void setMachine(int numMachine){
        machine = new Machine[numMachine + 1];
    }
    
    public Machine() {
        jobQ = new LinkedQueue();
    }
    
    public void incrementNumTasks(){
         numTasks++;
    }
    
    public int getNumTasks(){
        return numTasks;
    }
    
    public int getChangeTime(){
        return changeTime;
    }
    
    public void setChangeTime(int time){
        changeTime = time;
    }
    
    public Job getActiveJob(){
        return activeJob;
    }
    
    public void setActiveJob(Job job){
        activeJob = job;
    }
    
    public LinkedQueue getJobQ(){
        return jobQ;
    }
    
    public int getTotalWait(){
        return totalWait;
    }
    
    public void addTotalWait(int waitTime){
        totalWait += waitTime;
    }
    
   
}
