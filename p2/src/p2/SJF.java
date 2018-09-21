package p2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Shortest job first
 *
 */
public class SJF {
	public final static int MAX_QUANTA_RUN_TIME = 100;
	public static int timeCounter = 0;
	public String CPUStatus; // a string to represent the status of CPU at every quanta starting from 0 to 100.
	public String infoForProcess;
	public Queue<Process> processQueue;
	public ArrayList<Process> processes;
	public double responseTime;
	public double turnAroundTime;
	public double waitTime;
	public int processDone;

	/**
	 * Sorts the processes according to start time
	 */
	public SJF(ArrayList<Process> processes) {
		this.infoForProcess = "";
		this.CPUStatus = "";
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
	 * Starts SJF algorithm
	 */
	public void run() {
		timeCounter = 0;
		boolean isRunning = false;	// default: there is no process currently running
		Process current = null; // temp var
		double nextAvailability = 0.0; // var to keep track of whether a process is running
		for (; timeCounter < MAX_QUANTA_RUN_TIME; timeCounter++) {
			if (nextAvailability <= timeCounter) {
				// If the timeCounter is greater than or equal to nextAvailability, then the current process is done
				isRunning = false;
				if (current != null) {
					this.infoForProcess += this.printProcess(current);
					current = null;
					this.processDone++;
				}
			}

			// checks if the CPU is idle and if there are any processes in the queue
			if (!processQueue.isEmpty() && processQueue.peek().getArrivalTime() <= timeCounter && !isRunning) {
				current = processQueue.poll();
				isRunning = true;
				// expectedRunTime = when the current process is expected to complete its run
				nextAvailability = timeCounter + current.getExpectedRunTime();
				this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() + "\n";

				// time from process start to finish
				this.turnAroundTime += nextAvailability - current.getArrivalTime();
				// time in queue
				this.waitTime += timeCounter - (current.getArrivalTime() + current.getExpectedRunTime());
				// time from process arrival until start
				this.responseTime += timeCounter - current.getArrivalTime();

			}
			else {
				// if the CPU is busy, add the process number
				if (current != null) {
					this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() + "\n";
				}
				// else if the CPU is idle, wait for the process to run
				else {
					this.CPUStatus += timeCounter + ": Waiting for a process\n";
				}
			}

			// if the process is still running, add to the number of finished processes
			if (isRunning) {
				this.processDone++;
				for (; timeCounter < Math.round(nextAvailability); timeCounter++) {
					this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() + "\n";
					this.responseTime += timeCounter;
				}
				this.infoForProcess += this.printProcess(current);
			}

			System.out.println(this.infoForProcess);
			System.out.println(this.CPUStatus);
			System.out.println("\nAverage Turn-Around time: " + this.turnAroundTime / this.processDone);
			System.out.println("Average Wait time: " + this.waitTime / this.processDone);
			System.out.println("Average Response time: " + this.responseTime / this.processDone);
			System.out.println("Throughput: " + this.processDone / (float) timeCounter);
		}
	}
	
	/**
	 *  Prints the process times and priority
	 */
	private String printProcess(Process currProcess) {
		return "\nProcess " + currProcess.getProcessNumber() +
				" \nArrival time of current process: " + currProcess.getArrivalTime() +
				" \nRun time of current process: " + currProcess.getExpectedRunTime() +
				" \nPriority of current process: " + currProcess.getPriority() + "\n";
	}
	
} // end of class SJF