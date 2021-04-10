package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiTpToPlayer extends GuiScreen {

	ResourceLocation user_background = new ResourceLocation("citytools", "textures/gui/user_background.png");

	GuiScreen last;

	int type = 0;

	private GuiTextField tfName;

	public GuiTpToPlayer(GuiScreen last_screen) {
		last = last_screen;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			mc.displayGuiScreen(null);
			this.sendChatMessage("/tpa " + tfName.getText(), false);
			// ✔
			mc.ingameGUI.setRecordPlaying(I18n.format("citytools.menu.teleport.player.showtitle"), false);
		}
		if (button.id == 1) {
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

		tfName.drawTextBox();
		String str2 = I18n.format("citytools.menu.teleport.player.title", mc.getSession().getPlayerID());
		this.drawString(fontRendererObj, str2, this.width / 2 - 100, this.height / 2 - 36 - fontRendererObj.FONT_HEIGHT,
				-1);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void initButtons() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 40, 98, 20,
				I18n.format("citytools.menu.teleport.player.confirm")));
		this.buttonList
				.add(new GuiButton(1, this.width / 2 + 2, this.height / 2 + 40, 98, 20, I18n.format("gui.cancel")));
	}

	@Override
	public void initGui() {
		type = 0;
		initButtons();
		Keyboard.enableRepeatEvents(true);
		tfName = new GuiTextField(0, fontRendererObj, (width / 2) - 100, (height / 2) - 30, 200, 20);
		tfName.setMaxStringLength(64);
		tfName.setFocused(false);
		tfName.setCanLoseFocus(true);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		if (tfName.textboxKeyTyped(par1, par2)) // 向文本框传入输入的内容
			return;
		super.keyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		tfName.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false); // 关闭键盘连续输入
	}
}
