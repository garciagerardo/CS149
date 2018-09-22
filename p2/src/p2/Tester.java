package p2;

import java.io.*;

// Tests each algorithm; outputs the statistics of each algorithm's run to the respective text file 

public class Tester {

	public static void main(String[] args) {

		rrTester();
		Process.resetRandomGenerator();
		hpfNonPreTester();
		Process.resetRandomGenerator();
		hpfPreTester();
		Process.resetRandomGenerator();
		fcfsTester();
		Process.resetRandomGenerator();
		sjfTester();
		Process.resetRandomGenerator();
		srtTester();
		Process.resetRandomGenerator();

	}

	/**
	 * Runs the FCFS algorithm; outputs statistics to FCFS_OUT.txt file
	 */
	public static void fcfsTester() {
		try {
			PrintStream outToFile = new PrintStream("FCFS_OUT.txt");
			System.setOut(outToFile);
			for (int i = 1; i < 6; i++) {
				System.out.println("\n\n------ 	Now Running FCFS: Test " + i + " -----");
				FCFS fcfs = new FCFS(Process.generateProcesses(60));
				fcfs.run();
			}
		} catch (IOException a) {
			System.out.println("\n\n ----- Error running FCFS ----- " + "\n" + a);
		}
	}
	
	/**
	 * Runs the HPF Nonpreemptive algorithm; outputs statistics to HPF-NONPRE_OUT.txt file
	 */
	public static void hpfNonPreTester() {
		try {
			PrintStream outToFile = new PrintStream("HPF-NONPRE_OUT.txt");
			System.setOut(outToFile);
			for (int i = 1; i < 6; i++) {
				System.out.println("\n\n------- Now Running HPF Nonpreemptive: Test " + i + " ------");
				HPFNonpreemptive hpf = new HPFNonpreemptive(Process.generateProcesses(60));
				hpf.run();
			}
		} catch (IOException a) {
			System.out.println("\n\n ---- Error running HPF-NONPRE ------ " + "\n" + a);
		}
	}

	/**
	 * Runs the HPF Preemptive algorithm; outputs statistics to HPF-PRE_OUT.txt file
	 */
	public static void hpfPreTester() {
		try {
			PrintStream outToFile = new PrintStream("HPF-PRE_OUT.txt");
			System.setOut(outToFile);
			for (int i = 1; i < 6; i++) {
				System.out.println("\n\n------- Now Running HPF Preemptive: Test " + i + " ------");
				HPFPreemptive hpf = new HPFPreemptive(Process.generateProcesses(60));
				hpf.run();
			}
		} catch (IOException a) {
			System.out.println("\n\n ------ Error running HPF-PRE ------ " + "\n" + a);
		}
	}

	/**
	 * Runs the SRT algorithm; outputs statistics to SRT_OUT.txt file
	 */
	public static void srtTester() {
		try {
			PrintStream outToFile = new PrintStream("SRT_OUT.txt");
			System.setOut(outToFile);
			for (int i = 1; i < 6; i++) {
				System.out.println("\n\n------- Now Running SRT: Test " + i + " ------");
				SRT srt = new SRT(Process.generateProcesses(60));
				srt.run();
			}
		} catch (IOException a) {
			System.out.println("\n\n------ Error running SRT------- " + "\n" + a);
		}
	}

	/**
	 * Runs the SJF algorithm; outputs statistics to SJF_OUT.txt file
	 */
	public static void sjfTester() {
		try {
			PrintStream outToFile = new PrintStream("SJF_OUT.txt");
			System.setOut(outToFile);
			for (int i = 1; i < 6; i++) {
				System.out.println("\n\n------ Now Running Shortest Job First: Test " + i + " ------");
				SJF sjf = new SJF(Process.generateProcesses(60));
				sjf.run();
			}
		} catch (IOException a) {
			System.out.println("\n\n ----- Error running SJF ----- " + "\n" + a);
		}
	}

	/**
	 * Runs the RR algorithm; outputs statistics to RR_OUT.txt file
	 */
	public static void rrTester() {
		try {
			PrintStream outToFile = new PrintStream("RR_OUT.txt");
			System.setOut(outToFile);
			for (int i = 1; i < 6; i++) {
				System.out.println("------- Now Running RR: Test " + i + " ------");
				RoundRobin rr = new RoundRobin(Process.generateProcesses(60));
				rr.run();
				System.out.println();
				rr.calculateStats();
				System.out.println();
			}
		} catch (IOException a) {
			System.out.println("\n\n ------ Error running RR ------- " + "\n" + a);
		}
	}
}