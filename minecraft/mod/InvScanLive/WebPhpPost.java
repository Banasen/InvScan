package InvScanLive;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class WebPhpPost {
	public static void PrepPost(String PlayerName,IInventory inventory,boolean chest){
		if(chest){
			//item[slothere][id]=id&item[slothere][damage]=damagehere&item[slothere][rawName]=rawnamehere&item[slothere][name]=name&item[slothere][size]=sizehere&
		}
		else{
			String toPost="?name="+PlayerName+invToMap(inventory);
			try {
				sendPost(toPost);
				System.out.println(toPost);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String invToMap(IInventory inventory) {
		String output = "";
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				HashMap map2 = itemstackToMap(inventory.getStackInSlot(i));
				if(map2.containsValue("Air")){
					continue;
				}
				  output = output + "item["+i+"][rawName]="+map2.get("RawName")+"&item["+i+"][name]="+map2.get("Name")+"item["+i+"][size]="+map2.get("Size")+"&";
			}
		return output;
	}
	
	public static String getNameForItemStack(ItemStack is) {
		String name = "Unknown";
		try {
			name = is.getDisplayName();
		} catch (Exception e) {
			try {
				name = is.getItemName();
			} catch (Exception e2) {
			}
		}
		return name;
	}

	public static String getRawNameForStack(ItemStack is) {

		String rawName = "unknown";

		try {
			rawName = is.getItemName().toLowerCase();
		} catch (Exception e) {}
		try {
			if (rawName.length() - rawName.replaceAll("\\.", "").length() == 0) {
				String packageName = is.getItem().getClass().getName().toLowerCase();
				String[] packageLevels = packageName.split("\\.");
				if (!rawName.startsWith(packageLevels[0])&& packageLevels.length > 1) {
					rawName = packageLevels[0] + "." + rawName;
				}
			}
		} catch (Exception e) {}

		return rawName.trim();
	}
	

	public static HashMap itemstackToMap(ItemStack itemstack) {

		HashMap map = new HashMap();

		if (itemstack == null) {

			map.put("Name", "Air");
			return map;

		} else {

			map.put("Name", getNameForItemStack(itemstack));
			map.put("RawName", getRawNameForStack(itemstack));
			map.put("Size", itemstack.stackSize);
			map.put("DamageValue", itemstack.getItemDamage());
			map.put("MaxStack", itemstack.getMaxStackSize());
			return map;
		}
	}
	

	private static void sendPost(String urlParameters) throws Exception{
    //init parameters.
		URL url = new URL(Config.connectURL+"?");
		URLConnection conn = url.openConnection();
		String line;
		//conn.setDoOutput(true);  response if true
    // send to webserver
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(urlParameters);
		writer.flush();
	/*  read response??!?!
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
		while ((line = reader.readLine()) != null) {
		    System.out.println(line);
		}
	*/
		writer.close();
	//	reader.close();  close input reader
	}
}
