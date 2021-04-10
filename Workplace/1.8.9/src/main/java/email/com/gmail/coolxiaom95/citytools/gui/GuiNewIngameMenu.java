package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import com.google.common.collect.Lists;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.gui.hotkey.GuiHotkey;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraftforge.fml.client.GuiModList;

public class GuiNewIngameMenu extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	
	ResourceLocation options_button = new ResourceLocation("citytools", "textures/gui/options_button.png");
	ResourceLocation payrecord_button = new ResourceLocation("citytools", "textures/gui/pay_record.png");

	private int time;
	protected List<GuiMyButton> buttons = Lists.<GuiMyButton>newArrayList();
	protected List<GuiImageButton> imageButtons = Lists.<GuiImageButton>newArrayList();
	private GuiMyButton selectedButton;
	private GuiImageButton selectedImageButton;

	public GuiNewIngameMenu() {

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			for (int i = 0; i < this.buttons.size(); ++i) {
				GuiMyButton guibutton = (GuiMyButton) this.buttons.get(i);

				if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
					this.selectedButton = guibutton;
					guibutton.playPressSound(this.mc.getSoundHandler());
					this.actionPerformed_(guibutton);
				}
			}
			for (int i = 0; i < this.imageButtons.size(); ++i) {
				GuiImageButton guibutton = (GuiImageButton) this.imageButtons.get(i);

				if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
					this.selectedImageButton = guibutton;
					guibutton.playPressSound(this.mc.getSoundHandler());
					this.actionPerformed__(guibutton);
				}
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (this.selectedButton != null && state == 0) {
			this.selectedButton.mouseReleased(mouseX, mouseY);
			this.selectedButton = null;
		}
		if (this.selectedImageButton != null && state == 0) {
			this.selectedImageButton.mouseReleased(mouseX, mouseY);
			this.selectedImageButton = null;
		}
	}

	@Override
	public void initGui() {
		buttonList.clear();
		buttons.clear();
		imageButtons.clear();
		int j = this.height / 2 - 70;
		int i = 16;
		buttons.add(new GuiMyButton(66, !this.mc.isIntegratedServerRunning()? I18n.format("menu.disconnect"): I18n.format("menu.returnToMenu"), this.width / 2 - 150, j + i * 10, 120, 16));
		
		buttons.add(new GuiMyButton(0, I18n.format("menu.returnToGame"), this.width / 2 - 150, 
				j, 120, 16));
		buttons.add(new GuiMyButton(1, I18n.format("citytools.gamemenu.screenshot"), this.width / 2 - 150, 
				j + i * 1, 120, 16));
		buttons.add(new GuiMyButton(2, I18n.format("citytools.gamemenu.hotkeys"), this.width / 2 - 150, 
				j + i * 2, 120, 16));
		buttons.add(new GuiMyButton(3, I18n.format("citytools.gamemenu.ignorelist"), this.width / 2 - 150,
				j + i * 3, 120, 16));
		buttons.add(new GuiMyButton(4, I18n.format("citytools.gamemenu.chatnotice"), this.width / 2 - 150,
				j + i * 4, 120, 16));
		buttons.add(new GuiMyButton(5, I18n.format("gui.achievements"), this.width / 2 - 150, 
				j + i * 5, 120, 16));
		buttons.add(new GuiMyButton(6, I18n.format("gui.stats"), this.width / 2 - 150, 
				j + i * 6, 120, 16));
		buttons.add(new GuiMyButton(7, I18n.format("fml.menu.modoptions"), this.width / 2 - 150, 
				j + i * 7, 120, 16));
		buttons.add(new GuiMyButton(8, I18n.format("menu.options"), this.width / 2 - 150, 
				j + i * 8, 120, 16));
		
		imageButtons.add(new GuiImageButton(0, width - 48, height - 40, 32, 32, options_button, Lists.<String>newArrayList(I18n.format("citytools.gamemenu.options"))));
		imageButtons.add(new GuiImageButton(1, width - 80, height - 40, 32, 32, payrecord_button, Lists.<String>newArrayList(I18n.format("citytools.gamemenu.payrecord"))));
	}

	public void actionPerformed__(GuiImageButton button) {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new ConfigGui(mc.currentScreen));
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiPayRecord(this));
		}
	}

	public void actionPerformed_(GuiMyButton button) {
		if (button.id == 0) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		}
		if (button.id == 1) {
			File file1 = new File(mc.mcDataDir, "screenshots");
			String s = file1.getAbsolutePath().replace("./", "").replace(".\\", "");

			if (net.minecraft.util.Util.getOSType() == net.minecraft.util.Util.EnumOS.OSX) {
				try {
					logger.info(s);
					Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
					return;
				} catch (IOException ioexception1) {
					logger.error((String) "Couldn\'t open file", (Throwable) ioexception1);
				}
			} else if (net.minecraft.util.Util.getOSType() == net.minecraft.util.Util.EnumOS.WINDOWS) {
				String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });

				try {
					Runtime.getRuntime().exec(s1);
					return;
				} catch (IOException ioexception) {
					logger.error((String) "Couldn\'t open file", (Throwable) ioexception);
				}
			}

			boolean flag = false;

			try {
				Class<?> oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
				oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { file1.toURI() });
			} catch (Throwable throwable) {
				logger.error("Couldn\'t open link", throwable);
				flag = true;
			}

			if (flag) {
				logger.info("Opening via system class!");
				Sys.openURL("file://" + s);
			}
		}
		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiHotkey(this));
		}
		if (button.id == 3) {
			this.mc.displayGuiScreen(new GuiIgnore(this));
		}
		if (button.id == 4) {
			this.mc.displayGuiScreen(new GuiChatNotice(this));
		}
		if (button.id == 5) {
			if (this.mc.thePlayer != null)
				this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
		}
		if (button.id == 6) {
			if (this.mc.thePlayer != null)
				this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
		}
		if (button.id == 7) {
			this.mc.displayGuiScreen(new GuiModList(this));
		}
		if (button.id == 8) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (button.id == 66) {
			button.enabled = false;

			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld((WorldClient) null);

			if (this.mc.isIntegratedServerRunning()) {
				this.mc.getIntegratedServer().stopServer();
				this.mc.displayGuiScreen(new GuiMainMenu());
			} else if (this.mc.func_181540_al()) {
				RealmsBridge realmsbridge = new RealmsBridge();
				realmsbridge.switchToRealms(new GuiMainMenu());
			} else {
				if (Main.config_enable_mainmenu && !Main.config_mainmenu_showmultiplayer) {
					this.mc.displayGuiScreen(new GuiMainMenu());
				} else {
					this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
				}
			}

		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// this.drawDefaultBackground();

		// drawRectangle(0, 0, 110, this.width, time * 0.05F, 0.0F, 0.0F, 0.0F);

		if (time < 5) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-5.0F + time, 0.0F, 0.0F);

			Util.drawRectangle(this.width / 2 - 150, 0, 110, this.height, 0.1F + time * 0.1F, 255.0F, 255.0F, 255.0F);
		} else
			Util.drawRectangle(this.width / 2 - 150, 0, 110, this.height, 0.6F, 255.0F, 255.0F, 255.0F);
		
		for (int i = 0; i < this.buttons.size(); ++i) {
			((GuiMyButton) this.buttons.get(i)).drawButton(this.mc, mouseX, mouseY);
		}
		for (int i = 0; i < this.imageButtons.size(); ++i) {
			((GuiImageButton) this.imageButtons.get(i)).drawButton(this.mc, mouseX, mouseY);
		}
		boolean flag = mc.getNetHandler() != null;
		GlStateManager.pushMatrix();
		float f = 2f;
		GlStateManager.translate(this.width / 2 - 140,
				height / 2 - 75 - mc.fontRendererObj.FONT_HEIGHT * (f + 1 + (flag ? 1 : 0)), 0.0F);
		GlStateManager.scale(f, f, f);
		mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.gamemenu.title"), 0, 0, -1);

		GlStateManager.popMatrix();
		mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.gamemenu.subtitle"), this.width / 2 - 140,
				height / 2 - 75 - mc.fontRendererObj.FONT_HEIGHT * (1 + (flag ? 1 : 0)), -1);
		if (flag) {
			mc.fontRendererObj.drawStringWithShadow(
							mc.getNetHandler().getPlayerInfoMap().size() == 1 ? I18n.format("citytools.gamemenu.alone")
									: I18n.format("citytools.gamemenu.online",
											mc.getNetHandler().getPlayerInfoMap().size()),
							this.width / 2 - 140, height / 2 - 75 - mc.fontRendererObj.FONT_HEIGHT, -1);
		}
		if (time < 5)
			GlStateManager.popMatrix();
		
		for (int i = 0; i < this.imageButtons.size(); ++i) {
			((GuiImageButton) this.imageButtons.get(i)).drawLore(this.mc, mouseX, mouseY, this.width, this.height);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		}
	}

	@Override
	public void updateScreen() {
		SoundHandler soundhandler = this.mc.getSoundHandler();

		for (int i = 0; i < this.buttons.size(); ++i) {
			((GuiMyButton) this.buttons.get(i)).updateScreen();
		}

		soundhandler.update();
		if (this.time < 5) {
			this.time++;
		}
	}
}