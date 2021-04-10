package email.com.gmail.coolxiaom95.citytools.gui.globalshop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public abstract class GuiGlobalShopSlot {
	protected final Minecraft mc;
	public int width;
	public int height;
	/** The top of the slot container. Affects the overlays and scrolling. */
	public int top;
	/** The bottom of the slot container. Affects the overlays and scrolling. */
	public int bottom;
	public int right;
	public int left;
	/** The height of a slot. */
	public final int slotHeight;
	/** The buttonID of the button used to scroll up */
	private int scrollUpButtonID;
	/** The buttonID of the button used to scroll down */
	private int scrollDownButtonID;
	protected int mouseX;
	protected int mouseY;
	protected boolean field_148163_i = true;
	/** Where the mouse was in the window when you first clicked to scroll */
	protected int initialClickY = -2;
	/**
	 * What to multiply the amount you moved your mouse by (used for slowing down
	 * scrolling when over the items and not on the scroll bar)
	 */
	protected float scrollMultiplier;
	/** How far down this slot has been scrolled */
	protected float amountScrolled;
	/** The element in the list that was selected */
	protected int selectedElement = -1;
	/** The time when this button was last clicked. */
	protected long lastClicked;
	protected boolean field_178041_q = true;
	/** Set to true if a selected element in this gui will show an outline box */
	protected boolean showSelectionBox = true;
	protected boolean hasListHeader;
	public int headerPadding;
	private boolean enabled = true;
	private GuiGlobalShop gui;

	public GuiGlobalShopSlot(GuiGlobalShop gui, Minecraft mcIn, int width, int height, int topIn, int bottomIn,
			int slotHeightIn) {
		this.gui = gui;
		this.mc = mcIn;
		this.width = width;
		this.height = height;
		this.top = topIn;
		this.bottom = bottomIn;
		this.slotHeight = slotHeightIn;
		this.left = 0;
		this.right = width;
	}

	public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
		this.width = widthIn;
		this.height = heightIn;
		this.top = topIn;
		this.bottom = bottomIn;
		this.left = 0;
		this.right = widthIn;
	}

	public void setShowSelectionBox(boolean showSelectionBoxIn) {
		this.showSelectionBox = showSelectionBoxIn;
	}

	protected abstract int getSize();

	/**
	 * The element in the slot that was clicked, boolean for whether it was double
	 * clicked or not
	 */
	protected abstract void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY);

	/**
	 * Returns true if the element passed in is currently selected
	 */
	protected abstract boolean isSelected(int slotIndex);

	/**
	 * Return the height of the content being scrolled
	 */
	protected int getContentHeight() {
		return this.getSize() * this.slotHeight + this.headerPadding;
	}

	protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {
	}

	/**
	 * Handles drawing a list's header row.
	 */
	protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
	}

	protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
	}

	protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
	}

	public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_) {
		int i = this.left + this.width / 2 - this.getListWidth() / 2;
		int j = this.left + this.width / 2 + this.getListWidth() / 2;
		int k = p_148124_2_ - this.top - this.headerPadding + (int) this.amountScrolled - 4;
		int l = k / this.slotHeight;
		return p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= i && p_148124_1_ <= j && l >= 0 && k >= 0
				&& l < this.getSize() ? l : -1;
	}

	/**
	 * Registers the IDs that can be used for the scrollbar's up/down buttons.
	 */
	public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn) {
		this.scrollUpButtonID = scrollUpButtonIDIn;
		this.scrollDownButtonID = scrollDownButtonIDIn;
	}

	/**
	 * Stop the thing from scrolling out of bounds
	 */
	protected void bindAmountScrolled() {
		this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, (float) this.func_148135_f());
	}

	public int func_148135_f() {
		return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
	}

	/**
	 * Returns the amountScrolled field as an integer.
	 */
	public int getAmountScrolled() {
		return (int) this.amountScrolled;
	}

	public boolean isMouseYWithinSlotBounds(int p_148141_1_) {
		return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left
				&& this.mouseX <= this.right;
	}

	/**
	 * Scrolls the slot by the given amount. A positive value scrolls down, and a
	 * negative value scrolls up.
	 */
	public void scrollBy(int amount) {
		this.amountScrolled += (float) amount;
		this.bindAmountScrolled();
		this.initialClickY = -2;
	}

	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == this.scrollUpButtonID) {
				this.amountScrolled -= (float) (this.slotHeight * 2 / 3);
				this.initialClickY = -2;
				this.bindAmountScrolled();
			} else if (button.id == this.scrollDownButtonID) {
				this.amountScrolled += (float) (this.slotHeight * 2 / 3);
				this.initialClickY = -2;
				this.bindAmountScrolled();
			}
		}
	}

	public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
		if (this.field_178041_q) {
			this.mouseX = mouseXIn;
			this.mouseY = mouseYIn;
			int h = this.getScrollBarX();
			int g = h + 6;
			this.bindAmountScrolled();

            GlStateManager.disableLighting();
            GlStateManager.disableFog();

			int i = this.getSize();
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.pushMatrix();
			for (int i1 = 0; i1 < i; ++i1) {
				Slot slot = (Slot) this.gui.inventorySlots.inventorySlots.get(i1);
				// Slot, x, y, mouseX, mouseY
				this.drawItem(slot, i1, this.width / 2 - 150 + 72,
						(int) (this.top + i1 * this.slotHeight - amountScrolled), mouseX, mouseY, i1==this.selectedElement);
			}
			GlStateManager.popMatrix();
            
			GlStateManager.disableDepth();
            
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
			int j1 = this.func_148135_f();

			if (j1 > 0) {
				int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
				k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
				int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

				if (l1 < this.top) {
					l1 = this.top;
				}

	            GlStateManager.enableLighting();
	            GlStateManager.enableDepth();
	            RenderHelper.enableStandardItemLighting();
	            GlStateManager.enableRescaleNormal();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) h, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) g, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255)
						.endVertex();
				worldrenderer.pos((double) g, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				worldrenderer.pos((double) h, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) h, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) g, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255)
						.endVertex();
				worldrenderer.pos((double) g, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				worldrenderer.pos((double) h, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
				tessellator.draw();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				worldrenderer.pos((double) h, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255)
						.endVertex();
				worldrenderer.pos((double) (g - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D)
						.color(192, 192, 192, 255).endVertex();
				worldrenderer.pos((double) (g - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255)
						.endVertex();
				worldrenderer.pos((double) h, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
				tessellator.draw();
	            GlStateManager.enableBlend();
			}
			this.func_148142_b(mouseXIn, mouseYIn);

			GlStateManager.enableTexture2D();
			GlStateManager.shadeModel(7424);
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
		}
	}

	protected abstract void drawItem(Slot slot, int index, int x, int y, int mouseX, int mouseY, boolean isSelected);

	public void handleMouseInput() {
		if (this.isMouseYWithinSlotBounds(this.mouseY)) {
			if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top
					&& this.mouseY <= this.bottom) {
				int i = (this.width - this.getListWidth()) / 2;
				int j = (this.width + this.getListWidth()) / 2;
				int k = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
				int l = k / this.slotHeight;

				if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
					this.elementClicked(l, false, this.mouseX, this.mouseY);
					this.selectedElement = l;
				} else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
					this.func_148132_a(this.mouseX - i, this.mouseY - this.top + (int) this.amountScrolled - 4);
				}
			}

			if (Mouse.isButtonDown(0) && this.getEnabled()) {
				if (this.initialClickY == -1) {
					boolean flag1 = true;

					if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
						int j2 = (this.width - this.getListWidth()) / 2;
						int k2 = (this.width + this.getListWidth()) / 2;
						int l2 = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
						int i1 = l2 / this.slotHeight;

						if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0) {
							boolean flag = i1 == this.selectedElement
									&& Minecraft.getSystemTime() - this.lastClicked < 250L;
							this.elementClicked(i1, flag, this.mouseX, this.mouseY);
							this.selectedElement = i1;
							this.lastClicked = Minecraft.getSystemTime();
						} else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
							this.func_148132_a(this.mouseX - j2,
									this.mouseY - this.top + (int) this.amountScrolled - 4);
							flag1 = false;
						}

						int i3 = this.getScrollBarX();
						int j1 = i3 + 6;

						if (this.mouseX >= i3 && this.mouseX <= j1) {
							this.scrollMultiplier = -1.0F;
							int k1 = this.func_148135_f();

							if (k1 < 1) {
								k1 = 1;
							}

							int l1 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top))
									/ (float) this.getContentHeight());
							l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
							this.scrollMultiplier /= (float) (this.bottom - this.top - l1) / (float) k1;
						} else {
							this.scrollMultiplier = 1.0F;
						}

						if (flag1) {
							this.initialClickY = this.mouseY;
						} else {
							this.initialClickY = -2;
						}
					} else {
						this.initialClickY = -2;
					}
				} else if (this.initialClickY >= 0) {
					this.amountScrolled -= (float) (this.mouseY - this.initialClickY) * this.scrollMultiplier;
					this.initialClickY = this.mouseY;
				}
			} else {
				this.initialClickY = -1;
			}

			int i2 = Mouse.getEventDWheel();

			if (i2 != 0) {
				if (i2 > 0) {
					i2 = -1;
				} else if (i2 < 0) {
					i2 = 1;
				}

				this.amountScrolled += (float) (i2 * this.slotHeight / 2);
			}
		}
	}

	public void setEnabled(boolean enabledIn) {
		this.enabled = enabledIn;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	/**
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return 220;
	}

	protected int getScrollBarX() {
		return this.width / 2 + 124;
	}

	/**
	 * Overlays the background to hide scrolled items
	 */
	protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos((double) this.left, (double) endY, 0.0D).tex(0.0D, (double) ((float) endY / 32.0F))
				.color(64, 64, 64, endAlpha).endVertex();
		worldrenderer.pos((double) (this.left + this.width), (double) endY, 0.0D)
				.tex((double) ((float) this.width / 32.0F), (double) ((float) endY / 32.0F)).color(64, 64, 64, endAlpha)
				.endVertex();
		worldrenderer.pos((double) (this.left + this.width), (double) startY, 0.0D)
				.tex((double) ((float) this.width / 32.0F), (double) ((float) startY / 32.0F))
				.color(64, 64, 64, startAlpha).endVertex();
		worldrenderer.pos((double) this.left, (double) startY, 0.0D).tex(0.0D, (double) ((float) startY / 32.0F))
				.color(64, 64, 64, startAlpha).endVertex();
		tessellator.draw();
	}

	/**
	 * Sets the left and right bounds of the slot. Param is the left bound, right is
	 * calculated as left + width.
	 */
	public void setSlotXBoundsFromLeft(int leftIn) {
		this.left = leftIn;
		this.right = leftIn + this.width;
	}

	public int getSlotHeight() {
		return this.slotHeight;
	}

}