package email.com.gmail.coolxiaom95.citytools.gui.globalshop;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiGlobalShopListExtended extends GuiGlobalShopSlot {
	public GuiGlobalShopListExtended(GuiGlobalShop gui, Minecraft mcIn, int widthIn, int heightIn, int topIn,
			int bottomIn, int slotHeightIn) {
		super(gui, mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}

	/**
	 * The element in the slot that was clicked, boolean for whether it was double
	 * clicked or not
	 */
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
	}

	/**
	 * Returns true if the element passed in is currently selected
	 */
	protected boolean isSelected(int slotIndex) {
		return false;
	}

	protected void drawBackground() {
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		return false;
	}

	public boolean mouseReleased(int p_148181_1_, int p_148181_2_, int p_148181_3_) {
		return false;
	}
}