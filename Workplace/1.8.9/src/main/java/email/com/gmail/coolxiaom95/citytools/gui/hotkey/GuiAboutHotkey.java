package email.com.gmail.coolxiaom95.citytools.gui.hotkey;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAboutHotkey extends GuiScreen {
	private GuiScreen parentScreen;

	public GuiAboutHotkey(GuiScreen screen) {
		this.parentScreen = screen;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList
				.add(new GuiButton(200, this.width / 2 - 50, this.height - 29, 100, 20, I18n.format("gui.done")));
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int i = mc.fontRendererObj.FONT_HEIGHT + 2;
		int j = 170;
		this.drawCenteredString(this.fontRendererObj, I18n.format("citytools.hotkey.about.title"), this.width / 2, 8,
				16777215);
		this.drawString(mc.fontRendererObj, I18n.format("citytools.hotkey.about.0"), this.width / 2 - j, 30, -1);
		this.drawString(mc.fontRendererObj, I18n.format("citytools.hotkey.about.1"), this.width / 2 - j, 30 + i * 1,
				-1);
		this.drawString(mc.fontRendererObj, I18n.format("citytools.hotkey.about.2"), this.width / 2 - j, 30 + i * 2,
				-1);
		this.drawString(mc.fontRendererObj, I18n.format("citytools.hotkey.about.3"), this.width / 2 - j, 30 + i * 3,
				-1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
