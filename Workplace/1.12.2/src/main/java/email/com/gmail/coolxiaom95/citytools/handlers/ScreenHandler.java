package email.com.gmail.coolxiaom95.citytools.handlers;

import java.util.Calendar;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.gui.GuiNewIngameMenu;
import email.com.gmail.coolxiaom95.citytools.gui.GuiNewMainMenu;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ScreenHandler {

	int restp_cooldown = 0;
	String tp_playername = "";
	boolean isOpenIngameMenu = false;
	boolean isOpenInv = false;
	public static float offset = 90.0F;
	private static int tp_time = 0;

	public static void clearTpTime() {
		tp_time = 0;
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String msg = event.getMessage().getUnformattedText();
		boolean isIgnore = false;
		if (Main.chat_filter != null) {
			if (Main.chat_filter.size() > 0) {
				for (String s : Main.chat_filter) {
					if (msg.contains(s)) {
						event.setCanceled(true);
						isIgnore = true;
						break;
					}
				}
			}
		}
		if (msg.contains("\n")) {
			String msg1 = msg.substring(0, msg.indexOf("\n"));
			if (msg1.startsWith("§6§l生存都市§f§l>> §a你收到一条来自") && msg1.endsWith("的传送申请!")) {
				String playername = msg1
						.substring("§6§l生存都市§f§l>> §a你收到一条来自".length(), msg1.length() - "的传送申请!".length()).trim();
				boolean screened = false;
				for (String s : Main.chat_filter) {
					if (playername.contains(s)) {
						screened = true;
						break;
					}
				}
				if(!screened) {
					tp_time = 200;
					tp_playername = playername;
				}
			}
		}
		if (Main.config_pay_record_enable) {
			String msg1 = Util.clearColor(msg);
			// 已从 Mr_Xiao_M 接收$1
			if (msg1.startsWith("已从") && msg1.contains("接收")) {
				String player = msg1.substring(msg1.indexOf("已从") + 3, msg1.indexOf("接收") - 1);
				String money = msg1.substring(msg1.indexOf("接收") + 2);
				String data = "epay;" + getNowTime() + ";" + player + ";" + money;
				Main.instance.addPayRecord(data);
			}
			// [Money] 收到 $1.00 从 Mr_Xiao_M
			if (msg1.startsWith("[Money] 收到") && msg1.contains("从")) {
				String player = msg1.substring(msg1.indexOf("从") + 2);
				String money = msg1.substring(msg1.indexOf("收到") + 3, msg1.indexOf("从") - 1);
				String data = "pay;" + getNowTime() + ";" + player + ";" + money;
				Main.instance.addPayRecord(data);
			}
		}
		if(!isIgnore) {
			for(String s : Main.chatNotices) {
				if(s.contains(";")) {
					String[] a = s.split(";");
					if(a.length == 3) {
						if(msg.contains(a[0])) {
							String resPath = a[1];
							float pitch = Util.strToFloat(a[2], 1.0F);
							Minecraft.getMinecraft().getSoundHandler().stopSounds();
							Minecraft.getMinecraft().getSoundHandler()
								.playSound(PositionedSoundRecord.getMasterRecord(
										new SoundEvent(new ResourceLocation(resPath)), pitch));
						}
					}
				}
			}
		}
		Main.now_chatmsg = msg;
	}

	String getNowTime() {
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player != null && event.getEntityPlayer() != null) {
				if (event.getEntityPlayer().getName() == mc.player.getName()) {
					RayTraceResult moveobj = mc.getRenderViewEntity().rayTrace(4.0f, 1.0f);

					TileEntity signTile = mc.world.getTileEntity(moveobj.getBlockPos());
					if (signTile != null && signTile instanceof TileEntitySign) {
						ITextComponent[] text = ((TileEntitySign) signTile).signText;
						String[] sign = new String[text.length];
						int i = 0;
						for (ITextComponent context : text) {
							if (context == null) {
								context = new TextComponentString("");
							}
							sign[i] = context.getUnformattedText();
							// log.info(context.getUnformattedText());
							i++;
						}

						if (sign[0].startsWith("[领地传送]")) {
							if (restp_cooldown > 0) {
								Util.addMessage("§c冷却时间还没有到，请稍等片刻");
							} else {
								String name = sign[1];
								Util.sendChatMessage("/res tp " + name, false);
								restp_cooldown = 20;
							}
						}
					}
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onDrawBlockHighLight(DrawBlockHighlightEvent event) {
		event.setCanceled(true);
		EntityPlayer player = event.getPlayer();
		RayTraceResult movingObjectPositionIn = event.getTarget();
		int p_72731_3_ = event.getSubID();
		float partialTicks = event.getPartialTicks();
		if (p_72731_3_ == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(Main.config_box_r/255f, Main.config_box_g/255f, Main.config_box_b/255f, Main.config_box_a);
			GL11.glLineWidth(Main.config_box_width);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			BlockPos blockpos = movingObjectPositionIn.getBlockPos();
			Block block = Minecraft.getMinecraft().world.getBlockState(blockpos).getBlock();

			if (block.getDefaultState().getMaterial() != Material.AIR
					&& Minecraft.getMinecraft().world.getWorldBorder().contains(blockpos)) {
				
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
				RenderGlobal.drawSelectionBoundingBox(
						block.getBlockState().getBaseState().getBoundingBox(Minecraft.getMinecraft().world, blockpos)
								.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
								.offset(-d0, -d1, -d2), Main.config_box_r,Main.config_box_g,Main.config_box_b,Main.config_box_a);
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (Main.config_enable_inv && event.getGui() instanceof net.minecraft.client.gui.inventory.GuiInventory) {
			event.setGui(new email.com.gmail.coolxiaom95.citytools.gui.GuiInventory(mc.player));
			if(Main.config_enable_inv_playsound) {
				mc.getSoundHandler().stopSounds();
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(new SoundEvent(new ResourceLocation("citytools", "inv.open")), 1.0F));
			}
		}
		if (Main.config_enable_mainmenu && event.getGui() instanceof GuiMainMenu) {
			event.setGui(new GuiNewMainMenu());
		}
		if (Main.config_enable_gamemenu && event.getGui() instanceof GuiIngameMenu) {
			event.setGui(new GuiNewIngameMenu());
		}
	}

	@SubscribeEvent
	public void OnClientTick(ClientTickEvent event) {
		if (Minecraft.getMinecraft() != null) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.currentScreen instanceof GuiNewIngameMenu) {
				this.isOpenIngameMenu = true;
			} else {
				this.isOpenIngameMenu = false;
			}
			if (mc.currentScreen instanceof email.com.gmail.coolxiaom95.citytools.gui.GuiInventory) {
				this.isOpenInv = true;
			} else {
				this.isOpenInv = false;
				if(offset < 90) {
					offset += 15;
				}
				if(offset > 90) {
					offset = 90;
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
		if (Minecraft.getMinecraft() != null) {

			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player != null) {
				if (Main.config_enable) {
					int x = getXByAlign(Main.config_align, Main.config_x);
					int y = getYByAlign(Main.config_align, Main.config_y);

					int x_ = getXByAlign(Main.config_str_align, Main.config_str_x);
					int y_ = getYByAlign(Main.config_str_align, Main.config_str_y);
					if (Main.config_str.contains("\\n")) {
						String[] str = Main.config_str.replace("\\n", "\n").replace("\\r", "\r").replace("&", "§")
								.split("\n");
						for (int i = 0; i < str.length; i++) {
							mc.fontRenderer.drawStringWithShadow(str[i] + "§r", x_,
									y_ + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * i), -1);
						}
					} else {
						mc.fontRenderer.drawStringWithShadow(Main.config_str.replace("&", "§") + "§r", x_, y_, -1);
					}

					GuiInventory.drawEntityOnScreen(x, y, Main.config_scale, x + Main.config_s_x, y + Main.config_s_y,
							mc.player);
				}
				if (Main.config_enable_menu) {
					// mc.displayWidth / 2 - 94, mc.displayHeight / 2 - 38
					int x = getXByAlign(Main.config_menu_align, Main.config_menu_x);
					int y = getYByAlign(Main.config_menu_align, Main.config_menu_y);
					String menu_text = I18n.format("citytools.menu.text",
							Keyboard.getKeyName(Main.keyInputHandler.userinfo.getKeyCode()));
					mc.fontRenderer.drawStringWithShadow(menu_text + "§r",
							x + 10 - (mc.fontRenderer.getStringWidth(menu_text) / 2), y + 22, -1);
					mc.renderEngine.bindTexture(Main.menu_icon);
					Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 20, 20, 20, 20);
				}
				// Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("当前聊天: " +
				// now_chatmsg, 2, 2, -1);
				if (Main.config_enable_tp) {
					if (tp_time > 0) {

						Gui.drawRect(mc.displayWidth / 2 - 175, mc.displayHeight / 2 - 120, mc.displayWidth / 2 - 15,
								mc.displayHeight / 2 - 55, 1107296256);

						GlStateManager.resetColor();
						// bindTexture(getSkin(tp_playername));
						// 8,8
						// Gui.drawModalRectWithCustomSizedTexture(mc.displayWidth / 2 - 165,
						// mc.displayHeight / 2 - 112, 24, 24, 24, 24, 192,
						// 192);
						// 40,8
						// Gui.drawModalRectWithCustomSizedTexture(mc.displayWidth / 2 - 165,
						// mc.displayHeight / 2 - 112, 120, 24, 24, 24, 192,
						// 192);

						GlStateManager.pushMatrix();
						GlStateManager.translate(mc.displayWidth / 2 - 165, mc.displayHeight / 2 - 110, 0.0F);
						float f = 2f;
						GlStateManager.scale(f, f, f);
						mc.fontRenderer.drawString("“" + tp_playername + "”", 0, 0, -1, false);
						GlStateManager.popMatrix();

						mc.fontRenderer.drawString(I18n.format("citytools.tpmsg.title"), mc.displayWidth / 2 - 165,
								mc.displayHeight / 2 - 82, -1, true);

						mc.fontRenderer.drawString(
								I18n.format("citytools.tpmsg.choice",
										Keyboard.getKeyName(Main.keyInputHandler.tpyes.getKeyCode()),
										Keyboard.getKeyName(Main.keyInputHandler.tpno.getKeyCode())),
								mc.displayWidth / 2 - 165, mc.displayHeight / 2 - 70, -1, true);

						// mc.fontRendererObj.drawString("§7在"+tp_time+"后隐藏面板",mc.displayWidth / 2 -
						// 165, mc.displayHeight / 2 - 60, -1, true);
						tp_time--;
					}
				}

			}
			if (Main.config_enable_gamemenu) {
				Util.drawRectangle(0, 0, mc.displayWidth, mc.displayHeight, temp_time * 0.03F, 0.0F, 0.0F, 0.0F);
				if (this.isOpenIngameMenu) {
					if (temp_time < 15) {
						temp_time++;
					}
				} else {
					if (temp_time > 0) {
						temp_time--;
					}
				}
			}
			if (Main.config_enable_inv) {
				Util.drawRectangle(0, 0, mc.displayWidth, mc.displayHeight, temp_time1 * 0.04F, 0.0F, 0.0F, 0.0F);
				if (this.isOpenInv) {
					if (temp_time1 < 15) {
						temp_time1++;
					}
				} else {
					if (temp_time1 > 0) {
						temp_time1--;
					}
				}
			}
			if(Main.config_enable_frameshow) {
			if (Minecraft.getMinecraft().getRenderManager().pointedEntity instanceof EntityItemFrame) {
				ScaledResolution scaled = new ScaledResolution(mc);
				int width = scaled.getScaledWidth();
				int height = scaled.getScaledHeight();
				EntityItemFrame frame = (EntityItemFrame) (Minecraft.getMinecraft().getRenderManager().pointedEntity);
				
				if (frame.getDisplayedItem() != null) {
					int x = width / 2+5;
					int y = height / 2 + 10;
					List<String> textLines = frame.getDisplayedItem().getTooltip(mc.player, mc.gameSettings.advancedItemTooltips?ITooltipFlag.TooltipFlags.ADVANCED:ITooltipFlag.TooltipFlags.NORMAL);

					GlStateManager.pushMatrix();
					GlStateManager.disableRescaleNormal();
					GlStateManager.disableDepth();
					int i = 0;

					for (String s : textLines) {
						int j = mc.getRenderManager().getFontRenderer().getStringWidth(s);

						if (j > i) {
							i = j;
						}
					}

					int l1 = x + 12;
					int i2 = y - 12;
					int k = 8;

					if (textLines.size() > 1) {
						k += 2 + (textLines.size() - 1) * 10;
					}

					if (l1 + i > width) {
						l1 -= 28 + i;
					}

					if (i2 + k + 6 > height) {
						i2 = height - k - 6;
					}
					
					int l = 1342177280;
					this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l,300.0F);
					this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l,300.0F);
					this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l,300.0F);
					this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l,300.0F);
					this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l,300.0F);
					// int i1 = 1347420415;
					int i1 = (int) 2415984639L;
					int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
					this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1,300.0F);
					this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1,300.0F);
					this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1,300.0F);
					this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1,300.0F);


					int k1 = 0;
					for (String str1 : textLines) {
						mc.getRenderManager().getFontRenderer().drawStringWithShadow(str1, l1, i2, -1);

						if (k1 == 0) {
							i2 += 2;
						}

						i2 += mc.getRenderManager().getFontRenderer().FONT_HEIGHT;
						k1++;
					}
					
					GlStateManager.enableDepth();
					GlStateManager.enableRescaleNormal();
					GlStateManager.popMatrix();
				}}
			}
		}
		if (restp_cooldown > 0) {
			restp_cooldown--;
		}
	}

	protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, float zLevel)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)right, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)right, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
		if (Minecraft.getMinecraft() != null) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player != null) {

				EntityPlayer player = mc.player;
				if (Main.config_enable_wonderfulstate) {
					if (event.getType() == ElementType.FOOD || event.getType() == ElementType.HEALTH
							|| event.getType() == ElementType.AIR || event.getType() == ElementType.ARMOR
							|| event.getType() == ElementType.HEALTHMOUNT) {
						event.setCanceled(true);
					}
					if (event.getType() == ElementType.EXPERIENCE) {
						event.setCanceled(true);
						int width = event.getResolution().getScaledWidth();
						int height = event.getResolution().getScaledHeight();
						int x = width / 2 - 91;
						int y = 0 - (int) ((90 - offset) / 90.0F) * 66;
						GlStateManager.pushMatrix();
						GlStateManager.translate(x, y, offset<90?10:0);
						mc.mcProfiler.startSection("expBar");
						mc.getTextureManager().bindTexture(Gui.ICONS);
						int i = mc.player.xpBarCap();
						if (i > 0) {
							int j = 182;
							int k = (int) (mc.player.experience * (float) (j + 1));
							int l = height - 32 + 3;
							Util.drawTexturedModalRect(0, l, 0, 64, j, 5);

							if (k > 0) {
								Util.drawTexturedModalRect(0, l, 0, 69, k, 5);
							}
						}
						mc.mcProfiler.endSection();
						if (mc.player.experienceLevel > 0) {
							mc.mcProfiler.startSection("expLevel");
							int k1 = 8453920;
							String s = "" + mc.player.experienceLevel;
							int l1 = (182 - mc.fontRenderer.getStringWidth(s)) / 2;
							int i1 = height - 31 - 4;
							// int j1 = 0;
							mc.fontRenderer.drawString(s, l1 + 1, i1, 0);
							mc.fontRenderer.drawString(s, l1 - 1, i1, 0);
							mc.fontRenderer.drawString(s, l1, i1 + 1, 0);
							mc.fontRenderer.drawString(s, l1, i1 - 1, 0);
							mc.fontRenderer.drawString(s, l1, i1, k1);
							mc.mcProfiler.endSection();
						}
						GlStateManager.popMatrix();
					}
					if (event.getType() == ElementType.HEALTH) {
						event.setCanceled(true);
						String name = "§l" + player.getName();
						int width = event.getResolution().getScaledWidth();
						int height = event.getResolution().getScaledHeight();
						float hp = player.getHealth();
						float maxhp = player.getMaxHealth();
						int food = player.getFoodStats().getFoodLevel();

						int x = width / 2;
						int y = height - GuiIngameForge.left_height - 20 - (int)((90 - offset) / 90.0F) * 70;

						float zoom_headimg = 1f;
						int x_headimg = 5;
						int y_headimg = 5;
						int t_width = (int) (16 * zoom_headimg) + 15 + mc.fontRenderer.getStringWidth(name) + 90;
						GlStateManager.pushMatrix();
						GlStateManager.translate(x - (t_width / 2), y, offset<90?10:0);

						int frame_width = mc.fontRenderer.getStringWidth(name) + 5 + (int) (16 * zoom_headimg) + 100;
						int frame_height = 26;

						// 边框 (背景)
						Util.drawRect(0, 0, frame_width, frame_height, 0.3f + temp_time1 * 0.04F, 0F, 0F, 0F);
						// 边框 (上)
						Util.drawRect(0, 0, frame_width, -1, 1, 0F, 255.0F, 255.0F);
						// 边框 (下)
						Util.drawRect(0, frame_height, frame_width, frame_height + 1, 1, 0F, 255.0F, 255.0F);
						// 边框 (左)
						Util.drawRect(0, 0, -1, frame_height, 1, 0F, 255.0F, 255.0F);
						// 边框 (右)
						Util.drawRect(frame_width, 0, frame_width + 1, frame_height, 1, 0F, 255.0F, 255.0F);
						
						mc.fontRenderer.drawStringWithShadow(name, (int) (16 * zoom_headimg) + 10, 9, 0xFFFFFF);

						ResourceLocation skin = mc.player.getLocationSkin();
						mc.renderEngine.bindTexture(skin);

						// 8,8
						drawModalRectWithCustomSizedTexture(x_headimg, y_headimg, 16 * zoom_headimg, 16 * zoom_headimg,
								16 * zoom_headimg, 16 * zoom_headimg, 128 * zoom_headimg, 128 * zoom_headimg);
						// 40,8
						drawModalRectWithCustomSizedTexture(x_headimg, y_headimg, 80 * zoom_headimg, 16 * zoom_headimg,
								16 * zoom_headimg, 16 * zoom_headimg, 128 * zoom_headimg, 128 * zoom_headimg);

						int max_width = 76;
						int max_height = 10;
						int x1 = (int) (16 * zoom_headimg) + 15 + mc.fontRenderer.getStringWidth(name);
						// 血条背景
						Util.drawRect(x1 + 10, 12, x1 + 10 + max_width, 12 + max_height, 1, 128F, 128.0F, 128.0F);
						// 血条
						Util.drawRect(x1 + 10, 12, x1 + 10 + (int) (((float) (hp / maxhp)) * max_width),
								12 + max_height, 1, 0.0F, 255F, 255F);
						// 饥饿度
						Util.drawRect(x1 + 10, 11 + max_height,
								x1 + 10 + (int) (((float) ((float) food / 20.0f)) * max_width), 12 + max_height, 1,
								255F, 128F, 0.0F);
						// 呼吸值
						if (player.isInsideOfMaterial(Material.WATER)) {
							float air = Minecraft.getMinecraft().player.getAir();
							Util.drawRect(x1 + 10, 12, x1 + 10 + (int) (((float) (air / 300)) * max_width), 13, 1, 0.0F,
									128F, 255F);
						}
						mc.fontRenderer.drawStringWithShadow("HP", x1, 12, 0xFFFFFF);
						mc.fontRenderer.drawStringWithShadow((int) hp + " / " + (int) maxhp,
								x1 + 85 - mc.fontRenderer.getStringWidth((int) hp + " / " + (int) maxhp),
								11 - mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF);

						GlStateManager.popMatrix();

						Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);

					}
				}
			}
		}
	}
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, float width, float height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
    
	int temp_time = 0;
	int temp_time1 = 0;

	private int getXByAlign(int alagn, int x) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		int width =  sr.getScaledWidth();
		if (alagn == 0 || alagn == 6 || alagn == 7) {
			return x;
		} else if (alagn == 1 || alagn == 5 || alagn == 8) {
			return (width) / 2 + x;
		} else if (alagn == 2 || alagn == 3 || alagn == 4) {
			return (width) + x;
		}
		return x;
	}

	private int getYByAlign(int alagn, int y) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		int height = sr.getScaledHeight();
		if (alagn == 0 || alagn == 1 || alagn == 2) {
			return y;
		} else if (alagn == 3 || alagn == 7 || alagn == 8) {
			return (height) / 2 + y;
		} else if (alagn == 4 || alagn == 5 || alagn == 6) {
			return (height) + y;
		}
		return y;
	}
}
