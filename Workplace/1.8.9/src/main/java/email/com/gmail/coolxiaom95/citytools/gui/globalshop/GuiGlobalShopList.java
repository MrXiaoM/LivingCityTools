package email.com.gmail.coolxiaom95.citytools.gui.globalshop;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGlobalShopList extends GuiGlobalShopSlot {
	private final GuiGlobalShop gui;
	@SuppressWarnings("unused")
	private final Minecraft mc;

	public GuiGlobalShopList(GuiGlobalShop controls, Minecraft mcIn) {
		super(controls, mcIn, controls.width, controls.height, controls.height / 2 - 100 + 18,
				controls.height / 2 - 100 + 180, 20);

		this.gui = controls;
		this.mc = mcIn;
	}

	public void setDimensions(int widthIn, int heightIn) {
		super.setDimensions(widthIn, heightIn, heightIn / 2 - 100 + 18, heightIn / 2 - 100 + 180);
	}

	protected int getScrollBarX() {
		return super.getScrollBarX() + 16;
	}

	public int getListWidth() {
		return super.getListWidth();
	}

	@Override
	protected void drawItem(Slot slot, int index, int x, int y, int mouseX, int mouseY, boolean isSelected) {
		GuiGlobalShopList.this.gui.drawItem(slot, index, x, y, mouseX, mouseY, isSelected);
	}

	@Override
	protected int getSize() {
		// TODO 自动生成的方法存根
		int o = this.gui.inventorySlots.inventorySlots.size();
		if (o > 45) {
			o = 45;
		}
		return o;
	}

	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		// TODO 自动生成的方法存根

	}

	@Override
	protected boolean isSelected(int slotIndex) {
		// TODO 自动生成的方法存根
		return false;
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseX > this.gui.width / 2 - 60 && mouseX < this.getScrollBarX()) {
			if (mouseY > this.top && mouseY < this.bottom) {
				
				int y = (int)(mouseY - this.top + this.amountScrolled - 4);
				int index = y / this.slotHeight;
				
				this.selectedElement = index;
				return true;
			}
		}
		return false;
	}

	public boolean mouseReleased(int mouseX, int mouseY, int state) {
		return false;
	}
}