package magiccardsmod.client.renderer.entitys;

import magiccardsmod.client.model.entitys.MagicCardDiamondModel;
import magiccardsmod.entity.MagicCardDiamondEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class MagicCardDiamondRenderer extends MagicCardRenderer<MagicCardDiamondEntity>{

	public MagicCardDiamondRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		entityModel = new MagicCardDiamondModel();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(MagicCardDiamondEntity entity) {

		return new ResourceLocation("magiccards:textures/entitys/magiccard3dtwo.png");
	}
	
}
