package io.github.alekso56.InvScanLive;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config {
	static Property ChestUP,rate; //temp
	public static boolean DEBUG,uploadchests;
	static String connectURL;
	static Integer Refrate; //refreshrate or 0 == false
	
	public static void loadConfig(Configuration config) {
		config.load();
		connectURL = config.get("Web", "ConnectURL", "").getString();
		rate = config.get("Web", "UploadC", false);
		rate.comment = "Upload data by timer/minute(int) or false for logout and itemchange";
		if(rate.getBoolean(false || rate.getInt() < 0)){
			Refrate = 0;
		}
		else{
			Refrate = rate.getInt();
		}
		DEBUG = config.get("Misc", "DEBUG", false).getBoolean(true);
		ChestUP = config.get("Misc", "ChestUP", false);
		ChestUP.comment = "Upload chest contents regardless of signs?";
		uploadchests = ChestUP.getBoolean(true);
		config.save();
	}

}
