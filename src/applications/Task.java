package applications;

public class Task {

    private int machine;
    private int time;

    // constructor

    public Task(int theMachine, int theTime) {
        machine = theMachine;
        time = theTime;
    }

    public int getTime(){
        return time;
    }

    public int getMachine(){
        return machine;
    }
}
