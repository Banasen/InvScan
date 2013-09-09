package InvScanLive;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
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
	public void init(FMLInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(new Signs());

	}

	public static void uploadAllPlayerInvs() {
		Iterator iterator = MinecraftServer.getServer()
				.getConfigurationManager().playerEntityList.iterator();
		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();
			IInventory inventory = entityplayermp.inventory;
			String Username = entityplayermp.username;
			WebPhpPost.PrepPost(Username, inventory, false);
		}
	}

	public static void uploadAllChests() {
		// something...
		// WebPhpPost.PrepPost(PlayerName, inventory, true)
	}
}
