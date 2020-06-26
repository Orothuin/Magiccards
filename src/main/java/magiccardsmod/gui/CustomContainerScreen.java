package magiccardsmod.gui;

import java.util.Set;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Mostly just copied from ContainerScreen, since so many methods are private
 * that are needed to be tweaked a tiny bit to just scale a slot to a custom size
 * 
 * @author Eisenpaulchen
 *
 * @param <T>
 */
@OnlyIn(Dist.CLIENT)
public class CustomContainerScreen<T extends Container> extends ContainerScreen<T> {
	
	private Slot clickedSlot;
	private boolean isRightMouseClick;
	private ItemStack draggedStack = ItemStack.EMPTY;
	private int touchUpX;
	private int touchUpY;
	private Slot returningStackDestSlot;
	private long returningStackTime;
	private ItemStack returningStack = ItemStack.EMPTY;
	protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
	protected boolean dragSplitting;
	private int dragSplittingLimit;
	private int dragSplittingRemnant;
	@SuppressWarnings("unused")
	private ItemStack shiftClickedSlot = ItemStack.EMPTY;

	public CustomContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

	}

	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
				new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(this, p_render_1_, p_render_2_));
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepthTest();
		for (int h = 0; h < this.buttons.size(); ++h) {
			this.buttons.get(h).render(p_render_1_, p_render_2_, p_render_3_);
		}
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) i, (float) j, 0.0F);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		this.hoveredSlot = null;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1) {
			Slot slot = this.container.inventorySlots.get(i1);
			if (slot.isEnabled()) {
				this.drawSlot(slot);
			}

			if (this.isSlotSelected(slot, (double) p_render_1_, (double) p_render_2_) && slot.isEnabled()) {
				this.hoveredSlot = slot;
				GlStateManager.disableLighting();
				GlStateManager.disableDepthTest();
				int j1 = slot.xPos;
				int k1 = slot.yPos;
				GlStateManager.colorMask(true, true, true, false);
				int slotColor = this.getSlotColor(i1);

				if (j1 == 14 && k1 == 17)
					this.fillGradient(j1, k1, j1 + 52, k1 + 52, slotColor, slotColor);
				else
					this.fillGradient(j1, k1, j1 + 16, k1 + 16, slotColor, slotColor);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepthTest();
			}
		}

		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(p_render_1_, p_render_2_);
		RenderHelper.enableGUIStandardItemLighting();
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
				new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, p_render_1_, p_render_2_));
		PlayerInventory playerinventory = this.minecraft.player.inventory;
		ItemStack itemstack = this.draggedStack.isEmpty() ? playerinventory.getItemStack() : this.draggedStack;
		if (!itemstack.isEmpty()) {
			int k2 = this.draggedStack.isEmpty() ? 8 : 16;
			String s = null;
			if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
			} else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.setCount(this.dragSplittingRemnant);
				if (itemstack.isEmpty()) {
					s = "" + TextFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(itemstack, p_render_1_ - i - 8, p_render_2_ - j - k2, s);
		}

		if (!this.returningStack.isEmpty()) {
			float f = (float) (Util.milliTime() - this.returningStackTime) / 100.0F;
			if (f >= 1.0F) {
				f = 1.0F;
				this.returningStack = ItemStack.EMPTY;
			}

			int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
			int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
			int l1 = this.touchUpX + (int) ((float) l2 * f);
			int i2 = this.touchUpY + (int) ((float) i3 * f);
			this.drawItemStack(this.returningStack, l1, i2, (String) null);
		}

		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();
		RenderHelper.enableStandardItemLighting();
	}

	private void drawSlot(Slot slotIn) {
		int i = slotIn.xPos;
		int j = slotIn.yPos;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
		ItemStack itemstack1 = this.minecraft.player.inventory.getItemStack();
		String s = null;
		if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick
				&& !itemstack.isEmpty()) {
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		} else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
			if (this.dragSplittingSlots.size() == 1) {
				return;
			}

			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.container.canDragIntoSlot(slotIn)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack,
						slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
				int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
				if (itemstack.getCount() > k) {
					s = TextFormatting.YELLOW.toString() + k;
					itemstack.setCount(k);
				}
			} else {
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}

		this.blitOffset = 100;
		this.itemRenderer.zLevel = 100.0F;
		if (itemstack.isEmpty() && slotIn.isEnabled()) {
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
			if (textureatlassprite != null) {
				GlStateManager.disableLighting();
				this.minecraft.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
				blit(i, j, this.blitOffset, 16, 16, textureatlassprite);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				fill(i, j, i + 16, j + 16, -2130706433);
			}

			GlStateManager.enableDepthTest();
			this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.player, itemstack, i, j);
			this.itemRenderer.renderItemOverlayIntoGUI(this.font, itemstack, i, j, s);
		}

		this.itemRenderer.zLevel = 0.0F;
		this.blitOffset = 0;
	}

	private boolean isSlotSelected(Slot p_195362_1_, double p_195362_2_, double p_195362_4_) {
		return this.isPointInRegion(p_195362_1_.xPos, p_195362_1_.yPos, 16, 16, p_195362_2_, p_195362_4_);
	}

	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GlStateManager.translatef(0.0F, 0.0F, 32.0F);
		this.blitOffset = 200;
		this.itemRenderer.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = this.font;
		this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
		this.blitOffset = 0;
		this.itemRenderer.zLevel = 0.0F;
	}

	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
			return true;
		} else {
			InputMappings.Input mouseKey = InputMappings.Type.MOUSE.getOrMakeInput(p_mouseClicked_5_);
			boolean flag = this.minecraft.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseKey);
			Slot slot = this.getSelectedSlot(p_mouseClicked_1_, p_mouseClicked_3_);
			Util.milliTime();
			if (p_mouseClicked_5_ == 0 || p_mouseClicked_5_ == 1 || flag) {
				int j = this.guiLeft;
				int k = this.guiTop;
				boolean flag1 = this.hasClickedOutside(p_mouseClicked_1_, p_mouseClicked_3_, j, k, p_mouseClicked_5_);
				if (slot != null)
					flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
				int l = -1;
				if (slot != null) {
					l = slot.slotNumber;
				}

				if (flag1) {
					l = -999;
				}

				if (this.minecraft.gameSettings.touchscreen && flag1
						&& this.minecraft.player.inventory.getItemStack().isEmpty()) {
					this.minecraft.displayGuiScreen((Screen) null);
					return true;
				}

				if (l != -1) {
					if (this.minecraft.gameSettings.touchscreen) {
						if (slot != null && slot.getHasStack()) {
							this.clickedSlot = slot;
							this.draggedStack = ItemStack.EMPTY;
							this.isRightMouseClick = p_mouseClicked_5_ == 1;
						} else {
							this.clickedSlot = null;
						}
					} else if (!this.dragSplitting) {
						if (this.minecraft.player.inventory.getItemStack().isEmpty()) {
							if (this.minecraft.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseKey)) {
								this.handleMouseClick(slot, l, p_mouseClicked_5_, ClickType.CLONE);
							} else {
								boolean flag2 = l != -999
										&& (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), 340)
												|| InputMappings.isKeyDown(
														Minecraft.getInstance().mainWindow.getHandle(), 344));
								ClickType clicktype = ClickType.PICKUP;
								if (flag2) {
									this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy()
											: ItemStack.EMPTY;
									clicktype = ClickType.QUICK_MOVE;
								} else if (l == -999) {
									clicktype = ClickType.THROW;
								}

								this.handleMouseClick(slot, l, p_mouseClicked_5_, clicktype);
							}
						} else {
							this.dragSplitting = true;
							this.dragSplittingSlots.clear();
							if (p_mouseClicked_5_ == 0) {
								this.dragSplittingLimit = 0;
							} else if (p_mouseClicked_5_ == 1) {
								this.dragSplittingLimit = 1;
							} else if (this.minecraft.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseKey)) {
								this.dragSplittingLimit = 2;
							}
						}
					}
				}
			}

			return true;
		}
	}

	private Slot getSelectedSlot(double p_195360_1_, double p_195360_3_) {
		for (int i = 0; i < this.container.inventorySlots.size(); ++i) {
			Slot slot = this.container.inventorySlots.get(i);
			if (this.isSlotSelected(slot, p_195360_1_, p_195360_3_) && slot.isEnabled()) {
				return slot;
			}
		}

		return null;
	}

	private void updateDragSplitting() {
		ItemStack itemstack = this.minecraft.player.inventory.getItemStack();
		if (!itemstack.isEmpty() && this.dragSplitting) {
			if (this.dragSplittingLimit == 2) {
				this.dragSplittingRemnant = itemstack.getMaxStackSize();
			} else {
				this.dragSplittingRemnant = itemstack.getCount();

				for (Slot slot : this.dragSplittingSlots) {
					ItemStack itemstack1 = itemstack.copy();
					ItemStack itemstack2 = slot.getStack();
					int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
					Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
					int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
					if (itemstack1.getCount() > j) {
						itemstack1.setCount(j);
					}

					this.dragSplittingRemnant -= itemstack1.getCount() - i;
				}

			}
		}
	}
}
