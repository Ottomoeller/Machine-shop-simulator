package applications;

import dataStructures.LinkedQueue;

public class Machine {

	private LinkedQueue jobQ; // queue of waiting jobs for this machine
	private int changeTime; // machine change-over time
	private int totalWait; // total delay at this machine
	private int numTasks; // number of tasks processed on this machine
	private Job activeJob; // job currently active on this machine
	private static Machine[] machine; // array of machines

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
				machine[theMachine].totalWait += MachineShopSimulator.getTimeNow() - machine[theMachine].activeJob.getArrivalTime();
				machine[theMachine].incrementNumTasks();
				int t = machine[theMachine].activeJob.removeNextTask();
				MachineShopSimulator.getEventList().setFinishTime(theMachine, MachineShopSimulator.getTimeNow() + t);
			}
		} else {// task has just finished on machine[theMachine]
			// schedule change-over time
			lastJob = machine[theMachine].activeJob;
			machine[theMachine].setActiveJob(null);
			MachineShopSimulator.getEventList().setFinishTime(theMachine, MachineShopSimulator.getTimeNow() + machine[theMachine].changeTime);
		}

		return lastJob;
	}


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
