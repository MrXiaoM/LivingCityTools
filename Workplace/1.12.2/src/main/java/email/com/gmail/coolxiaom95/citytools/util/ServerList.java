package email.com.gmail.coolxiaom95.citytools.util;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ServerList {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger();

	public static void func_147414_b(ServerData p_147414_0_) {
		ServerList serverlist = new ServerList(Minecraft.getMinecraft());
		serverlist.loadServerList();

		for (int i = 0; i < serverlist.countServers(); ++i) {
			ServerData serverdata = serverlist.getServerData(i);

			if (serverdata.serverName.equals(p_147414_0_.serverName)
					&& serverdata.serverIP.equals(p_147414_0_.serverIP)) {
				serverlist.func_147413_a(i, p_147414_0_);
				break;
			}
		}

		serverlist.saveServerList();
	}

	/** The Minecraft instance. */
	@SuppressWarnings("unused")
	private final Minecraft mc;

	private final List<ServerData> servers = Lists.<ServerData>newArrayList();

	public ServerList(Minecraft mcIn) {
		this.mc = mcIn;
		this.loadServerList();
	}

	/**
	 * Adds the given ServerData instance to the list.
	 */
	public void addServerData(ServerData p_78849_1_) {
		this.servers.add(p_78849_1_);
	}

	/**
	 * Counts the number of ServerData instances in the list.
	 */
	public int countServers() {
		return this.servers.size();
	}

	public void func_147413_a(int p_147413_1_, ServerData p_147413_2_) {
		this.servers.set(p_147413_1_, p_147413_2_);
	}

	/**
	 * Gets the ServerData instance stored for the given index in the list.
	 */
	public ServerData getServerData(int p_78850_1_) {
		return this.servers.get(p_78850_1_);
	}

	/**
	 * Loads a list of servers from servers.dat, by running
	 * ServerData.getServerDataFromNBTCompound on each NBT compound found in the
	 * "servers" tag list.
	 */
	public void loadServerList() {
		this.servers.clear();
		this.servers.add(new ServerData("??b??l???????????? ??7(??????)", "mc.66ko.cc", false));
		this.servers.add(new ServerData("??a??l????????????", "dx.66ko.cc", false));
		this.servers.add(new ServerData("??a??l????????????", "lt.66ko.cc", false));
		this.servers.add(new ServerData("??a??l????????????", "yd.66ko.cc", false));
		this.servers.add(new ServerData("??c??l????????????", "218.93.206.50", false));
	}

	/**
	 * Removes the ServerData instance stored for the given index in the list.
	 */
	public void removeServerData(int p_78851_1_) {
		this.servers.remove(p_78851_1_);
	}

	/**
	 * Runs getNBTCompound on each ServerData instance, puts everything into a
	 * "servers" NBT list and writes it to servers.dat.
	 */
	public void saveServerList() {
		/*
		 * try { NBTTagList nbttaglist = new NBTTagList();
		 * 
		 * for (ServerData serverdata : this.servers) {
		 * nbttaglist.appendTag(serverdata.getNBTCompound()); }
		 * 
		 * NBTTagCompound nbttagcompound = new NBTTagCompound();
		 * nbttagcompound.setTag("servers", nbttaglist);
		 * CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir,
		 * "servers.dat")); } catch (Exception exception) {
		 * logger.error((String)"Couldn\'t save server list", (Throwable)exception); }
		 */
	}

	/**
	 * Takes two list indexes, and swaps their order around.
	 */
	public void swapServers(int p_78857_1_, int p_78857_2_) {
		ServerData serverdata = this.getServerData(p_78857_1_);
		this.servers.set(p_78857_1_, this.getServerData(p_78857_2_));
		this.servers.set(p_78857_2_, serverdata);
		this.saveServerList();
	}
}