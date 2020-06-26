package magiccardsmod.client.tileentityrenderer;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import magiccardsmod.block.CardPressBlock;
import magiccardsmod.tileentitys.CardPressTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class CardPressRenderer extends TileEntityRenderer<CardPressTileEntity> {

	@Override
	public void render(CardPressTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
		
		IWorld world = tile.getWorld();

		BlockPos pos = tile.getPos();

		BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();

		BlockState state = world.getBlockState(pos).with(CardPressBlock.STOMP, true);
		IBakedModel model = blockRenderer.getBlockModelShapes().getModel(state);
		IModelData data = model.getModelData(world, pos, state, ModelDataManager.getModelData(tile.getWorld(), pos));

		ItemStack stack = tile.getOutputInventory().getStackInSlot(0).isEmpty() ? tile.getInputInventory().getStackInSlot(0) : tile.getOutputInventory().getStackInSlot(0);
		
		if (!stack.isEmpty()) {
			
			GlStateManager.pushMatrix();
			
			double d0 = +x;
			double d1 = +y;
			double d2 = +z;
			GlStateManager.translated(d0 , d1 , d2 );
			
			GlStateManager.translated(0.5D,0.6D, 0.5D);
			
			RenderHelper.disableStandardItemLighting();
			
			GlStateManager.blendFunc(770, 771);
			GlStateManager.enableBlend();

			GlStateManager.disableCull();

			if (Minecraft.isAmbientOcclusionEnabled())
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
			else
				GlStateManager.shadeModel(GL11.GL_FLAT);
			
			GlStateManager.rotatef(90, 1, 0, 0);

			GlStateManager.scalef(0.9F, 0.9F, 0.9F);
			
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
		
			GlStateManager.popMatrix();
		}
		
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.pushMatrix();
	
		RenderHelper.disableStandardItemLighting();
		
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();

		GlStateManager.disableCull();

		if (Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		else
			GlStateManager.shadeModel(GL11.GL_FLAT);

		Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

		double f = (-(6.0 / 16.0) * Math.pow(Math.cos(((tile.ticks % 70)) * Math.PI / 70),4)+(6.0 / 16.0));
		
		Tessellator.getInstance().getBuffer().setTranslation(x - pos.getX(), y - pos.getY() - f, z - pos.getZ());
	
		blockRenderer.getBlockModelRenderer().renderModel(world, model, state, pos,
				Tessellator.getInstance().getBuffer(), true, new Random(), 42, data);

		Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);

		Tessellator.getInstance().draw();

		GlStateManager.popMatrix();

		
		
	}
}
