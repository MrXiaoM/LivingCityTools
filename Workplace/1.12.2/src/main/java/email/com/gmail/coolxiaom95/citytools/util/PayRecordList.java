package email.com.gmail.coolxiaom95.citytools.util;

import java.util.List;

import com.google.common.collect.Lists;

import email.com.gmail.coolxiaom95.citytools.gui.GuiPayRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class PayRecordList extends GuiListExtended {
	private final List<PayRecordListEntry> items = Lists.<PayRecordListEntry>newArrayList();
	GuiPayRecord gui;
	public PayRecordList(GuiPayRecord ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn,
			int slotHeightIn) {
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.gui=ownerIn;
	}

	public void func_148195_a(List<String> str) {
		this.items.clear();
		this.items.add(new PayRecordListEntry("§7§l§o方式", "§7§l§o日期", "§7§l§o玩家名称", "§7§l§o支付金额"));
		if (str != null) {
			for (String s : str) {
				if(s.contains(";")) {
					if(s.split(";").length >= 4) {
						String[] s1 = s.split(";");
						this.items.add(new PayRecordListEntry(s1[0], s1[1], s1[2], s1[3]));
					}
				}
			}
		}
	}
	
	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@Override
	public PayRecordListEntry getListEntry(int index) {
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

	public class PayRecordListEntry implements GuiListExtended.IGuiListEntry {

		final String method;
		final String date;
		final String player;
		final String money;

		public PayRecordListEntry(String method, String date, String player, String money) {
			this.method = method;
			this.date = date;
			this.player = player;
			this.money = money;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
				boolean isSelected, float partialTicks) {
			mc.fontRenderer.drawString(this.method, x, y + 2, -1);
			mc.fontRenderer.drawString(this.date, x + 30, y + 2, -1);
			mc.fontRenderer.drawString(this.player, x + 110, y + 2, -1);
			mc.fontRenderer.drawString(this.money, x + 200, y + 2, -1);
			Util.drawRect(x, y + mc.fontRenderer.FONT_HEIGHT + 4, x + 240, y + mc.fontRenderer.FONT_HEIGHT + 6, 255, 66, 66, 66);
		}
		
		public String getStringData() {
			return this.method+";"+this.date+";"+this.player+";"+this.money;
		}
		
		@Override
		public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

		}

		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
			// TODO 自动生成的方法存根
			
		}
	}
}
