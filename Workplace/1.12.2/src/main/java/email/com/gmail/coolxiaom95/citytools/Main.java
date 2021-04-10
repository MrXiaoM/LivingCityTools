package email.com.gmail.coolxiaom95.citytools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import email.com.gmail.coolxiaom95.citytools.gui.hotkey.MyKey;
import email.com.gmail.coolxiaom95.citytools.handlers.KeyInputHandler;
import email.com.gmail.coolxiaom95.citytools.handlers.ScreenHandler;
import email.com.gmail.coolxiaom95.citytools.util.ScriptUtil;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

public class Main extends DummyModContainer {

	public static String commoditiesData = "";
	public static final boolean debug = true;
	public static Configuration configFile;
	public static boolean config_enable;
	public static boolean config_enable_inv;
	public static boolean config_enable_inv_playsound;
	public static boolean config_enable_menu;
	public static boolean config_enable_gamemenu;
	public static boolean config_enable_mainmenu;
	public static boolean config_enable_tp;
	public static boolean config_enable_unbreakable;
	public static boolean config_enable_frameshow;
	public static boolean config_enable_wonderfulstate;

	public static int config_x;
	public static int config_y;
	public static int config_s_x;
	public static int config_s_y;

	public static boolean config_mainmenu_showmultiplayer;

	public static int config_scale = 1;
	public static float config_box_r = 0.0f;
	public static float config_box_g = 0.0f;
	public static float config_box_b = 0.0f;
	public static float config_box_a = 1.4f;
	public static float config_box_width = 4.0f;

	public static boolean config_frameshow_replace_color;
	
	public static int config_align = 0;

	public static String config_str = "";
	public static int config_str_x;
	public static int config_str_y;
	public static int config_str_align = 0;

	public static int config_wonderfulstate_style = 0;

	public static boolean config_menu_showicon;
	public static int config_menu_x = -94;
	public static int config_menu_y = -38;
	public static int config_menu_align = 4;
	
	public static boolean config_pay_record_enable = true;

	public static Main instance;

	public static ArrayList<String> friends = new ArrayList<String>();
	public static ArrayList<String> chat_filter = new ArrayList<String>();
	public static ArrayList<MyKey> hotkey_list = new ArrayList<MyKey>();
	public static ArrayList<String> pay_record = new ArrayList<String>();
	public static ArrayList<String> chatNotices = new ArrayList<String>();

	public static String now_chatmsg = "";
	public static ResourceLocation menu_icon = new ResourceLocation("citytools", "textures/gui/menu.png");
	public static ResourceLocation tp_tips = new ResourceLocation("citytools", "textures/gui/tp_tips.png");

	public static String serverid = "sc";

	public static KeyInputHandler keyInputHandler;
	public static ScreenHandler screenHandler;
	public static ScriptUtil scriptUtil;
	/**
	 * 出自 白玉楼之梦 实例代码
	 */
	public static final Map<String, ResourceLocation> skinCache = new ConcurrentHashMap<String, ResourceLocation>();

	public static org.apache.logging.log4j.Logger log;

	public Main() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		
		meta.modId = "citytools";
		meta.name = "都市小工具";
		meta.version = "1.9.1";
		meta.authorList = Arrays.asList("Mr_Xiao_M");
		meta.credits = "Forge & FML Team, szszss, The_bird";
		meta.description = "这是一个在生存都市服务器上使用的mod，你也可以用来装X";
		meta.url = "https://space.bilibili.com/330771760";
		System.out.println(FMLPlugin.modLocation.getPath());
		FMLClientHandler.instance().addModAsResource(this);
	}
    
	public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange("[1.12,1.12.2]");
    }
    
	public String getGuiClassName() {
		return "email.com.gmail.coolxiaom95.citytools.gui.GuiFactory";
	}
	
	public Class<?> getCustomResourcePackClass() {
		return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
	}
	
	public File getSource() {
		return FMLPlugin.modLocation;
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent preInitEvent) {
		instance = this;
		log = preInitEvent.getModLog();
		scriptUtil = new ScriptUtil();
		configFile = new Configuration(preInitEvent.getSuggestedConfigurationFile());
		syncConfig();
		this.loadIgnoreList();
		this.loadKeyList();
		this.loadPayRecordList();
		this.loadNoticeList();
	}

	public static void syncConfig() {
		config_enable = configFile.getBoolean("纸娃娃_0_是否启用", "general", true, "在游戏中绘制一个以你为模型的小人");
		config_x = configFile.getInt("纸娃娃_1_x坐标", "general", 40, -5000, 5000, "需要绘制纸娃娃的相对坐标x");
		config_y = configFile.getInt("纸娃娃_2_y坐标", "general", 80, -5000, 5000, "需要绘制纸娃娃的相对坐标y");
		config_s_x = configFile.getInt("纸娃娃_3_x轴视角", "general", -60, -500, 500, "纸娃娃看着的位置x");
		config_s_y = configFile.getInt("纸娃娃_4_y轴视角", "general", -90, -500, 500, "纸娃娃看着的位置y");
		config_scale = configFile.getInt("纸娃娃_5_缩放大小", "general", 30, 30, 2700, "缩放大小…就是放大到多少呗");
		config_align = configFile.getInt("纸娃娃_6_排列位置", "general", 0, 0, 8,
				"0-左上 1-中上 2-右上 3-中右 4-右下 5-中下 6-左下 7-中左 8-中间");
		config_str = configFile.getString("显示的文字_0_内容", "general", "", "在屏幕显示的文字(\\n换行, &会被转成颜色代码)");
		config_str_x = configFile.getInt("显示的文字_1_x坐标", "general", 5, -5000, 5000, "需要绘制的文字的相对坐标x");
		config_str_y = configFile.getInt("显示的文字_2_y坐标", "general", 5, -5000, 5000, "需要绘制的文字的相对坐标y");
		config_str_align = configFile.getInt("显示的文字_3_排列位置", "general", 0, 0, 8,
				"0-左上 1-中上 2-右上 3-中右 4-右下 5-中下 6-左下 7-中左 8-中间");
		config_enable_inv = configFile.getBoolean("都市背包_0_是否启用", "general", true,
				"修改背包的动画和样式\n" + "§c这是个实验性功能，可能会跟一些修改背包界面的mod发生冲突!\n" + "§c如果发生冲突请关闭此功能或开启兼容模式§r");
		config_enable_inv_playsound = configFile.getBoolean("都市背包_1_播放音效", "general", true,
				"在打开或关闭背包的时候播放音效");
		config_box_r = configFile.getFloat("方块选择框_0_颜色R", "general", 0.0f, 0.0f, 255.0f, "红色通道的值");
		config_box_g = configFile.getFloat("方块选择框_1_颜色G", "general", 0.0f, 0.0f, 255.0f, "绿色通道的值");
		config_box_b = configFile.getFloat("方块选择框_2_颜色B", "general", 0.0f, 0.0f, 255.0f, "蓝色通道的值");
		config_box_a = configFile.getFloat("方块选择框_3_颜色A", "general", 0.4f, 0.0f, 1.0f, "透明通道的值");
		config_box_width = configFile.getFloat("方块选择框_4_宽度", "general", 4.0f, 1.0f, 50.0f, "边框宽度");
		config_enable_menu = configFile.getBoolean("菜单_0_是否启用", "general", true, "在游戏右下角添加一个菜单图标，并开启菜单功能");
		config_menu_x = configFile.getInt("菜单_1_x坐标", "general", -94, -5000, 5000, "");
		config_menu_y = configFile.getInt("菜单_2_y坐标", "general", -38, -5000, 5000, "");
		config_menu_align = configFile.getInt("菜单_3_排列位置", "general", 4, 0, 8,
				"0-左上 1-中上 2-右上 3-中右 4-右下 5-中下 6-左下 7-中左 8-中间");
		config_menu_showicon = configFile.getBoolean("菜单_4_是否显示图标", "general", true, "是否显示菜单图标，这不影响菜单的打开");
		config_enable_mainmenu = configFile.getBoolean("更好的主菜单_0_是否启用", "general", true, "替换掉游戏的主菜单，会与修改主菜单的mod冲突");
		config_mainmenu_showmultiplayer = configFile.getBoolean("更好的主菜单_1_显示多人游戏按钮", "general", false,
				"为一些特殊玩家着想，在主菜单添加多人游戏的入口");
		config_enable_gamemenu = configFile.getBoolean("更好的菜单_0_是否启用", "general", true, "替换掉游戏Esc菜单");
		config_enable_tp = configFile.getBoolean("TP提示_0_是否启用", "general", true, "在玩家tp你的时候显示一块提示框");
		config_enable_unbreakable = configFile.getBoolean("神器无法破坏_0_是否启用", "general", false,
				"在你的视角内神器破坏方块时不显示耐久条 (强迫症福利)\n§c§l(这是一个实验性功能，若出现异样请关闭此项)");
		config_enable_wonderfulstate = configFile.getBoolean("更好的状态栏_0_是否启用", "general", true, "更改你的血条/饥饿值样式");
		config_wonderfulstate_style = configFile.getInt("更好的状态栏_1_样式", "general", 1, 0, 1,
				"样式分别有:\n 0 - UNDERTALE\n 1 - DELTARUNE\n");
		config_enable_frameshow = configFile.getBoolean("展示框物品显示_0_是否启用", "general", true, "显示你准星指向的展示框的物品信息");
		config_frameshow_replace_color = configFile.getBoolean("展示框物品显示_1_将颜色字符转换成&", "general", false, "非必要时无需开启");
		if (configFile.hasChanged()) {
			configFile.save();
		}
		if(!config_enable_inv) {
			if(Minecraft.getMinecraft() != null) {
				if(Minecraft.getMinecraft().player != null) {
					Minecraft.getMinecraft().player.inventoryContainer = new ContainerPlayer(
							Minecraft.getMinecraft().player.inventory,
							!Minecraft.getMinecraft().player.world.isRemote,
							Minecraft.getMinecraft().player);
				}
			}
		}
	}

	@Subscribe
	public void init(FMLInitializationEvent event) {
		Main.keyInputHandler = new KeyInputHandler();
		Main.screenHandler = new ScreenHandler();
		MinecraftForge.EVENT_BUS.register(Main.instance);
		MinecraftForge.EVENT_BUS.register(Main.keyInputHandler);
		MinecraftForge.EVENT_BUS.register(Main.screenHandler);
		Minecraft mc = Minecraft.getMinecraft();
		String result = "";
		
		for(String s : mc.getResourceManager().getResourceDomains()) {
		    result += s+ ", ";
		}
		System.out.println(result);
	}
	/**
	 * 载入账本
	 */
	public void loadPayRecordList() {
		try {
			log.info("正在初始化账本数据");
			File file = new File(".\\payRecord.citytools");
			if (!file.exists()) {
				pay_record = new ArrayList<String>();
				log.info("找不到文件，跳过");
				return;
			}
			pay_record.clear();

			log.info("找到文件，清空原列表");
			List<String> lines = Util.readFile(".\\payRecord.citytools");
			log.info("正在获取行");
			for (String line : lines) {
				if (!line.isEmpty()) {
					pay_record.add(line);
					log.info(line);
				}
			}
			log.info("账本获取完成");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	public void clearPayRecord() {
		pay_record.clear();
		try {
			savePayRecordList(pay_record);
		}catch(Throwable x) {
			x.printStackTrace();
		}
	}
	
	public void addPayRecord(String str) {
		pay_record.add(str);
		try {
			savePayRecordList(pay_record);
		}catch(Throwable x) {
			x.printStackTrace();
		}
	}
	/**
	 * 保存账本
	 */
	public void savePayRecordList(ArrayList<String> list) throws IOException {
		pay_record = list;

		File file = new File(".\\payRecord.citytools");
		if (list.size() < 0) {
			if (file.exists()) {
				file.delete();
			}
			return;
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		String content = "";
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if (!str.trim().isEmpty()) {
				content += str + (i != list.size() - 1 ? "\n" : "");
			}
		}
		Util.writeFile(".\\payRecord.citytools", content);
	}

	/**
	 * 载入聊天提醒列表
	 */
	public void loadNoticeList() {
		try {
			log.info("正在初始化聊天提醒列表");
			File file = new File(".\\chatNoticesList.citytools");
			if (!file.exists()) {
				chatNotices = new ArrayList<String>();
				log.info("找不到文件，跳过");
				return;
			}
			chatNotices.clear();

			log.info("找到文件，清空原列表");
			List<String> lines = Util.readFile(".\\chatNoticesList.citytools");
			log.info("正在获取行");
			for (String line : lines) {
				if (!line.isEmpty()) {
					chatNotices.add(line);
				}
			}
			log.info("聊天提醒列表获取完成");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 保存聊天提醒列表
	 */
	public void saveNoticeList(ArrayList<String> list) throws IOException {
		chatNotices = list;

		File file = new File(".\\chatNoticesList.citytools");
		if (list.size() < 0) {
			if (file.exists()) {
				file.delete();
			}
			return;
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		String content = "";
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if (!str.trim().isEmpty()) {
				content += str + (i != list.size() - 1 ? "\n" : "");
			}
		}
		Util.writeFile(".\\chatNoticesList.citytools", content);
	}
	
	/**
	 * 载入屏蔽列表
	 */
	public void loadIgnoreList() {
		try {
			log.info("正在初始化屏蔽列表");
			File file = new File(".\\ignoreList.citytools");
			if (!file.exists()) {
				chat_filter = new ArrayList<String>();
				log.info("找不到文件，跳过");
				return;
			}
			chat_filter.clear();

			log.info("找到文件，清空原列表");
			List<String> lines = Util.readFile(".\\ignoreList.citytools");
			log.info("正在获取行");
			for (String line : lines) {
				if (!line.isEmpty()) {
					chat_filter.add(line);
				}
			}
			log.info("屏蔽列表获取完成");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 保存屏蔽列表
	 */
	public void saveIgnoreList(ArrayList<String> list) throws IOException {
		chat_filter = list;

		File file = new File(".\\ignoreList.citytools");
		if (list.size() < 0) {
			if (file.exists()) {
				file.delete();
			}
			return;
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		String content = "";
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if (!str.trim().isEmpty()) {
				content += str + (i != list.size() - 1 ? "\n" : "");
			}
		}
		Util.writeFile(".\\ignoreList.citytools", content);
	}

	/**
	 * 载入热键列表
	 */
	public void loadKeyList() {
		try {
			if (true) {
				log.info("正在初始化热键列表");
				File file = new File(".\\Hotkeys.citytools");
				if (!file.exists()) {
					hotkey_list.clear();
					log.info("找不到文件，跳过");
					return;
				}
				hotkey_list.clear();

				log.info("找到文件，清空原列表");
				List<String> lines = Util.readFile(".\\Hotkeys.citytools");
				log.info("正在获取行");
				for (String line : lines) {
					if (line.contains(":")) {
						String desc = line.substring(0, line.lastIndexOf(":"));
						String code = line.substring(line.lastIndexOf(":") + 1, line.length()).trim();
						int keycode = this.tryParseInt(code);
						if (keycode != -666 && !desc.trim().isEmpty()) {
							hotkey_list.add(new MyKey(desc, keycode));
						}
					}
				}
				log.info("热键列表获取完成");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 保存热键列表
	 */
	public void saveKeyList(ArrayList<MyKey> list) throws IOException {
		hotkey_list = list;

		File file = new File(".\\Hotkeys.citytools");
		if (list.size() < 0) {
			if (file.exists()) {
				file.delete();
			}
			return;
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		String content = "";
		for (int i = 0; i < list.size(); i++) {
			MyKey key = list.get(i);
			if (!key.getKeyDescription().trim().isEmpty()) {
				content += key.getKeyDescription() + ":" + key.getKeyCode() + (i != list.size() - 1 ? "\n" : "");
				;
			}
		}
		Util.writeFile(".\\Hotkeys.citytools", content);
	}

	private int tryParseInt(String s) {
		int result = -666;
		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals("citytools")) {
			syncConfig();
		}
	}
}
