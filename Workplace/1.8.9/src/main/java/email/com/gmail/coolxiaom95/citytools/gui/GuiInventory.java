package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.foom.oneclickcrafting.GuiCommonMethods;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.handlers.ScreenHandler;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInventory extends GuiScreen {
	/** The location of the inventory background texture */
	protected static final ResourceLocation inventoryBackground = new ResourceLocation(
			"textures/gui/container/inventory.png");
	protected static final ResourceLocation inventoryNewBackground = new ResourceLocation("citytools",
			"textures/gui/container/inventory.png");
    protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	/**
	 * Draws the entity to the screen. Args: xPos, yPos, scale, mouseX, mouseY,
	 * entityLiving
	 */
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY,
			EntityLivingBase ent) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX, (float) posY, 50.0F);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
		ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
		ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private boolean hasActivePotionEffects;
	protected int xSize = 263;
	protected int ySize = 90;
	public ContainerPlayer inventorySlots;
	protected int guiLeft;
	protected int guiTop;
	private Slot theSlot;
	private Slot clickedSlot;
	private boolean isRightMouseClick;
	private ItemStack draggedStack;
	private int touchUpX;
	private int touchUpY;
	private Slot returningStackDestSlot;
	private long returningStackTime;
	private ItemStack returningStack;
	private Slot currentDragTargetSlot;
	private long dragItemDropDelay;
	protected final Set<Slot> dragSplittingSlots = Sets.<Slot>newHashSet();
	protected boolean dragSplitting;
	private int dragSplittingLimit;
	private int dragSplittingButton;
	private boolean ignoreMouseUp;
	private int dragSplittingRemnant;
	private long lastClickTime;
	private Slot lastClickSlot;
	private int lastClickButton;
	private boolean doubleClick;
	private ItemStack shiftClickedSlot;
	private boolean isOneClickCraftingModExist = false;
	private boolean tooltipRender = false;
	
	public GuiInventory(EntityPlayer player) {
		try {
			Class.forName("com.foom.oneclickcrafting.GuiCommonMethods");
			isOneClickCraftingModExist = true;
		} catch(ClassNotFoundException e) {
			isOneClickCraftingModExist = false;
		}
		ScreenHandler.offset = 90;
		this.inventorySlots = (ContainerPlayer) player.inventoryContainer;
		this.ignoreMouseUp = true;
		this.allowUserInput = true;
	}

	/**
	 * 更新物品栏格子位置
	 * 当然… 更新了就还原不回来了…
	 */
	public void updateInventorySlotPosition() {
		int x = 30;
		int y = 10;

		int craftX = x + 166;
		int craftY = y + 4 + 36;
		List<Slot> slots = new ArrayList<Slot>();
		for (Slot s : this.inventorySlots.inventorySlots) {
			int i = s.slotNumber;
			// 这是背包库存 (物品栏) 的格子序数
			if (i >= 9 && i < 36) {
				int ix = (i % 9) * 18;
				int iy = (int) ((i) / 9) * 18;
				s.xDisplayPosition = x + ix;
				s.yDisplayPosition = y + 4 + iy;
			}
			// 这是快捷栏库存的格子序数
			if (i >= 36 && i < 45) {
				int ix = (i % 9) * 18;
				int iy = (int) ((i - 36) / 9) * 18;
				s.xDisplayPosition = x + ix;
				s.yDisplayPosition = y + iy;
			}
			// 下面的都是盔甲栏 [头盔, 胸甲, 护腿, 靴子]
			if (i == 5) {
				s.xDisplayPosition = x - 22;
				s.yDisplayPosition = y + 18 * 0;
			}
			if (i == 6) {
				s.xDisplayPosition = x - 22;
				s.yDisplayPosition = y + 18 * 1;
			}
			if (i == 7) {
				s.xDisplayPosition = x - 22;
				s.yDisplayPosition = y + 18 * 2;
			}
			if (i == 8) {
				s.xDisplayPosition = x - 22;
				s.yDisplayPosition = y + 18 * 3;
			}
			// 合成 (取出物品)
			if (i == 0) {
				s.xDisplayPosition = craftX + 43;
				s.yDisplayPosition = craftY + 9;
			}
			// 合成 (材料格子)
			// ■□
			// □□
			if (i == 1) {
				s.xDisplayPosition = craftX;
				s.yDisplayPosition = craftY;
			}
			// 合成 (材料格子)
			// □■
			// □□
			if (i == 2) {
				s.xDisplayPosition = craftX + 18;
				s.yDisplayPosition = craftY;
			}
			// 合成 (材料格子)
			// □□
			// ■□
			if (i == 3) {
				s.xDisplayPosition = craftX;
				s.yDisplayPosition = craftY + 18;
			}
			// 合成 (材料格子)
			// □□
			// □■
			if (i == 4) {
				s.xDisplayPosition = craftX + 18;
				s.yDisplayPosition = craftY + 18;
			}

			slots.add(s);
		}
		this.inventorySlots.inventorySlots = slots;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
		}

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
		}
	}

	protected boolean checkHotbarKeys(int keyCode) {
		if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
			for (int i = 0; i < 9; ++i) {
				if (keyCode == this.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 2);
					return true;
				}
			}
		}

		return false;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	private void drawActivePotionEffects() {
		int i = 5;
		int j = 5;
		// int k = 166;
		Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();

		if (!collection.isEmpty()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			int l = 26;

			if (collection.size() > 5) {
				l = 132 / (collection.size() - 1);
			}

			for (PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects()) {
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
				if (!potion.shouldRender(potioneffect))
					continue;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				Util.drawRect(i + 4, j + 4, i + 80, j + 28, 0.5f, 0f, 0f, 0f);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.getTextureManager().bindTexture(inventoryBackground);
				if (potion.hasStatusIcon()) {
					int i1 = potion.getStatusIconIndex();
					this.drawTexturedModalRect(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
				}

				potion.renderInventoryEffect(i, j, potioneffect, mc);
				if (!potion.shouldRenderInvText(potioneffect))
					continue;
				String s1 = I18n.format(potion.getName(), new Object[0]);

				if (potioneffect.getAmplifier() > 1) {
					s1 = s1 + " " + I18n.format("enchantment.level." + (potioneffect.getAmplifier()+1), new Object[0]);
				}

				this.fontRendererObj.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 8), 16777215);
				String s = Potion.getDurationString(potioneffect);
				this.fontRendererObj.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 8 + 10), 8355711);
				j += l;
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(inventoryNewBackground);
		int x = this.width / 2 - 110;
		int y = this.height - 88;
		Gui.drawModalRectWithCustomSizedTexture(x, y+(int)ScreenHandler.offset, 0, 0, 263, 90, 263, 90);
		
		int i = this.guiLeft;
		int j = (int) (this.guiTop + ScreenHandler.offset);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) i, (float) j, 0.0F);
		this.mc.getTextureManager().bindTexture(widgetsTexPath);
        EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
        this.drawTexturedModalRect(30 + entityplayer.inventory.currentItem * 18 - 4, 10 - 4, 0, 22, 24, 24);
        GlStateManager.popMatrix();
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// TODO: OneClickCrafting
		if(this.isOneClickCraftingModExist) {
			if(!GuiCommonMethods.runningCrafting && GuiCommonMethods.recipeList.size() > 0) {
	         	RenderHelper.enableGUIStandardItemLighting();
	         	this.itemRender.renderItemIntoGUI(GuiCommonMethods.renderItem, 244, 74+(int)ScreenHandler.offset);
	         	this.itemRender.renderItemOverlays(this.fontRendererObj, GuiCommonMethods.renderItem, 244, 74+(int)ScreenHandler.offset);
	         	if(tooltipRender) {
	         		this.renderToolTip(GuiCommonMethods.renderItem, 240, 85+(int)ScreenHandler.offset);
	         	}

	         	this.fontRendererObj.drawString(GuiCommonMethods.maxCyclesList.get(GuiCommonMethods.itemShown) + "x", 232, 75, 0);
			}
		}
	}

	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = null;
		if (stack != null)
			font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = fontRendererObj;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack == null ? 0 : 8), altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}
	boolean closed = false;
	boolean open = false;
	long closeTime = 0L;
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		if(!close) {
			if(this.openTime != 0L) {
				float d0 = (Minecraft.getSystemTime() - this.openTime) / 100.0F;
				if (d0 > 1.0F)
				{
					d0 = 1.0F;
				}
				ScreenHandler.offset = ((1.0F - d0) * 90.0F);
            }
			else {
				this.openTime = Minecraft.getSystemTime() + 50L;
			}
		}
		else {
			if(!closed) {
				if(this.closeTime != 0L) {
					float d0 = (Minecraft.getSystemTime() - this.closeTime) / 50.0F;
					if (d0 > 1.0F)
					{
						d0 = 1.0F;
					}
					ScreenHandler.offset = (d0 * 90.0F);
					if(ScreenHandler.offset >= 90) {
						closed = true;
						this.mc.thePlayer.closeScreen();
					}
            	}
				else {
					this.closeTime = Minecraft.getSystemTime();
				}
			}
		}
		int i = this.guiLeft;
		int j = (int) (this.guiTop + ScreenHandler.offset);
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		super.drawScreen(mouseX, mouseY, partialTicks);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		// 改变原点位置
		GlStateManager.translate((float) i, (float) j, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		this.theSlot = null;
		int k = 240;
		int l = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k / 1.0F, (float) l / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i1);
			this.drawSlot(slot);
			if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
				this.theSlot = slot;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				int j1 = slot.xDisplayPosition;
				int k1 = slot.yDisplayPosition;
				GlStateManager.colorMask(true, true, true, false);
				this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}

		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;

		if (itemstack != null) {
			int j2 = 8;
			int k2 = this.draggedStack == null ? 8 : 16;
			String s = null;

			if (this.draggedStack != null && this.isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.stackSize = MathHelper.ceiling_float_int((float) itemstack.stackSize / 2.0F);
			} else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.stackSize = this.dragSplittingRemnant;

				if (itemstack.stackSize == 0) {
					s = "" + EnumChatFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(itemstack, mouseX - i - j2, mouseY - j - k2, s);
		}

		if (this.returningStack != null) {
			float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

			if (f >= 1.0F) {
				f = 1.0F;
				this.returningStack = null;
			}

			int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
			int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
			int l1 = this.touchUpX + (int) ((float) l2 * f);
			int i2 = this.touchUpY + (int) ((float) i3 * f);
			this.drawItemStack(this.returningStack, l1, i2, (String) null);
		}

		this.drawItemStack(new ItemStack(Item.getItemById(58)), 207, 28, (String) null);
		this.drawItemStack(new ItemStack(Item.getItemById(130)), 232, 28, (String) null);
		
		GlStateManager.popMatrix();
		if(mouseY > j + 28 && mouseY< j + 44) {
			if(mouseX > i + 207 && mouseX < i + 223) {
				this.drawHoveringText(Lists.<String>newArrayList(I18n.format("citytools.inv.workbench"), I18n.format("citytools.inv.permission")), mouseX, mouseY);
			}
			if(mouseX > i + 232 && mouseX < i+ 248) {
				this.drawHoveringText(Lists.<String>newArrayList(I18n.format("citytools.inv.enderchest"), I18n.format("citytools.inv.permission")), mouseX, mouseY);
			}
		}
		if (inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
			ItemStack itemstack1 = this.theSlot.getStack();
			this.renderToolTip(itemstack1, mouseX, mouseY);
		}

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();

		if (this.hasActivePotionEffects) {
			this.drawActivePotionEffects();
		}
	}

	private void drawSlot(Slot slotIn) {
		int i = slotIn.xDisplayPosition;
		int j = slotIn.yDisplayPosition;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
		ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
		String s = null;

		if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
			itemstack = itemstack.copy();
			itemstack.stackSize /= 2;
		} else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
			if (this.dragSplittingSlots.size() == 1) {
				return;
			}

			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack,
						slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);

				if (itemstack.stackSize > itemstack.getMaxStackSize()) {
					s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}

				if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack)) {
					s = EnumChatFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
					itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
				}
			} else {
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}

		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;

		if (itemstack == null) {
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

			if (textureatlassprite != null) {
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
				this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
		}

		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 */
	private Slot getSlotAtPosition(int x, int y) {
		for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);

			if (this.isMouseOverSlot(slot, x, y)) {
				return slot;
			}
		}

		return null;
	}

	/**
	 * Returns the slot that is currently displayed under the mouse.
	 */
	public Slot getSlotUnderMouse() {
		return this.theSlot;
	}

	/**
	 * Called when the mouse is clicked over a slot or outside the gui.
	 */
	protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
		if (slotIn != null) {
			slotId = slotIn.slotNumber;
		}

		this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, clickedButton, clickType,
				this.mc.thePlayer);
	}
	
	public void handleInput() throws IOException {
		//TODO: OneClickCrafting
		if(this.isOneClickCraftingModExist) {
			if(!GuiCommonMethods.runningCrafting) {
				super.handleInput();
			}
		}
		else super.handleInput();
	}
	
	public void handleMouseInput() throws IOException {
		//TODO: OneClickCrafting
		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
	    int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
	    tooltipRender = Math.abs(mouseX - this.guiLeft - 252) < 9 && Math.abs(mouseY - this.guiTop - 82) < 9;
	    if(this.isOneClickCraftingModExist)
	    	GuiCommonMethods.handleMouseWheel(Mouse.getDWheel());
	    super.handleMouseInput();
	}

	
	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
	public void initGui() {
		this.buttonList.clear();

		if (this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
		} else {
			super.initGui();
			this.mc.thePlayer.openContainer = this.inventorySlots;
			this.guiLeft = this.width / 2 - 110;
			this.guiTop = this.height - 90;
			
		}
		this.updateActivePotionEffects();
		updateInventorySlotPosition();
		//TODO: OneClickCrafting
		if(this.isOneClickCraftingModExist) {
			GuiCommonMethods.isCraftingTable = false;
		    GuiCommonMethods.runningRestart();
		}
	}

	/**
	 * Returns if the passed mouse position is over the specified slot. Args : slot,
	 * mouseX, mouseY
	 */
	private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
		return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
	}

	/**
	 * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX,
	 * rectY, rectWidth, rectHeight, pointX, pointY
	 */
	protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
		int i = this.guiLeft;
		int j = this.guiTop;
		pointX = pointX - i;
		pointY = pointY - j;
		return pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	boolean close = false;
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
			if(!close) {
				if(Main.config_enable_inv_playsound) {
					this.mc.getSoundHandler().stopSounds();
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("citytools", "inv.close"), 1.0F));
				}
				close = true;
			}
		}

		this.checkHotbarKeys(keyCode);

		if (this.theSlot != null && this.theSlot.getHasStack()) {
			if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
				this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
			} else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
				this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		//TODO: OneClickCrafting
		boolean flag3 = true;
		if(this.isOneClickCraftingModExist) {
			if(Math.abs(mouseX - this.guiLeft - 252) < 9 && Math.abs(mouseY - this.guiTop - 82) < 9) {
				GuiCommonMethods.runCrafting(mouseButton, isShiftKeyDown(), 2);
				flag3 = false;
			} else {
				flag3 = true;
			}
		}
		if(flag3) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(mouseY > this.guiTop + 28 && mouseY< this.guiTop + 44) {
			// 随身工作台
			if(mouseX > this.guiLeft + 207 && mouseX < this.guiLeft + 223) {
				Util.sendChatMessage("/eworkbench", false);
			}
			// 随身末影箱
			if(mouseX > this.guiLeft + 232 && mouseX < this.guiLeft + 248) {
				Util.sendChatMessage("/eenderchest", false);
			}
		}
		
		boolean flag = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		long i = Minecraft.getSystemTime();
		this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L
				&& this.lastClickButton == mouseButton;
		this.ignoreMouseUp = false;

		if (mouseButton == 0 || mouseButton == 1 || flag) {
			int j = this.guiLeft;
			int k = this.guiTop;
			boolean flag1 = mouseX < j || mouseY < k || mouseX >= j + this.xSize || mouseY >= k + this.ySize;
			if (slot != null)
				flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
			int l = -1;

			if (slot != null) {
				l = slot.slotNumber;
			}

			if (flag1) {
				l = -999;
			}

			if (this.mc.gameSettings.touchscreen && flag1 && this.mc.thePlayer.inventory.getItemStack() == null) {
				this.mc.displayGuiScreen((GuiScreen) null);
				return;
			}

			if (l != -1) {
				if (this.mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack()) {
						this.clickedSlot = slot;
						this.draggedStack = null;
						this.isRightMouseClick = mouseButton == 1;
					} else {
						this.clickedSlot = null;
					}
				} else if (!this.dragSplitting) {
					if (this.mc.thePlayer.inventory.getItemStack() == null) {
						if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
							this.handleMouseClick(slot, l, mouseButton, 3);
						} else {
							boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
							int i1 = 0;

							if (flag2) {
								this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
								i1 = 1;
							} else if (l == -999) {
								i1 = 4;
							}

							this.handleMouseClick(slot, l, mouseButton, i1);
						}

						this.ignoreMouseUp = true;
					} else {
						this.dragSplitting = true;
						this.dragSplittingButton = mouseButton;
						this.dragSplittingSlots.clear();

						if (mouseButton == 0) {
							this.dragSplittingLimit = 0;
						} else if (mouseButton == 1) {
							this.dragSplittingLimit = 1;
						} else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
							this.dragSplittingLimit = 2;
						}
					}
				}
			}
		}

		this.lastClickSlot = slot;
		this.lastClickTime = i;
		this.lastClickButton = mouseButton;
		}
	}

	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

		if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (clickedMouseButton == 0 || clickedMouseButton == 1) {
				if (this.draggedStack == null) {
					if (slot != this.clickedSlot && this.clickedSlot.getStack() != null) {
						this.draggedStack = this.clickedSlot.getStack().copy();
					}
				} else if (this.draggedStack.stackSize > 1 && slot != null
						&& Container.canAddItemToSlot(slot, this.draggedStack, false)) {
					long i = Minecraft.getSystemTime();

					if (this.currentDragTargetSlot == slot) {
						if (i - this.dragItemDropDelay > 500L) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.handleMouseClick(slot, slot.slotNumber, 1, 0);
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
							this.dragItemDropDelay = i + 750L;
							--this.draggedStack.stackSize;
						}
					} else {
						this.currentDragTargetSlot = slot;
						this.dragItemDropDelay = i;
					}
				}
			}
		} else if (this.dragSplitting && slot != null && itemstack != null
				&& itemstack.stackSize > this.dragSplittingSlots.size()
				&& Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack)
				&& this.inventorySlots.canDragIntoSlot(slot)) {
			this.dragSplittingSlots.add(slot);
			this.updateDragSplitting();
		}
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state); // Forge, Call parent to release buttons
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		int i = this.guiLeft;
		int j = this.guiTop;
		boolean flag = mouseX < i || mouseY < j || mouseX >= i + this.xSize || mouseY >= j + this.ySize;
		if (slot != null)
			flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
		int k = -1;

		if (slot != null) {
			k = slot.slotNumber;
		}

		if (flag) {
			k = -999;
		}

		if (this.doubleClick && slot != null && state == 0
				&& this.inventorySlots.canMergeSlot((ItemStack) null, slot)) {
			if (isShiftKeyDown()) {
				if (slot != null && slot.inventory != null && this.shiftClickedSlot != null) {
					for (Slot slot2 : this.inventorySlots.inventorySlots) {
						if (slot2 != null && slot2.canTakeStack(this.mc.thePlayer) && slot2.getHasStack()
								&& slot2.inventory == slot.inventory
								&& Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
							this.handleMouseClick(slot2, slot2.slotNumber, state, 1);
						}
					}
				}
			} else {
				this.handleMouseClick(slot, k, state, 6);
			}

			this.doubleClick = false;
			this.lastClickTime = 0L;
		} else {
			if (this.dragSplitting && this.dragSplittingButton != state) {
				this.dragSplitting = false;
				this.dragSplittingSlots.clear();
				this.ignoreMouseUp = true;
				return;
			}

			if (this.ignoreMouseUp) {
				this.ignoreMouseUp = false;
				return;
			}

			if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
				if (state == 0 || state == 1) {
					if (this.draggedStack == null && slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack();
					}

					boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);

					if (k != -1 && this.draggedStack != null && flag2) {
						this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
						this.handleMouseClick(slot, k, 0, 0);

						if (this.mc.thePlayer.inventory.getItemStack() != null) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
							this.touchUpX = mouseX - i;
							this.touchUpY = mouseY - j;
							this.returningStackDestSlot = this.clickedSlot;
							this.returningStack = this.draggedStack;
							this.returningStackTime = Minecraft.getSystemTime();
						} else {
							this.returningStack = null;
						}
					} else if (this.draggedStack != null) {
						this.touchUpX = mouseX - i;
						this.touchUpY = mouseY - j;
						this.returningStackDestSlot = this.clickedSlot;
						this.returningStack = this.draggedStack;
						this.returningStackTime = Minecraft.getSystemTime();
					}

					this.draggedStack = null;
					this.clickedSlot = null;
				}
			} else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
				this.handleMouseClick((Slot) null, -999, Container.func_94534_d(0, this.dragSplittingLimit), 5);

				for (Slot slot1 : this.dragSplittingSlots) {
					this.handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit),
							5);
				}

				this.handleMouseClick((Slot) null, -999, Container.func_94534_d(2, this.dragSplittingLimit), 5);
			} else if (this.mc.thePlayer.inventory.getItemStack() != null) {
				if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
					this.handleMouseClick(slot, k, state, 3);
				} else {
					boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

					if (flag1) {
						this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
					}

					this.handleMouseClick(slot, k, state, flag1 ? 1 : 0);
				}
			}
		}

		if (this.mc.thePlayer.inventory.getItemStack() == null) {
			this.lastClickTime = 0L;
		}

		this.dragSplitting = false;
	}

	public void onGuiClosed() {
		if (this.mc.thePlayer != null) {
			this.inventorySlots.onContainerClosed(this.mc.thePlayer);
		}
	}

	protected void updateActivePotionEffects() {
		boolean hasVisibleEffect = false;
		for (PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects()) {
			Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
			if (potion.shouldRender(potioneffect)) {
				hasVisibleEffect = true;
				break;
			}
		}

		if (!this.mc.thePlayer.getActivePotionEffects().isEmpty() && hasVisibleEffect) {
			//this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
			this.hasActivePotionEffects = true;
		} else {
			//this.guiLeft = (this.width - this.xSize) / 2;
			this.hasActivePotionEffects = false;
		}

	}

	private void updateDragSplitting() {
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

		if (itemstack != null && this.dragSplitting) {
			this.dragSplittingRemnant = itemstack.stackSize;

			for (Slot slot : this.dragSplittingSlots) {
				ItemStack itemstack1 = itemstack.copy();
				int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);

				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1)) {
					itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
				}

				this.dragSplittingRemnant -= itemstack1.stackSize - i;
			}
		}
	}
	long openTime;
	public void updateScreen() {
		if (this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
		}

		this.updateActivePotionEffects();

		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
		}
	}
}