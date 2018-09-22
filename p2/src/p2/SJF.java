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
	private int finished;					// number of finished processes
	private Queue<Process> processQueue;
	private ArrayList<Process> processes;
	// statistics for averages
	private float responseTime;
	private float turnAroundTime;
	private float waitTime;
	/***********************/

	/**
	 * Sorts the processes according to start time
	 */
	public SJF(ArrayList<Process> processes) {
		this.processInfo = "";
		this.status = "";
		this.processes = processes;
		Collections.sort(this.processes, (o1, o2) -> o1.getArrivalTime() < o2.getArrivalTime() ? -1 :
             o1.getArrivalTime() > o2.getArrivalTime() ? 1:
                 o1.getExpectedRunTime() < o2.getExpectedRunTime() ? -1 :
                     o1.getExpectedRunTime() > o2.getExpectedRunTime() ? 1: 0);
		
		// ready queue -- processes that can be ran by CPU
		ArrayList<Process> sjfProcesses = new ArrayList<>();
		ArrayList<Process> tempProcesses = (ArrayList<Process>)processes.clone();
		tempProcesses.remove(0);
		processQueue = new LinkedList<Process>();
		processQueue.add(processes.get(0));

		// to keep track of total runtime in quanta
		int time = (int) Math.ceil(processes.get(0).getArrivalTime()) + (int) Math.ceil(processes.get(0).getExpectedRunTime());
		
		boolean done = false;
		while (!done){
			// default: job start before current job finished
			boolean jobArrived = false;
			
			// get jobs from the job queue
			for (int j = 0; j < tempProcesses.size(); j++) {
				if (tempProcesses.get(j).getArrivalTime() < time) {
					sjfProcesses.add(tempProcesses.get(j));
					jobArrived = true;
				}
				// get the next job from tempProcesses if no jobs in queue
				else if (!jobArrived) {
					sjfProcesses.add(tempProcesses.get(j));
					tempProcesses.remove(j);
					break;
				}
				else {
					break;
				}
			}
			
			// finds the shortest jobs
			Collections.sort(sjfProcesses, new Comparator<Process>() {
				@Override
				public int compare(Process o1, Process o2) {
					return o1.getExpectedRunTime() < o2.getExpectedRunTime() ? -1 :
								 o1.getExpectedRunTime() > o2.getExpectedRunTime() ? 1: 0;
				}
			});
			
			// add shortest jobs to the process queue and remove the same jobs from tempProcess queue
			processQueue.add(sjfProcesses.get(0));
			tempProcesses.remove(sjfProcesses.get(0));
			
			// add to total runtime in quanta
			time += (int) Math.ceil(sjfProcesses.get(0).getExpectedRunTime());
			sjfProcesses = new ArrayList<>();
			
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
		boolean isRunning = false;	// default: there is no process currently running
		Process current = null;
		double nextAvailability = 0.0; // var to keep track of whether a process is running
		for (; totalTime < MAX_QUANTA_RUN_TIME; totalTime++) {
			if (nextAvailability <= totalTime) {
				// If the totalTime is greater than or equal to nextAvailability, then the current process is done
				isRunning = false;
				if (current != null) {
					this.processInfo += this.printProcess(current);
					current = null;
					this.finished++;
				}
			}

			// checks if the CPU is idle and if there are any processes in the queue
			if (!processQueue.isEmpty() && processQueue.peek().getArrivalTime() <= totalTime && !isRunning) {
				current = processQueue.poll();
				isRunning = true;
				// expectedRunTime = when the current process is expected to complete its run
				nextAvailability = totalTime + current.getExpectedRunTime();
				this.status += totalTime + ": Process " + current.getProcessNumber() + "\n";

				// time from process start to finish
				this.turnAroundTime += nextAvailability - current.getArrivalTime();
				// time in queue
				this.waitTime += totalTime - (current.getArrivalTime() + current.getExpectedRunTime());
				// time from process arrival until start
				this.responseTime += totalTime - current.getArrivalTime();

			}
			else {
				// if the CPU is busy, add the process number
				if (current != null) {
					this.status += totalTime + ": Process " + current.getProcessNumber() + "\n";
				}
				// else if the CPU is idle, wait for the process to run
				else {
					this.status += totalTime + ": Waiting for a process\n";
				}
			}

			// if the process is still running, add to the number of finished processes
			if (isRunning) {
				this.finished++;
				for (; totalTime < Math.round(nextAvailability); totalTime++) {
					this.status += totalTime + ": Process " + current.getProcessNumber() + "\n";
					this.responseTime += totalTime;
				}
				this.processInfo += this.printProcess(current);
			}

			System.out.println(this.processInfo);
			System.out.println(this.status);
			System.out.println("\nAverage Turnaround time: " + this.turnAroundTime / this.finished);
			System.out.println("Average wait time: " + this.waitTime / this.finished);
			System.out.println("Average response time: " + this.responseTime / this.finished);
			System.out.println("Throughput: " + this.finished / (float) totalTime);
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