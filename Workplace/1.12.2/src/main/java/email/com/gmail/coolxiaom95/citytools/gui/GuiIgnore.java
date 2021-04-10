package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.util.IgnoreList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiIgnore extends GuiScreen {

	private GuiScreen last;
	private IgnoreList list = new IgnoreList(this, this.mc, this.width, this.height, 32, this.height - 40, 20);
	private GuiButton btnCancel = new GuiButton(233, width / 2 + 55, height - 32, 100, 20,
			I18n.format("citytools.ignore.cancel"));
	private GuiButton btnAdd = new GuiButton(233, width / 2 - 50, height - 32, 100, 20,
			I18n.format("citytools.ignore.add"));
	private GuiButton btnSave = new GuiButton(233, width / 2 - 155, height - 32, 100, 20,
			I18n.format("citytools.ignore.saveandclose"));
	private GuiButton btnLoad = new GuiButton(233, width - 158, 8, 150, 20, I18n.format("citytools.ignore.load"));

	public GuiIgnore(GuiScreen lastScreen) {
		last = lastScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == btnCancel) {
			mc.displayGuiScreen(last);
		}
		if (button == btnAdd) {
			list.addEmpty();
		}
		if (button == btnSave) {
			ArrayList<String> strList = new ArrayList<String>();
			for (int i = 0; i < list.getSize(); i++) {
				String text = list.getListEntry(i).getText();
				if (!text.isEmpty()) {
					strList.add(text);
				}
			}
			Main.instance.saveIgnoreList(strList);
			mc.displayGuiScreen(last);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		if (list != null)
			list.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, I18n.format("citytools.ignore.title"), this.width / 2, 10, -1);
		this.drawCenteredString(this.fontRenderer, "§7" + I18n.format("citytools.ignore.subtitle"), this.width / 2,
				20, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.list.handleMouseInput();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();

		this.list = new IgnoreList(this, this.mc, this.width, this.height, 32, this.height - 40, 20);
		this.list.func_148195_a(Main.chat_filter);

		this.buttonList.add(btnCancel);
		this.buttonList.add(btnAdd);
		this.buttonList.add(btnSave);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		if (list.keyTyped(par1, par2))
			return;
		super.keyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		list.mouseClick(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.list.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		this.list.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

	}

	@Override
	public void updateScreen() {
		btnCancel.x = width / 2 + 55;
		btnCancel.y = height - 32;

		btnSave.x = width / 2 - 155;
		btnSave.y = height - 32;

		btnAdd.x = width / 2 - 50;
		btnAdd.y = height - 32;

		btnLoad.x = width - 158;
		btnLoad.y = 8;
	}
}
