package InvScanLive;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Commands implements ICommand {
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "isl";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/ISL ?";
	}

	@Override
	public List getCommandAliases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if(astring.length == 0)
	    {
		  icommandsender.sendChatToPlayer("ISL USAGE: makeT,Uplayers,Uchests");
	      return;
	    }
		String carg = astring[0].toLowerCase();
		String upl = "uplayers";
		String spm = "?";
		String makt = "maket";
	    if(carg.equals(makt) && Config.maketexture){
	    	// compile a zip with all textures
	    	icommandsender.sendChatToPlayer("Creating textures..... plox wait");
	    	//invscan.createPack();
	    }
	    else if(carg.equals(upl)){
	    	InvScanLive.uploadAllPlayerInvs();
	    	icommandsender.sendChatToPlayer("uploading all player invs!");
	    }
	    else if(carg.equals("uchests")){
	    	InvScanLive.uploadAllChests();
	    	icommandsender.sendChatToPlayer("uploading all chests!");
	    }
	    else if(carg.equals(spm)){
	    	icommandsender.sendChatToPlayer("ISL USAGE: makeT,Uplayers,Uchests");
	    }
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) { 
		System.out.println(MinecraftServer.getServer().getConfigurationManager().getOps().contains(icommandsender.getCommandSenderName()));
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		// TODO Auto-generated method stub
		return false;
	}

}
