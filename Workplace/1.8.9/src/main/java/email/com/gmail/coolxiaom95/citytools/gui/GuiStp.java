package email.com.gmail.coolxiaom95.citytools.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiStp extends GuiScreen {

	ResourceLocation user_background = new ResourceLocation("citytools", "textures/gui/user_background.png");

	GuiScreen last;

	public GuiStp(GuiScreen lastScreen) {
		last = lastScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp sc", false);
		}
		if (button.id == 1) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp sk", false);
		}
		if (button.id == 2) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp kd", false);
		}
		if (button.id == 3) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp kk", false);
		}
		if (button.id == 4) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp zd", false);
		}
		if (button.id == 5) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp mrsc", false);
		}
		if (button.id == 6) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/stp tnt", false);
		}
		if (button.id == 666) {
			mc.displayGuiScreen(last);
		}
	}

	public void bindTexture(ResourceLocation texture) {
		mc.renderEngine.bindTexture(texture);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.bindTexture(user_background);
		drawScaledCustomSizeModalRect(this.width / 2 - 124, this.height / 2 - 83, 0, 0, 248, 166, 248, 166, 248, 166);

		this.drawString(fontRendererObj, I18n.format("citytools.menu.servers.title"), this.width / 2 - 110,
				this.height / 2 - 75, -1);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void initButtons() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 108, this.height / 2 - 60, 100, 20,
				I18n.format("citytools.menu.servers.sc")));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 8, this.height / 2 - 60, 100, 20,
				I18n.format("citytools.menu.servers.sk")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 108, this.height / 2 - 36, 100, 20,
				I18n.format("citytools.menu.servers.kd")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 8, this.height / 2 - 36, 100, 20,
				I18n.format("citytools.menu.servers.kk")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 108, this.height / 2 - 12, 100, 20,
				I18n.format("citytools.menu.servers.zd")));
		this.buttonList.add(new GuiButton(5, this.width / 2 + 8, this.height / 2 - 12, 100, 20,
				I18n.format("citytools.menu.servers.mrsc")));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 108, this.height / 2 + 12, 100, 20,
				I18n.format("citytools.menu.servers.tnt")));
		this.buttonList
				.add(new GuiButton(666, this.width / 2 - 75, this.height / 2 + 56, 150, 20, I18n.format("gui.back")));
	}

	@Override
	public void initGui() {
		initButtons();
	}
}
