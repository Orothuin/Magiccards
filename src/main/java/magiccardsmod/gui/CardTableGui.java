package magiccardsmod.gui;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import magiccardsmod.container.CardTableContainer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class CardTableGui extends CustomContainerScreen<CardTableContainer> {

	private static final ResourceLocation CARD_TABLE_GUI_TEXTURES = new ResourceLocation(
			"magiccards:textures/gui/cardtable.png");
	private static final ResourceLocation CARD_TABLE_DIAMOND_GUI_TEXTURES = new ResourceLocation(
			"magiccards:textures/gui/cardtablediamond.png");
	private static final ResourceLocation CARD_TABLE_EMERALD_GUI_TEXTURES = new ResourceLocation(
			"magiccards:textures/gui/cardtableemerald.png");

	private Button applyButton;

	public CardTableGui(CardTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		guiLeft = 0;
		guiTop = 0;
		xSize = 176;
		ySize = 178;
	}

	@Override
	protected void init() {

		super.init();
		ProxyItemRenderer pRenderer = new ProxyItemRenderer(itemRenderer);
		this.itemRenderer = pRenderer;
		pRenderer.addRenderException(14, 17, 52);

		applyButton = new Button(guiLeft + 75, guiTop + 34, 27, 17, "", new Button.IPressable() {

			@Override
			public void onPress(Button p_onPress_1_) {

				minecraft.playerController.sendEnchantPacket((getContainer()).windowId, 10);
			}
		}) {
			@Override
			public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {

				bindLevelConsideringBackground();
				
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);

				int i = this.isHovered() ? 1 : 0;
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				this.blit(this.x, this.y, 0, 178 + i * this.height, this.width, this.height);
			}
		};

		this.addButton(applyButton);

	}

	@Override
	protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {

		if (x == 14 && y == 17)

			return super.isPointInRegion(x, y, 53, 53, mouseX, mouseY);

		return super.isPointInRegion(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {

		super.render(p_render_1_, p_render_2_, p_render_3_);

		this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		int color = new Color(253, 234, 95).getRGB();
		
		switch (container.getTile().getTableLevel()) {
		case 1:
			color = new Color(253, 234, 95).getRGB();
			break;

		case 2:
			color = new Color(74, 236, 216).getRGB();
			break;

		case 3:
			color = new Color(20, 213, 92).getRGB();
			break;
		}
		
		String s = this.title.getFormattedText() + " " + container.getTile().getTableLevel();

		this.font.drawString(s, xSize / 2 - font.getStringWidth(s) / 2, 7, color);

		if (getContainer().selectedEnchantment != null && getContainer().level != -1)
			this.font.drawString(
					getContainer().selectedEnchantment.getDisplayName(getContainer().level).getString(), 9.0F,
					(float) (this.ySize - 96 + 2), color);
		
		else if (getContainer().selectedEnchantment != null && getContainer().level == -1)
			this.font.drawString(new TranslationTextComponent("can not apply enchantment").getString(), 9.0F,
					(float) (this.ySize - 96 + 2), color);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		bindLevelConsideringBackground();

		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
	}

	private void bindLevelConsideringBackground() {

		switch (container.getTile().getTableLevel()) {
		case 1:
			minecraft.getTextureManager().bindTexture(CARD_TABLE_GUI_TEXTURES);
			break;

		case 2:
			minecraft.getTextureManager().bindTexture(CARD_TABLE_DIAMOND_GUI_TEXTURES);
			break;

		case 3:
			minecraft.getTextureManager().bindTexture(CARD_TABLE_EMERALD_GUI_TEXTURES);
			break;
		}
	}
}
