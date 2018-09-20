package p2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;
import java.util.Comparator;

//test edit
public class SJF {
	// Fields
	public String CPUStatus; // a string to represent the status of CPU at every quanta starting from 0 to 100.
	public String infoForProcess;
	public final static int MAX_QUANTA_RUN_TIME = 100;
	public static int timeCounter = 0; // a counter to keep track of time (from 0 to MAX_QUANTA_RUN_TIME)
	public Queue<Process> processQueue;
	public ArrayList<Process> processes;
	public double responseTime;
	public int processDone;
	public double turnAroundTime;
	public double waitTime;

	// constuctor: sorts processes according to when they are started
	public SJF( ArrayList<Process> processes )
	{
		this.infoForProcess = "";
		this.CPUStatus = "";
		this.processes = processes;
		Collections.sort(this.processes, (o1, o2) -> o1.getArrivalTime() < o2.getArrivalTime() ? -1 :
             o1.getArrivalTime() > o2.getArrivalTime() ? 1:
                 o1.getExpectedRunTime() < o2.getExpectedRunTime() ? -1 :
                     o1.getExpectedRunTime() > o2.getExpectedRunTime() ? 1: 0);
		
		// Ready queue, used to store processes which can be executed by the CPU when given an
		// opportunity.
		ArrayList<Process> sjfProcesses = new ArrayList<>();
		ArrayList<Process> tempProcesses = (ArrayList<Process>)processes.clone();
		tempProcesses.remove(0);
		processQueue = new LinkedList<Process>();
		processQueue.add(processes.get(0));

		// variable time keeps track of total runtime in quanta
		int time = (int) Math.ceil(processes.get(0).getArrivalTime()) + (int) Math.ceil(processes.get(0).getExpectedRunTime());
		
		boolean done = false;
		while (!done){
			// did a new job start before the current job finished?
			boolean jobArrived = false;
			
			// finds jobs that are queueing up
			for (int j = 0; j < tempProcesses.size(); j++) {
				if (tempProcesses.get(j).getArrivalTime() < time) {
					sjfProcesses.add(tempProcesses.get(j));
					jobArrived = true;
				}
				// if no jobs in queue, get next one in tempProcesses
				else if (!jobArrived)
				{
					sjfProcesses.add(tempProcesses.get(j));
					tempProcesses.remove(j);
					break;
				}
				else
					break;
			}
			
			// uses standard sort() method to find shortest job
			Collections.sort(sjfProcesses, new Comparator<Process>()
			{
				@Override
				public int compare(Process o1, Process o2)
				{
					return o1.getExpectedRunTime() < o2.getExpectedRunTime() ? -1 :
								 o1.getExpectedRunTime() > o2.getExpectedRunTime() ? 1: 0;
				}
			});
			
			// add shortest job to queue,
			processQueue.add(sjfProcesses.get(0));
			// remove that same job from the temp queue
			tempProcesses.remove(sjfProcesses.get(0));
			time += (int) Math.ceil(sjfProcesses.get(0).getExpectedRunTime());
			sjfProcesses = new ArrayList<>();
			
			// once all jobs are queued, done
			if (processQueue.size() == processes.size())
				done = true;
		}
	}
	// method run() starts the SJF algorithm
	public void run() {
		timeCounter = 0;
		boolean isRunning = false; // is there a processes currently running? default set to false
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

			} else {
				if (current != null) // if CPU is busy,
				{
					// add process number
					this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() + "\n";
				} else // if CPU is idle,
				{
					// wait for process to run
					this.CPUStatus += timeCounter + ": Waiting for a process\n";
				}

			}

			// checks whether the process is still running after 100 quanta (time slices)
			if (isRunning) {
				this.processDone++;
				for (; timeCounter < Math.round(nextAvailability); timeCounter++) {
					this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() + "\n";
					this.responseTime += timeCounter;
				}
				this.infoForProcess += this.printProcess(current);
			}

			// print output to txt file about processes
			System.out.println(this.infoForProcess);
			System.out.println(this.CPUStatus);
			System.out.println("\nAverage Turn-Around time: " + this.turnAroundTime / this.processDone);
			System.out.println("Average Wait time: " + this.waitTime / this.processDone);
			System.out.println("Average Response time: " + this.responseTime / this.processDone);
			System.out.println("Throughput: " + this.processDone / (float) timeCounter);
		}
	}
	// method to return formatted string of a process
	private String printProcess(Process currProcess)
{
		return "\nProcess " + currProcess.getProcessNumber() +
				" \nArrival Time of current process: " + currProcess.getArrivalTime() +
				" \nRun time of current process: " + currProcess.getExpectedRunTime() +
				" \nPriority of current process: " + currProcess.getPriority() + "\n";
	}
}