package magiccardsmod.client.tileentityrenderer;

import com.mojang.blaze3d.platform.GlStateManager;

import magiccardsmod.block.Pedestal;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.entity.MagicCardDiamondEntity;
import magiccardsmod.entity.MagicCardEmeraldEntity;
import magiccardsmod.entity.MagicCardEntity;
import magiccardsmod.entity.MagicCardGoldEntity;
import magiccardsmod.entity.MagicCardIronEntity;
import magiccardsmod.init.ItemInit;
import magiccardsmod.tileentitys.PedestalTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class PedestalRenderer extends TileEntityRenderer<PedestalTileEntity>{
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void render(PedestalTileEntity tileEntityIn, double x, double y, double z, float partialTicks,
			int destroyStage) {
		
		IWorld world = tileEntityIn.getWorld();
		
		BlockPos pos = tileEntityIn.getPos();
		
		AbstractCardEntity entity = null;
		
		ItemStack stack = tileEntityIn.getItem();
	
		if(stack.getItem()==ItemInit.MAGIC_CARD.get())
			entity=new MagicCardEntity((World) world, pos.getX(), pos.getY(), pos.getZ());
		
		else if(stack.getItem()==ItemInit.MAGIC_CARD_GOLD.get())
			entity=new MagicCardGoldEntity((World) world, pos.getX(), pos.getY(), pos.getZ());
		
		else if(stack.getItem()==ItemInit.MAGIC_CARD_IRON.get())
			entity=new MagicCardIronEntity((World) world, pos.getX(), pos.getY(), pos.getZ());
		
		else if(stack.getItem()==ItemInit.MAGIC_CARD_DIAMOND.get())
			entity=new MagicCardDiamondEntity((World) world, pos.getX(), pos.getY(), pos.getZ());
		
		else if(stack.getItem()==ItemInit.MAGIC_CARD_EMERALD.get())
			entity=new MagicCardEmeraldEntity((World) world, pos.getX(), pos.getY(), pos.getZ());
		
		if(entity==null)return;
		
		GlStateManager.pushMatrix();
		
		switch(world.getBlockState(pos).get(Pedestal.FACING)) {
		
		case EAST:
			GlStateManager.translated(x+0.58, y+1.09f, z+0.51);
			break;
		case NORTH:
			GlStateManager.translated(x+0.5, y+1.09f, z+0.42);
			break;
		case SOUTH:
			GlStateManager.translated(x+0.5, y+1.09f, z+0.58);
			break;
		case WEST:
			GlStateManager.translated(x+0.42, y+1.09f, z+0.49);
			break;
		}
		
		GlStateManager.rotatef(-world.getBlockState(pos).get(Pedestal.FACING).getHorizontalAngle(), 0, 1.0f, 0);
		
		GlStateManager.rotatef(22.0f, 1, 0.0f, 0);
		
		Minecraft.getInstance().getRenderManager().renderEntity(entity, 0, 0, 0, 0.0f, partialTicks, true);
			
		GlStateManager.popMatrix();
		
		//super.render(tileEntityIn, x, y, z, partialTicks, destroyStage);
	}
}
