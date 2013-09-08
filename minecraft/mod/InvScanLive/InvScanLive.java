package InvScanLive;


import java.util.List;
import java.util.ListIterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

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
	}

	@ServerStarting
	// register commands
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new Commands());
	}
	
	@Mod.Init
	public void init( FMLInitializationEvent evt ){
		MinecraftForge.EVENT_BUS.register(new Signs());
		
	}
	
	public static void uploadAllPlayerInvs(){
	 List Players =	MinecraftServer.getServer().getConfigurationManager().playerEntityList;
	 for (int i=0; i<Players.size(); i++)
	 {
	//  Minecraft.getMinecraft().thePlayer.inventory.mainInventory.);
	//  WebPhpPost.PrepPost(Players.get(i).toString(), Players.get(i), false)
		 System.out.println(Players.get(i));
	  
	 }
	}
}
