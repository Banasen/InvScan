package InvScanLive;

import java.awt.List;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cpw.mods.fml.common.Mod.Block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
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
	
	// on rightclick print sign text,  because i can't find shiz...
	@ForgeSubscribe
    public void onPlayerInteract(PlayerInteractEvent e) {
          if (e.entityPlayer.worldObj.blockHasTileEntity(e.x, e.y, e.z)  && !once && e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z) == 63 || e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z) == 68){
        	  TileEntity sign = e.entityPlayer.worldObj.getBlockTileEntity(e.x, e.y, e.z);
        	  TileEntitySign signe = (TileEntitySign)sign;
        	  sigsText = signe.signText;
        	  System.out.println(sigsText[0]);
        	  System.out.println(sigsText[1]);
        	  System.out.println(sigsText[2]);
        	  System.out.println(sigsText[3]);
        	  once = true;
        	  if(isValidPreparedSign(sigsText)){
        		  System.out.println("VALID SIGN!");
        		  if(passAndUser){
        			  // do stuff
        		  }
        		  else if (passw){
        			  // do stuff without user
        		  }
        	  }
        	  else{
        		  System.out.println("INVALID SIGN!");
        	  }
          }
          else{once = false;}
         }
/*
	  public List getTileEntities(int i, int j, int k, int l, int i1, int j1) {
	         List arraylist = new List();
	       // check in chunks: usually just from one
	       for (int cx = (i >> 4); cx <= ((l - 1) >> 4); cx++) {
	          for (int cz = (k >> 4); cz <= ((j1 - 1) >> 4); cz++) {
	               Chunk c = getChunkAt(cx, cz);
	               if (c == null) continue;
	               for (Object te : c.chunkTileEntityMap.values()) {
	                    TileEntity tileentity = (TileEntity) te;
	                    if ((!tileentity.isInvalid()) && (tileentity.xCoord >= i) && (tileentity.yCoord >= j) && (tileentity.zCoord >= k) && (tileentity.xCoord < l) && (tileentity.yCoord < i1) && (tileentity.zCoord < j1)) {
	                        arraylist.add(tileentity);
	                    }
	                }
	             }
	         }
	         return arraylist;
	     }
*/
	// check sign contents
	public static boolean isValidPreparedSign(String[] lines) {
		if (lines[0].equalsIgnoreCase("INVSCAN")) {
				return false;
			}
			else if (lines[1] == ""){
				return false;
			}
		boolean pass = false;
		boolean user = false;
	    if (lines[2] != "") {
		      pass = true;
			}
	    if(lines[3] != ""){
	    	user = true;
	    }
		if(pass && user){
			passAndUser = true;
		}
		if(pass && !user){
			passw = true;
			passAndUser = false;
		}
		InvScanLive.uploadAllPlayerInvs();
		return true;
	}
}
