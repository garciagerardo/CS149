package p2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;

public class FCFS {
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


	// constructor that sorts processes according to when they are started, using Collections.sort()
	public FCFS( ArrayList<Process> processes )
	{
		this.infoForProcess = "";
		this.CPUStatus = "";
		this.processes = processes;
		Collections.sort(this.processes, (o1, o2) -> {
            // compare processes against one another based on arrival time
            return o1.getArrivalTime() < o2.getArrivalTime() ? -1 :
                 o1.getArrivalTime() > o2.getArrivalTime() ? 1: 0;
        });

		// Ready queue, used to store processes which can be executed by the CPU when given an
		// opportunity.
		processQueue = new LinkedList<Process>();
		for(Process proc: this.processes){
			processQueue.add(proc);
		}
	}

	// Method run() starts the FCFS algorithm
	public void run()
	{
		// counter (using quanta as scale)
		timeCounter = 0;
		boolean isRunning = false; // is there a processes currently running? default set to false
		Process current = null; // temp var
		double nextAvailability = 0.0; // var to keep track of whether a process is running
		for (; timeCounter < MAX_QUANTA_RUN_TIME; timeCounter++)
		{
			if (nextAvailability <= timeCounter)
			{
				// If the timeCounter is greater than or equal to nextAvailability, then the current process is done
				isRunning = false;
				if (current != null) {
					this.infoForProcess += this.printProcess(current);
					current = null;
					this.processDone++;
				}
			}
			// checks if the CPU is idle and if there are any processes in the queue
			if (!processQueue.isEmpty() && processQueue.peek().getArrivalTime() <= timeCounter && !isRunning)
			{
				current = processQueue.poll();
				isRunning = true;
				// expectedRunTime = when the current process is expected to complete its run
				nextAvailability = timeCounter + current.getExpectedRunTime();
				this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() +"\n";

				// time from process start to finish
				this.turnAroundTime += nextAvailability - current.getArrivalTime();
				// time in queue
				this.waitTime += timeCounter - (current.getArrivalTime() + current.getExpectedRunTime());
				// time from process arrival until start
				this.responseTime += timeCounter - current.getArrivalTime();
			}
			else
			{
				if (current != null) // if CPU is busy,
				{
					// add process number
					this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() +  "\n";
				}
				else // if CPU is idle,
				{
					// wait for process to run
					this.CPUStatus += timeCounter + ": Waiting for a process\n";
				}
				
			}
		}
		// checks whether the process is still running after 100 quanta (time slices)
		if (isRunning)
		{
			this.processDone++;
			for (; timeCounter < Math.round(nextAvailability); timeCounter++) {
				this.CPUStatus += timeCounter + ": Process " + current.getProcessNumber() +  "\n";
				this.responseTime += timeCounter;
			}
			this.infoForProcess += this.printProcess(current);
		}
		
		// print output to txt file about processes
		System.out.println(this.infoForProcess);
		System.out.println(this.CPUStatus);
		System.out.println("\nAverage Turn-Around time: " + this.turnAroundTime/this.processDone);
		System.out.println("Average Wait time: " + this.waitTime/this.processDone);
		System.out.println("Average Response time: " + this.responseTime/this.processDone);
		System.out.println("Throughput: " + this.processDone/ (float) timeCounter);
	}

	////

	// method to return formatted string of a process
	private String printProcess(Process currProcess) {
			return "\nProcess " + currProcess.getProcessNumber() +
					" \nArrival Time of current process: " + currProcess.getArrivalTime() +
					" \nRun time of current process: " + currProcess.getExpectedRunTime() +
					" \nPriority of current process: " + currProcess.getPriority() + "\n";
	}
}