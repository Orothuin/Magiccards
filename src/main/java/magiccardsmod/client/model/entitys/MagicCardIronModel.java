package magiccardsmod.client.model.entitys;

import magiccardsmod.entity.MagicCardIronEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class MagicCardIronModel extends EntityModel<MagicCardIronEntity>{
	
	private final RendererModel bone;
	private final RendererModel bone5;
	private final RendererModel bone6;
	private final RendererModel bone7;
	private final RendererModel bone4;
	private final RendererModel bone2;
	private final RendererModel bone3;

	public MagicCardIronModel() {
		textureWidth = 40;
		textureHeight = 40;

		bone = new RendererModel(this);
		bone.setRotationPoint(-0.1667F, 23.8333F, -0.3333F);
		setRotationAngle(bone, 0.0F, 180.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 3, 14, -3.8333F, -0.8333F, -5.6667F, 7, 1, 10, 0.0F, false));

		bone5 = new RendererModel(this);
		bone5.setRotationPoint(-0.8333F, 0.1667F, -8.6667F);
		bone.addChild(bone5);
		setRotationAngle(bone5, 0.0F, 180.0F, 0.0F);
		

		bone6 = new RendererModel(this);
		bone6.setRotationPoint(3.25F, 0.5F, -4.75F);
		bone5.addChild(bone6);
		bone6.cubeList.add(new ModelBox(bone6, 0, 14, -2.25F, -2.0F, 1.0F, 2, 2, 1, 0.0F, false));
		bone6.cubeList.add(new ModelBox(bone6, 0, 19, -1.0F, -2.0F, -0.25F, 1, 2, 2, 0.0F, false));

		bone7 = new RendererModel(this);
		bone7.setRotationPoint(-3.0F, -0.5F, -3.75F);
		bone5.addChild(bone7);
		setRotationAngle(bone7, 0.0F, -90.0F, 0.0F);
		bone7.cubeList.add(new ModelBox(bone7, 0, 14, -1.25F, -1.0F, 0.25F, 2, 2, 1, 0.0F, false));
		bone7.cubeList.add(new ModelBox(bone7, 0, 19, 0.0F, -1.0F, -1.0F, 1, 2, 2, 0.0F, false));

		bone4 = new RendererModel(this);
		bone4.setRotationPoint(2.4167F, 0.6667F, 3.5833F);
		bone.addChild(bone4);
		

		bone2 = new RendererModel(this);
		bone2.setRotationPoint(1.0F, 0.0F, -1.0F);
		bone4.addChild(bone2);
		bone2.cubeList.add(new ModelBox(bone2, 0, 14, -2.25F, -2.0F, 1.0F, 2, 2, 1, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 19, -1.0F, -2.0F, -0.25F, 1, 2, 2, 0.0F, false));

		bone3 = new RendererModel(this);
		bone3.setRotationPoint(-5.25F, -1.0F, 0.0F);
		bone4.addChild(bone3);
		setRotationAngle(bone3, 0.0F, -90.0F, 0.0F);
		bone3.cubeList.add(new ModelBox(bone3, 0, 14, -1.25F, -1.0F, 0.25F, 2, 2, 1, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 19, 0.0F, -1.0F, -1.0F, 1, 2, 2, 0.0F, false));
	}

	@Override
	public void render(MagicCardIronEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
	}

	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = (float) (x * (Math.PI / 180F));
		modelRenderer.rotateAngleY = (float) (y * (Math.PI / 180F));
		modelRenderer.rotateAngleZ = (float) (z * (Math.PI / 180F));
	}
}
