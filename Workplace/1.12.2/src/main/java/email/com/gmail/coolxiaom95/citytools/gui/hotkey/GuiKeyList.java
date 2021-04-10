package email.com.gmail.coolxiaom95.citytools.gui.hotkey;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import email.com.gmail.coolxiaom95.citytools.Main;

@SideOnly(Side.CLIENT)
public class GuiKeyList extends GuiListExtended {
	private final GuiHotkey field_148191_k;
	private final Minecraft mc;
	private final ArrayList<KeyEntry> listEntries;
	private int maxListLabelWidth = 0;

	public GuiKeyList(GuiHotkey controls, Minecraft mcIn) {
		super(mcIn, controls.width, controls.height, 20, controls.height - 42, 20);
		this.field_148191_k = controls;
		this.mc = mcIn;
		this.listEntries = new ArrayList<KeyEntry>();

		for (MyKey keybinding : Main.hotkey_list) {

			int j = mcIn.fontRenderer.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));

			if (j > this.maxListLabelWidth) {
				this.maxListLabelWidth = j;
			}

			this.listEntries.add(new GuiKeyList.KeyEntry(keybinding));
		}
	}

	protected void mouseClicked_(int par1, int par2, int par3) {
		for (KeyEntry key : listEntries) {
			key.text.mouseClicked(par1, par2, par3);
		}
	}

	public boolean keyTyped(char par1, int par2) {
		for (KeyEntry key : listEntries) {
			if (key.keyTyped(par1, par2))
				return true;
		}
		return false;
	}

	protected int getSize() {
		return this.listEntries.size();
	}

	public KeyEntry getListEntry(int index) {
		return this.listEntries.get(index);
	}

	public void addKey(MyKey key) {
		this.listEntries.add(new KeyEntry(key));
	}

	public ArrayList<MyKey> getKeys() {
		ArrayList<MyKey> result = new ArrayList<MyKey>();
		for (KeyEntry key : listEntries) {
			result.add(new MyKey(key.getText(), key.keybinding.getKeyCode()));
		}
		return result;
	}

	protected int getScrollBarX() {
		return super.getScrollBarX() + 15;
	}

	public int getListWidth() {
		return super.getListWidth() + 32;
	}

	@SideOnly(Side.CLIENT)
	public class KeyEntry implements GuiListExtended.IGuiListEntry {
		private final MyKey keybinding;
		private final GuiButton btnChangeKeyBinding;
		private final GuiButton btnRemove;
		private final GuiTextField text;

		private KeyEntry(MyKey p_i45029_2_) {
			this.keybinding = p_i45029_2_;
			this.text = new GuiTextField(0, mc.fontRenderer, 0, 0, 100, 20);
			this.text.setFocused(false);
			this.text.setMaxStringLength(128);// 设置最大长度
			this.text.setCanLoseFocus(true);
			this.text.setText(p_i45029_2_.getKeyDescription());
			this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20,
					I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
			this.btnRemove = new GuiButton(0, 0, 0, 75, 20,
					I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
		}

		public String getText() {
			return text.getText();
		}

		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
				boolean isSelected, float partialTicks) {
			boolean flag = GuiKeyList.this.field_148191_k.buttonId == this.keybinding;

			// GuiKeyList.this.mc.fontRendererObj.drawString(this.keyDesc, x + 90 -
			// GuiKeyList.this.maxListLabelWidth,
			// y + slotHeight / 2 - GuiKeyList.this.mc.fontRendererObj.FONT_HEIGHT / 2,
			// 16777215);

			text.x = x;
			text.y = y + 2;
			text.width = 100;
			text.height = slotHeight;
			text.drawTextBox();

			// mc.fontRendererObj.drawString("W:"+listWidth, x, y, -1);

			this.btnChangeKeyBinding.x = x + 106;
			this.btnChangeKeyBinding.y = y;
			this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());

			this.btnRemove.x = x + 185;
			this.btnRemove.y = y;
			this.btnRemove.displayString = "§c§l" + I18n.format("citytools.hotkey.remove");

			if (flag) {
				this.btnChangeKeyBinding.displayString = "§f> §e"
						+ this.btnChangeKeyBinding.displayString + "§f <";
			}
			this.btnChangeKeyBinding.drawButton(GuiKeyList.this.mc, mouseX, mouseY, mc.getRenderPartialTicks());
			this.btnRemove.drawButton(GuiKeyList.this.mc, mouseX, mouseY, mc.getRenderPartialTicks());
		}

		public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			if (this.btnChangeKeyBinding.mousePressed(GuiKeyList.this.mc, p_148278_2_, p_148278_3_)) {
				GuiKeyList.this.field_148191_k.buttonId = this.keybinding;
				return true;
			} else if (this.btnRemove.mousePressed(GuiKeyList.this.mc, p_148278_2_, p_148278_3_)) {
				GuiKeyList.this.listEntries.remove(this);
				this.btnRemove.playPressSound(GuiKeyList.this.mc.getSoundHandler());
				return true;
			} else {
				return false;
			}
		}

		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			this.btnChangeKeyBinding.mouseReleased(x, y);
			this.btnRemove.mouseReleased(x, y);
		}

		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {

		}

		public boolean keyTyped(char par1, int par2) {
			if (text.textboxKeyTyped(par1, par2)) // 向文本框传入输入的内容
				return true;
			return false;
		}

		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
			// TODO 自动生成的方法存根
			
		}
	}
}