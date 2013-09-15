package InvScanLive;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "InvScanLive", name = "InvScanLive", version = "0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class InvScanLive {
	@Instance("InvScanLive")
	public static InvScanLive instance;
	protected static Configuration config;

	@Mod.PreInit
	// aka load config
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		Config.loadConfig(config);
		Signs.Proclist = Signs.loadArray();
	}

	@ServerStarting
	// register commands
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new Commands());
		MinecraftForge.EVENT_BUS.register(new Signs());
		if(!Config.Refrate.equals(0)){
		TickRegistry.registerScheduledTickHandler(new Scheduler(), Side.SERVER);
		}
		else{System.out.println("[INFO] [InvScan] Config value for timer is false! ignoring timer");}
	}
	@Mod.ServerStopping
	public void serverShutdown(FMLServerStoppingEvent event){
		Signs.saveArray(Signs.Proclist);
	}

	public static void uploadAllPlayerInvs() {
		Iterator iterator = MinecraftServer.getServer()
				.getConfigurationManager().playerEntityList.iterator();
		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();
			IInventory inventory = entityplayermp.inventory;
			String Username = entityplayermp.username;
			WebPhpPost.PrepPost(Username, inventory, false,false);
		}
	}

	public static boolean uploadAllChests() {
		if (Signs.Proclist != null) {
			while (Signs.Proclist.iterator().hasNext()) {
				Signs.createDbString(MinecraftServer.getServer().worldServerForDimension(1).provider.worldObj, Signs.signx, Signs.signy, Signs.signz, Signs.pass, Signs.user);
			}
		}
		else{
			return false;
		}
		return true;
	}
	
	public static void maketextures(){
		for (int i = 1; i < Item.itemsList.length; i++)
	      {
	        if ((i < Block.blocksList.length) && (Block.blocksList[i] != null) && (Block.blocksList[i].blockID != 0))
	        {
	            Block block = Block.blocksList[i];
	            String name = block.getUnlocalizedName();
	            if (name == null) {
	              name = block.getClass().getCanonicalName();
	            }
	            System.out.println("Block. Name: " + name + ". ID: " + i);
	            
	        } else if (Item.itemsList[i] != null)
	        {
	            Item item = Item.itemsList[i];
	            String name = item.getUnlocalizedName();
	            if (name == null) {
	              name = item.getClass().getCanonicalName();
	            }
	            System.out.println("Item. Name: " + name + ". ID: " + i);
	          }
	      }
		//compile zip output if error then send errormsg to player
	}
}
