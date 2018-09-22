package p2;

import java.util.*;
import java.lang.*;

/**
 * HPF Uses 4 priority queues
 */
public class HPFPreemptive {

	private ArrayList<Process> processList;
	private final static int MAX_QUANTA_RUN_TIME = 100;
	private static int counter = 0;
	private Queue<Process> queue;
	
	private String quanta;
	private String infoProcess;

	private float turnaroundTime;
	private float waitTime;
	private float response;
	private int completedProcesses;
	
	/**
	 * Constructor for HPF preemptive
	 * 
	 * @param processes
	 *            - the processes to be run
	 */
	public HPFPreemptive(ArrayList<Process> processes) {
		this.infoProcess = "";
		this.quanta = "";
		this.processList = processes;
		Collections.sort(this.processList, new Comparator<Process>() {
			@Override
			public int compare(Process process1, Process process2) {
				if (process1.getArrivalTime() < process2.getArrivalTime())
					return -1;
				else if (process1.getArrivalTime() > process2.getArrivalTime())
					return 0;
				else
					return Integer.compare(process1.getPriority(), process2.getPriority());
			}
		});

		queue = new LinkedList<Process>();
		for (Process process : this.processList) {
			queue.add(process);
		}
	}


	/**
	 * print info about the process that was run
	 * 
	 * @param myProcess
	 *            - the process to print information about
	 * @return information on that process
	 */
	private String printProcessesInfo(Process myProcess) {

		int processNum = myProcess.getProcessNumber();
		float timeOfArrival = myProcess.getArrivalTime();
		float runTime = myProcess.getExpectedRunTime();
		int priority = myProcess.getPriority();

		String ans = "\n" + "Process " + processNum + " \n" + "process arrives at " + timeOfArrival + " \n"
				+ "run time is " + runTime + " \n" + "priority " + priority + "\n";

		return ans;
	}
	
	public void run() {
		counter = 0;
		boolean isProcessRunning = false;
		Process current = null;
		float nextAvailable = 0;
		for (; counter < MAX_QUANTA_RUN_TIME; counter++) {

			if (nextAvailable <= counter) {
				isProcessRunning = false;
				if (current != null) {
					this.infoProcess += this.printProcessesInfo(current);
					current = null;
					this.completedProcesses++;
				}
			}
			if (!queue.isEmpty() && queue.peek().getArrivalTime() <= counter && !isProcessRunning) {
				current = queue.poll();
				isProcessRunning = true;
				nextAvailable = counter + current.getExpectedRunTime();
				this.quanta += counter + ": " + current.getProcessNumber() + "\n";

				this.turnaroundTime += nextAvailable - current.getArrivalTime();
				this.waitTime += (nextAvailable - current.getArrivalTime()) - current.getExpectedRunTime();
				this.response += counter;

			} else {
				if (current != null) {
					this.quanta += counter + ": " + current.getProcessNumber() + "\n";
				} else {
					this.quanta += counter + ": Waiting for a process\n";
				}

			}
		}

		if (isProcessRunning) {
			for (int i = MAX_QUANTA_RUN_TIME; i < Math.round(nextAvailable); i++) {
				this.quanta += i + ": " + current.getProcessNumber() + "\n";
			}
			this.infoProcess += this.printProcessesInfo(current);
		}
		System.out.println(this.infoProcess);
		System.out.println(this.quanta);
		System.out.println("\n turnaroundtime: " + this.turnaroundTime / this.completedProcesses);
		System.out.println(" avg waiting time: " + this.waitTime / this.completedProcesses);
		System.out.println("avg response time: " + this.response / this.completedProcesses);
		System.out.println("throughput: " + this.completedProcesses / (float) counter);
	}

}