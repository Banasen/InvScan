package InvScanLive;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config {
	static Property ChestUP,rate,mkT; //temp
	public static boolean DEBUG,uploadchests,maketexture;
	static String connectURL;
	static Integer Refrate; //refreshrate or 0 == false
	
	public static void loadConfig(Configuration config) {
		config.load();
		connectURL = config.get("Web", "ConnectURL", "http://mywebserver.info/php/").getString();
		rate = config.get("Web", "UploadC", false);
		rate.comment = "Upload data by timer/minute(int) or false for logout and itemchange";
		if(rate.getBoolean(false) || rate.getInt() < 0){
			Refrate = 0;
		}
		else{
			Refrate = rate.getInt();
		}
		mkT = config.get("Misc", "maketexture", false);
		mkT.comment = "Enable the texture interceptor? (make a zip after doing /isl makeT ingame)";
		maketexture = mkT.getBoolean(true);
		DEBUG = config.get("Misc", "DEBUG", false).getBoolean(true);
		ChestUP = config.get("Misc", "ChestUP", false);
		ChestUP.comment = "Upload chest contents regardless of signs? (nonfunctional right now)";
		uploadchests = ChestUP.getBoolean(true);
		config.save();
	}

}
