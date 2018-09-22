package p2;

import java.util.*;

/**
 * Shortest remaining time (SRT) is a preemptive scheduling algorithm that has a list
 * of processes that are ready and running. If a job arrives and has a shorter
 * time to complete than the remaining time on the current job, it immediately preempts
 * the CPU.
 */
public class SRT {
	/*** Instance variables ***/
    private final static int MAX_QUANTA_RUN_TIME = 100;
    private int processesCompleted;
    private Queue<Process> queue;
    private ArrayList<Process> processes;
    private ArrayList<Process> readyProcesses;
    // statistics for averages
    private float turnaroundTime;
    private float waitingTime;
    private float responseTime;
    /***********************/
    
    /**
     * Takes a list of processes and adds them to process queue
     */
    public SRT(ArrayList<Process> processes) {
        this.processes = processes;
       
        // sort by arrival time then expected runtime
        Collections.sort(this.processes, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.getArrivalTime() < p2.getArrivalTime()) {
                    return -1;
                }
                else if (p1.getArrivalTime() > p2.getArrivalTime()) {
                    return 0;
                }
                else {
                    if (p1.getExpectedRunTime() > p2.getExpectedRunTime()) {
                        return 0;
                    }
                    return -1;
                }
            }
        });
        
        queue = new LinkedList<Process>();
        // add all the processes to process queue
        for (Process p : this.processes) {
            queue.add(p);
        }
    }
   
    
    /**
     * Returns a process from the ready queue with the lowest burst time
     */
    public Process getProcessRQ() {
        Process current = null;
        int index = 0;
        for (int i = 0; i < readyProcesses.size(); i++) {
            if (current == null) {
                current = readyProcesses.get(i);
                index = i;
            } else if (current.getBurstTime() > readyProcesses.get(i).getBurstTime()) {
                current = readyProcesses.get(i);
                index = i;
            }
        }
        readyProcesses.remove(index);
        return current;
    }
    public String printProcessesInfo(Process currProcess) {
        return "\nProcess " + currProcess.getProcessNumber() + " \nArrival Time of this process is: "
                + currProcess.getArrivalTime() + " \nExpected Run Time is: " + currProcess.getExpectedRunTime()
                + " \nPriority " + currProcess.getPriority() + "\n";
    }

    /**
     * Run the SRT algorithm and output statistics
     */
    public void run() {
    	// current process
        Process current = null;
        // readyProcesses: ready and waiting processes
        readyProcesses = new ArrayList<Process>();
        String processHistory = "";
        // expected values print
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("Process #" + processes.get(i).getProcessNumber());
            System.out.println("Priority: " + processes.get(i).getPriority() + "\n");
            System.out.println("Arrival Time: " + processes.get(i).getArrivalTime());
            System.out.println("Expected Runtime: " + processes.get(i).getExpectedRunTime());
        }
        // processing system
        for (int t = 0; t <= 100; t++) {
            System.out.println("Time: " + t);
            
            // checks for null pointer and checks if everyone's arrived
            if (!queue.isEmpty()) { 
            		//if current is null and process has arrived
                if (current == null & queue.peek().getArrivalTime() <= t) {
                	//current is the arrived from the queue list
                    current = queue.poll();
                    current.setStartTime(t);
                //if current is not null check who's arrived
                }
                else if ((queue.peek().getArrivalTime() <= t)) {
                	//checks that current is replaced and how lower burst time
                    if (current.getBurstTime() > queue.peek().getBurstTime()) {
                        //add current to readyProcesses that is waiting
                        readyProcesses.add(current);
                        //set current as running process
                        current = queue.poll();
                        current.setStartTime(t);
                    }
                    // if the older one has a lower burst time 
                    else if (current.getBurstTime() <= queue.peek().getBurstTime()) {
                    		//new process added to readyProcesses that is waiting
                        readyProcesses.add(queue.poll());
                    }
                }
            }
            if (current == null) {
                processHistory = processHistory + "NULL ";
            }
            else {
                processHistory = processHistory + current.getProcessNumber() + " ";
            }
            //decrement only if it's not null
            if (current != null) {
                System.out.println("Process #" + current.getProcessNumber());
                current.decBurstTime();
                
                // gather process information after it is finished (when burst time = 0)
                if (current.getBurstTime() == 0) {
                    this.turnaroundTime += (t + 1) - current.getArrivalTime();
                    
                    this.waitingTime += ((t + 1) - current.getArrivalTime()) - (current.getExpectedRunTime());               
                    
                    this.responseTime += current.getStartTime() - current.getArrivalTime();
                    
                    this.processesCompleted++;
                    //check arrived has anybody waiting
                    if (readyProcesses.size() == 0) {
                        current = null; //no one is waiting. current is null
                        
                    }
                    else {
                    	//process is waiting. get process with lowest burst time
                        current = getProcessRQ();
                        //process never ran and is in queue
                        if(current.getBurstTime() == current.getExpectedRunTime()) {
                            current.setStartTime(t);
                        }
                    }
                }         
            }
        }

        System.out.println(processHistory);
        System.out.println("\nAverage turnaround time: " + this.turnaroundTime / this.processesCompleted);
        System.out.println("Average wait time: " + this.waitingTime / this.processesCompleted);
        System.out.println("Average response time: " + this.responseTime / this.processesCompleted);
    }

} // end of class SRT