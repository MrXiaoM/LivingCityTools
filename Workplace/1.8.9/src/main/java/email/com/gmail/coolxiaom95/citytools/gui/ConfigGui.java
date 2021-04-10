package email.com.gmail.coolxiaom95.citytools.gui;

import email.com.gmail.coolxiaom95.citytools.Main;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {
	public ConfigGui(GuiScreen parent) {
		super(parent, (new ConfigElement(Main.configFile.getCategory("general"))).getChildElements(), "citytools",
				false, false, I18n.format("citytools.settings.title"));
		this.titleLine2 = I18n.format("citytools.settings.subtitle");
	}
}
