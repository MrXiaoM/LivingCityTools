package email.com.gmail.coolxiaom95.citytools.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

public class IgnoreList extends GuiListExtended {
	private final List<IgnoreListEntry> items = Lists.<IgnoreListEntry>newArrayList();

	public IgnoreList(GuiScreen ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn,
			int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}

	public void addEmpty() {
		this.items.add(new IgnoreListEntry(""));
	}

	public void func_148195_a(List<String> str) {
		this.items.clear();

		if (str != null)
			for (String s : str) {
				this.items.add(new IgnoreListEntry(s));
			}
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override
	public IgnoreListEntry getListEntry(int index) {
		if (index < this.items.size()) {
			return this.items.get(index);
		} else
			return null;
	}

	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return super.getListWidth() + 25;
	}

	@Override
	protected int getScrollBarX() {
		return super.getScrollBarX() + 10;
	}

	@Override
	public int getSize() {
		return this.items.size();
	}

	public boolean keyTyped(char par1, int par2) {
		for (IgnoreListEntry i : items) {
			if (i.keyTyped(par1, par2)) {
				return true;
			}
		}
		return false;
	}

	public void mouseClick(int par1, int par2, int par3) {
		for (IgnoreListEntry i : items) {
			i.mouseClicked(par1, par2, par3);
		}
	}
}