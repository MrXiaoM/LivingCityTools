package email.com.gmail.coolxiaom95.citytools.util;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import email.com.gmail.coolxiaom95.citytools.gui.GuiNewMainMenu;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class ServerListEntry implements GuiNewList.IGuiListEntry {
	private static final Logger logger = LogManager.getLogger();
	private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5,
			(new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
	private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("citytools",
			"textures/gui/unknown_server.png");
	private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation(
			"textures/gui/server_selection.png");
	private final GuiNewMainMenu field_148303_c;
	private final Minecraft mc;
	private final ServerData field_148301_e;
	private final ResourceLocation field_148306_i;
	private String field_148299_g;
	private DynamicTexture field_148305_h;
	private long field_148298_f;

	protected ServerListEntry(GuiNewMainMenu p_i45048_1_, ServerData p_i45048_2_) {
		this.field_148303_c = p_i45048_1_;
		this.field_148301_e = p_i45048_2_;
		this.mc = Minecraft.getMinecraft();
		this.field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
		this.field_148305_h = (DynamicTexture) this.mc.getTextureManager().getTexture(this.field_148306_i);
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected) {
		if (!this.field_148301_e.pinged) {
			this.field_148301_e.pinged = true;
			this.field_148301_e.pingToServer = -2L;
			this.field_148301_e.serverMOTD = "正在连接...";
			this.field_148301_e.populationInfo = "";
			field_148302_b.submit(new Runnable() {
				@Override
				public void run() {
					try {
						ServerListEntry.this.field_148303_c.getOldServerPinger()
								.ping(ServerListEntry.this.field_148301_e);
					} catch (UnknownHostException var2) {
						ServerListEntry.this.field_148301_e.pingToServer = -1L;
						ServerListEntry.this.field_148301_e.serverMOTD = "§4无法解析域名";
					} catch (Exception var3) {
						ServerListEntry.this.field_148301_e.pingToServer = -1L;
						ServerListEntry.this.field_148301_e.serverMOTD = "§4无法连接到服务器";
					}
				}
			});
		}

		boolean flag = this.field_148301_e.version > 340;
		boolean flag1 = this.field_148301_e.version < 340;
		boolean flag2 = flag || flag1;
		this.mc.fontRenderer.drawString(this.field_148301_e.serverName, x + 24 + 3, y + 1, 16777215);
		List<String> list = this.mc.fontRenderer
				.listFormattedStringToWidth(net.minecraftforge.fml.client.FMLClientHandler.instance()
						.fixDescription(this.field_148301_e.serverMOTD), listWidth - 48 - 2);

		if (list.size() > 1) {
			this.mc.fontRenderer.drawString("§e>> §f§l双击进入§e <<", x + 24 + 3, y + 12, 8421504);
		} else {
			for (int i = 0; i < Math.min(list.size(), 2); ++i) {
				this.mc.fontRenderer.drawString(list.get(i), x + 24 + 3,
						y + 11 + this.mc.fontRenderer.FONT_HEIGHT * i, 8421504);
			}
		}

		String s2 = flag2 ? "§4" + this.field_148301_e.gameVersion
				: this.field_148301_e.populationInfo;
		int j = this.mc.fontRenderer.getStringWidth(s2);
		this.mc.fontRenderer.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
		int k = 0;
		String s = null;
		int l;
		String s1;

		if (flag2) {
			l = 5;
			s1 = flag ? "客户端已过期!" : "服务端已过期!";
			s = this.field_148301_e.playerList;
		} else if (this.field_148301_e.pinged && this.field_148301_e.pingToServer != -2L) {
			if (this.field_148301_e.pingToServer < 0L) {
				l = 5;
			} else if (this.field_148301_e.pingToServer < 150L) {
				l = 0;
			} else if (this.field_148301_e.pingToServer < 300L) {
				l = 1;
			} else if (this.field_148301_e.pingToServer < 600L) {
				l = 2;
			} else if (this.field_148301_e.pingToServer < 1000L) {
				l = 3;
			} else {
				l = 4;
			}

			if (this.field_148301_e.pingToServer < 0L) {
				s1 = "(无连接)";
			} else {
				s1 = "延迟: " + this.field_148301_e.pingToServer + "ms";
				s = this.field_148301_e.playerList;
			}
		} else {
			k = 1;
			l = (int) (Minecraft.getSystemTime() / 100L + slotIndex * 2 & 7L);

			if (l > 4) {
				l = 8 - l;
			}

			s1 = "正在检测...";
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(Gui.ICONS);
		Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, k * 10, 176 + l * 8, 10, 8, 256.0F, 256.0F);

		if (this.field_148301_e.getBase64EncodedIconData() != null
				&& !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g)) {
			this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
			this.prepareServerIcon();
			this.field_148303_c.getServerList().saveServerList();
		}

		if (this.field_148305_h != null) {
			this.drawServerIcon(x, y, this.field_148306_i);
		} else {
			this.drawServerIcon(x, y, UNKNOWN_SERVER);
		}

		int i1 = mouseX - x;
		int j1 = mouseY - y;

		if (i1 >= listWidth - 15 && i1 <= listWidth - 5 && j1 >= 0 && j1 <= 8) {
			this.field_148303_c.setHoveringText(s1);
		} else if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8) {
			this.field_148303_c.setHoveringText(s);
		}

		if (this.mc.gameSettings.touchscreen || isSelected) {
			this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
			Gui.drawRect(x, y, x + 20, y + 20, -1601138544);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			int k1 = mouseX - x;

			if (k1 < 24 && k1 > 12) {
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 20.0F, 20, 20, 160.0F, 160.0F);
			} else {
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 20, 20, 160.0F, 160.0F);
			}

		}
	}

	protected void drawServerIcon(int x, int y, ResourceLocation p_178012_3_) {
		this.mc.getTextureManager().bindTexture(p_178012_3_);
		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 20, 20, 20.0F, 20.0F);
		GlStateManager.disableBlend();
	}

	public ServerData getServerData() {
		return this.field_148301_e;
	}

	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
			int p_148278_6_) {
		if (p_148278_5_ <= 24) {
			if (p_148278_5_ < 24 && p_148278_5_ > 12) {
				this.field_148303_c.selectServer(slotIndex);
				this.field_148303_c.connectToSelected();
				return true;
			}
		}

		this.field_148303_c.selectServer(slotIndex);

		if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
			this.field_148303_c.connectToSelected();
		}

		this.field_148298_f = Minecraft.getSystemTime();
		return false;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent,
	 * relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
	}

	private void prepareServerIcon() {
		if (this.field_148301_e.getBase64EncodedIconData() == null) {
			this.mc.getTextureManager().deleteTexture(this.field_148306_i);
			this.field_148305_h = null;
		} else {
			ByteBuf bytebuf = Unpooled.copiedBuffer(this.field_148301_e.getBase64EncodedIconData(), Charsets.UTF_8);
			ByteBuf bytebuf1 = Base64.decode(bytebuf);
			BufferedImage bufferedimage;
			label101: {
				try {
					bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf1));
					Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
					Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
					break label101;
				} catch (Throwable throwable) {
					logger.error("Invalid icon for server " + this.field_148301_e.serverName + " ("
							+ this.field_148301_e.serverIP + ")", throwable);
					this.field_148301_e.setBase64EncodedIconData((String) null);
				} finally {
					bytebuf.release();
					bytebuf1.release();
				}

				return;
			}

			if (this.field_148305_h == null) {
				this.field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
				this.mc.getTextureManager().loadTexture(this.field_148306_i, this.field_148305_h);
			}

			bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(),
					this.field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
			this.field_148305_h.updateDynamicTexture();
		}
	}

	@Override
	public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
	}
}