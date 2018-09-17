package p2;

import java.util.*;
import java.lang.*;

/**
 * HPF non preemptive
 * 
 */
public class HPFNonpreemptive {

	public HPFNonpreemptive(ArrayList<Process> someProcesses) {
		processesInformation = "";
		quantaScale = "";
		processes = someProcesses;

		//HPf comparator
		//compares by arrival, then by priority since it is non premptive
		Collections.sort(processes, new Comparator<Process>() {
			public int compare(Process p1, Process p2) {
				if (p1.getPriority() == p2.getPriority()) 
					return Float.compare(p1.getArrivalTime(), p2.getArrivalTime());
				else
					return Integer.compare(p1.getPriority(), p2.getPriority());
			}
		});
		queue = new LinkedList<Process>();
		for (Process proc : this.processes) {
			queue.add(proc);
		}
	}

	/**
	 * Method to run this HPF algorithm
	 */
	public void run() {
		timeCounter = 0;
		boolean isProcessRunning = false;
		Process current = null;
		float nextAvailability = 0;
		for (; timeCounter < MAX_QUANTA_RUN_TIME; timeCounter++) {
			/*
			 * check for processes to add to queue, if current process empty
			 * add to queue
			 * execute process once there is a current process
			 */
			if (nextAvailability <= timeCounter) {
				isProcessRunning = false;
				if (current != null) {
					this.processesInformation += this.printProcessesInfo(current);
					current = null;
					this.processesCompleted++;
				}
			}
			if (!queue.isEmpty() && queue.peek().getArrivalTime() <= timeCounter && !isProcessRunning) {
				current = queue.poll();
				isProcessRunning = true;
				nextAvailability = timeCounter + current.getExpectedRunTime();
				this.quantaScale += timeCounter + ": " + current.getProcessNumber() + "\n";

				this.turnaroundTime += nextAvailability - current.getArrivalTime();
				this.waitingTime += (nextAvailability - current.getArrivalTime()) - current.getExpectedRunTime();
				this.responseTime += timeCounter;

			} else {
				if (current != null) {
					this.quantaScale += timeCounter + ": " + current.getProcessNumber() + "\n";
				} else {
					this.quantaScale += timeCounter + ": Waiting for a process\n";
				}

			}
		}

		if (isProcessRunning) {
			for (int i = MAX_QUANTA_RUN_TIME; i < Math.round(nextAvailability); i++) {
				this.quantaScale += i + ": " + current.getProcessNumber() + "\n";
			}
			this.processesInformation += this.printProcessesInfo(current);
		}
		System.out.println(this.processesInformation);
		System.out.println(this.quantaScale);
		System.out.println("\n turnaroundtime: " + this.turnaroundTime / this.processesCompleted);
		System.out.println(" avg waiting time: " + this.waitingTime / this.processesCompleted);
		System.out.println("avg response time: " + this.responseTime / this.processesCompleted);
		System.out.println("throughput: " + this.processesCompleted / (float) timeCounter);
	}

	
	private String printProcessesInfo(Process currentProcess) {
		int processNumber = currentProcess.getProcessNumber();
		float arrivalTime = currentProcess.getArrivalTime();
		float runTime = currentProcess.getExpectedRunTime();
		int priority = currentProcess.getPriority();

		String processInfo = "\n" + "Process " + processNumber + " \n" + "Arrival Time of this process is: "
				+ arrivalTime + " \n" + "Expected Run Time is: " + runTime + " \n" + "Priority " + priority + "\n";

		return processInfo;
	}

	private final static int MAX_QUANTA_RUN_TIME = 100;
	private static int timeCounter = 0;
	private Queue<Process> queue;
	private ArrayList<Process> processes;
	private String quantaScale;
	private String processesInformation;
	private float turnaroundTime;
	private float waitingTime;
	private float responseTime;
	private int processesCompleted;
}