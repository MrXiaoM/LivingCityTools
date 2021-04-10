package email.com.gmail.coolxiaom95.citytools.util;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;

public class ScriptUtil {
	public static List<String> scriptLine;
	Thread thread;

	public ScriptUtil() {
		thread = new Thread(new awa());
	}

	public void runScript() {
		if (!thread.isAlive())
			thread.run();
	}

	private class awa implements Runnable {
		@Override
		public void run() {
			for (String line : scriptLine) {
				if (line.startsWith("#"))
					continue;
				// 等待
				if (line.startsWith("wait ")) {
					if (line.split(" ").length == 2) {
						String value = line.split(" ")[1];
						long vlong = strToLong(value);
						if (vlong > 0) {
							try {
								Thread.sleep(vlong);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("[" + line + "] 语法错误，正确用法“wait 等待时间”，其中，变量等待时间的单位为毫秒");
						}
					} else {
						System.out.println("[" + line + "] 变量数量不正确，应为1");
					}
				}
				// 切换物品
				if (line.startsWith("changeItem ")) {
					if (line.split(" ").length == 2) {
						String value = line.split(" ")[1];
						int vint = strToInt(value);
						if (vint >= 0 && vint < 9) {
							Minecraft mc = Minecraft.getMinecraft();
							mc.player.inventory.currentItem = vint;
							mc.player.swingArm(EnumHand.MAIN_HAND);
						} else {
							System.out.println("[" + line + "] 语法错误，正确用法“changeItem 数字”，其中，变量数字的取值范围为0-8");
						}
					} else {
						System.out.println("[" + line + "] 变量数量不正确，应为1");
					}
				}
				// 使用物品
				if (line.trim().startsWith("useItemInHand")) {
					Minecraft mc = Minecraft.getMinecraft();
					if (mc.player.getHeldItemMainhand() != null)
						mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
					else
						System.out.println("[" + line + "] 手中必须要拿着物品!");
				}
				// 潜行
				if (line.trim().startsWith("sneak")) {
					Minecraft.getMinecraft().getConnection().sendPacket(new CPacketEntityAction(
							Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
					Minecraft.getMinecraft().getConnection().sendPacket(new CPacketEntityAction(
							Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
				}
				if (line.trim().startsWith("jump")) {
					
				}
				// 打开物品栏
				if (line.trim().startsWith("openInventory")) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().player));
				}

				// 窗口点击
				if (line.startsWith("windowClick ")) {
					if (line.split(" ").length == 4) {
						Minecraft mc = Minecraft.getMinecraft();
						String value = line.split(" ")[1];
						String button = line.split(" ")[2];
						String type = line.split(" ")[3];
						int vint = strToInt(value);
						int vint2 = strToInt(button);
						int vint3 = strToInt(type); // 0-6
						if (vint2 >= 0 && vint2 < 9) {
							if (vint3 >= 0 && vint3 <= 6) {
								if (Minecraft.getMinecraft().currentScreen != null) {
									if (mc.currentScreen instanceof GuiContainer) {
										GuiContainer gui = (GuiContainer) mc.currentScreen;
										mc.playerController.windowClick(gui.inventorySlots.windowId, vint, vint2,ClickType.values()[vint3],
												mc.player);
									}
								} else {
									System.out.println("[" + line + "] 运行时错误，未打开任何窗口");
								}
							} else {
								System.out.println("[" + line + "] 变量 类型 取值范围错误，应为 0-6");
							}
						} else {
							System.out.println("[" + line + "] 变量 按钮 取值范围错误，应为 0-8");
						}
					} else {
						System.out.println("[" + line + "] 变量数量不正确，应为3");
					}
				}
				// 在玩家聊天栏添加消息
				if (line.startsWith("toPlayer ")) {
					String value = line.substring(9);
					Util.addMessage(value);
				}
				// 发送一条消息
				if (line.startsWith("send ")) {
					String value = line.substring(5);
					Util.sendChatMessage(value, false);
				}
				// 输出日志
				if (line.startsWith("log ")) {
					String value = line.substring(4);
					System.out.print(value);
				}
			}
		}

		private long strToLong(String str) {
			try {
				return Long.parseLong(str);
			} catch (NumberFormatException e) {
				// 肯定是输入了不正确的数字，不用整了，直接让它返回 -1 就得
			}
			return -1;
		}

		private int strToInt(String str) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				// 肯定是输入了不正确的数字，不用整了，直接让它返回 -1 就得
			}
			return -1;
		}
	}
}
