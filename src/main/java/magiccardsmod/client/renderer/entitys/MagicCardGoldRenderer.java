package magiccardsmod.client.renderer.entitys;

import magiccardsmod.client.model.entitys.MagicCardGoldModel;
import magiccardsmod.entity.MagicCardGoldEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class MagicCardGoldRenderer extends MagicCardRenderer<MagicCardGoldEntity>{

	public MagicCardGoldRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		entityModel = new MagicCardGoldModel();
	}

}
