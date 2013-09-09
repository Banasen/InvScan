package InvScanLive;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class Commands implements ICommand {
    //make commands to edit/delete existing uploads/reupload an inv etc..
	// also make command to trigger MakeTexture
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
	      return;
	    }
	    if(astring[0] == "makeT" && Config.maketexture){
	    	// compile a zip with all textures
	    }
	    else if(astring[0] == "Uplayers"){
	    	InvScanLive.uploadAllPlayerInvs();
	    }
	    else if(astring[0] == "Uchests"){
	    	InvScanLive.uploadAllChests();
	    }
	    else if(astring[0] == "?"){
	    	icommandsender.sendChatToPlayer("ISL USAGE: makeT,Uplayers,Uchests");
	    }
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return false;
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
