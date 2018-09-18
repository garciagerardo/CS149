package p2;

import java.util.ArrayList;
import java.util.Random;


//Process class will simulate different processes

public class Process {

//Variables
    private float arrivalTime;
    private int priority;
    private float burstTime;
    private int processNumber;	// pid
    private float startTime;
    private float finishTime;
    private float expectedRunTime;
    private final float expectedRunTimeForCal;
    private static Random randomGenerator = new Random(0);
    public final static int MAXIMUM_QUANTA_TIME = 99;
    public final static int MINIMUM_QUANTA_TIME = 0;
    public final static float MINIMUM_TOTAL_RUN_TIME = (float) 0.1;
    public final static float MAXIMUM_TOTAL_RUN_TIME = 10;

    public Process(int aProcessNumber) {
        arrivalTime = generateRandomArrivalTime();
        expectedRunTime = generateRandomExpectedRunTime();
        expectedRunTimeForCal = expectedRunTime;
        priority = generateRandomPriority();
        processNumber = aProcessNumber;
        startTime = -1;
        finishTime = -1;
        burstTime = expectedRunTime;
    }

   //This is for random time between 0 to 99.
     
    private float generateRandomArrivalTime() {
        return MAXIMUM_QUANTA_TIME * randomGenerator.nextFloat();
    }

  //This is for random time between 0.1 to 10.
   
    private float generateRandomExpectedRunTime() {
        return MINIMUM_TOTAL_RUN_TIME + (MAXIMUM_TOTAL_RUN_TIME - MINIMUM_TOTAL_RUN_TIME) * randomGenerator.nextFloat();
    }


  //This generates a priority for random integer between 1 to 4.
   
    private int generateRandomPriority() {
        return randomGenerator.nextInt(4) + 1;
    }

   
 // This generates a list of processes
     
    public static ArrayList<Process> generateProcesses(int numOfProcesses) {
        ArrayList<Process> randomProcesses = new ArrayList<>();
        for(int i = 1; i <= numOfProcesses; i++) {
            randomProcesses.add(new Process(i));
        }
        return randomProcesses;
    }

    public float getArrivalTime() {
        return arrivalTime;
    }

    //This sets the expected run time of the process
     
    public void setExpectedRunTime(float time) {
        expectedRunTime = time;
    }

   //This returns expected run time of a process
   
    public float getExpectedRunTime() {
        return expectedRunTime;
    }

    //This returns initial expected run time of a process 
     
    public float getExpectedRunTimeForCal() {
        return expectedRunTimeForCal;
    }
    
    public float getBurstTime() {
    	return burstTime;
    }
    
    public int getPriority() {
        return priority;
    }

     //This returns process number of a Process.
    
    public int getProcessNumber() {
        return processNumber;
    }

     // Sets  start time of a Process
   
    public void setStartTime(float startTime){
        this.startTime = startTime;
    }

    // Returns start time of a Process
    public float getStartTime() {
        return startTime;
    }

    public void setFinishTime(float finishTime) {
        this.finishTime = finishTime;
    }
    public float getFinishTime() {
        return finishTime;
    }
    public void decBurstTime() {
    	
    	if(burstTime <  1) {
    		burstTime = 0;
    	}
    	else {
        	burstTime--;
    	}
    }
    
    public static void resetRandomGenerator(){
    	randomGenerator = new Random(0);
    }
}