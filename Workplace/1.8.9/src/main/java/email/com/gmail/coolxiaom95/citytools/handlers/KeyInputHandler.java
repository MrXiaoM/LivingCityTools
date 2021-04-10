package email.com.gmail.coolxiaom95.citytools.handlers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.gui.ConfigGui;
import email.com.gmail.coolxiaom95.citytools.gui.GuiUser;
import email.com.gmail.coolxiaom95.citytools.gui.hotkey.MyKey;
import email.com.gmail.coolxiaom95.citytools.util.ScriptUtil;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyInputHandler {
	public KeyBinding options;
	public KeyBinding userinfo;
	public KeyBinding tpyes;
	public KeyBinding tpno;
	// public KeyBinding test;

	public KeyInputHandler() {
		this.init();
	}

	public void init() {
		this.options = new KeyBinding("打开 Mod 设置", 24, "都市工具 Mod");
		this.userinfo = new KeyBinding("打开菜单(如果可用)", 50, "都市工具 Mod");
		this.tpyes = new KeyBinding("接受传送请求", 21, "都市工具 Mod");
		this.tpno = new KeyBinding("拒绝传送请求", 49, "都市工具 Mod");
		// this.test = new KeyBinding("TEST", 36, "都市工具 Mod");
		ClientRegistry.registerKeyBinding(this.options);
		ClientRegistry.registerKeyBinding(this.userinfo);
		ClientRegistry.registerKeyBinding(this.tpyes);
		ClientRegistry.registerKeyBinding(this.tpno);
		// ClientRegistry.registerKeyBinding(this.test);
	}

	@SubscribeEvent
	public void onKeyDown(KeyInputEvent event) {
		if (this.options.isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new ConfigGui(Minecraft.getMinecraft().currentScreen));
		}
		if (Main.config_enable_menu) {
			if (this.userinfo.isKeyDown()) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiUser());
			}
		}
		if (this.tpyes.isKeyDown()) {
			ScreenHandler.clearTpTime();
			Util.sendChatMessage("/etpaccept", false);
		}
		if (this.tpno.isKeyDown()) {
			ScreenHandler.clearTpTime();
			Util.sendChatMessage("/etpdeny", false);
		}
		for (MyKey key : Main.hotkey_list) {
			if(Keyboard.getKeyName(key.getKeyCode()) != null) {
				if (key.isKeyDown()) {
					if (key.getKeyDescription().startsWith("script:")) {
						try {
							String scriptname = key.getKeyDescription().substring(7);
							if (new File("./scripts/" + scriptname + ".cts").exists()) {
								System.out.println("执行脚本 “" + scriptname + ".cts”");
								List<String> script = Util.readFile("./scripts/" + scriptname + ".cts");
								ScriptUtil.scriptLine = script;
								Main.scriptUtil.runScript();
							} else {
								System.out.println("脚本文件 “" + scriptname + ".cts” 不存在!");
							}
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					} else {
						Util.sendChatMessage(key.getKeyDescription(), false);
					}
				}
			}
		}
	}
}
