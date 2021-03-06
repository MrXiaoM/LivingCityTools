package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiCredit extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
	private static final ResourceLocation XIAOM = new ResourceLocation("citytools",
			"textures/gui/credits/mrxiaom.png");
	private static final ResourceLocation THEBIRD = new ResourceLocation("citytools",
			"textures/gui/credits/thebird.png");
	private static final ResourceLocation COLDBIRD = new ResourceLocation("citytools",
			"textures/gui/credits/cold_bird.png");
	private static final ResourceLocation MIMA = new ResourceLocation("citytools", 
			"textures/gui/credits/mima0.png");
	private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
	private int field_146581_h;
	private List<String> field_146582_i;
	private int field_146579_r;
	private float field_146578_s = 0.5F;
	private GuiScreen last;

	public GuiCredit(GuiScreen lastScreen) {
		last = lastScreen;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	private void drawCreditScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		int i = this.width;
		float f = 0.0F - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
		float f1 = this.height - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
		float f2 = 0.015625F;
		float f3 = (this.field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
		float f4 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
		float f5 = (f4 - 20.0F - (this.field_146581_h + p_146575_3_)) * 0.005F;

		if (f5 < f3) {
			f3 = f5;
		}

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		f3 = f3 * f3;
		f3 = f3 * 96.0F / 255.0F;
		worldrenderer.pos(0.0D, this.height, this.zLevel).tex(0.0D, f * f2).color(f3, f3, f3, 1.0F).endVertex();
		worldrenderer.pos(i, this.height, this.zLevel).tex(i * f2, f * f2).color(f3, f3, f3, 1.0F).endVertex();
		worldrenderer.pos(i, 0.0D, this.zLevel).tex(i * f2, f1 * f2).color(f3, f3, f3, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, f1 * f2).color(f3, f3, f3, 1.0F).endVertex();
		tessellator.draw();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawCreditScreen(mouseX, mouseY, partialTicks);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		int i = 274;
		int j = this.width / 2 - i / 2;
		int k = this.height + 50;
		float f = -(this.field_146581_h + partialTicks) * this.field_146578_s;
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, f, 0.0F);
		this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
		this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);

		float icon_size = 64;
		int p = k + 120;
		mc.getTextureManager().bindTexture(THEBIRD);
		Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 128, p, 0.0F, 0.0F, (int) icon_size, (int) icon_size,
				icon_size, icon_size);
		String s_tb_1 = "??6??la ??????The bird?????r";
		String s_tb_2 = "??b?????????????????????????????r";
		mc.fontRenderer.drawStringWithShadow(s_tb_1,
				this.width / 2 - 128 + 32 - mc.fontRenderer.getStringWidth(s_tb_1) / 2, p + 70, -1);
		mc.fontRenderer.drawStringWithShadow(s_tb_2,
				this.width / 2 - 128 + 32 - mc.fontRenderer.getStringWidth(s_tb_2) / 2,
				p + 70 + mc.fontRenderer.FONT_HEIGHT + 2, -1);

		mc.getTextureManager().bindTexture(MIMA);
		Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 32, p, 0.0F, 0.0F, (int) icon_size, (int) icon_size,
				icon_size, icon_size);
		String s_mm_1 = "??f??l?????? mima0??r";
		String s_mm_2 = "??b????????????????????????????????r";
		mc.fontRenderer.drawStringWithShadow(s_mm_1, 
				this.width / 2 - mc.fontRenderer.getStringWidth(s_mm_1) / 2,
				p + 70, -1);
		mc.fontRenderer.drawStringWithShadow(s_mm_2, 
				this.width / 2 - mc.fontRenderer.getStringWidth(s_mm_2) / 2,
				p + 70 + mc.fontRenderer.FONT_HEIGHT + 2, -1);

		mc.getTextureManager().bindTexture(XIAOM);
		Gui.drawModalRectWithCustomSizedTexture(this.width / 2 + 64, p, 0.0F, 0.0F, (int) icon_size, (int) icon_size,
				icon_size, icon_size);
		String s_xm_1 = "??a??l?????? Mr_Xiao_M??r";
		String s_xm_2 = "??b??????????????? Mod ????????r";
		mc.fontRenderer.drawStringWithShadow(s_xm_1,
				this.width / 2 + 64 + 32 - mc.fontRenderer.getStringWidth(s_xm_1) / 2, p + 70, -1);
		mc.fontRenderer.drawStringWithShadow(s_xm_2,
				this.width / 2 + 64 + 32 - mc.fontRenderer.getStringWidth(s_xm_2) / 2,
				p + 70 + mc.fontRenderer.FONT_HEIGHT + 2, -1);

		mc.getTextureManager().bindTexture(COLDBIRD);
		Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 32, p + 100, 0.0F, 0.0F, (int) icon_size,
				(int) icon_size, icon_size, icon_size);
		String s_cb_1 = "??b??l?????? cold_bird??r";
		String s_cb_2 = "??b??????????????????????????r";
		mc.fontRenderer.drawStringWithShadow(s_cb_1, this.width / 2 - mc.fontRenderer.getStringWidth(s_cb_1) / 2,
				p + 170, -1);
		mc.fontRenderer.drawStringWithShadow(s_cb_2, this.width / 2 - mc.fontRenderer.getStringWidth(s_cb_2) / 2,
				p + 170 + mc.fontRenderer.FONT_HEIGHT + 2, -1);

		int l = k + 350;

		for (int i1 = 0; i1 < this.field_146582_i.size(); ++i1) {
			if (i1 == this.field_146582_i.size() - 1) {
				float f1 = l + f - (this.height / 2 - 6);

				if (f1 < 0.0F) {
					GlStateManager.translate(0.0F, -f1, 0.0F);
				}
			}

			if (l + f + 12.0F + 8.0F > 0.0F && l + f < this.height) {
				String s = this.field_146582_i.get(i1);

				if (s.startsWith("[C]")) {
					this.fontRenderer.drawStringWithShadow(s.substring(3),
							j + (i - this.fontRenderer.getStringWidth(s.substring(3))) / 2, l, 16777215);
				} else {
					this.fontRenderer.fontRandom.setSeed(i1 * 4238972211L + this.field_146581_h / 4);
					this.fontRenderer.drawStringWithShadow(s, j, l, 16777215);
				}
			}

			l += 12;
		}

		GlStateManager.popMatrix();
		this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(0, 769);
		int j1 = this.width;
		int k1 = this.height;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, k1, this.zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(j1, k1, this.zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(j1, 0.0D, this.zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();

		String s1 = I18n.format("citytools.credits.esc");
		this.drawString(mc.fontRenderer, s1, this.width - mc.fontRenderer.getStringWidth(s1) - 10,
				this.height - mc.fontRenderer.FONT_HEIGHT - 10, -256);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initGui() {
		mc.getSoundHandler().stopSounds();
		mc.getMusicTicker().playMusic(MusicType.CREDITS);
		if (this.field_146582_i == null) {
			this.field_146582_i = Lists.<String>newArrayList();

			try {
				String s = "";
				int i = 274;
				InputStream inputstream = this.mc.getResourceManager()
						.getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));

				while ((s = bufferedreader.readLine()) != null) {
					s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
					s = s.replaceAll("\t", "    ");
					this.field_146582_i.addAll(this.mc.fontRenderer.listFormattedStringToWidth(s, i));
					this.field_146582_i.add("");
				}

				inputstream.close();
				this.field_146579_r = this.field_146582_i.size() * 12;
			} catch (Exception exception) {
				logger.error("Couldn\'t load credits", exception);
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			mc.getSoundHandler().stopSounds();
			this.mc.displayGuiScreen(last);
		}
	}
	 
	@Override
	public void updateScreen() {
		MusicTicker musicticker = this.mc.getMusicTicker();
		SoundHandler soundhandler = this.mc.getSoundHandler();

		if (this.field_146581_h == 0) {
			musicticker.update();
			musicticker.playMusic(MusicTicker.MusicType.CREDITS);
			soundhandler.resumeSounds();

		}

		soundhandler.update();
		this.field_146581_h++;
		float f = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;

		if (this.field_146581_h > f) {
			this.mc.displayGuiScreen(last);
		}
	}
}