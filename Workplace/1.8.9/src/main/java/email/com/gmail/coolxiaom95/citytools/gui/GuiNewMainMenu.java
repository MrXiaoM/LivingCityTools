package email.com.gmail.coolxiaom95.citytools.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.util.GuiNewList;
import email.com.gmail.coolxiaom95.citytools.util.MainMenuServerList;
import email.com.gmail.coolxiaom95.citytools.util.ServerList;
import email.com.gmail.coolxiaom95.citytools.util.ServerListEntry;
import email.com.gmail.coolxiaom95.citytools.util.ServerPinger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

@SideOnly(Side.CLIENT)
public class GuiNewMainMenu extends GuiScreen implements GuiYesNoCallback {

	public static class MyGuiYesNoCallback implements GuiYesNoCallback {
		String url;
		GuiScreen last;

		public MyGuiYesNoCallback(GuiScreen gui, String url) {
			this.url = url;
			last = gui;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void confirmClicked(boolean arg0, int arg1) {
			if (arg0) {
				if (arg1 == 13) {

					try {
						@SuppressWarnings("rawtypes")
						Class oclass = Class.forName("java.awt.Desktop");
						Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null,
								new Object[0]);
						oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
								new Object[] { new URI(this.url) });
					} catch (Throwable throwable) {
						logger.error("Couldn\'t open link", throwable);
					}
				}
			}

			Minecraft.getMinecraft().displayGuiScreen(last);
		}
	}

	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");

	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	private String splashText;

	private int panoramaTimer;

	private DynamicTexture viewportTexture;
	private ResourceLocation backgroundTexture;
	private final ServerPinger oldServerPinger = new ServerPinger();
	private MainMenuServerList serverListSelector;

	private ServerList savedServerList;

	private boolean directConnect;
	private String hoveringText;
	private ServerData selectedServer;

	private boolean initialized;

	String copyright = "§eMinecraft 由 Mojang AB 版权所有，禁止盗版！";
	Calendar calendar = Calendar.getInstance();

	private boolean isBgCustomModExist = false;
	public GuiNewMainMenu() {
		try {
			Class.forName("top.mrxiaom.custombg.Util");
			this.isBgCustomModExist = true;
		}
		catch(ClassNotFoundException e) {
			this.isBgCustomModExist = false;
		}
		net.minecraftforge.fml.client.FMLClientHandler.instance().setupServerList();
		this.splashText = "missingno";
		BufferedReader bufferedreader = null;

		try {
			List<String> list = Lists.<String>newArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),
					Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					list.add(s);
				}
			}

			if (!list.isEmpty()) {
				while (true) {
					this.splashText = list.get(RANDOM.nextInt(list.size()));

					if (this.splashText.hashCode() != 125780783) {
						break;
					}
				}
			}
		} catch (IOException var12) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException var11) {
					;
				}
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (button.id == 2) {
			this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
		}

		if (button.id == 3) {
			openLink("http://www.pds.ink/forum.php?fromuid=49398");
		}

		if (button.id == 4) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (button.id == 5) {
			this.mc.shutdown();
		}

		if (button.id == 6) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (button.id == 7) {
			this.directConnect = true;
			this.mc.displayGuiScreen(new GuiScreenServerList(this,
					this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "",
							false)));
		}

		if (button.id == 8) {
			this.refreshServerList();
		}

		if (button.id == 9) {
			openLink("https://jq.qq.com/?_wv=1027&k=57GPSmX");
		}

		if (button.id == 10) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
	}

	private void addButtons(int p_73969_1_, int p_73969_2_) {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 150, p_73969_1_ + p_73969_2_ * 0, 65, 20,
				I18n.format("menu.singleplayer")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 150, p_73969_1_ + p_73969_2_ * 1, 65, 20,
				I18n.format("fml.menu.mods")));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 150, p_73969_1_ + p_73969_2_ * 2, 65, 20,
				I18n.format("citytools.mainmenu.forum")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 150, p_73969_1_ + p_73969_2_ * 3, 65, 20,
				I18n.format("menu.options")));
		this.buttonList.add(new GuiButtonLanguage(6, this.width / 2 - 174, p_73969_1_ + p_73969_2_ * 3));

		this.buttonList.add(new GuiButton(7, this.width / 2 + 85, p_73969_1_ + p_73969_2_ * 0, 65, 20,
				I18n.format("selectServer.direct")));
		this.buttonList.add(new GuiButton(8, this.width / 2 + 85, p_73969_1_ + p_73969_2_ * 1, 65, 20,
				I18n.format("selectServer.refresh")));
		this.buttonList.add(new GuiButton(9, this.width / 2 + 85, p_73969_1_ + p_73969_2_ * 2, 65, 20,
				I18n.format("citytools.mainmenu.qq")));
		this.buttonList.add(
				new GuiButton(5, this.width / 2 + 85, p_73969_1_ + p_73969_2_ * 3, 65, 20, I18n.format("menu.quit")));

		if (Main.config_mainmenu_showmultiplayer) {
			this.buttonList.add(new GuiButton(10, this.width / 2 - 150, p_73969_1_ + p_73969_2_ * 4, 65, 20,
					I18n.format(I18n.format("menu.multiplayer"))));
		}
	}

	@Override
	public void confirmClicked(boolean result, int id) {
		if (this.directConnect) {
			this.directConnect = false;

			if (result) {
				this.connectToServer(this.selectedServer);
			} else {
				this.mc.displayGuiScreen(this);
			}
		}
	}

	public void connectToSelected() {
		GuiNewList.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null
				: this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());

		this.connectToServer(((ServerListEntry) guilistextended$iguilistentry).getServerData());
	}

	private void connectToServer(ServerData server) {
		net.minecraftforge.fml.client.FMLClientHandler.instance().connectToServer(this, server);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void drawHoveringBox(int x, int y, int widthIn, int heightIn) {
		GlStateManager.pushMatrix();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		int i = widthIn;

		int l1 = x + 12;
		int i2 = y - 12;
		int k = heightIn;

		this.zLevel = 300.0F;
		this.itemRender.zLevel = 300.0F;
		// int l = -267386864;
		int l = 0x99000000;
		this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
		this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
		this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
		this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
		this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
		// int i1 = 1347420415;
		int i1 = (int) 2415984639L;
		int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
		this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
		this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
		this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
		this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void drawJumpText() {
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.width / 2 + 90, this.height / 2 - 50.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper
				.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		f = f * 100.0F / (this.fontRendererObj.getStringWidth(this.splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();

	}

	public void drawMyHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
		if (!textLines.isEmpty()) {
			GlStateManager.pushMatrix();
			int i = 0;

			for (String s : textLines) {
				int j = font.getStringWidth(s);

				if (j > i) {
					i = j;
				}
			}

			int l1 = x + 12;
			int i2 = y - 12;
			int k = 8;

			if (textLines.size() > 1) {
				k += 2 + (textLines.size() - 1) * 10;
			}

			if (l1 + i > this.width) {
				l1 -= 28 + i;
			}

			if (i2 + k + 6 > this.height) {
				i2 = this.height - k - 6;
			}

			this.zLevel = 300.0F;
			this.itemRender.zLevel = 300.0F;
			int l = (int) 2684354560L;
			this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
			this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
			this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
			this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
			this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
			int i1 = 1347420415;
			int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
			this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
			this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
			this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
			this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

			for (int k1 = 0; k1 < textLines.size(); ++k1) {
				String s1 = textLines.get(k1);
				font.drawStringWithShadow(s1, l1, i2, -1);

				if (k1 == 0) {
					i2 += 2;
				}

				i2 += 10;
			}

			this.zLevel = 0.0F;
			this.itemRender.zLevel = 0.0F;
			GlStateManager.popMatrix();
			GlStateManager.resetColor();
		}
	}

	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		int i = 8;

		for (int j = 0; j < i * i; ++j) {
			GlStateManager.pushMatrix();
			float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
			float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F,
					0.0F, 0.0F);
			GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k) {
				GlStateManager.pushMatrix();

				if (k == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.hoveringText = null;

		GlStateManager.disableAlpha();
		if(this.isBgCustomModExist) {
			top.mrxiaom.custombg.Util.drawBackground();
		}else {
			this.renderSkybox(mouseX, mouseY, partialTicks);
		}
		GlStateManager.enableAlpha();
		int i = 274;
		int j = this.width / 2 - i / 2;
		int k = this.height / 2 - 90;
		if(!this.isBgCustomModExist) {
			this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
			this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		}
		this.drawHoveringBox(this.width / 2 - 88, this.height / 2 - 8, 152, 124);
		this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);

		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
		

		drawJumpText();
		copyright = "§e§l" + ((mouseX >= this.width - this.fontRendererObj.getStringWidth(copyright) - 2
				&& mouseY >= this.height - this.fontRendererObj.FONT_HEIGHT - 2 && mouseX < this.width
				&& mouseY < this.height) ? "§n" : "") + "Minecraft 由 Mojang AB 版权所有，禁止盗版！";
		this.drawString(this.fontRendererObj, copyright,
				this.width - this.fontRendererObj.getStringWidth(copyright) - 2,
				this.height - this.fontRendererObj.FONT_HEIGHT - 2, -1);
		String networkfix = "§b"
				+ ((mouseX > this.width
						- mc.fontRendererObj.getStringWidth(I18n.format("citytools.networkfix.buttonname"))
						&& mouseX < this.width && mouseY > this.height - (mc.fontRendererObj.FONT_HEIGHT * 2) - 4
						&& mouseY < this.height - mc.fontRendererObj.FONT_HEIGHT - 4) ? "§n" : "")
				+ I18n.format("citytools.networkfix.buttonname");
		this.drawString(this.fontRendererObj, networkfix,
				this.width - this.fontRendererObj.getStringWidth(networkfix) - 2,
				this.height - (this.fontRendererObj.FONT_HEIGHT * 2) - 4, -1);
		String s2 = "§a§l欢迎! “" + mc.getSession().getUsername() + "”";
		String s3 = "§r我的世界 1.8.9 §l[ 生存都市客户端 ]";

		this.drawString(this.fontRendererObj, s2, 2, this.height - this.fontRendererObj.FONT_HEIGHT * 2, -1);
		this.drawString(this.fontRendererObj, s3, 2, this.height - this.fontRendererObj.FONT_HEIGHT * 1, -1);

		super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.hoveringText != null) {
			drawMyHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY,
					fontRendererObj);
		}
	}

	void drawsth() {
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.width / 2, 75.0F, 0.0F);
		float f = 2F - MathHelper
				.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, 0, -1);
		GlStateManager.popMatrix();
	}

	public ServerPinger getOldServerPinger() {
		return this.oldServerPinger;
	}

	public ServerList getServerList() {
		return this.savedServerList;
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.serverListSelector.handleMouseInput();
	}

	@Override
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background",
				this.viewportTexture);
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
		}

		int j = this.height / 2 - 20;

		this.addButtons(j, 24);

		Keyboard.enableRepeatEvents(true);

		if (!this.initialized) {
			mc.func_181535_r().func_181557_a();
			this.initialized = true;
			this.savedServerList = new ServerList(this.mc);

			this.savedServerList.loadServerList();

			this.serverListSelector = new MainMenuServerList(this, this.mc, this.width, 100, this.height / 2 - 20,
					this.height / 2 + 200, 24);
			this.serverListSelector.func_148195_a(this.savedServerList);
		} else {
			this.serverListSelector.setDimensions(this.width, 100, this.height / 2 - 20, this.height / 2 + 200);
		}

		this.mc.setConnectedToRealms(false);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		int i = this.serverListSelector.func_148193_k();
		GuiNewList.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.getListEntry(i);

		if (keyCode == 63) {
			this.refreshServerList();
		} else {
			if (i >= 0) {
				if (keyCode == 200) {
					if (isShiftKeyDown()) {
						if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
							this.savedServerList.swapServers(i, i - 1);
							this.selectServer(this.serverListSelector.func_148193_k() - 1);
							this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
							this.serverListSelector.func_148195_a(this.savedServerList);
						}
					} else if (i > 0) {
						this.selectServer(this.serverListSelector.func_148193_k() - 1);
						this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());

						if (this.serverListSelector.getListEntry(
								this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
							if (this.serverListSelector.func_148193_k() > 0) {
								this.selectServer(this.serverListSelector.getSize() - 1);
								this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
							} else {
								this.selectServer(-1);
							}
						}
					} else {
						this.selectServer(-1);
					}
				} else if (keyCode == 208) {
					if (isShiftKeyDown()) {
						if (i < this.savedServerList.countServers() - 1) {
							this.savedServerList.swapServers(i, i + 1);
							this.selectServer(i + 1);
							this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
							this.serverListSelector.func_148195_a(this.savedServerList);
						}
					} else if (i < this.serverListSelector.getSize()) {
						this.selectServer(this.serverListSelector.func_148193_k() + 1);
						this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());

						if (this.serverListSelector.getListEntry(
								this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
							if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1) {
								this.selectServer(this.serverListSelector.getSize() + 1);
								this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
							} else {
								this.selectServer(-1);
							}
						}
					} else {
						this.selectServer(-1);
					}
				} else if (keyCode != 28 && keyCode != 156) {
					super.keyTyped(typedChar, keyCode);
				} else {
					this.actionPerformed(this.buttonList.get(2));
				}
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX > this.width - mc.fontRendererObj.getStringWidth(copyright) && mouseX < this.width
				&& mouseY > this.height - mc.fontRendererObj.FONT_HEIGHT - 2 && mouseY < this.height - 2) {
			mc.displayGuiScreen(new GuiCredit(this));
		} else if (mouseX > this.width
				- mc.fontRendererObj.getStringWidth(I18n.format("citytools.networkfix.buttonname"))
				&& mouseX < this.width && mouseY > this.height - (mc.fontRendererObj.FONT_HEIGHT * 2) - 4
				&& mouseY < this.height - mc.fontRendererObj.FONT_HEIGHT - 4) {
			mc.displayGuiScreen(new GuiNetworkFix(this));
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		this.serverListSelector.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

		this.oldServerPinger.clearPendingNetworks();
	}

	public void openLink(String url) {
		GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(new MyGuiYesNoCallback(this, url), url, 13,
				true);
		this.mc.displayGuiScreen(guiconfirmopenlink);
	}

	private void refreshServerList() {
		this.savedServerList = new ServerList(this.mc);
		this.savedServerList.loadServerList();
		this.serverListSelector = new MainMenuServerList(this, this.mc, this.width, 100, this.height / 2 - 20,
				this.height / 2 + 200, 24);
		this.serverListSelector.func_148195_a(this.savedServerList);
	}

	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.rotateAndBlurSkybox(p_73971_3_);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
		float f1 = this.height * f / 256.0F;
		float f2 = this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, j, this.zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(i, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	private void rotateAndBlurSkybox(float p_73968_1_) {
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;

		for (int j = 0; j < i; ++j) {
			float f = 1.0F / (j + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (j - i / 2) / 256.0F;
			worldrenderer.pos(k, l, this.zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(k, 0.0D, this.zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, l, this.zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	public void selectServer(int index) {
		this.serverListSelector.setSelectedSlotIndex(index);
	}

	public void setHoveringText(String p_146793_1_) {
		this.hoveringText = p_146793_1_;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.oldServerPinger.pingPendingNetworks();

		++this.panoramaTimer;
	}

}
