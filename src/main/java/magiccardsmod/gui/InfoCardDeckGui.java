package magiccardsmod.gui;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import magiccardsmod.container.InfoCardDeckContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class InfoCardDeckGui extends ContainerScreen<InfoCardDeckContainer> {

	static final ResourceLocation CARDDECK_GUI_TEXTURES = new ResourceLocation(
			"magiccards:textures/gui/infocarddeck.png");
	static final ResourceLocation CARDDECK_ALTERNATE_GUI_TEXTURES = new ResourceLocation(
			"magiccards:textures/gui/infocarddeckalternate.png");

	private int page = 0;
	public static int lastPage = 10;
	private Button left, right;

	private boolean leftHovered;
	private boolean rightHovered;
	
	public static final int TEXT_COLOR = new Color(68,57,43).getRGB();
	
	public InfoCardDeckGui(InfoCardDeckContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	
		guiLeft = 0;
		guiTop = (this.height - this.ySize) / 2;
		xSize = 139;
		ySize = 140;
	}
	
	@Override
	protected void init() {

		super.init();

		left = new Button(guiLeft + 1, guiTop + 12, 24, 119, "", new Button.IPressable() {

			@Override
			public void onPress(Button p_onPress_1_) {
				if (page > 0)
					page--;
			}
		}) {
			@Override
			public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
				leftHovered = isHovered;
			}
		};
		right = new Button(guiLeft + 117, guiTop + 12, 24, 119, "", new Button.IPressable() {

			@Override
			public void onPress(Button p_onPress_1_) {
				if (page < lastPage)
					page++;
			}
		}) {
			@Override
			public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
				rightHovered = isHovered;
			}
		};
		addButton(right);
		addButton(left);
		
		minecraft.playerController.sendEnchantPacket((getContainer()).windowId, 111);
	}
	
	
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		lastPage = getContainer().getPages().size();
		
		super.render(p_render_1_, p_render_2_, p_render_3_);
		
		RenderHelper.enableGUIStandardItemLighting();
		
		if(page>0) {
		container.getPages().get(page-1).draw(this);
		
		RenderHelper.disableStandardItemLighting();
		
		ItemStack stack = container.getPages().get(page-1).getHoveredItem(this,p_render_1_, p_render_2_);
		
		if(stack!=null)
		this.renderTooltip(stack, p_render_1_, p_render_2_);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (page > 0)
			this.minecraft.getTextureManager().bindTexture(CARDDECK_ALTERNATE_GUI_TEXTURES);
		else
			this.minecraft.getTextureManager().bindTexture(CARDDECK_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(i + 25, j + 12, 0, 0, 92, 119);
		if (page > 0) {
			
			left.active=true;
			
			if (leftHovered)
				this.minecraft.getTextureManager().bindTexture(CARDDECK_ALTERNATE_GUI_TEXTURES);
			else
				this.minecraft.getTextureManager().bindTexture(CARDDECK_GUI_TEXTURES);
			this.blit(i, j, 0, 119, 92, 135);
		}else left.active=false;
		if (page < lastPage) {
			
			right.active=true;
			if (rightHovered)
				this.minecraft.getTextureManager().bindTexture(CARDDECK_ALTERNATE_GUI_TEXTURES);
			else
				this.minecraft.getTextureManager().bindTexture(CARDDECK_GUI_TEXTURES);
			this.blit(i + 48, j + 5, 92, 0, 92, 135);
		}else right.active=false;
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		if(page>0)
	    this.font.drawString(String.valueOf(page), 114.0F-String.valueOf(page).length()*6, 120,  TEXT_COLOR);
	     
	   }
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
