package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.util.PayRecordList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiPayRecord extends GuiScreen {

	private GuiScreen last;
	private PayRecordList list = new PayRecordList(this, this.mc, this.width, this.height, 32, this.height - 40, 20);
	
	public GuiPayRecord(GuiScreen lastScreen) {
		last = lastScreen;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 233) {
			mc.displayGuiScreen(last);
		}
		if (button.id == 234) {
			Main.instance.clearPayRecord();
			this.list.func_148195_a(Main.pay_record);
		}
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();

		this.list = new PayRecordList(this, this.mc, this.width, this.height, 48, this.height - 40, 20);
		this.list.func_148195_a(Main.pay_record);
		GuiButton btnBack = new GuiButton(233, width / 2 - 105, height - 32, 100, 20,
				I18n.format("gui.back"));
		GuiButton btnClear = new GuiButton(234, width / 2 + 5, height - 32, 100, 20,
				I18n.format("citytools.payrecord.clear"));
		this.buttonList.add(btnBack);
		this.buttonList.add(btnClear);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.list.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(mc.fontRendererObj, I18n.format("citytools.payrecord.title"), this.width / 2, 16, 0xffffff);
		this.drawCenteredString(mc.fontRendererObj, I18n.format("citytools.payrecord.subtitle"), this.width / 2, 28, 0x515658);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
