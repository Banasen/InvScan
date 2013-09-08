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
	//item[slothere][rawName]=raw.name.here&item[slothere][name]=name here&item[slothere][size]=sizehere&
	//convert playerinv to invscan format (copy pasta from ocs!)
		HashMap map = invToMap(inventory);
		Iterator<Integer> keySetIterator = map.keySet().iterator();

		while(keySetIterator.hasNext()){
		  Integer key = keySetIterator.next();
		  System.out.println("key: " + key + " value: " + map.get(key));
		}
		
		if(chest){
			//item[slothere][id]=id&item[slothere][damage]=damagehere&item[slothere][rawName]=rawnamehere&item[slothere][name]=name&item[slothere][size]=sizehere&
		}
	 String toPost="?name="+PlayerName+"";
	}
	
	public static HashMap invToMap(IInventory inventory) {
		HashMap map = new HashMap();
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				map.put(i + 1, itemstackToMap(inventory.getStackInSlot(i)));
			}
		return map;
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
		} catch (Exception e) {
		}
		try {
			if (rawName.length() - rawName.replaceAll("\\.", "").length() == 0) {
				String packageName = is.getItem().getClass().getName()
						.toLowerCase();
				String[] packageLevels = packageName.split("\\.");
				if (!rawName.startsWith(packageLevels[0])
						&& packageLevels.length > 1) {
					rawName = packageLevels[0] + "." + rawName;
				}
			}
		} catch (Exception e) {

		}

		return rawName.trim();
	}
	

	public static HashMap itemstackToMap(ItemStack itemstack) {

		HashMap map = new HashMap();

		if (itemstack == null) {

			map.put("Name", "empty");
			map.put("Size", 0);
			map.put("Damagevalue", 0);
			map.put("MaxStack", 64);
			return map;

		} else {

			map.put("Name", getNameForItemStack(itemstack));			
			map.put("RawName", getRawNameForStack(itemstack));
			map.put("Size", itemstack.stackSize);
			map.put("DamageValue", itemstack.getItemDamage());
			map.put("MaxStack", itemstack.getMaxStackSize());
		}

		return map;
	}
	

	private void sendPost(String urlParameters) throws Exception{
    //init parameters.
		URL url = new URL(Config.connectURL);
		URLConnection conn = url.openConnection();
		String line;
		//conn.setDoOutput(true);
    // send to webserver
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(urlParameters);
		writer.flush();
	/*
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
		while ((line = reader.readLine()) != null) {
		    System.out.println(line);
		}
	*/
		writer.close();
	//	reader.close();  
	}
}
