package p2;

import java.util.*;
import java.lang.*;

/**
 * HPF Uses 4 priority queues and runs on Round Robin
 */
public class HPFPreemptive {

	/**
	 * Constructor for HPFpreemptive
	 * 
	 * @param someProcesses
	 *            - the processes to be run
	 */
	public HPFPreemptive(ArrayList<Process> processes) {
		this.processesInformation = "";
		this.quantaScale = "";
		this.processes = processes;
		Collections.sort(this.processes, new Comparator<Process>() {
			@Override
			public int compare(Process proc1, Process proc2) {
				if (proc1.getArrivalTime() < proc2.getArrivalTime())
					return -1;
				else if (proc1.getArrivalTime() > proc2.getArrivalTime())
					return 0;
				else
					return Integer.compare(proc1.getPriority(), proc2.getPriority());
			}
		});

		queue = new LinkedList<Process>();
		for (Process proc : this.processes) {
			queue.add(proc);
		}
	}

	public void run() {
		timeCounter = 0;
		boolean isProcessRunning = false;
		Process current = null;
		float nextAvailability = 0;
		for (; timeCounter < MAX_QUANTA_RUN_TIME; timeCounter++) {

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

	/**
	 * After running the algorithm, print information about the process that was
	 * run.
	 * 
	 * @param currentProcess
	 *            - the process to print information about
	 * @return information on that process
	 */
	private String printProcessesInfo(Process theProcess) {

		int processNumber = theProcess.getProcessNumber();
		float arrivalTime = theProcess.getArrivalTime();
		float runTime = theProcess.getExpectedRunTime();
		int priority = theProcess.getPriority();

		String answer = "\n" + "Process " + processNumber + " \n" + "process arrives at " + arrivalTime + " \n"
				+ "run time is " + runTime + " \n" + "priority " + priority + "\n";

		return answer;
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