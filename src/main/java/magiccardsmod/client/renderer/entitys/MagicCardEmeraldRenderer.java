package magiccardsmod.client.renderer.entitys;

import magiccardsmod.client.model.entitys.MagicCardEmeraldModel;
import magiccardsmod.entity.MagicCardEmeraldEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class MagicCardEmeraldRenderer extends MagicCardRenderer<MagicCardEmeraldEntity>{

	public MagicCardEmeraldRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		entityModel = new MagicCardEmeraldModel();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(MagicCardEmeraldEntity entity) {

		return new ResourceLocation("magiccards:textures/entitys/magiccard3dtwo.png");
	}
	
}
