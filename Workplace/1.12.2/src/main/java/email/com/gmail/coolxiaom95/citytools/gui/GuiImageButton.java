package email.com.gmail.coolxiaom95.citytools.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiImageButton extends Gui
{
    ResourceLocation buttonTextures;
    /** Button width in pixels */
    public int width;
    /** Button height in pixels */
    public int height;
    /** The x position of this control. */
    public int xPosition;
    /** The y position of this control. */
    public int yPosition;
    /** The string displayed on this control. */
    public int id;
    /** True if this control is enabled, false to disable. */
    public boolean enabled;
    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    public int packedFGColour; //FML
    
    List<String> lore;
    
    public boolean pressed = false;
    
    public GuiImageButton(int buttonId, int x, int y, int width, int height, ResourceLocation buttonTextures)
    {
    	this(buttonId, x, y ,width, height, buttonTextures, null);
    }
    
    public GuiImageButton(int buttonId, int x, int y, int width, int height, ResourceLocation buttonTextures, List<String> lore)
    {
        this.width = 200;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.buttonTextures = buttonTextures;
        this.lore = lore;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;
        
        if(pressed) i = 3;
        
        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
            if(pressed) i = 4;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, i * this.height, this.width, this.height, this.width , this.height * 3);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
    	soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

	public void drawLore(Minecraft mc, int mouseX, int mouseY, int screenWidth, int screenHeight) {
		if(this.hovered && lore != null) {
			if(lore.size() != 0 && !lore.isEmpty()) {
				GlStateManager.disableRescaleNormal();
	            RenderHelper.disableStandardItemLighting();
	            GlStateManager.disableLighting();
	            GlStateManager.disableDepth();
	            int i = 0;

	            for (String s : lore)
	            {
	                int j = mc.fontRenderer.getStringWidth(s);

	                if (j > i)
	                {
	                    i = j;
	                }
	            }

	            int l1 = mouseX + 12;
	            int i2 = mouseY - 12;
	            int k = 8;

	            if (lore.size() > 1)
	            {
	                k += 2 + (lore.size() - 1) * 10;
	            }

	            if (l1 + i > screenWidth)
	            {
	                l1 -= 28 + i;
	            }

	            if (i2 + k + 6 > screenHeight)
	            {
	                i2 = screenHeight - k - 6;
	            }

	            this.zLevel = 300.0F;
	            //this.itemRender.zLevel = 300.0F;
	            int l = -267386864;
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

	            for (int k1 = 0; k1 < lore.size(); ++k1)
	            {
	                String s1 = (String)lore.get(k1);
	                mc.fontRenderer.drawStringWithShadow(s1, (float)l1, (float)i2, -1);

	                if (k1 == 0)
	                {
	                    i2 += 2;
	                }

	                i2 += 10;
	            }

	            this.zLevel = 0.0F;
	            //this.itemRender.zLevel = 0.0F;
	            GlStateManager.enableLighting();
	            GlStateManager.enableDepth();
	            RenderHelper.enableStandardItemLighting();
	            GlStateManager.enableRescaleNormal();
			}
		}
	}
}