package email.com.gmail.coolxiaom95.citytools.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiMyButton extends Gui {

	public int id;

	public int x;
	public int y;
	public int width = 50;
	public int height = 20;
	public boolean enabled = true;

	public String text = "";

	boolean hovered = false;

	int time = 0;

	public GuiMyButton(int buttonID, String buttonText, int buttonX, int buttonY) {
		id = buttonID;
		text = buttonText;
		x = buttonX;
		y = buttonY;
	}

	public GuiMyButton(int buttonID, String buttonText, int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
		id = buttonID;
		text = buttonText;
		x = buttonX;
		y = buttonY;
		width = buttonWidth;
		height = buttonHeight;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
				&& mouseY < this.y + this.height;
		if (time > 0)
			drawRectangle(x + time, y, width, height, 0.0F + time * 0.06F, 255.0F, 255.0F, 255.0F);
		mc.fontRendererObj.drawStringWithShadow((enabled ? "§f" : "§7") + text + "§r", x + time + 10,
				y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
	}

	public void mouseReleased(int mouseX, int mouseY) {
	}

	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return enabled && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
				&& mouseY < this.y + this.height;
	}

	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}

	public void updateScreen() {
		if (hovered) {
			if (time < 6)
				time += 2;
		} else {
			if (time != 0) {
				time -= 2;
				if (time < 0)
					time = 0;
			}
		}
	}

	private void drawRectangle(int rX, int rY, int rWidth, int rHeight, float a, float r, float g, float b) {
		int left = rX;
		int top = rY;
		int right = rX + rWidth;
		int bottom = rY + rHeight;

		if (left < right) {
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			int j = top;
			top = bottom;
			bottom = j;
		}

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(r, g, b, a);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
		worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
		worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
		worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
}
