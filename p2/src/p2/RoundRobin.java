package p2;

import java.util.*;

public class RoundRobin {

	// Private variables for the RoundRobin algorithm
	private final static int MAX_QUANTA_RUN_TIME = 100;
	private final static int TIMESLICE = 1; // 1 quantum
	private static int timeCounter;
	private Queue<Process> queue;
	private ArrayList<Process> allProcesses;
	private String processesInformation;
	private String quantaScale;

	/**
	 * Constructor for RoundRobin takes incoming processes as input and sorts based on their arrival time
	 * @param allProcesses - list of randomly generated processes
	 */
	public RoundRobin(ArrayList<Process> someProcesses) {
		processesInformation = "";
		quantaScale = "";
		allProcesses = someProcesses;

		Collections.sort(this.allProcesses, new Comparator<Process>() {
			public int compare(Process o1, Process o2) {
				if (o1.getArrivalTime() < o2.getArrivalTime()) {
					return -1;
				} else if (o1.getArrivalTime() > o2.getArrivalTime()) {
					return 1;
				} else
					return 0;
			}
		});

		queue = new LinkedList<Process>();
		for (Process proc : this.allProcesses) {

			queue.add(proc);
			this.processesInformation += this.printProcessesInfo(proc);
		}
	}

	/**
	 * Runs the algorithm on a list of processes; outputs info about run
	 */
	public void run() {
		timeCounter = 0;
		Process current = null;
		boolean starved = false; // Starve kills the inactive processes in the ready Queue
		boolean finished = false; // The Boolean makes certain no process is being starved
		Queue<Process> ready = new LinkedList<Process>();
		while (!finished) {
			starved = checkStarve(allProcesses, ready, timeCounter, MAX_QUANTA_RUN_TIME, starved);
			if (!starved) { // If any process is starved, current time exceeded 100 quanta. Don't add any
							// more processes from queue to ready
				for (Process p : queue) {
					if (p.getArrivalTime() <= timeCounter) {
						ready.add(p);
					} else {
						break;
					}
				}
				queue.removeIf((process) -> (process.getArrivalTime() <= timeCounter)); // Empty the queue
			}
			if (current != null) {
				if (current.getExpectedRunTime() > 0) {
					ready.add(current);
				} else {
					current.setFinishTime(timeCounter);
				}
				current = null;
			}

			if ((!starved && queue.isEmpty() && ready.isEmpty() && current == null)
					|| (starved && ready.isEmpty() && current == null)) {
				finished = true;
			}
			if (current == null && !ready.isEmpty()) {
				current = ready.poll();
				if (current.getStartTime() < 0) {
					current.setStartTime(timeCounter);
				}
				current.setExpectedRunTime(current.getExpectedRunTime() - TIMESLICE);
				this.quantaScale += timeCounter + ": " + current.getProcessNumber() + "\n";
			} else if (!finished) {
				this.quantaScale += timeCounter + ": Waiting for a process\n";
			}
			if (!finished)
				timeCounter += TIMESLICE;
		}
		current = null;
		System.out.println(this.processesInformation);
		System.out.println(this.quantaScale);
	}

	/**
	 * Removes processes that are starved, not started, from the ready Queue, and other processes; updates starved to true
	 * 
	 * @param processes - collection of processes
	 * @param readyQueue - queue of ready processes
	 * @param quanta - time when starved processes are ignored
	 * @param currentTime - current quanta time
	 * @param starved - check if a process is starved
	 * @return true for starve if a purge occurs else false
	 */
	public boolean checkStarve(ArrayList<Process> processes, Queue<Process> readyQueue, int quanta, int currentTime, boolean starved) {
		if (!starved && (currentTime >= quanta)) {
			readyQueue.removeIf((process) -> (process.getStartTime() < 0));
			processes.removeIf((process) -> (process.getStartTime() < 0));
			starved = true;
		}
		return starved;
	}

	/**
	 * Outputs statistics about the algorithm
	 */
	public void calculateStats() {
		float turnAroundTime = 0;
		float waitTime = 0;
		float responseTime = 0;

		for (Process pr : allProcesses) {
			turnAroundTime += pr.getFinishTime() - pr.getArrivalTime();
			waitTime += (pr.getFinishTime() - pr.getArrivalTime()) - pr.getExpectedRunTimeForCal();
			responseTime += pr.getStartTime() - pr.getArrivalTime();
		}
		float avgTurnAroundTime = 0;
		float avgResponseTime = 0;
		float avgWaitTime = 0;
		float throughput = 0;

		if (allProcesses.size() > 0) {
			avgTurnAroundTime = turnAroundTime/allProcesses.size();
			avgWaitTime = waitTime/allProcesses.size();
			avgResponseTime = responseTime/allProcesses.size();

			if (timeCounter > 0) {
				throughput = (float) allProcesses.size() / timeCounter;
			}
		}

		System.out.println("Average turnaround time = " + avgTurnAroundTime);
		System.out.println("Average waiting time = " + avgWaitTime);
		System.out.println("Average response time = " + avgResponseTime);
		System.out.println("Throughput = " + throughput);
	}

	/**
	 * After running the algorithm, print information about the process that was run
	 * 
	 * @param currentProcess - the process to print information about
	 * @return information on that process
	 */
	private String printProcessesInfo(Process currentProcess) {
		int processNumber = currentProcess.getProcessNumber();
		float arrivalTime = currentProcess.getArrivalTime();
		float runTime = currentProcess.getExpectedRunTime();
		int priority = currentProcess.getPriority();

		String rrInfo = "\n" + "Process " + processNumber + " \n" + "Arrival Time of this process is: " + arrivalTime
				+ " \n" + "Expected Run Time is: " + runTime + " \n" + "Priority " + priority + "\n";
		return rrInfo;
	}

}