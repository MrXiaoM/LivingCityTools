package email.com.gmail.coolxiaom95.citytools.gui.hotkey;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import email.com.gmail.coolxiaom95.citytools.Main;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHotkey extends GuiScreen {
	private GuiScreen parentScreen;
	public MyKey buttonId = null;
	private GuiKeyList keyBindingList;

	public GuiHotkey(GuiScreen screen) {
		this.parentScreen = screen;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		this.buttonList.clear();
		this.keyBindingList = new GuiKeyList(this, this.mc);
		this.buttonList.add(new GuiButton(200, this.width / 2 - 102, this.height - 29, 100, 20,
				I18n.format("citytools.hotkey.saveandclose")));
		this.buttonList.add(new GuiButton(201, this.width - 105, 5, 100, 20, I18n.format("citytools.hotkey.about")));
		this.buttonList.add(
				new GuiButton(202, this.width / 2 + 2, this.height - 29, 100, 20, I18n.format("citytools.hotkey.add")));
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.keyBindingList.handleMouseInput();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
			Main.instance.saveKeyList(this.keyBindingList.getKeys());
		} else if (button.id == 201) {
			this.mc.displayGuiScreen(new GuiAboutHotkey(this));
		} else if (button.id == 202) {
			this.keyBindingList.addKey(new MyKey("", 0));
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (this.buttonId != null) {
			// this.buttonId.setKeyCode(-100 + mouseButton);
			this.buttonId = null;
		} else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
			this.keyBindingList.mouseClicked_(mouseX, mouseY, mouseButton);
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (state != 0 || !this.keyBindingList.mouseReleased(mouseX, mouseY, state)) {
			super.mouseReleased(mouseX, mouseY, state);
		}
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.buttonId != null) {
			if (keyCode == 1) {
				this.buttonId.setKeyCode(0);
			} else if (keyCode != 0) {
				this.buttonId.setKeyCode(keyCode);
			} else if (typedChar > 0) {
				this.buttonId.setKeyCode(typedChar + 256);
			}

			this.buttonId = null;
		} else {

			if (this.keyBindingList.keyTyped(typedChar, keyCode))
				return;
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRendererObj, I18n.format("citytools.hotkey.title"), this.width / 2, 8,
				16777215);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
