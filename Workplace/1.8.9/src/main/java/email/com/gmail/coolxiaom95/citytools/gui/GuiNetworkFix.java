package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiNetworkFix extends GuiScreen{
	
	GuiScreen parentScreen;
	public GuiNetworkFix(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	@Override
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 30,
				200, 20, I18n.format("gui.back")));
		this.buttonList.add(new GuiButton(1, width / 2 + 100, height - 50,
				100, 20, I18n.format("citytools.networkfix.config")));
		boolean isWindows = false;
        for (Entry<String, String> entry : (new TreeMap<String, String>(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet())
        {
            if(((String)entry.getKey()).equalsIgnoreCase("os_name")) {
            	if(((String)entry.getValue()).startsWith("Windows")) {
            		isWindows = true;
            	}
            }
        }
        buttonList.get(1).enabled = isWindows;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 0) {
			this.mc.displayGuiScreen(parentScreen);
		}
		if(button.id == 1) {
			this.openNetworkAdapter();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.networkfix.title"), width /2 - mc.fontRendererObj.getStringWidth(I18n.format("citytools.networkfix.title"))/2, 20, -1);
		for(int i = 0; i<=5;i++) {
			mc.fontRendererObj.drawStringWithShadow(I18n.format("citytools.networkfix.context."+i), width /2 - 80, 50 + (mc.fontRendererObj.FONT_HEIGHT+2)*i, -1);
		}
		if(buttonList.get(1).isMouseOver() && !buttonList.get(1).enabled)
		{
			List<String> s = new ArrayList<String>();
			s.add(I18n.format("citytools.networkfix.config.unsupport"));
			this.drawHoveringText(s, mouseX, mouseY);
		}
	}

	@Override
	public void updateScreen() {

	}
	
	private void openNetworkAdapter() {
        try {
			Runtime.getRuntime().exec("rundll32.exe shell32.dll,Control_RunDLL ncpa.cpl");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
