package InvScanLive;

import java.awt.List;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cpw.mods.fml.common.Mod.Block;
import cpw.mods.fml.common.network.Player;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class Signs {
	public static boolean once = false;
	static boolean passAndUser = false;
	static boolean passw = false;
	public String[] sigsText = new String[] { "", "", "", "" };
	ArrayList<String> Proclist = loadArray();

	// on rightclick print sign text, because i can't find shiz...
	@ForgeSubscribe
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.entityPlayer.worldObj.blockHasTileEntity(e.x, e.y, e.z) && !once
				&& e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z) == 63
				|| e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z) == 68) {
			TileEntity sign = e.entityPlayer.worldObj.getBlockTileEntity(e.x,
					e.y, e.z);
			TileEntitySign signe = (TileEntitySign) sign;
			sigsText = signe.signText;
			System.out.println(sigsText[0]);
			System.out.println(sigsText[1]);
			System.out.println(sigsText[2]);
			System.out.println(sigsText[3]);
			once = true;
			if (isValidPreparedSign(sigsText)) {
				System.out.println("VALID SIGN!");
				if (passAndUser) {
					// do stuff
				} else if (passw) {
					// do stuff without user
				}
			} else {
				System.out.println("INVALID SIGN!");
			}
		} else {
			once = false;
		}
	}
	
	public void saveArray(ArrayList<String> proclist2) {
		try {
			FileOutputStream fos = new FileOutputStream("config/chests.db");
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(proclist2);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	
	public ArrayList<String> loadArray() {
		try {
			FileInputStream fis = new FileInputStream("config/chests.db");
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			ArrayList<String> input_array = (ArrayList<String>) in.readObject();
			in.close();
			return input_array;
		} catch (Exception e) {
			System.out.println("Database not found, will save db on exit");
			saveArray(Proclist);
			return Proclist;
		}
	}
	
	public void splitString(String dbOutput, int index) {
		StringTokenizer stringtokenizer = new StringTokenizer(dbOutput, ":");
		if (stringtokenizer.hasMoreElements()) {
			int x = Integer.parseInt(stringtokenizer.nextToken());
			int y = Integer.parseInt(stringtokenizer.nextToken());
			int z = Integer.parseInt(stringtokenizer.nextToken());
			int signx = Integer.parseInt(stringtokenizer.nextToken());
			int signy = Integer.parseInt(stringtokenizer.nextToken());
			int signz = Integer.parseInt(stringtokenizer.nextToken());
			String pass = stringtokenizer.nextToken();
			String user = stringtokenizer.nextToken();
			/*if (get sign block  == not sign then) {
				Proclist.remove(index);
				return false;
			}*/
		}
	}

	// check sign contents
	public static boolean isValidPreparedSign(String[] lines) {
		String scan = "[INVSCAN]";
		if (!lines[0].equalsIgnoreCase(scan)) {
			return false;
		} else if (lines[1] == "") {
			return false;
		}
		boolean pass = false;
		boolean user = false;
		if (lines[2] != "") {
			pass = true;
		}
		if (lines[3] != "") {
			user = true;
		}
		if (pass && user) {
			passAndUser = true;
		}
		if (pass && !user) {
			passw = true;
			passAndUser = false;
		}
		return true;
	}
}
