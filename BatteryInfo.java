import java.io.*;
public class BatteryInfo {
	public float origMaxCharge;
	public float maxCharge;
	public float currentCharge;
	public int currentPercent;
	public int currentCapacity;
	public int current; //the actual current, like, in Amps
	public int timeLeft; // = currentCharge/current
	public String timeLeftString;
	public String status;
	
	public BatteryInfo() {
		/* files that contain information about the battery */
		final File maxChargeFile = new File("/sys/class/power_supply/BAT1/charge_full");
		final File currentChargeFile = new File("/sys/class/power_supply/BAT1/charge_now");
		final File origMaxChargeFile = new File("/sys/class/power_supply/BAT1/charge_full_design");
		final File currentFile = new File("/sys/class/power_supply/BAT1/current_now");
		final File statusFile = new File("/sys/class/power_supply/BAT1/status");
		
		BufferedReader reader = null;
		
		/* read the maximum charge of the battery */
		try{
			reader = new BufferedReader(new FileReader(maxChargeFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		String st;
		try {
			while ((st = reader.readLine()) != null) {
				maxCharge = Integer.valueOf(st);
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		/* read the current charge of the battery */
		try{
			reader = new BufferedReader(new FileReader(currentChargeFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		st="";
		try {
			while ((st = reader.readLine()) != null) {
				currentCharge = Integer.valueOf(st);
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		/* read the maximum charge of the battery when it was factory new */
		try{
			reader = new BufferedReader(new FileReader(origMaxChargeFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		st="";
		try {
			while ((st = reader.readLine()) != null) {
				origMaxCharge = Integer.valueOf(st);
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		/* read the current of the battery */
		try{
			reader = new BufferedReader(new FileReader(currentFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		st="";
		try {
			while ((st = reader.readLine()) != null) {
				current = Integer.valueOf(st);
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		/* read the status of the battery */
		try{
			reader = new BufferedReader(new FileReader(statusFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		st="";
		try {
			while ((st = reader.readLine()) != null) {
				status = st;
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		/* close the reader */
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* calculate the current battery level and current capacity as percentages */
		currentPercent = Math.round((currentCharge/maxCharge)*100);
		currentCapacity = Math.round((maxCharge/origMaxCharge)*100);
		/* weird way of calculating remaining time but it works and isn't too inefficient */
		timeLeft = Math.round(currentCharge*100/current); //result in xyz -x=hrs, yz = fraction of hour minutes
		timeLeftString = Integer.parseInt(Integer.toString(timeLeft).substring(0, 1)) +":";
		timeLeftString += Math.round((timeLeft%100)*.6);
		if(Math.round((timeLeft%100)*.6)%10 == 0) {
			timeLeftString += "";
		}
	}
}
