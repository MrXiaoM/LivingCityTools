package email.com.gmail.coolxiaom95.citytools;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class FMLPlugin implements IFMLLoadingPlugin {

	public static File modLocation;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "email.com.gmail.coolxiaom95.citytools.ModTransformer" };
	}

	@Override
	public String getModContainerClass() {
		
		return "email.com.gmail.coolxiaom95.citytools.Main";
	}

	@Override
	public String getSetupClass() {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		// TODO 自动生成的方法存根
        modLocation = (File)data.get("coremodLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO 自动生成的方法存根
		return null;
	}

}
