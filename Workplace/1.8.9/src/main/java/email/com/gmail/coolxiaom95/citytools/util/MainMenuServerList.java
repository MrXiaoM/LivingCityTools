package email.com.gmail.coolxiaom95.citytools.util;

import java.util.ArrayList;
import java.util.List;

import email.com.gmail.coolxiaom95.citytools.gui.GuiNewMainMenu;
import net.minecraft.client.Minecraft;

public class MainMenuServerList extends GuiNewList {
	private final GuiNewMainMenu owner;
	private final List<ServerListEntry> field_148198_l = new ArrayList<ServerListEntry>();
	private int selectedSlotIndex = -1;

	public MainMenuServerList(GuiNewMainMenu ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn,
			int bottomIn, int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.owner = ownerIn;
	}

	public int func_148193_k() {
		return this.selectedSlotIndex;
	}

	public void func_148195_a(ServerList p_148195_1_) {
		this.field_148198_l.clear();

		for (int i = 0; i < p_148195_1_.countServers(); ++i) {
			this.field_148198_l.add(new ServerListEntry(this.owner, p_148195_1_.getServerData(i)));
		}
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override
	public GuiNewList.IGuiListEntry getListEntry(int index) {
		if (index < this.field_148198_l.size()) {
			return this.field_148198_l.get(index);
		} else
			return null;
	}

	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return super.getListWidth();
	}

	@Override
	protected int getScrollBarX() {
		return super.getScrollBarX() + 30;
	}

	@Override
	public int getSize() {
		return this.field_148198_l.size();
	}

	/**
	 * Returns true if the element passed in is currently selected
	 */
	@Override
	protected boolean isSelected(int slotIndex) {
		return slotIndex == this.selectedSlotIndex;
	}

	public void setSelectedSlotIndex(int selectedSlotIndexIn) {
		this.selectedSlotIndex = selectedSlotIndexIn;
	}
}
