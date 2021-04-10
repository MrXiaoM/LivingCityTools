package email.com.gmail.coolxiaom95.citytools.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import email.com.gmail.coolxiaom95.citytools.Main;
import email.com.gmail.coolxiaom95.citytools.handlers.ScreenHandler;
import email.com.gmail.coolxiaom95.citytools.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
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
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInventory extends GuiScreen{
	protected static final ResourceLocation inventoryBackground = new ResourceLocation(
			"textures/gui/container/inventory.png");
	protected static final ResourceLocation inventoryNewBackground = new ResourceLocation("citytools",
			"textures/gui/container/inventory.png");
    protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");

    
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
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
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
    /** The X size of the inventory window in pixels. */
    protected int xSize = 285;
    /** The Y size of the inventory window in pixels. */
    protected int ySize = 90;
    /** A list of the players inventory slots */
    public Container inventorySlots;
    /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
    protected int guiLeft;
    /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
    protected int guiTop;
    /** holds the slot currently hovered */
    private Slot hoveredSlot;
    /** Used when touchscreen is enabled. */
    private Slot clickedSlot;
    /** Used when touchscreen is enabled. */
    private boolean isRightMouseClick;
    /** Used when touchscreen is enabled */
    private ItemStack draggedStack = ItemStack.EMPTY;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    /** Used when touchscreen is enabled */
    private ItemStack returningStack = ItemStack.EMPTY;
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
    private ItemStack shiftClickedSlot = ItemStack.EMPTY;
	
	public GuiInventory(EntityPlayer player) {
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
				s.xPos = x + ix;
				s.yPos = y + 4 + iy;
			}
			// 这是快捷栏库存的格子序数
			if (i >= 36 && i < 45) {
				int ix = (i % 9) * 18;
				int iy = (int) ((i - 36) / 9) * 18;
				s.xPos = x + ix;
				s.yPos = y + iy;
			}
			// 下面的都是盔甲栏 [头盔, 胸甲, 护腿, 靴子]
			if (i == 5) {
				s.xPos = x - 22;
				s.yPos = y + 18 * 0;
			}
			if (i == 6) {
				s.xPos = x - 22;
				s.yPos = y + 18 * 1;
			}
			if (i == 7) {
				s.xPos = x - 22;
				s.yPos = y + 18 * 2;
			}
			if (i == 8) {
				s.xPos = x - 22;
				s.yPos = y + 18 * 3;
			}
			// 合成 (取出物品)
			if (i == 0) {
				s.xPos = craftX + 43;
				s.yPos = craftY + 9;
			}
			// 合成 (材料格子)
			// ■□
			// □□
			if (i == 1) {
				s.xPos = craftX;
				s.yPos = craftY;
			}
			// 合成 (材料格子)
			// □■
			// □□
			if (i == 2) {
				s.xPos = craftX + 18;
				s.yPos = craftY;
			}
			// 合成 (材料格子)
			// □□
			// ■□
			if (i == 3) {
				s.xPos = craftX;
				s.yPos = craftY + 18;
			}
			// 合成 (材料格子)
			// □□
			// □■
			if (i == 4) {
				s.xPos = craftX + 18;
				s.yPos = craftY + 18;
			}
			// 副手
			if (i == 45) {
				s.xPos = x - 44;
				s.yPos = y + 18 * 1;
			}
			slots.add(s);
		}
		this.inventorySlots.inventorySlots = slots;
	}
    
	protected boolean checkHotbarKeys(int keyCode) {
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null)
        {
            for (int i = 0; i < 9; ++i)
            {
                if (this.mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode))
                {
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
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
		Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();

		if (!collection.isEmpty()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			int l = 26;

			if (collection.size() > 5) {
				l = 132 / (collection.size() - 1);
			}

			for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
            {
                Potion potion = potioneffect.getPotion();
                if(!potion.shouldRender(potioneffect)) continue;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				Util.drawRect(i + 4, j + 4, i + 80, j + 28, 0.5f, 0f, 0f, 0f);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.getTextureManager().bindTexture(inventoryBackground);
                if (potion.hasStatusIcon())
                {
                    int i1 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }
                potion.renderInventoryEffect(potioneffect, this, i, j, this.zLevel);
				if (!potion.shouldRenderInvText(potioneffect))
					continue;
				String s1 = I18n.format(potion.getName(), new Object[0]);

				if (potioneffect.getAmplifier() > 1) {
					s1 = s1 + " " + I18n.format("enchantment.level." + (potioneffect.getAmplifier()+1), new Object[0]);
				}

				this.fontRenderer.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 8), 16777215);
				String s = Potion.getPotionDurationString(potioneffect, 1.0F);
				this.fontRenderer.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 8 + 10), 8355711);
				j += l;
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(inventoryNewBackground);
		int x = this.width / 2 - 110;
		int y = this.height - 88;
		Gui.drawModalRectWithCustomSizedTexture(x-22, y+(int)ScreenHandler.offset, 0, 0, 285, 90, 285, 90);
		
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

	}
	private void drawItemStack(ItemStack stack, int x, int y, String altText)
	{
	    GlStateManager.translate(0.0F, 0.0F, 32.0F);
	    this.zLevel = 200.0F;
	    this.itemRender.zLevel = 200.0F;
	    net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
	    if (font == null) font = fontRenderer;
	    this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
	    this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
	    this.zLevel = 0.0F;
	    this.itemRender.zLevel = 0.0F;
	}
	boolean closed = false;
	boolean open = false;
	long closeTime = 0L;
	//TODO drawScreen
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
						this.mc.player.closeScreen();
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
        GlStateManager.translate((float)i, (float)j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        this.hoveredSlot = null;
        //int k = 240;
        //int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
        {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);

            if (slot.isEnabled())
            {
                this.drawSlot(slot);
                //mc.fontRenderer.drawString(""+slot.slotNumber, slot.xPos+4, slot.yPos+4, 0xffffff);
            }

            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled())
            {
                this.hoveredSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xPos;
                int k1 = slot.yPos;
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
        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

        if (!itemstack.isEmpty())
        {
            //int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick)
            {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
            }
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1)
            {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty())
                {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!this.returningStack.isEmpty())
        {
            float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F)
            {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String)null);
        }
        
        this.drawItemStack(new ItemStack(Item.getItemById(58)), 207, 28, (String) null);
		this.drawItemStack(new ItemStack(Item.getItemById(130)), 232, 28, (String) null);
		
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        
		if(mouseY > j + 28 && mouseY< j + 44) {
			if(mouseX > i + 207 && mouseX < i + 223) {
				this.drawHoveringText(Lists.<String>newArrayList(I18n.format("citytools.inv.workbench"), I18n.format("citytools.inv.permission")), mouseX, mouseY);
			}
			if(mouseX > i + 232 && mouseX < i+ 248) {
				this.drawHoveringText(Lists.<String>newArrayList(I18n.format("citytools.inv.enderchest"), I18n.format("citytools.inv.permission")), mouseX, mouseY);
			}
		}
		
        this.renderHoveredToolTip(mouseX, mouseY);

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();

		if (this.hasActivePotionEffects) {
			this.drawActivePotionEffects();
		}
	}
	
    protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_)
    {
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack())
        {
            this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
        }
    }
    
    private void drawSlot(Slot slotIn)
    {
        int i = slotIn.xPos;
        int j = slotIn.yPos;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty())
        {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        }
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
        {
            if (this.dragSplittingSlots.size() == 1)
            {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k)
                {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            }
            else
            {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.isEnabled())
        {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

            if (textureatlassprite != null)
            {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private Slot getSlotAtPosition(int x, int y)
    {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
        {
            Slot slot = this.inventorySlots.inventorySlots.get(i);
            if (this.isMouseOverSlot(slot, x, y) && slot.isEnabled())
            {
                return slot;
            }
        }
        return null;
    }
    
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        if (slotIn != null)
        {
            slotId = slotIn.slotNumber;
        }

        this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
    }
    
    protected boolean hasClickedOutside(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return flag;
    }
    
	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
    //TODO initGui
	public void initGui() {
		this.buttonList.clear();

		if (this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
		} else {
			super.initGui();
			this.mc.player.openContainer = this.inventorySlots;
			this.guiLeft = this.width / 2 - 110;
			this.guiTop = this.height - 90;
		}
		this.updateActivePotionEffects();
		updateInventorySlotPosition();
	}

	private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
    {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }

    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = this.guiLeft;
        int j = this.guiTop;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	boolean close = false;
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
			if(!close) {
				if(Main.config_enable_inv_playsound) {
					this.mc.getSoundHandler().stopSounds();
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(new SoundEvent(new ResourceLocation("citytools", "inv.close")), 1.0F));
				}
				close = true;
			}
        }

        this.checkHotbarKeys(keyCode);

        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
        {
            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode))
            {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            }
            else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode))
            {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

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
		boolean flag = this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100);
        Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        long i = Minecraft.getSystemTime();
        this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton;
        this.ignoreMouseUp = false;

        if (mouseButton == 0 || mouseButton == 1 || flag)
        {
            int j = this.guiLeft;
            int k = this.guiTop;
            boolean flag1 = this.hasClickedOutside(mouseX, mouseY, j, k);
            if (slot != null) flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
            int l = -1;

            if (slot != null)
            {
                l = slot.slotNumber;
            }

            if (flag1)
            {
                l = -999;
            }

            if (this.mc.gameSettings.touchscreen && flag1 && this.mc.player.inventory.getItemStack().isEmpty())
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                return;
            }

            if (l != -1)
            {
                if (this.mc.gameSettings.touchscreen)
                {
                    if (slot != null && slot.getHasStack())
                    {
                        this.clickedSlot = slot;
                        this.draggedStack = ItemStack.EMPTY;
                        this.isRightMouseClick = mouseButton == 1;
                    }
                    else
                    {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.dragSplitting)
                {
                    if (this.mc.player.inventory.getItemStack().isEmpty())
                    {
                        if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100))
                        {
                            this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
                        }
                        else
                        {
                            boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;

                            if (flag2)
                            {
                                this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                                clicktype = ClickType.QUICK_MOVE;
                            }
                            else if (l == -999)
                            {
                                clicktype = ClickType.THROW;
                            }

                            this.handleMouseClick(slot, l, mouseButton, clicktype);
                        }

                        this.ignoreMouseUp = true;
                    }
                    else
                    {
                        this.dragSplitting = true;
                        this.dragSplittingButton = mouseButton;
                        this.dragSplittingSlots.clear();

                        if (mouseButton == 0)
                        {
                            this.dragSplittingLimit = 0;
                        }
                        else if (mouseButton == 1)
                        {
                            this.dragSplittingLimit = 1;
                        }
                        else if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100))
                        {
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

	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen)
        {
            if (clickedMouseButton == 0 || clickedMouseButton == 1)
            {
                if (this.draggedStack.isEmpty())
                {
                    if (slot != this.clickedSlot && !this.clickedSlot.getStack().isEmpty())
                    {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                }
                else if (this.draggedStack.getCount() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false))
                {
                    long i = Minecraft.getSystemTime();

                    if (this.currentDragTargetSlot == slot)
                    {
                        if (i - this.dragItemDropDelay > 500L)
                        {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.dragItemDropDelay = i + 750L;
                            this.draggedStack.shrink(1);
                        }
                    }
                    else
                    {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        }
        else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot))
        {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state); // Forge, Call parent to release buttons
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        int i = this.guiLeft;
        int j = this.guiTop;
        boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);
        if (slot != null) flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
        int k = -1;

        if (slot != null)
        {
            k = slot.slotNumber;
        }

        if (flag)
        {
            k = -999;
        }

        if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, slot))
        {
            if (isShiftKeyDown())
            {
                if (!this.shiftClickedSlot.isEmpty())
                {
                    for (Slot slot2 : this.inventorySlots.inventorySlots)
                    {
                        if (slot2 != null && slot2.canTakeStack(this.mc.player) && slot2.getHasStack() && slot2.isSameInventory(slot) && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true))
                        {
                            this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            else
            {
                this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
            }

            this.doubleClick = false;
            this.lastClickTime = 0L;
        }
        else
        {
            if (this.dragSplitting && this.dragSplittingButton != state)
            {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }

            if (this.ignoreMouseUp)
            {
                this.ignoreMouseUp = false;
                return;
            }

            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen)
            {
                if (state == 0 || state == 1)
                {
                    if (this.draggedStack.isEmpty() && slot != this.clickedSlot)
                    {
                        this.draggedStack = this.clickedSlot.getStack();
                    }

                    boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);

                    if (k != -1 && !this.draggedStack.isEmpty() && flag2)
                    {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);

                        if (this.mc.player.inventory.getItemStack().isEmpty())
                        {
                            this.returningStack = ItemStack.EMPTY;
                        }
                        else
                        {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                            this.touchUpX = mouseX - i;
                            this.touchUpY = mouseY - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                    }
                    else if (!this.draggedStack.isEmpty())
                    {
                        this.touchUpX = mouseX - i;
                        this.touchUpY = mouseY - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }

                    this.draggedStack = ItemStack.EMPTY;
                    this.clickedSlot = null;
                }
            }
            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty())
            {
                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);

                for (Slot slot1 : this.dragSplittingSlots)
                {
                    this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                }

                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }
            else if (!this.mc.player.inventory.getItemStack().isEmpty())
            {
                if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100))
                {
                    this.handleMouseClick(slot, k, state, ClickType.CLONE);
                }
                else
                {
                    boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (flag1)
                    {
                        this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                    }

                    this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }

        if (this.mc.player.inventory.getItemStack().isEmpty())
        {
            this.lastClickTime = 0L;
        }

        this.dragSplitting = false;
	}

	public void onGuiClosed() {
		if (this.mc.player != null) {
			this.inventorySlots.onContainerClosed(this.mc.player);
		}
	}

	protected void updateActivePotionEffects() {
		 boolean hasVisibleEffect = false;
	     for(PotionEffect potioneffect : this.mc.player.getActivePotionEffects()) {
	         Potion potion = potioneffect.getPotion();
	         if(potion.shouldRender(potioneffect)) { hasVisibleEffect = true; break; }
	     }
	     if (this.mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect)
	     {
	         this.hasActivePotionEffects = false;
	     }
	     else
	     {
	         this.hasActivePotionEffects = true;
	     }
	}
	private void updateDragSplitting()
    {
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && this.dragSplitting)
        {
            if (this.dragSplittingLimit == 2)
            {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            }
            else
            {
                this.dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots)
                {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j)
                    {
                        itemstack1.setCount(j);
                    }

                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }
	
	long openTime;
	public void updateScreen() {
		if (this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
		}

		this.updateActivePotionEffects();
        
		if (!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
			this.mc.player.closeScreen();
		}
	}
}