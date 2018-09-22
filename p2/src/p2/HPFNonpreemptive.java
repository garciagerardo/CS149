package p2;

import java.util.*;
import java.lang.*;

/**
 * HPF non preemptive
 * 
 */
public class HPFNonpreemptive {

	private final static int MAX_QUANTA_RUN_TIME = 100;
	private static int counter = 0;
	private Queue<Process> queue;
	private ArrayList<Process> processes;
	private String scaleOfQuanta;
	private String infoOfProcess;
	private float turnaroundTime;
	private float waitTime;
	private float response;
	private int completedProcesses;
	
	public HPFNonpreemptive(ArrayList<Process> myProcess) {
		infoOfProcess = "";
		scaleOfQuanta = "";
		processes = myProcess;

		//HPF comparator first compares by arrival and then by priority
		Collections.sort(processes, new Comparator<Process>() {
			public int compare(Process process1, Process process2) {
				if (process1.getPriority() == process2.getPriority()) 
					return Float.compare(process1.getArrivalTime(), process2.getArrivalTime());
				else
					return Integer.compare(process1.getPriority(), process2.getPriority());
			}
		});
		queue = new LinkedList<Process>();
		for (Process process : this.processes) {
			queue.add(process);
		}
	}

	private String printProcessesInfo(Process currentProcess) {
		int processNum = currentProcess.getProcessNumber();
		float timeOfArrival = currentProcess.getArrivalTime();
		float runTime = currentProcess.getExpectedRunTime();
		int priority = currentProcess.getPriority();

		String processInfo = "\n" + "Process " + processNum + " \n" + "Arrival Time of this process is: "
				+ timeOfArrival + " \n" + "Expected Run Time is: " + runTime + " \n" + "Priority " + priority + "\n";

		return processInfo;
	}
	/**
	 * HPF algorithm run method
	 */
	public void run() {
		counter = 0;
		boolean isRunning = false;
		Process current = null;
		float nextAvailability = 0;
		for (; counter < MAX_QUANTA_RUN_TIME; counter++) {
			/*
			 * if current process empty add to queue
			 * if there is a current process execute process 
			 */
			if (nextAvailability <= counter) {
				isRunning = false;
				if (current != null) {
					this.infoOfProcess += this.printProcessesInfo(current);
					current = null;
					this.completedProcesses++;
				}
			}
			if (!queue.isEmpty() && queue.peek().getArrivalTime() <= counter && !isRunning) {
				current = queue.poll();
				isRunning = true;
				nextAvailability = counter + current.getExpectedRunTime();
				this.scaleOfQuanta += counter + ": " + current.getProcessNumber() + "\n";

				this.turnaroundTime += nextAvailability - current.getArrivalTime();
				this.waitTime += (nextAvailability - current.getArrivalTime()) - current.getExpectedRunTime();
				this.response += counter;

			} else {
				if (current != null) {
					this.scaleOfQuanta += counter + ": " + current.getProcessNumber() + "\n";
				} else {
					this.scaleOfQuanta += counter + ": Waiting for a process\n";
				}

			}
		}

		if (isRunning) {
			for (int i = MAX_QUANTA_RUN_TIME; i < Math.round(nextAvailability); i++) {
				this.scaleOfQuanta += i + ": " + current.getProcessNumber() + "\n";
			}
			this.infoOfProcess += this.printProcessesInfo(current);
		}
		System.out.println(this.infoOfProcess);
		System.out.println(this.scaleOfQuanta);
		System.out.println("\n turnaroundtime: " + this.turnaroundTime / this.completedProcesses);
		System.out.println(" avg waiting time: " + this.waitTime / this.completedProcesses);
		System.out.println("avg response time: " + this.response / this.completedProcesses);
		System.out.println("throughput: " + this.completedProcesses / (float) counter);
	}

	
}