package email.com.gmail.coolxiaom95.citytools.gui;

import email.com.gmail.coolxiaom95.citytools.Main;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiUser extends GuiScreen {

	ResourceLocation user_background = new ResourceLocation("citytools", "textures/gui/user_background.png");

	public GuiUser() {

	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			mc.displayGuiScreen(new GuiEmail(this));
		}
		if (button.id == 1) {
			this.sendChatMessage("/ffly", false);
		}
		if (button.id == 2) {
			mc.displayGuiScreen(new GuiPay(this));
		}
		if (button.id == 3) {
			this.sendChatMessage("/spawn", false);
		}
		if (button.id == 4) {
			mc.displayGuiScreen(new GuiStp(this));
		}
		if (button.id == 5) {
			mc.displayGuiScreen(new GuiTeleport(this, Main.serverid));
		}
		if (button.id == 666) {
			mc.displayGuiScreen(null);
		}
	}

	public void bindTexture(ResourceLocation texture) {
		mc.renderEngine.bindTexture(texture);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.bindTexture(user_background);
		drawScaledCustomSizeModalRect(this.width / 2 - 124, this.height / 2 - 83, 0, 0, 248, 166, 248, 166, 248, 166);

		ResourceLocation skin = mc.player.getLocationSkin();
		this.bindTexture(skin);
		// drawScaledCustomSizeModalRect(this.width / 2 - 124 + 16, this.height / 2 - 83
		// + 16, 8, 8, 8, 8, 16, 16, 16, 16);

		// 8,8
		drawModalRectWithCustomSizedTexture(this.width / 2 - 124 + 16, this.height / 2 - 83 + 16, 32, 32, 32, 32, 256,
				256);
		// 40,8
		drawModalRectWithCustomSizedTexture(this.width / 2 - 124 + 16, this.height / 2 - 83 + 16, 160, 32, 32, 32, 256,
				256);
		String str1 = I18n.format("citytools.menu.info.1", mc.getSession().getUsername());
		String str2 = I18n.format("citytools.menu.info.2", mc.player.experienceLevel);
		String str3 = I18n.format("citytools.menu.info.3", mc.player.getHealth(), mc.player.getMaxHealth());

		String str4 = I18n.format("citytools.menu.info.4", "§7" + I18n.format("citytools.menu.info.4.none"));
		if (mc.player.inventory.getCurrentItem() != null) {
			str4 = I18n.format("citytools.menu.info.4", mc.player.inventory.getCurrentItem().getDisplayName());
		}

		String str5 = I18n.format("citytools.menu.info.5", "§7" + I18n.format("citytools.menu.info.5.none"));
		if (mc.player.inventory.armorItemInSlot(2) != null) {
			str5 = I18n.format("citytools.menu.info.5", mc.player.inventory.armorItemInSlot(2).getDisplayName());
		}

		this.drawString(fontRenderer, str1, this.width / 2 - 124 + 56, this.height / 2 - 83 + 18, -1);
		this.drawString(fontRenderer, str2, this.width / 2 - 124 + 56,
				this.height / 2 - 83 + 18 + fontRenderer.FONT_HEIGHT * 1, -1);
		this.drawString(fontRenderer, str3, this.width / 2 - 124 + 56,
				this.height / 2 - 83 + 18 + fontRenderer.FONT_HEIGHT * 2, -1);

		this.drawString(fontRenderer, str4 + "§r", this.width / 2 + 20, this.height / 2 - 83 + 18, -1);
		this.drawString(fontRenderer, str5 + "§r", this.width / 2 + 20,
				this.height / 2 - 83 + 18 + fontRenderer.FONT_HEIGHT * 1, -1);

		this.drawString(fontRenderer, mc.player.getUniqueID().toString(), this.width / 2 - 118,
				this.height / 2 + 79 - fontRenderer.FONT_HEIGHT, -1);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void initButtons() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 108, this.height / 2 - 30, 100, 20,
				I18n.format("citytools.menu.button.email")));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 8, this.height / 2 - 30, 100, 20,
				I18n.format("citytools.menu.button.fly")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 108, this.height / 2 - 6, 100, 20,
				I18n.format("citytools.menu.button.pay")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 8, this.height / 2 - 6, 100, 20,
				I18n.format("citytools.menu.button.spawn")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 108, this.height / 2 + 18, 100, 20,
				I18n.format("citytools.menu.button.servers")));
		this.buttonList.add(new GuiButton(5, this.width / 2 + 8, this.height / 2 + 18, 100, 20,
				I18n.format("citytools.menu.button.teleport")));
		this.buttonList
				.add(new GuiButton(666, this.width / 2 - 75, this.height / 2 + 42, 150, 20, I18n.format("gui.back")));
	}

	@Override
	public void initGui() {
		initButtons();
	}
}
