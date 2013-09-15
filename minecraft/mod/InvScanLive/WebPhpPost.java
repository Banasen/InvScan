package InvScanLive;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;


public class WebPhpPost {
	public static void PrepPost(String PlayerName,IInventory inventory,boolean chest,boolean nopost){
		    // needs to be fixed for chests post!
			String toPost="?name="+PlayerName+invToMap(inventory,chest);
			if(!nopost){
			try {
				sendPost(toPost,chest);
				System.out.println(toPost);
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			else{Signs.chestdata = toPost;}
	}
	
	public static String invToMap(IInventory inventory,boolean chest) {
		String output = "";
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				HashMap map2 = itemstackToMap(inventory.getStackInSlot(i));
				if(map2.containsValue("Air")){
					continue;
				}
				if(chest){
				  output = output + "item["+i+"][id]="+map2.get("id")+"&item["+i+"][damage]="+map2.get("DamageValue")+"&item["+i+"][rawName]="+map2.get("RawName")+"&item["+i+"][name]="+map2.get("Name")+"item["+i+"][size]="+map2.get("Size")+"&";
				}
				else{
				  output = output + "item["+i+"][rawName]="+map2.get("RawName")+"&item["+i+"][name]="+map2.get("Name")+"item["+i+"][size]="+map2.get("Size")+"&";
			}
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
			map.put("id", itemstack.itemID);
			return map;
		}
	}
	

	public static void sendPost(String urlParameters,boolean chest) throws Exception{
    //init parameters.
	URL url;
	if (!chest){
	   url = new URL(Config.connectURL+"PostPlayer.php?");
	}
	else{
	   url = new URL(Config.connectURL+"PostChest.php?");
	}
		URLConnection conn = url.openConnection();
		String line;
		conn.setDoOutput(true);
    // send to webserver
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(urlParameters);
		writer.flush();
		writer.close();
	}
}
