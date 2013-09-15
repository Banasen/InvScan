package InvScanLive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class Signs {
	static boolean passAndUser = false;
	static boolean passw = false;
	static int index;
	public static int signx;
	public static int signy;
	public static int signz;
	public static String user;
	public static String pass;
	public static String chestcoord;
	public static String chestdata;
	public static String[] sigsText = new String[] { "", "", "", "" };
	public static ArrayList<String> Proclist;

	// on rightclick print sign text, because i can't find shiz...

	@ForgeSubscribe
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.entityPlayer.worldObj.isRemote) {
			boolean wasinDb = isInDb(e.x, e.y, e.z);
			System.out.println("interact server");
			if (wasinDb&&e.action.equals(e.action.RIGHT_CLICK_BLOCK)) {
				e.entityPlayer.sendChatToPlayer("was in db already.");
				return;
			}
			else if(e.action.equals(e.action.LEFT_CLICK_BLOCK)&&wasinDb){
					Proclist.remove(index);
					e.entityPlayer.sendChatToPlayer("removed index: "+index);
					return;
				}
		    else if (e.entityPlayer.worldObj.blockHasTileEntity(e.x, e.y, e.z)
					&& e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z) == 68
					&& e.action.equals(e.action.RIGHT_CLICK_BLOCK)
					&& !wasinDb) {
				TileEntity sign = e.entityPlayer.worldObj.getBlockTileEntity(
						e.x, e.y, e.z);
				TileEntitySign signe = (TileEntitySign) sign;
				sigsText = signe.signText;
				/*
				 * System.out.println(sigsText[0]); // invscan
				 * System.out.println(sigsText[1]); // name of chest
				 * System.out.println(sigsText[2]); // password
				 * System.out.println(sigsText[3]); // username just in case
				 */
				if (isValidPreparedSign(sigsText)) {
					System.out.println("VALID SIGN!");
					if (passAndUser) {
						createDbString(e.entityPlayer.worldObj, e.x, e.y, e.z,
								sigsText[2], sigsText[3]);
					} else if (passw) {
						createDbString(e.entityPlayer.worldObj, e.x, e.y, e.z,
								sigsText[2], e.entityPlayer.username);
					} else {
						createDbString(e.entityPlayer.worldObj, e.x, e.y, e.z,
								"NONE", e.entityPlayer.username);
					}

				} else {
					System.out.println("INVALID SIGN!");
				}
		    }
		}
	}

	public static boolean isInDb(int x, int y, int z) {
		if (Proclist != null) {
			index = 0;
			while (Proclist.iterator().hasNext()) {
				if (splitString(Proclist.iterator().next(), x, y, z)) {
					return true;
				}
				index = index + 1;
			}
		}
		return false;
	}

	public static void createDbString(World world, int x, int y, int z, String pass,
			String user) {
		IInventory chestinv = getnearbyChestInv(
				getDirectionofSign(world, x, y, z), world, x, y, z);
		if (chestinv != null) {
			WebPhpPost.PrepPost(sigsText[1], chestinv, true, true);
			// signx,signy,signz,chestcord,lastchestinv,user,pass
			String dbstring = x + ":" + y + ":" + z + ":"
			+ getDirectionofSign(world, x, y, z) + ":" + chestdata
					+ ":" + pass + ":" + user;
			// compare dbstring to dbstrings in database
			if (Proclist != null) {
				while (Proclist.iterator().hasNext()) {
					if (Proclist.iterator().next() == dbstring) {
						// match
						return;
					}
				}
			}
			try {
				WebPhpPost.sendPost(chestdata,true);
				Proclist.add(dbstring);
			} catch (Exception e) {
				System.out.println("failed to send post to server ...");
			}
			System.out.println(dbstring);
		} else {
			System.out.println("but no chest! xD");
		}
	}

	public static void saveArray(ArrayList<String> Proclist) {
		try {
			FileOutputStream fos = new FileOutputStream("chests.db");
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(Proclist);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static String getDirectionofSign(IBlockAccess par1IBlockAccess, int par2,
			int par3, int par4) {
		int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		if (l == 2) {
			return "z+";
		}

		if (l == 3) {
			return "z-";
		}

		if (l == 4) {
			return "x+";
		}

		if (l == 5) {
			return "x-";
		} else {
			return null;
		}
	}

	public static IInventory getnearbyChestInv(String signdirection, World world,
			int x, int y, int z) {
		if (signdirection.contentEquals("z+") && world.blockExists(x, y, z + 1)) {
			if (world.blockHasTileEntity(x, y, z + 1)) {
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z + 1);
				if (tileEntity instanceof TileEntityChest) {
					return (IInventory) tileEntity;
				}
			}
		} else if (signdirection.contentEquals("z-")
				&& world.blockExists(x, y, z - 1)) {
			if (world.blockHasTileEntity(x, y, z - 1)) {
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z - 1);
				if (tileEntity instanceof TileEntityChest) {
					return (IInventory) tileEntity;
				}
			}
		} else if (signdirection.contentEquals("x+")
				&& world.blockExists(x + 1, y, z)) {
			if (world.blockHasTileEntity(x + 1, y, z)) {
				TileEntity tileEntity = world.getBlockTileEntity(x + 1, y, z);
				if (tileEntity instanceof TileEntityChest) {
					return (IInventory) tileEntity;
				}
			}
		} else if (signdirection.contentEquals("x-")
				&& world.blockExists(x - 1, y, z)) {
			if (world.blockHasTileEntity(x - 1, y, z)) {
				TileEntity tileEntity = world.getBlockTileEntity(x - 1, y, z);
				if (tileEntity instanceof TileEntityChest) {
					return (IInventory) tileEntity;
				}
			}
		}
		return null; // no inventory here or chest
	}

	public static ArrayList<String> loadArray() {
		try {
			FileInputStream fis = new FileInputStream("chests.db");
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			ArrayList<String> input_array = (ArrayList<String>) in.readObject();
			in.close();
			return input_array;
		} catch (Exception e) {
			System.out.println("Database not found, will save db on exit");
			ArrayList<String> Proclist = new ArrayList<String>();
			saveArray(Proclist);
			return Proclist;
		}
	}

	public static boolean splitString(String dbOutput, int x, int y, int z) {
		StringTokenizer stringtokenizer = new StringTokenizer(dbOutput, ":");
		if (stringtokenizer.hasMoreElements()) {
			signx = Integer.parseInt(stringtokenizer.nextToken());
			signy = Integer.parseInt(stringtokenizer.nextToken());
			signz = Integer.parseInt(stringtokenizer.nextToken());
			chestcoord = stringtokenizer.nextToken();
			String laschestinv = stringtokenizer.nextToken();
			pass = stringtokenizer.nextToken();
			user = stringtokenizer.nextToken();
			if (x == signx && y == signy && z == signz) {
				return true;
			} else if (chestcoord.contentEquals("z-")) {
				if (signx == x && signy == y && signz - 1 == z) {
					return true;
				}
			} else if (chestcoord.contentEquals("z+")) {
				if (signx == x && signy == y && signz + 1 == z) {
					return true;
				}
			} else if (chestcoord.contentEquals("x+")) {
				if (signx + 1 == x && signy == y && signz == z) {
					return true;
				}
			} else if (chestcoord.contentEquals("x-")) {
				if (signx + 1 == x && signy == y && signz == z) {
					return true;
				}
			}
		}
		return false; // no match
	}

	// check sign contents
	public static boolean isValidPreparedSign(String[] lines) {
		String scan = "[INVSCAN]";
		if (!lines[0].equalsIgnoreCase(scan)) {
			return false;
		} else if (lines[1].isEmpty()) {
			return false;
		}
		boolean pass = false;
		boolean user = false;
		if (!lines[2].isEmpty()) {
			pass = true;
		}
		if (!lines[3].isEmpty()) {
			user = true;
		}
		if (pass && user) {
			passAndUser = true;
			passw = false;
		} else if (pass && !user) {
			passw = true;
			passAndUser = false;
		} else {
			passw = false;
			passAndUser = false;
		}
		return true;
	}
}
