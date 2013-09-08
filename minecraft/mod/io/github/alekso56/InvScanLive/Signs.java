package io.github.alekso56.InvScanLive;

import java.util.regex.Pattern;

import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class Signs extends TileEntity {
	public static final byte NAME_LINE = 0;
	public static final byte Chest_Name = 1;
	public static final byte Player = 2;
	public static final byte Password_line = 3;
	public String[] signText = new String[] { "", "", "", "" };
	// compile pattern securely.
	public static final Pattern[] SIGN_PATTERN = {
			Pattern.compile("^?[\\w -.]*$"), Pattern.compile("^[1-9][0-9]*$"),
			Pattern.compile("(?i)^[\\d.bs(free) :]+$"),
			Pattern.compile("^[\\w #:-]+$") };

	// currently TOTALLY USELESS!1111 i need to find the nbt reader shit....
	@Override
	public void readFromNBT(NBTTagCompound par1) {
		{
			super.readFromNBT(par1);
			for (int i = 0; i < 4; ++i) {
				this.signText[i] = par1.getString("Text" + (i + 1));

				if (this.signText[i].length() > 15) {
					this.signText[i] = this.signText[i].substring(0, 15);
				}
				System.out.println(signText[i]);
				if (isValidPreparedSign(signText)) {
					// do things with text
				}
			}
		}
	}

	// check sign contents with pattern
	public static boolean isValidPreparedSign(String[] lines) {
		for (int i = 0; i < 4; i++) {
			if (!SIGN_PATTERN[i].matcher(lines[i]).matches()) {
				return false;
			}
		}
		return true;
	}
}
