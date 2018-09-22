package p2;

import java.io.*;
//This will test and then output the algorithms to a text file 

public class Tester {
	
	public static void main(String[] args) {
		
		testsRR();
		Process.resetRandomGenerator();
		testsHPFNonPre();
		Process.resetRandomGenerator();
		testsHPFPre();
		Process.resetRandomGenerator();
		testsFCFC();
		Process.resetRandomGenerator();
		testsSJF();
		Process.resetRandomGenerator();
		testsSRTF();
		Process.resetRandomGenerator();
		
	}
	
	public static void testsFCFC() {
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

	//This will test SRT and output to the SRT.txt
  public static void testsSRTF() {
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
  
//This will test SJF and output to the SJF_OUT.txt
	public static void testsSJF() {
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
	
	
	
	// This will test HPF Nonpreemptive and output to the HPF-NONPRE_OUT.txt
	public static void testsHPFNonPre() {
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
	
	//This will test HPF Preemptive and output to the HPF-PRE_OUT.txt
	public static void testsHPFPre() {
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
	
	//This will test RR and output to the RR_OUT.txt
	public static void testsRR() {
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
		}}
}