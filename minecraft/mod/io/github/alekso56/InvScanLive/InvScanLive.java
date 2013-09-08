package io.github.alekso56.InvScanLive;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
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

}
