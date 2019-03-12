import java.io.*;
public class ScreenManager {
	private final File maxBrightnessFile = new File("/sys/class/backlight/intel_backlight/max_brightness");
	private final File currentBrightnessFile = new File("/sys/class/backlight/intel_backlight/brightness");
	public int maxBrightness;
	public int currentBrightness;
	public final int minBrightness = 1;
	
	public ScreenManager() {
		BufferedReader reader = null;
		
		/* read the max brightness of the screen */
		try{
			reader = new BufferedReader(new FileReader(maxBrightnessFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		String st;
		try {
			while ((st = reader.readLine()) != null) {
				maxBrightness = Integer.valueOf(st);
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		/* read the current brightness of the screen */
		try{
			reader = new BufferedReader(new FileReader(currentBrightnessFile));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		 
		st="";
		try {
			while ((st = reader.readLine()) != null) {
				currentBrightness = Integer.valueOf(st);
			  	}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBrightness(int brightness) throws IOException {
		/* don't let the user change the brightness out of bounds */
		if((brightness)> maxBrightness || brightness < minBrightness ) {
			return;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(currentBrightnessFile));
		writer.write(String.valueOf(brightness));
		writer.close();
		
		currentBrightness = brightness;
	}
	
	
}
