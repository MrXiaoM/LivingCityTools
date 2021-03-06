package email.com.gmail.coolxiaom95.citytools.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiNewList extends GuiNewSlot {
	@SideOnly(Side.CLIENT)
	public interface IGuiListEntry {
		void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
				boolean isSelected);

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_);

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent,
		 * relativeX, relativeY
		 */
		void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY);

		void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_);
	}

	public GuiNewList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn,
			int mouseYIn) {
		this.getListEntry(entryID).drawEntry(entryID, p_180791_2_, p_180791_3_, this.getListWidth(), p_180791_4_,
				mouseXIn, mouseYIn, this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == entryID);
	}

	/**
	 * The element in the slot that was clicked, boolean for whether it was double
	 * clicked or not
	 */
	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
	}

	@Override
	protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {
		this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public abstract GuiNewList.IGuiListEntry getListEntry(int index);

	/**
	 * Returns true if the element passed in is currently selected
	 */
	@Override
	protected boolean isSelected(int slotIndex) {
		return false;
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		if (this.isMouseYWithinSlotBounds(mouseY)) {
			int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);

			if (i >= 0) {
				int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
				int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
				int l = mouseX - j;
				int i1 = mouseY - k;

				if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i1)) {
					this.setEnabled(false);
					return true;
				}
			}
		}

		return false;
	}

	public boolean mouseReleased(int p_148181_1_, int p_148181_2_, int p_148181_3_) {
		for (int i = 0; i < this.getSize(); ++i) {
			int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
			int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
			int l = p_148181_1_ - j;
			int i1 = p_148181_2_ - k;
			this.getListEntry(i).mouseReleased(i, p_148181_1_, p_148181_2_, p_148181_3_, l, i1);
		}

		this.setEnabled(true);
		return false;
	}
}