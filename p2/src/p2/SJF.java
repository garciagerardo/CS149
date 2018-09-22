package p2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Shortest job first (SJF) is a non-preemptive scheduling algorithm that
 * schedules processes to be ran in order of shortest completion time first.
 */
public class SJF {
	/*** Instance variables ***/
	private final static int MAX_QUANTA_RUN_TIME = 100;
	private static int totalTime = 0;
	private String status;					// CPU status
	private String processInfo;				// process statistics
	private int completed;					// number of completed processes
	private Queue<Process> processQueue;
	private ArrayList<Process> processes;
	// statistics for averages
	private float responseTime;
	private float turnAroundTime;
	private float waitTime;
	/***********************/

	/**
	 * Sorts the processes according to start time and adds to process queue
	 */
	public SJF(ArrayList<Process> processes) {
		this.processInfo = "";
		this.status = "";
		this.processes = processes;
		// sort by expected runtime
		Collections.sort(this.processes, (o1, o2) -> o1.getArrivalTime() < o2.getArrivalTime() ? -1 :
             o1.getArrivalTime() > o2.getArrivalTime() ? 1:
                 o1.getExpectedRunTime() < o2.getExpectedRunTime() ? -1 :
                     o1.getExpectedRunTime() > o2.getExpectedRunTime() ? 1: 0);
		// ready queue -- processes that can be ran by CPU
		ArrayList<Process> readyProcesses = new ArrayList<>();
		ArrayList<Process> tempProcesses = (ArrayList<Process>)processes.clone();
		tempProcesses.remove(0);
		processQueue = new LinkedList<Process>();
		processQueue.add(processes.get(0));

		// to keep track of total runtime in quanta
		int time = (int) Math.ceil(processes.get(0).getArrivalTime()) + (int) Math.ceil(processes.get(0).getExpectedRunTime());
		
		boolean done = false;
		while (!done){
			// default: job start before current job completed
			boolean jobArrived = false;
			
			// get jobs from the job queue
			for (int j = 0; j < tempProcesses.size(); j++) {
				if (tempProcesses.get(j).getArrivalTime() < time) {
					readyProcesses.add(tempProcesses.get(j));
					jobArrived = true;
				}
				// get the next job from tempProcesses if no jobs in queue
				else if (!jobArrived) {
					readyProcesses.add(tempProcesses.get(j));
					tempProcesses.remove(j);
					break;
				}
				else {
					break;
				}
			}
			
			// finds the shortest jobs
			Collections.sort(readyProcesses, new Comparator<Process>() {
				@Override
				public int compare(Process o1, Process o2) {
					return o1.getExpectedRunTime() < o2.getExpectedRunTime() ? -1 :
								 o1.getExpectedRunTime() > o2.getExpectedRunTime() ? 1: 0;
				}
			});
			
			// add shortest jobs to the process queue and remove the same jobs from tempProcess queue
			processQueue.add(readyProcesses.get(0));
			tempProcesses.remove(readyProcesses.get(0));
			
			// add to total runtime in quanta
			time += (int) Math.ceil(readyProcesses.get(0).getExpectedRunTime());
			readyProcesses = new ArrayList<>();
			
			// once all jobs are queued, done
			if (processQueue.size() == processes.size()) {
				done = true;
			}
		}
	}
	
	/**
	 * Starts SJF algorithm and output statistics
	 */
	public void run() {
		totalTime = 0;
		boolean isRunning = false;		// default: there is no process currently running
		Process current = null;			// current process
		double nextProcess = 0.0; 		// the next available process
		for (; totalTime < MAX_QUANTA_RUN_TIME; totalTime++) {
			// as long as the next available process is less than total time, then the current process is done
			if (nextProcess <= totalTime) {
				isRunning = false;
				if (current != null) {
					this.processInfo += this.printProcess(current);
					current = null;
					this.completed++;
				}
			}

			// check for idle CPU and calculate process statistics
			if (!processQueue.isEmpty() && processQueue.peek().getArrivalTime() <= totalTime && !isRunning) {
				current = processQueue.poll();
				isRunning = true;
				nextProcess = totalTime + current.getExpectedRunTime();
				this.status += totalTime + ": Process " + current.getProcessNumber() + "\n";

				// time from process start to finish
				this.turnAroundTime += nextProcess - current.getArrivalTime();
				// time in queue
				this.waitTime += totalTime - (current.getArrivalTime() + current.getExpectedRunTime());
				// time from process arrival until start
				this.responseTime += totalTime - current.getArrivalTime();

			}
			// else if the CPU is busy, get the process number or wait for the process to run
			else {
				if (current != null) {
					this.status += totalTime + ": Process " + current.getProcessNumber() + "\n";
				}
				else {
					this.status += totalTime + ": Waiting for a process\n";
				}
			}

			// add process to the number of completed processes
			if (isRunning) {
				this.completed++;
				for (; totalTime < Math.round(nextProcess); totalTime++) {
					this.status += totalTime + ": Process " + current.getProcessNumber() + "\n";
					this.responseTime += totalTime;
				}
				this.processInfo += this.printProcess(current);
			}

			System.out.println(this.processInfo);
			System.out.println(this.status);
			System.out.println("\nAverage Turnaround time: " + this.turnAroundTime / this.completed);
			System.out.println("Average wait time: " + this.waitTime / this.completed);
			System.out.println("Average response time: " + this.responseTime / this.completed);
			System.out.println("Throughput: " + this.completed / (float) totalTime);
		}
	}
	
	/**
	 *  Prints the process times and priority
	 */
	private String printProcess(Process currProcess) {
		return "\nProcess #" + currProcess.getProcessNumber() +
				" \nPriority: " + currProcess.getPriority() +
				" \nArrival time: " + currProcess.getArrivalTime() +
				" \nRun time: " + currProcess.getExpectedRunTime() + "\n";
	}
	
} // end of class SJF