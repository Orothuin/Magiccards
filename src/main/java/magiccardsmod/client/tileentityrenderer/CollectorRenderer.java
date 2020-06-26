package magiccardsmod.client.tileentityrenderer;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import magiccardsmod.block.EssenceCollectorBlock;
import magiccardsmod.tileentitys.CollectorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
@OnlyIn(Dist.CLIENT)
public class CollectorRenderer extends TileEntityRenderer<CollectorTileEntity> {
	
	@Override
	public void render(CollectorTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {

		tile.renderTicks += partialTicks;

		IWorld world = tile.getWorld();

		BlockPos pos = tile.getPos();
		
		tile.b=true;
		
		BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
		
		int i=1;
		
		if(tile.getWorld().getDimension().isNether())i=3;
		else if(tile.getPos().getY()>EssenceCollectorBlock.WINDESSENCE_HEIGHT)i=2;
		
		BlockState state = world.getBlockState(pos).with(EssenceCollectorBlock.SPHERE, i);
		IBakedModel model = blockRenderer.getBlockModelShapes().getModel(state);
		IModelData data = model.getModelData(world, pos, state, ModelDataManager.getModelData(tile.getWorld(), pos));

		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();

		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();

		GlStateManager.disableCull();
		
		
		
		float speed = 70.0f;

		float dY = 1.0f / 16.0f * (float) Math.sin((2 * Math.PI / speed) * (tile.renderTicks % speed));

		GlStateManager.translated(x + 0.5, y+dY+1-tile.getConcentration(), z + 0.5);
		
		GlStateManager.scalef(tile.getConcentration(), tile.getConcentration(), tile.getConcentration());
		
		GlStateManager.rotatef((360.0f / speed) * (tile.renderTicks % speed), 0, 1, 0);
		
		GlStateManager.translated(-x - 0.5, -y, -z - 0.5);
		
		if (Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		else
			GlStateManager.shadeModel(GL11.GL_FLAT);
		
		
		
		
		Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

		Tessellator.getInstance().getBuffer().setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
			
		blockRenderer.getBlockModelRenderer().renderModel(world, model, state, pos,
				Tessellator.getInstance().getBuffer(), true, new Random(), 42, data);
		
		Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);

		Tessellator.getInstance().draw();
		
		GlStateManager.translated(x, y, z+1);

		//blockRenderer.getBlockModelRenderer().renderModelBrightness(model, state, 15, false);

		GlStateManager.translated(-x, -y, -z-1);

		GlStateManager.popMatrix();
		
		tile.b=false;
		
	}

}
