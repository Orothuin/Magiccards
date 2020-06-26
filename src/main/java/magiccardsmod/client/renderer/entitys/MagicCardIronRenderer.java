package magiccardsmod.client.renderer.entitys;

import magiccardsmod.client.model.entitys.MagicCardIronModel;
import magiccardsmod.entity.MagicCardIronEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class MagicCardIronRenderer extends MagicCardRenderer<MagicCardIronEntity>{

	public MagicCardIronRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		entityModel = new MagicCardIronModel();
	}

}
