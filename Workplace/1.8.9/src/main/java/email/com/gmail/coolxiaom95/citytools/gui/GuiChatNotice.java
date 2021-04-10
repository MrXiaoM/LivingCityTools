package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiChatNotice extends GuiScreen {
	private GuiScreen parentScreen;
	private GuiChatNoticeList noticeList;

	public GuiChatNotice(GuiScreen screen) {
		this.parentScreen = screen;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		this.noticeList = new GuiChatNoticeList(this, this.mc);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(200, this.width / 2 - 102, this.height - 29, 100, 20,
				I18n.format("citytools.hotkey.saveandclose")));
		this.buttonList.add(
				new GuiButton(201, this.width / 2 + 2, this.height - 29, 100, 20, 
						I18n.format("citytools.hotkey.add")));
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.noticeList.handleMouseInput();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
			Main.instance.saveNoticeList(this.noticeList.getItems());
		} else if (button.id == 201) {
			this.noticeList.addItem("","citytools:notice.default","1.0");
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton != 0 || !this.noticeList.mouseClicked(mouseX, mouseY, mouseButton)) {
			this.noticeList.mouseClicked_(mouseX, mouseY, mouseButton);
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state != 0 || !this.noticeList.mouseReleased(mouseX, mouseY, state)) {
			super.mouseReleased(mouseX, mouseY, state);
		}
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.noticeList.keyTyped(typedChar, keyCode))
			return;
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.noticeList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRendererObj, I18n.format("citytools.chatnotice.title"), this.width / 2, 8,
				16777215);
		this.drawCenteredString(this.fontRendererObj, I18n.format("citytools.chatnotice.subtitle"), this.width / 2, 18,
				16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public class GuiChatNoticeList extends GuiListExtended {
		private final GuiChatNotice gui;
		private final Minecraft mc;
		private final ArrayList<ChatNoticeEntry> listEntries;
		//private int maxListLabelWidth = 0;

		public GuiChatNoticeList(GuiChatNotice controls, Minecraft mcIn) {
			super(mcIn, controls.width, controls.height, 32, controls.height - 42, 64);
			this.gui = controls;
			this.mc = mcIn;
			this.listEntries = new ArrayList<ChatNoticeEntry>();

			for (String s : Main.chatNotices) {
				if(s.contains(";")) {
					String[] a = s.split(";");
					if(a.length == 3) {
						this.listEntries.add(new GuiChatNoticeList.ChatNoticeEntry(a[0], a[1], a[2]));
					}
				}
			}
		}

		protected void mouseClicked_(int par1, int par2, int par3) {
			for (ChatNoticeEntry key : listEntries) {
				key.text.mouseClicked(par1, par2, par3);
				key.sound.mouseClicked(par1, par2, par3);
				key.pitch.mouseClicked(par1, par2, par3);
			}
		}

		public boolean keyTyped(char par1, int par2) {
			for (ChatNoticeEntry key : listEntries) {
				if (key.keyTyped(par1, par2))
					return true;
			}
			return false;
		}

		protected int getSize() {
			return this.listEntries.size();
		}

		public ChatNoticeEntry getListEntry(int index) {
			return this.listEntries.get(index);
		}

		public ArrayList<String> getItems() {
			ArrayList<String> result = new ArrayList<String>();
			for(ChatNoticeEntry item : this.listEntries) {
				result.add(item.getItem());
			}
			return result;
		}

		public void addItem(String key, String sound, String patch) {
			this.listEntries.add(new ChatNoticeEntry(key, sound, patch));
		}

		protected int getScrollBarX() {
			return super.getScrollBarX() + 15;
		}

		public int getListWidth() {
			return super.getListWidth() + 32;
		}

		public GuiChatNotice getGui() {
			return gui;
		}

		@SideOnly(Side.CLIENT)
		public class ChatNoticeEntry implements GuiListExtended.IGuiListEntry {
			private final GuiButton btnRemove;
			private final GuiButton btnPlay;
			private final GuiTextField text;
			private final GuiTextField sound;
			private final GuiTextField pitch;

			private ChatNoticeEntry(String key, String sound, String patch) {
				this.text = new GuiTextField(0, mc.fontRendererObj, 0, 0, 100, 20);
				this.text.setFocused(false);
				this.text.setMaxStringLength(128);// 设置最大长度
				this.text.setCanLoseFocus(true);
				this.text.setText(key);
				this.sound = new GuiTextField(0, mc.fontRendererObj, 0, 0, 100, 20);
				this.sound.setFocused(false);
				this.sound.setMaxStringLength(128);// 设置最大长度
				this.sound.setCanLoseFocus(true);
				this.sound.setText(sound);
				this.pitch = new GuiTextField(0, mc.fontRendererObj, 0, 0, 100, 20);
				this.pitch.setFocused(false);
				this.pitch.setMaxStringLength(128);// 设置最大长度
				this.pitch.setCanLoseFocus(true);
				this.pitch.setText(patch);
				this.btnRemove = new GuiButton(0, 0, 0, 75, 20,
						"§c§l" + I18n.format("citytools.chatnotice.remove"));
				this.btnPlay = new GuiButton(0, 0, 0, 75, 20,
						I18n.format("citytools.chatnotice.play"));
			}

			public String getText() {
				return text.getText();
			}
			
			public String getItem() {
				return this.text.getText() + ";" + this.sound.getText() + ";" + Util.strToFloat(this.pitch.getText(), 1.0F);
			}

			public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
					boolean isSelected) {
				
				// GuiKeyList.this.mc.fontRendererObj.drawString(this.keyDesc, x + 90 -
				// GuiKeyList.this.maxListLabelWidth,
				// y + slotHeight / 2 - GuiKeyList.this.mc.fontRendererObj.FONT_HEIGHT / 2,
				// 16777215);
				
				mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.chatnotice.text"), 
						x, y, 0xffffff);
				text.xPosition = x;
				text.yPosition = y + 10;
				text.width = 175;
				text.height = 16;
				text.drawTextBox();
				mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.chatnotice.sound"), 
						x, y + 28, 0xffffff);
				sound.xPosition = x;
				sound.yPosition = y + 38;
				sound.width = 150;
				sound.height = 16;
				sound.drawTextBox();
				mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.chatnotice.patch"),
						x + 155, y + 28, 0xffffff);
				pitch.xPosition = x + 155;
				pitch.yPosition = y + 38;
				pitch.width = 20;
				pitch.height = 16;
				pitch.drawTextBox();
				
				// mc.fontRendererObj.drawString("W:"+listWidth, x, y, -1);

				this.btnRemove.xPosition = x + 185;
				this.btnRemove.yPosition = y + 10;
				this.btnRemove.displayString = "§c§l" + I18n.format("citytools.chatnotice.remove");
				this.btnRemove.drawButton(GuiChatNotice.GuiChatNoticeList.this.mc, mouseX, mouseY);
				this.btnPlay.xPosition = x + 185;
				this.btnPlay.yPosition = y + 34;
				this.btnPlay.displayString = I18n.format("citytools.chatnotice.play");
				this.btnPlay.drawButton(GuiChatNotice.GuiChatNoticeList.this.mc, mouseX, mouseY);
				Util.drawRect(x, y+60, x+258, y+62, 1.0F, 128.0F, 128.0F, 128.0F);
			}

			public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
					int p_148278_6_) {
				if (this.btnRemove.mousePressed(GuiChatNoticeList.this.mc, p_148278_2_, p_148278_3_)) {
					GuiChatNoticeList.this.listEntries.remove(this);
					this.btnRemove.playPressSound(GuiChatNoticeList.this.mc.getSoundHandler());
					return true;
				}
				if (this.btnPlay.mousePressed(GuiChatNoticeList.this.mc, p_148278_2_, p_148278_3_)) {
					GuiChatNoticeList.this.mc.getSoundHandler().stopSounds();
					GuiChatNoticeList.this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(
							new ResourceLocation(this.sound.getText()), Util.strToFloat(this.pitch.getText(),1.0F)));
					return true;
				} else {
					return false;
				}
			}
			
			public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
				this.btnRemove.mouseReleased(x, y);
			}

			public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {

			}

			public boolean keyTyped(char par1, int par2) {
				if (text.textboxKeyTyped(par1, par2) || sound.textboxKeyTyped(par1, par2) || pitch.textboxKeyTyped(par1, par2)) // 向文本框传入输入的内容
					return true;
				return false;
			}
		}
	}
}
