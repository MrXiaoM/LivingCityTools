package email.com.gmail.coolxiaom95.citytools.gui.globalshop;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGlobalShop extends GuiScreen {
	protected static final ResourceLocation shop_bg = new ResourceLocation("citytools",
			"textures/gui/globalshop/bg.png");
	protected static final ResourceLocation shop_mailbox = new ResourceLocation("citytools",
			"textures/gui/globalshop/mailbox.png");
	protected static final ResourceLocation shop_my = new ResourceLocation("citytools",
			"textures/gui/globalshop/my.png");
	protected static final ResourceLocation shop_refresh = new ResourceLocation("citytools",
			"textures/gui/globalshop/refresh.png");
	protected static final ResourceLocation shop_sort = new ResourceLocation("citytools",
			"textures/gui/globalshop/sort.png");
	protected static final ResourceLocation shop_close = new ResourceLocation("citytools",
			"textures/gui/globalshop/close.png");
	protected static final ResourceLocation shop_prePage = new ResourceLocation("citytools",
			"textures/gui/globalshop/pre_page.png");
	protected static final ResourceLocation shop_nextPage = new ResourceLocation("citytools",
			"textures/gui/globalshop/next_page.png");
	/** window height is calculated with these values; the more rows, the heigher */
	//private int inventoryRows;
	ItemStack hovingStack = null;

    protected List<GuiPictureButton> btnList = Lists.<GuiPictureButton>newArrayList();
    private GuiPictureButton selectedBtn;
	/** The X size of the inventory window in pixels. */
	protected int xSize = 176;
	/** The Y size of the inventory window in pixels. */
	protected int ySize = 166;
	/** A list of the players inventory slots */
	public Container inventorySlots;
	/** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int guiLeft;
	/** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int guiTop;
	/** Used when touchscreen is enabled. */
	private Slot clickedSlot;
	/** Used when touchscreen is enabled. */
	private boolean isRightMouseClick;
	/** Used when touchscreen is enabled */
	private ItemStack draggedStack;
	protected final Set<Slot> dragSplittingSlots = Sets.<Slot>newHashSet();
	protected boolean dragSplitting;
	private int dragSplittingLimit;
	private GuiGlobalShopList itemList;
	private GuiPictureButton btnPrePage;
	private GuiPictureButton btnNextPage;
	public GuiGlobalShop(IInventory upperInv, IInventory lowerInv) {
		this.inventorySlots = new ContainerGuiShop(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer);
		this.allowUserInput = false;
		//int i = 222;
		if(lowerInv != null) {
		//this.inventoryRows = lowerInv.getSizeInventory() / 9;
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
	public void initGui() {
		super.initGui();
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = this.width/ 2 - 150;
		this.guiTop = this.height / 2 - 100;
		if (this.itemList == null) {
			this.itemList = new GuiGlobalShopList(this, this.mc);
		} else {
			this.itemList.setDimensions(this.width, this.height);
		}
		this.btnList.clear();
		this.btnList.add(new GuiPictureButton(0, this.guiLeft + 285, this.guiTop + 3, 12, 12, "", shop_close));
		this.btnList.add(btnPrePage = new GuiPictureButton(1, this.guiLeft + 70, this.guiTop + 184, 12, 12, "", shop_prePage));
		this.btnList.add(btnNextPage = new GuiPictureButton(2, this.guiLeft + 278, this.guiTop + 184, 12, 12, "", shop_nextPage));
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.itemList.handleMouseInput();
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int x = this.width / 2 - 150;
		int y = this.height / 2 - 100;

		mc.renderEngine.bindTexture(shop_bg);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 300, 200, 300, 200);
		itemList.drawScreen(mouseX, mouseY, partialTicks);
		
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
		
		mc.renderEngine.bindTexture(shop_bg);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 300, 16, 300, 200);
		Gui.drawModalRectWithCustomSizedTexture(x, y+180, 0, 180, 300, 20, 300, 200);
		
		ItemStack item1= getItemFromSlot(inventorySlots.inventorySlots.get(45));
		if(item1 != null) {
			btnPrePage.enabled = true;
		}
		else {
			btnPrePage.enabled = false;
		}
		ItemStack item2= getItemFromSlot(this.inventorySlots.inventorySlots.get(53));
		if(item2 != null) {
			btnNextPage.enabled = true;
		}
		else {
			btnNextPage.enabled = false;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
        for (int i = 0; i < this.btnList.size(); ++i)
        {
            ((GuiPictureButton)this.btnList.get(i)).drawButton(this.mc, mouseX, mouseY);
        }

		//drawModalRectWithCustomSizedTexture(x, y+ 183, 0f, 183f, 300, 17, 300f, 17f);
		if(hovingStack!=null) {
	        this.renderToolTips(hovingStack, mouseX, mouseY);
	        hovingStack = null;
		}
	}

    protected void renderToolTips(ItemStack stack, int x, int y)
    {
        List<String> textLines = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < textLines.size(); ++i)
        {
            if (i == 0)
            {
            	textLines.set(i, stack.getRarity().rarityColor + (String)textLines.get(i));
            }
            else
            {
            	textLines.set(i, EnumChatFormatting.GRAY + (String)textLines.get(i));
            }
        }

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if(font==null) {
        	font = fontRendererObj;
        }
        if (!textLines.isEmpty())
        {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines)
            {
                int j = font.getStringWidth(s);

                if (j > i)
                {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1)
            {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > this.width)
            {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > this.height)
            {
                i2 = this.height - k - 6;
            }

            this.zLevel = 300.0F;
            this.itemRender.zLevel = 300.0F;
            int l = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1)
            {
                String s1 = (String)textLines.get(k1);
                if(s1.contains("shift+左键购买")) {
                	s1 = s1.replace("shift+左键购买", "左键选中该商品");
                }
                font.drawStringWithShadow(s1, (float)l1, (float)i2, -1);

                if (k1 == 0)
                {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
	private ItemStack getItemFromSlot(Slot slotIn) {
    	if(slotIn==null) return null;
    	ItemStack itemstack = slotIn.getStack();
		ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
		if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
			itemstack = itemstack.copy();
			itemstack.stackSize /= 2;
		} else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
			if (this.dragSplittingSlots.size() == 1) {
				return null;
			}

			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
				itemstack = itemstack1.copy();
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack,
						slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);

				if (itemstack.stackSize > itemstack.getMaxStackSize()) {
					//EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}

				if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack)) {
					//EnumChatFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
					itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
				}
			} else {
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}
		return itemstack;
    }
    
	protected void drawItem(Slot slotIn, int index, int x, int y, int mouseX, int mouseY, boolean isSelected) {
		if(y < itemList.top-15 || y > itemList.bottom)
			return;
		
		int i = x;
		int j = y;
		ItemStack itemstack = slotIn.getStack();
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
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
			if(isSelected) {
				drawRect(i, j, i + 215, j + 20, 0x99000000);
			}
			if (itemstack != null) {
				List<String> list = new ArrayList<String>();

		        if (itemstack.hasTagCompound())
		        {

		            if (itemstack.getTagCompound().hasKey("display", 10))
		            {
		                NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("display");

		                if (nbttagcompound.getTagId("Lore") == 9)
		                {
		                    NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);

		                    if (nbttaglist1.tagCount() > 0)
		                    {
		                        for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
		                        {
		                            list.add(nbttaglist1.getStringTagAt(j1));
		                        }
		                    }
		                }
		            }
		        }
				String price = list.get(list.size() - 3);
				//System.out.println(price);
				int a = price.lastIndexOf("「")+6;
				int b = price.lastIndexOf("」")-5;
				
				String money = "§5§l价格:" + (b<=a?"§c§lN/A":price.substring(a,b));
				mc.fontRendererObj.drawString(money, x + 20, y + 4, 0);
				mc.fontRendererObj.drawString(itemstack.getDisplayName(), x + 80, y + 4, 0);
			}
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            
			this.itemRender.renderItemAndEffectIntoGUI(itemstack, i+2, j+2);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
            
			if( itemstack != null //物品不为空
				// 鼠标指向按钮区域
				&& (mouseX > i && mouseX < i + 198 && mouseY > j && mouseY < j + 20)
				// 鼠标不在控件区域外
				&& (mouseY>itemList.top && mouseY< itemList.bottom)) {
				hovingStack = itemstack;
			}
			
			GlStateManager.enableAlpha();
            GlStateManager.disableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
		}

		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	
	private void updateDragSplitting() {
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

		if (itemstack != null && this.dragSplitting) {
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
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton != 0 || !this.itemList.mouseClicked(mouseX, mouseY, mouseButton)) {
			if (mouseButton == 0)
	        {
	            for (int i = 0; i < this.btnList.size(); ++i)
	            {
	                GuiPictureButton guibutton = (GuiPictureButton)this.btnList.get(i);

	                if (guibutton.mousePressed(this.mc, mouseX, mouseY))
	                {
	                    this.selectedBtn = guibutton;
	                    guibutton.playPressSound(this.mc.getSoundHandler());
	                    this.onButtonClick(guibutton);
	                }
	            }
	        }
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	private void onButtonClick(GuiPictureButton button) {
		if(button.id == 0) {
			this.mc.thePlayer.closeScreen();
		}
		if(button.id == 1) {
			this.mc.playerController.windowClick(this.inventorySlots.windowId,
					45, 0, 0, mc.thePlayer);
		}
		if(button.id == 2) {
			this.mc.playerController.windowClick(this.inventorySlots.windowId,
					53, 0, 0, mc.thePlayer);
		}
	}
	
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		if (state != 0 || !this.itemList.mouseReleased(mouseX, mouseY, state)) {
	        if (this.selectedBtn != null && state == 0)
	        {
	            this.selectedBtn.mouseReleased(mouseX, mouseY);
	            this.selectedBtn = null;
	        }
			super.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
		int i = this.guiLeft;
		int j = this.guiTop;
		pointX = pointX - i;
		pointY = pointY - j;
		return pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1;
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
			this.mc.thePlayer.closeScreen();
		}
	}
	
	public void onGuiClosed() {
		if (this.mc.thePlayer != null) {
			this.inventorySlots.onContainerClosed(this.mc.thePlayer);
		}
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void updateScreen() {
		super.updateScreen();

		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
		}
	}
}