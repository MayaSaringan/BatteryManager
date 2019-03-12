import java.io.*;
import java.util.concurrent.TimeUnit;

public class ProcessManager {
	private final File procDirectory = new File("/proc");
	private final File procStat = new File("/proc/stat");
	private final File procUptime = new File("/proc/uptime");
	public float mostCpu1, mostCpu2, mostCpu3, mostCpu4, mostCpu5, mostCpu6, mostCpu7, mostCpu8, mostCpu9, mostCpu10;
	public int pid1, pid2, pid3, pid4, pid5, pid6, pid7, pid8, pid9, pid10;
	public float uptime1, uptime2, uptime3, uptime4, uptime5, uptime6, uptime7, uptime8, uptime9, uptime10;
	public float curCpu1, curCpu2, curCpu3, curCpu4, curCpu5, curCpu6, curCpu7, curCpu8, curCpu9, curCpu10;
	public int curPid1, curPid2, curPid3, curPid4, curPid5, curPid6, curPid7, curPid8, curPid9, curPid10;
	public float cpuSum1, cpuSum2;
	public int systemUptime;
	public String curName1, curName2, curName3, curName4, curName5, curName6, curName7, curName8, curName9, curName10;
	
	
	/* driver function to find the 10 most CPU consuming processes right now */
	public ProcessManager() {
		mostCpu1=mostCpu2=mostCpu3=mostCpu4=mostCpu5=mostCpu6=mostCpu7=mostCpu8=mostCpu9=mostCpu10=0;
		curCpu1=curCpu2=curCpu3=curCpu4=curCpu5=curCpu6=curCpu7=curCpu8=curCpu9=mostCpu10=0;
		getSystemUptime();
		getTopTen();
		getCpuSum1();
		try {
			TimeUnit.SECONDS.sleep(1);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		getCpuSum2();
		getTopTenCurrent();
		getNames();
	}
	
	/* gets the process names for the top 10 CPU consuming processes */
	public void getNames() {
		String[] fileList = new String[10];
		fileList[0] = "/proc/"+curPid1+"/stat";
		fileList[1] = "/proc/"+curPid2+"/stat";
		fileList[2] = "/proc/"+curPid3+"/stat";
		fileList[3] = "/proc/"+curPid4+"/stat";
		fileList[4] = "/proc/"+curPid5+"/stat";
		fileList[5] = "/proc/"+curPid6+"/stat";
		fileList[6] = "/proc/"+curPid7+"/stat";
		fileList[7] = "/proc/"+curPid8+"/stat";
		fileList[8] = "/proc/"+curPid9+"/stat";
		fileList[9] = "/proc/"+curPid10+"/stat";
		
		String[] nameList = new String[10];
		
		for(int i=0; i<10; i++) {
			File pidStat = new File(fileList[i]);
			/* make a buffered reader to read the file /proc/pid/stat */
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(pidStat));
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			
			/* read the file /proc/pid/stat into a string */
			String fileContents="";
			try {
				fileContents = reader.readLine();
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			/* split the string into substrings separated by spaces */
			String[] pidStatSplit = fileContents.split(" ", 6);
			
			/* get the name of the process being looked at */
			try {
				Integer.valueOf(pidStatSplit[3]); //4th value in the file should be an int
				nameList[i] = pidStatSplit[1];
			}catch(NumberFormatException e) { //if there was a space in the process name
				nameList[i] = pidStatSplit[1]+" "+pidStatSplit[2];
			}
		}
		curName1 = nameList[0];
		curName2 = nameList[1];
		curName3 = nameList[2];
		curName4 = nameList[3];
		curName5 = nameList[4];
		curName6 = nameList[5];
		curName7 = nameList[6];
		curName8 = nameList[7];
		curName9 = nameList[8];
		curName10 = nameList[9];
		
	}
	
	/* finds ten processes using the most CPU time */
	public void getTopTen() {
		String[] procFileList = procDirectory.list();
		int i=0;
		int currentPid;
		float currentCpu, currentUptime;
		for(i=0; i<procFileList.length; i++) {
			if(Character.isDigit(procFileList[i].charAt(0))) {
				File pidStat = new File("/proc/"+procFileList[i]+"/stat");
				
				/* make a buffered reader to read the file /proc/pid/stat */
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(pidStat));
				}catch(FileNotFoundException e) {
					e.printStackTrace();
				}
				
				/* read the file /proc/pid/stat into a string */
				String fileContents="";
				try {
					fileContents = reader.readLine();
				}catch(IOException e) {
					e.printStackTrace();
				}
				
				/* split the string into substrings separated by spaces */
				String[] pidStatSplit = fileContents.split(" ", 24);
				currentPid = Integer.valueOf(pidStatSplit[0]); //pid of process file being looked at
				
				/* get the cpu usage of the process being looked at */
				try {
					Integer.valueOf(pidStatSplit[3]); //4th value in the file should be an int
					currentCpu = Integer.valueOf(pidStatSplit[13]);
					currentUptime = Integer.valueOf(pidStatSplit[21]);
				}catch(NumberFormatException e) { //if there was a space in the process name
					currentCpu = Integer.valueOf(pidStatSplit[14]);
					currentUptime = Integer.valueOf(pidStatSplit[22]);
				}
				currentUptime = currentUptime/100; //convert from system ticks to seconds
				currentUptime = systemUptime - currentUptime; //how long the process has run for
				currentCpu = currentCpu/currentUptime; //how much cpu the process is using per second
				if(currentCpu > mostCpu10) {
					sortTopTen(currentPid, currentCpu, currentUptime);
				}
			}
		}
	}
	
	public void getTopTenCurrent() {
		String[] fileList = new String[10];
		fileList[0] = "/proc/"+pid1+"/stat";
		fileList[1] = "/proc/"+pid2+"/stat";
		fileList[2] = "/proc/"+pid3+"/stat";
		fileList[3] = "/proc/"+pid4+"/stat";
		fileList[4] = "/proc/"+pid5+"/stat";
		fileList[5] = "/proc/"+pid6+"/stat";
		fileList[6] = "/proc/"+pid7+"/stat";
		fileList[7] = "/proc/"+pid8+"/stat";
		fileList[8] = "/proc/"+pid9+"/stat";
		fileList[9] = "/proc/"+pid10+"/stat";
		
		float[] cpus = new float[10];
		cpus[0] = mostCpu1;
		cpus[1] = mostCpu2;
		cpus[2] = mostCpu3;
		cpus[3] = mostCpu4;
		cpus[4] = mostCpu5;
		cpus[5] = mostCpu6;
		cpus[6] = mostCpu7;
		cpus[7] = mostCpu8;
		cpus[8] = mostCpu9;
		cpus[9] = mostCpu10;
		
		int[] pids = new int[10];
		pids[0] = pid1;
		pids[1] = pid2;
		pids[2] = pid3;
		pids[3] = pid4;
		pids[4] = pid5;
		pids[5] = pid6;
		pids[6] = pid7;
		pids[7] = pid8;
		pids[8] = pid9;
		pids[9] = pid10;
		
		float[] uptimes = new float[10];
		uptimes[0] = uptime1;
		uptimes[1] = uptime2;
		uptimes[2] = uptime3;
		uptimes[3] = uptime4;
		uptimes[4] = uptime5;
		uptimes[5] = uptime6;
		uptimes[6] = uptime7;
		uptimes[7] = uptime8;
		uptimes[8] = uptime9;
		uptimes[9] = uptime10;
		
		
		for(int i=0; i<10; i++) {
			File pidStat = new File(fileList[i]);
			float currentCpu=0;
			/* make a buffered reader to read the file /proc/pid/stat */
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(pidStat));
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			
			/* read the file /proc/pid/stat into a string */
			String fileContents="";
			try {
				fileContents = reader.readLine();
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			/* split the string into substrings separated by spaces */
			String[] pidStatSplit = fileContents.split(" ", 16);
			
			/* get the cpu usage of the process being looked at */
			try {
				Integer.valueOf(pidStatSplit[3]); //4th value in the file should be an int
				currentCpu = Integer.valueOf(pidStatSplit[13]);
			}catch(NumberFormatException e) { //if there was a space in the process name
				currentCpu = Integer.valueOf(pidStatSplit[14]);
			}
			currentCpu = currentCpu - (cpus[i]*uptimes[i]); //the difference in cpu usage
			currentCpu = currentCpu/(cpuSum2 - cpuSum1); //difference in total cpu usage
			sortTopCurCpu(pids[i], 100*currentCpu);
		}
	}
	
	/* gets the system uptime in seconds from the file /proc/uptime */
	public void getSystemUptime() {
		BufferedReader reader = null;
		String fileContents = "";
		try {
			reader = new BufferedReader(new FileReader(procUptime));
			fileContents = reader.readLine();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] procStatSplit = fileContents.split(" ");
		systemUptime = (int)Math.round(Double.valueOf(procStatSplit[0]));
	}
	
	
	/* gets the sum of the cpu numbers from the file /proc/stat, stores in cpuSum2 */
	public void getCpuSum1() {
		BufferedReader reader = null;
		String fileContents = "";
		try {
			reader = new BufferedReader(new FileReader(procStat));
			fileContents = reader.readLine();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] procStatSplit = fileContents.split(" ");
		for(int i=2; i<12; i++) {
			cpuSum1 += Integer.valueOf(procStatSplit[i]);
		}
	}
	
	/* gets the sum of the cpu numbers from the file /proc/stat, stores in cpuSum2 */
	public void getCpuSum2() {
		BufferedReader reader = null;
		String fileContents = "";
		try {
			reader = new BufferedReader(new FileReader(procStat));
			fileContents = reader.readLine();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] procStatSplit = fileContents.split(" ");
		for(int i=2; i<12; i++) {
			cpuSum2 += Integer.valueOf(procStatSplit[i]);
		}
	}
	
	/* takes as input a pid and its cpu usage. Sorts the top 10 most cpu using processes */
	public void sortTopTen(int pid, float cpu, float uptime) {
		if(cpu < mostCpu9) {
			pid10 = pid;
			mostCpu10 = cpu;
			uptime10 = uptime;
		}
		if(cpu > mostCpu9) {
			pid10 = pid9;
			mostCpu10 = mostCpu9;
			uptime10 = uptime9;
			pid9 = pid;
			mostCpu9 = cpu;
			uptime9 = uptime;
		}
		if(cpu > mostCpu8) {
			pid9 = pid8;
			mostCpu9 = mostCpu8;
			uptime9 = uptime8;
			pid8 = pid;
			mostCpu8 = cpu;
			uptime8 = uptime;
		}
		if(cpu > mostCpu7) {
			pid8 = pid7;
			mostCpu8 = mostCpu7;
			uptime8 = uptime7;
			pid7 = pid;
			mostCpu7 = cpu;
			uptime7 = uptime;
		}
		if(cpu > mostCpu6) {
			pid7 = pid6;
			mostCpu7 = mostCpu6;
			uptime7 = uptime6;
			pid6 = pid;
			mostCpu6 = cpu;
			uptime6 = uptime;
		}
		if(cpu > mostCpu5) {
			pid6 = pid5;
			mostCpu6 = mostCpu5;
			uptime6 = uptime5;
			pid5 = pid;
			mostCpu5 = cpu;
			uptime5 = uptime;
		}
		if(cpu > mostCpu4) {
			pid5 = pid4;
			mostCpu5 = mostCpu4;
			uptime5 = uptime4;
			pid4 = pid;
			mostCpu4 = cpu;
			uptime4 = uptime;
		}
		if(cpu > mostCpu3) {
			pid4 = pid3;
			mostCpu4 = mostCpu3;
			uptime4 = uptime3;
			pid3 = pid;
			mostCpu3 = cpu;
			uptime3 = uptime;
		}
		if(cpu > mostCpu2) {
			pid3 = pid2;
			mostCpu3 = mostCpu2;
			uptime3 = uptime2;
			pid2 = pid;
			mostCpu2 = cpu;
			uptime2 = uptime;
		}
		if(cpu > mostCpu1) {
			pid2 = pid1;
			mostCpu2 = mostCpu1;
			uptime2 = uptime1;
			pid1 = pid;
			mostCpu1 = cpu;
			uptime1 = uptime;
		}
	}
	
	/* takes as input a pid and its current cpu usage. Sorts the top 10 most cpu using processes */
	public void sortTopCurCpu(int pid, float cpu) {
		if(cpu < curCpu9) {
			curPid10 = pid;
			curCpu10 = cpu;
		}
		if(cpu >= curCpu9) {
			curPid10 = pid9;
			curCpu10 = curCpu9;
			curPid9 = pid;
			curCpu9 = cpu;
		}
		if(cpu >= curCpu8) {
			curPid9 = pid8;
			curCpu9 = curCpu8;
			curPid8 = pid;
			curCpu8 = cpu;
		}
		if(cpu >= curCpu7) {
			curPid8 = pid7;
			curCpu8 = curCpu7;
			curPid7 = pid;
			curCpu7 = cpu;
		}
		if(cpu >= curCpu6) {
			curPid7 = pid6;
			curCpu7 = curCpu6;
			curPid6 = pid;
			curCpu6 = cpu;
		}
		if(cpu >= curCpu5) {
			curPid6 = pid5;
			curCpu6 = curCpu5;
			curPid5 = pid;
			curCpu7 = cpu;
		}
		if(cpu >= curCpu4) {
			curPid5 = pid4;
			curCpu5 = curCpu4;
			curPid4 = pid;
			curCpu4 = cpu;
		}
		if(cpu >= curCpu3) {
			curPid4 = pid3;
			curCpu4 = curCpu3;
			curPid3 = pid;
			curCpu3 = cpu;
		}
		if(cpu >= curCpu2) { 
			curPid3 = pid2;
			curCpu3 = curCpu2;
			curPid2 = pid;
			curCpu2 = cpu;
		}
		if(cpu >= curCpu1) {
			curPid2 = pid1;
			curCpu2 = curCpu1;
			curPid1 = pid;
			curCpu1 = cpu;
		}
	}
	
	public static void main(String[] args) {
		ProcessManager process = new ProcessManager();
	}
	
}
