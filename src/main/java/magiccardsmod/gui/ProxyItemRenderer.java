package magiccardsmod.gui;

import java.util.ArrayList;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ProxyItemRenderer extends ItemRenderer {

	public ProxyItemRenderer(TextureManager textureManagerIn, ModelManager modelManagerIn, ItemColors itemColorsIn) {
		super(textureManagerIn, modelManagerIn, itemColorsIn);
	}

	private ArrayList<RenderSizeException> exceptions;

	public ProxyItemRenderer(ItemRenderer original) {
		super(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getModelManager(),
				Minecraft.getInstance().getItemColors());
		exceptions = new ArrayList<>();
	}

	public void addRenderException(int x, int y, float size) {
		exceptions.add(new RenderSizeException(x, y, size));
	}

	@Override
	protected void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel) {
		
		GlStateManager.pushMatrix();
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
				.setBlurMipmap(false, false);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlphaTest();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.setupGuiTransform(x, y, bakedmodel.isGui3d());
		bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel,
				ItemCameraTransforms.TransformType.GUI, false);
		this.renderItem(stack, bakedmodel);
		GlStateManager.disableAlphaTest();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
				.restoreLastBlurMipmap();
	}

	private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {

		GlStateManager.translatef((float) xPosition, (float) yPosition, 100.0F + this.zLevel);

		RenderSizeException exception = null;
		for (RenderSizeException r : exceptions)
			if (r.x == xPosition && r.y == yPosition)
				exception = r;

		if (exception != null)
			GlStateManager.translatef(exception.size / 2, exception.size / 2, 0.0F);
		else
			GlStateManager.translatef(8.0F, 8.0F, 0.0F);

		GlStateManager.scalef(1.0F, -1.0F, 1.0F);

		if (exception != null)
			GlStateManager.scalef(exception.size, exception.size, exception.size);
		else
			GlStateManager.scalef(16.0F, 16.0F, 16.0F);
		if (isGui3d) {
			GlStateManager.enableLighting();
		} else {
			GlStateManager.disableLighting();
		}
	}
	
	@Override
	public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text) {
		
		RenderSizeException exception = null;
		for (RenderSizeException r : exceptions)
			if (r.x == xPosition && r.y == yPosition)
				exception = r;
		
		GlStateManager.pushMatrix();
		
		if (exception != null)
			GlStateManager.scalef(2,2,2);
		
		if (exception != null)
		super.renderItemOverlayIntoGUI(fr, stack, xPosition+1, yPosition, text);
		else 
			super.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, text);
		
		GlStateManager.popMatrix();
	}
	
	private class RenderSizeException {

		public int x;
		public int y;
		public float size;

		public RenderSizeException(int x, int y, float size) {
			this.x = x;
			this.y = y;
			this.size = size;
		}
	}

}
