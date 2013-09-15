package InvScanLive;

import java.util.EnumSet;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class Scheduler implements IScheduledTickHandler {
Integer spacing = Config.Refrate*20*60;
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	    InvScanLive.uploadAllPlayerInvs();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		InvScanLive.uploadAllChests();
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "InvSwipe";
	}

	@Override
	public int nextTickSpacing() {
		return spacing;
	}

}
