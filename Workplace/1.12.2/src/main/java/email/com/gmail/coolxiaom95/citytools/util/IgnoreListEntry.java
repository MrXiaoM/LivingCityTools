package email.com.gmail.coolxiaom95.citytools.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;

public class IgnoreListEntry implements GuiListExtended.IGuiListEntry {

	private GuiTextField textBox1;
	boolean a = false;
	String awa = "";

	public IgnoreListEntry(String s) {
		textBox1 = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 0, 0);

		if (s != null) {
			awa = s;
			textBox1.writeText(awa);
			textBox1.setCursorPositionZero();
		}
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialticks) {
		if (!a) {
			mouseClicked(x, y, 0);
			a = true;
		}
		textBox1.x = x;
		textBox1.y = y;
		textBox1.width = listWidth;
		textBox1.height = slotHeight;
		textBox1.drawTextBox();
	}

	public String getText() {
		if (textBox1 != null)
			return textBox1.getText();
		else
			return "";
	}

	public void initGui() {
		// textBox1.setEnableBackgroundDrawing(false);
		textBox1.setCanLoseFocus(true);
		textBox1.setFocused(false);

	}

	public boolean keyTyped(char par1, int par2) {
		if (textBox1.textboxKeyTyped(par1, par2)) // 向文本框传入输入的内容
			return true;
		else
			return false;
	}

	public void mouseClicked(int par1, int par2, int par3) {
		textBox1.mouseClicked(par1, par2, par3); // 调用文本框的鼠标点击检查
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
