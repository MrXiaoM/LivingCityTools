package email.com.gmail.coolxiaom95.citytools.gui;

import email.com.gmail.coolxiaom95.citytools.Main;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiTeleport extends GuiScreen {

	ResourceLocation user_background = new ResourceLocation("citytools", "textures/gui/user_background.png");

	GuiScreen last;
	String current_server = "sc";

	public GuiTeleport(GuiScreen lastScreen, String server) {
		last = lastScreen;
		current_server = server;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/rspawn zy", false);
		}
		if (button.id == 1) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp sd", false);
		}
		if (button.id == 2) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/rspawn zz", false);
		}
		if (button.id == 3) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp zy", false);
		}
		if (button.id == 4) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/rspawn dy1", false);
		}
		if (button.id == 5) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/rspawn modi", false);
		}
		if (button.id == 6) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp pvp", false);
		}

		if (button.id == 7) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/is", false);
		}
		if (button.id == 8) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp sd", false);
		}

		if (button.id == 9) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/is", false);
		}
		if (button.id == 10) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp sd", false);
		}
		if (button.id == 11) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp test", false);
		}
		if (button.id == 12) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp 矿区", false);
		}

		if (button.id == 13) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/rspawn zd", false);
		}
		if (button.id == 14) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/plot home", false);
		}
		if (button.id == 15) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp sd", false);
		}
		if (button.id == 16) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp shopcm", false);
		}
		if (button.id == 17) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp 传送大厅", false);
		}
		if (button.id == 18) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/warp 世界BOSS", false);
		}

		if (button.id == 199) {
			mc.displayGuiScreen(new GuiTpToPlayer(this));
		}
		if (button.id == 666) {
			mc.displayGuiScreen(last);
		}

		if (button.id == 201) {
			if (current_server != "sc")
				Main.serverid = "sc";
			mc.displayGuiScreen(new GuiTeleport(last, "sc"));
		}
		if (button.id == 202) {
			Main.serverid = "kd";
			if (current_server != "kd")
				mc.displayGuiScreen(new GuiTeleport(last, "kd"));
		}
		if (button.id == 203) {
			Main.serverid = "kk";
			if (current_server != "kk")
				mc.displayGuiScreen(new GuiTeleport(last, "kk"));
		}
		if (button.id == 204) {
			Main.serverid = "zd";
			if (current_server != "zd")
				mc.displayGuiScreen(new GuiTeleport(last, "zd"));
		}
	}

	public void bindTexture(ResourceLocation texture) {
		mc.renderEngine.bindTexture(texture);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.bindTexture(user_background);
		drawScaledCustomSizeModalRect(this.width / 2 - 124, this.height / 2 - 83, 0, 0, 248, 166, 248, 166, 248, 166);

		this.drawString(fontRenderer, I18n.format("citytools.menu.teleport.title"), this.width / 2 - 110,
				this.height / 2 - 75, -1);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void initButtons() {
		this.buttonList.clear();
		if (current_server == "sc") {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 108, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.sc.zy")));
			this.buttonList.add(new GuiButton(1, this.width / 2 + 8, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.sc.sd")));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 108, this.height / 2 - 36, 100, 20,
					I18n.format("citytools.menu.teleport.sc.zz")));
			this.buttonList.add(new GuiButton(3, this.width / 2 + 8, this.height / 2 - 36, 100, 20,
					I18n.format("citytools.menu.teleport.sc.zz2")));
			this.buttonList.add(new GuiButton(4, this.width / 2 - 108, this.height / 2 - 12, 100, 20,
					I18n.format("citytools.menu.teleport.sc.dy")));
			this.buttonList.add(new GuiButton(5, this.width / 2 + 8, this.height / 2 - 12, 100, 20,
					I18n.format("citytools.menu.teleport.sc.md")));
			this.buttonList.add(new GuiButton(6, this.width / 2 - 108, this.height / 2 + 12, 100, 20,
					I18n.format("citytools.menu.teleport.sc.pvp")));
		}
		if (current_server == "kd") {
			this.buttonList.add(new GuiButton(7, this.width / 2 - 108, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.kd.is")));
			this.buttonList.add(new GuiButton(8, this.width / 2 + 8, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.kd.sd")));
		}
		if (current_server == "kk") {
			this.buttonList.add(new GuiButton(9, this.width / 2 - 108, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.kk.is")));
			this.buttonList.add(new GuiButton(10, this.width / 2 + 8, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.kk.sd")));
			this.buttonList.add(new GuiButton(11, this.width / 2 - 108, this.height / 2 - 36, 100, 20,
					I18n.format("citytools.menu.teleport.kk.fb")));
			this.buttonList.add(new GuiButton(12, this.width / 2 + 8, this.height / 2 - 36, 100, 20,
					I18n.format("citytools.menu.teleport.kk.kc")));
		}
		if (current_server == "zd") {
			this.buttonList.add(new GuiButton(13, this.width / 2 - 108, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.zd.zd")));
			this.buttonList.add(new GuiButton(14, this.width / 2 + 8, this.height / 2 - 60, 100, 20,
					I18n.format("citytools.menu.teleport.zd.dp")));
			this.buttonList.add(new GuiButton(15, this.width / 2 - 108, this.height / 2 - 36, 100, 20,
					I18n.format("citytools.menu.teleport.zd.sd")));
			this.buttonList.add(new GuiButton(16, this.width / 2 + 8, this.height / 2 - 36, 100, 20,
					I18n.format("citytools.menu.teleport.zd.dh")));
			this.buttonList.add(new GuiButton(17, this.width / 2 - 108, this.height / 2 - 12, 100, 20,
					I18n.format("citytools.menu.teleport.zd.fb")));
			this.buttonList.add(new GuiButton(18, this.width / 2 + 8, this.height / 2 - 12, 100, 20,
					I18n.format("citytools.menu.teleport.zd.boss")));
		}
		this.buttonList.add(new GuiButton(199, this.width / 2 + 8, this.height / 2 + 12, 100, 20,
				I18n.format("citytools.menu.teleport.player")));

		this.buttonList.add(new GuiButton(201, this.width / 2 - 120, this.height / 2 + 65, 60, 15,
				(current_server == "sc" ? "§e§l" : "§r") + I18n.format("citytools.menu.servers.sc") + "§r"));
		this.buttonList.add(new GuiButton(202, this.width / 2 - 60, this.height / 2 + 65, 60, 15,
				(current_server == "kd" ? "§e§l" : "§r") + I18n.format("citytools.menu.servers.kd") + "§r"));
		this.buttonList.add(new GuiButton(203, this.width / 2, this.height / 2 + 65, 60, 15,
				(current_server == "kk" ? "§e§l" : "§r") + I18n.format("citytools.menu.servers.kk") + "§r"));
		this.buttonList.add(new GuiButton(204, this.width / 2 + 60, this.height / 2 + 65, 60, 15,
				(current_server == "zd" ? "§e§l" : "§r") + I18n.format("citytools.menu.servers.zd") + "§r"));

		this.buttonList
				.add(new GuiButton(666, this.width / 2 - 110, this.height / 2 + 40, 220, 20, I18n.format("gui.back")));
	}

	@Override
	public void initGui() {

		initButtons();
	}
}
