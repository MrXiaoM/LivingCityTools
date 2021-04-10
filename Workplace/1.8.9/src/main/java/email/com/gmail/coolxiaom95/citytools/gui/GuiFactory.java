package email.com.gmail.coolxiaom95.citytools.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory {
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class mainConfigGuiClass() {
		return ConfigGui.class;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set runtimeGuiCategories() {
		return null;
	}
}
