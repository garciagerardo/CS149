package p2;

import java.util.*;

/**
* Round Robin scheduling algorithm
*/
public class RoundRobin {
	private final static int MAX_QUANTA_RUN_TIME = 100;
	private final static int TIME_SLICE = 1;
	private static int timeCounter;
	
	private Queue<Process> queue;
	private ArrayList<Process> processes;
	
	private String quantaScale;
	private String processesInformation;
	
	/**
	 * Constructor for RR takes the incoming processes and sorts based on arrival time. 
	 * @param processes - a list of randomly generated processes.
	 */
	public RoundRobin(ArrayList<Process> someProcesses) {
		processesInformation = "";
		quantaScale = "";
		processes = someProcesses;
		Collections.sort(this.processes, new Comparator<Process>() {
			public int compare(Process o1, Process o2) {
				return o1.getArrivalTime() < o2.getArrivalTime() ? -1 :
					 o1.getArrivalTime() > o2.getArrivalTime() ? 1: 0;
			}
		});
		queue = new LinkedList<Process>();
		for(Process proc: this.processes){
			
			queue.add(proc);
			this.processesInformation += this.printProcessesInfo(proc);
		}
	}
	
	/**
	 * Runs Round Robin on list of processes and logs the information
	 */
	public void run() {
		timeCounter = 0;
		Process current = null;
		boolean starved = false; 
		boolean finish = false;
		Queue<Process> readyQueue = new LinkedList<Process>();
		while (!finish) {
			// We use starve to kill any inactive processes in readyQueue and the boolean acts as a checker
			starved = checkStarve(processes,readyQueue,timeCounter, MAX_QUANTA_RUN_TIME, starved);
			// If starved, we do not add the processes from queue to readyQueue because the current time is already over 100 quanta
			if(!starved) {
				// We are using a loop instead of poll because there can be more than 1 processes those have arrived i.e. 1.2, 1.3. 1.5 have arrived when current time is 2 
				for(Process process: queue) {
					if(process.getArrivalTime() <= timeCounter) {
						readyQueue.add(process);
					} else {
						break;
					}
				}
				// We remove all processes from queue
				queue.removeIf((process) -> (process.getArrivalTime() <= timeCounter));
			}
			if (current != null) {
				if (current.getExpectedRunTime() > 0) {
					readyQueue.add(current);
				} else {
					current.setFinishTime(timeCounter);
					}
				current = null;
			}
			
			if((!starved && queue.isEmpty() && readyQueue.isEmpty() && current == null)|| 
					(starved && readyQueue.isEmpty() && current == null)) {
				finish = true;
			}
			if (current == null && !readyQueue.isEmpty()) {
				current = readyQueue.poll();
				if(current.getStartTime() < 0) {
					current.setStartTime(timeCounter);
				}
				current.setExpectedRunTime(current.getExpectedRunTime() - TIME_SLICE);
				this.quantaScale += timeCounter + ": " + current.getProcessNumber() + "\n";
			} else if (!finish) {
				this.quantaScale += timeCounter + ": Waiting for a process\n";
			}
			if(!finish) 
				timeCounter += TIME_SLICE;
		}
		current = null;
		System.out.println(this.processesInformation);
		System.out.println(this.quantaScale);
	}
	
	/**
	 * Calculates and logs the statistics of Round Robin
	 */
	public void showCalculation() {
		float totalTurnaroundTime = 0;
		float totalWaitTime = 0;
		float totalResponseTime = 0;
		for(Process process: processes) {
			totalTurnaroundTime += process.getFinishTime() - process.getArrivalTime();
			totalWaitTime += (process.getFinishTime() - process.getArrivalTime()) - process.getExpectedRunTimeForCal();
			totalResponseTime += process.getStartTime() - process.getArrivalTime();
		}
		float averageTurnaroundTime = 0;
		float averageWaitTime = 0;
		float averageResponseTime = 0;
		float throughput = 0;
		
		if(processes.size() > 0) {
			averageTurnaroundTime = totalTurnaroundTime / processes.size();
			averageWaitTime = totalWaitTime / processes.size();
			averageResponseTime = totalResponseTime / processes.size();
			if(timeCounter > 0)
				throughput = (float)processes.size() / timeCounter;
		}
		
		System.out.println("Average turnaround time = " + averageTurnaroundTime);
		System.out.println("Average waiting time = " + averageWaitTime);
		System.out.println("Average response time = " + averageResponseTime);
		System.out.println("Throughput = " + throughput);
	}
	
	/**
     * Removes starved processes, processes not started, from readyQueue and processes and sets starved to true
     * @param processes - collection of processes
     * @param readyQueue - ready processes queue
     * @param currentTime - the current quanta time
     * @param quanta - the time when starved processes are ignored
     * @param starved - the boolean to check if starved 
     * @return true for starve if a purge occurs else false
     */
	public boolean checkStarve(ArrayList<Process> processes, Queue<Process> readyQueue, int currentTime, int quanta, boolean starved) {
		if(!starved && currentTime >= quanta) {
			readyQueue.removeIf((process) -> (process.getStartTime() < 0));
            processes.removeIf((process) -> (process.getStartTime() < 0));
            starved = true;
        }
        return starved;
	}
	
	 /**
     * After running the algorithm, print information about the process that was run. 
     * @param currentProcess - the process to print information about 
     * @return information on that process
     */
    private String printProcessesInfo(Process currentProcess) 
    {
        int processNumber = currentProcess.getProcessNumber(); 
        float arrivalTime = currentProcess.getArrivalTime();
        float runTime = currentProcess.getExpectedRunTime();
        int priority = currentProcess.getPriority();
        
        String processInfo = "\n" + "Process " + processNumber +
                " \n" + "Arrival Time of this process is: " + arrivalTime +
                " \n" + "Expected Run Time is: " + runTime +
                " \n" + "Priority " + priority + "\n";
        
    		return processInfo;
    }
	
}