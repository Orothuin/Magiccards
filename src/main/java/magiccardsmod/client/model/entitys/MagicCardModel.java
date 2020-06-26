package magiccardsmod.client.model.entitys;

import magiccardsmod.entity.MagicCardEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class MagicCardModel extends EntityModel<MagicCardEntity> {
	private final RendererModel bone;
	private final RendererModel bone8;
	private final RendererModel bone6;
	private final RendererModel bone7;
	private final RendererModel bone5;
	private final RendererModel bone2;

	public MagicCardModel() {
		textureWidth = 40;
		textureHeight = 40;

		bone = new RendererModel(this);
		bone.setRotationPoint(-0.1667F, 23.8333F, -0.3333F);
		setRotationAngle(bone, 0.0F, 180.0F, 0.0F);
		

		bone8 = new RendererModel(this);
		bone8.setRotationPoint(-0.3333F, -0.3333F, -0.6667F);
		bone.addChild(bone8);
		bone8.cubeList.add(new ModelBox(bone8, 3, 0, -3.5F, -0.5F, -5.0F, 7, 1, 10, 0.0F, false));

		bone6 = new RendererModel(this);
		bone6.setRotationPoint(1.4167F, 0.1667F, -5.9167F);
		bone.addChild(bone6);
		setRotationAngle(bone6, 0.0F, 90.0F, 0.0F);
		bone6.cubeList.add(new ModelBox(bone6, 0, 0, -2.25F, -1.0F, 1.0F, 2, 1, 1, 0.0F, false));
		bone6.cubeList.add(new ModelBox(bone6, 0, 5, -1.0F, -1.0F, -0.25F, 1, 1, 2, 0.0F, false));

		bone7 = new RendererModel(this);
		bone7.setRotationPoint(-3.0833F, -0.8333F, -4.6667F);
		bone.addChild(bone7);
		setRotationAngle(bone7, 0.0F, 180.0F, 0.0F);
		bone7.cubeList.add(new ModelBox(bone7, 0, 0, -1.25F, 0.0F, 0.25F, 2, 1, 1, 0.0F, false));
		bone7.cubeList.add(new ModelBox(bone7, 0, 5, 0.0F, 0.0F, -1.0F, 1, 1, 2, 0.0F, false));

		bone5 = new RendererModel(this);
		bone5.setRotationPoint(3.4167F, 0.1667F, 2.5833F);
		bone.addChild(bone5);
		bone5.cubeList.add(new ModelBox(bone5, 0, 0, -2.25F, -1.0F, 1.0F, 2, 1, 1, 0.0F, false));
		bone5.cubeList.add(new ModelBox(bone5, 0, 5, -1.0F, -1.0F, -0.25F, 1, 1, 2, 0.0F, false));

		bone2 = new RendererModel(this);
		bone2.setRotationPoint(-2.8333F, -0.8333F, 3.5833F);
		bone.addChild(bone2);
		setRotationAngle(bone2, 0.0F, -90.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, -1.25F, 0.0F, 0.25F, 2, 1, 1, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 5, 0.0F, 0.0F, -1.0F, 1, 1, 2, 0.0F, false));
	}
	@Override
	public void render(MagicCardEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
		
	}
	public void renderer() {
		bone.render(0.625f);
	}

	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {

		modelRenderer.rotateAngleX = (float) (x * (Math.PI / 180F));
		modelRenderer.rotateAngleY = (float) (y * (Math.PI / 180F));
		modelRenderer.rotateAngleZ = (float) (z * (Math.PI / 180F));
	}
}