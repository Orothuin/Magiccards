package magiccardsmod.client.renderer.entitys;

import java.nio.FloatBuffer;
import java.util.Map;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import magiccardsmod.client.model.entitys.MagicCardModel;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class MagicCardRenderer<T extends AbstractCardEntity> extends EntityRenderer<T> {

	protected EntityModel<T> entityModel;
	protected final FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);

	public static float scale = 1;

	private static final DynamicTexture TEXTURE_BRIGHTNESS = Util.make(new DynamicTexture(16, 16, false),
			(p_203414_0_) -> {
				p_203414_0_.getTextureData().untrack();

				for (int i = 0; i < 16; ++i) {
					for (int j = 0; j < 16; ++j) {
						p_203414_0_.getTextureData().setPixelRGBA(j, i, -1);
					}
				}
				p_203414_0_.updateDynamicTexture();
			});

	@SuppressWarnings("unchecked")
	public MagicCardRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);

		entityModel = (EntityModel<T>) new MagicCardModel();
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {

		return new ResourceLocation("magiccards:textures/entitys/magiccard3d.png");
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();

		try {
			
			float f2 = MathHelper.func_219805_h(partialTicks, entity.prevRotationYaw, entity.rotationYaw);

			float f7 = MathHelper.func_219805_h(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
			this.renderLivingAt(entity, x, y, z);
			float f8 = this.handleRotationFloat(entity, partialTicks);

			ItemStack card = entity.getThrownCard();

			if (card.isEnchanted()) {

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.FLY_CARD_ENCHANTMENT.get())) {
					scale=4;
				}
			}
			
			float f4 = this.prepareScale(entity, partialTicks);

			float f5 = 0.0F;
			float f6 = 0.0F;

			float f4Temp = f4;
			//f4*=scale;
			float r = 1.5f * (f4 / f4Temp);

			// Transformation to apply the rotation around the center of the model
			GlStateManager.translatef(0, +r - (r - 1.5f), 0.0f);
			GlStateManager.rotatef(180 - f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(-f7, 1.0F, 0.0F, 0.0F);
			GlStateManager.translatef(0, -r, 0);

			GlStateManager.enableAlphaTest();
			
			if (this.renderOutlines) {

				GlStateManager.enableColorMaterial();
				GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
				
				this.renderModel(entity, f6, f5, f8, f2, f7, f4, true);
				//renderEffect(8, entity, f6, f5, f8, f2, f7, f4);

				GlStateManager.tearDownSolidRenderingTextureCombine();
				GlStateManager.disableColorMaterial();

			} else {
				boolean flag1 = this.setDoRenderBrightness(entity, partialTicks);
				this.renderModel(entity, f6, f5, f8, f2, f7, f4, true);
				//renderEffect(8, entity, f6, f5, f8, f2, f7, f4);
				if (flag1) {
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);

			}
			scale = 1;
			GlStateManager.disableRescaleNormal();
		} catch (Exception exception) {

		}

		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.enableTexture();
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
		return this.setBrightness(entityLivingBaseIn, partialTicks, true);
	}

	protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor, boolean setTexture) {

		boolean flag = this.isVisible(entitylivingbaseIn);
		boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getInstance().player);
		if (flag || flag1) {

			if (setTexture)
				if (!this.bindEntityTexture(entitylivingbaseIn)) {
					return;
				}

			if (flag1) {
				GlStateManager.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}

			this.entityModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
					scaleFactor);

			if (flag1) {
				GlStateManager.unsetProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}

		}

	}

	protected boolean isVisible(T p_193115_1_) {
		return !p_193115_1_.isInvisible() || this.renderOutlines;
	}

	protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {

		GlStateManager.translatef((float) x, (float) y, (float) z);
	}

	protected float handleRotationFloat(T livingBase, float partialTicks) {
		return (float) livingBase.ticksExisted + partialTicks;
	}

	public float prepareScale(T entitylivingbaseIn, float partialTicks) {
		GlStateManager.enableRescaleNormal();
		GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
		GlStateManager.scalef(scale, scale, scale);
		float f = 0.0625F;
		GlStateManager.translatef(0.0f, -1.501F, 0.0F);
		return f;
	}

	protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {

		int i = 0;
		boolean flag = (i >> 24 & 255) > 0;
		boolean flag1 = false;
		if (!flag && !flag1) {
			return false;
		} else if (!flag && !combineTextures) {
			return false;
		} else {
			GlStateManager.activeTexture(GLX.GL_TEXTURE0);
			GlStateManager.enableTexture();
			GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_TEXTURE0);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PRIMARY_COLOR);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_TEXTURE0);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
			GlStateManager.activeTexture(GLX.GL_TEXTURE1);
			GlStateManager.enableTexture();
			GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, GLX.GL_INTERPOLATE);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_CONSTANT);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE2_RGB, GLX.GL_CONSTANT);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND2_RGB, 770);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
			this.brightnessBuffer.position(0);
			if (flag1) {
				this.brightnessBuffer.put(1.0F);
				this.brightnessBuffer.put(0.0F);
				this.brightnessBuffer.put(0.0F);
				this.brightnessBuffer.put(0.3F);
			} else {
				float f1 = (float) (i >> 24 & 255) / 255.0F;
				float f2 = (float) (i >> 16 & 255) / 255.0F;
				float f3 = (float) (i >> 8 & 255) / 255.0F;
				float f4 = (float) (i & 255) / 255.0F;
				this.brightnessBuffer.put(f2);
				this.brightnessBuffer.put(f3);
				this.brightnessBuffer.put(f4);
				this.brightnessBuffer.put(1.0F - f1);
			}

			this.brightnessBuffer.flip();
			GlStateManager.texEnv(8960, 8705, this.brightnessBuffer);
			GlStateManager.activeTexture(GLX.GL_TEXTURE2);
			GlStateManager.enableTexture();
			GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
			GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_TEXTURE1);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
			GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 7681);
			GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_PREVIOUS);
			GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
			GlStateManager.activeTexture(GLX.GL_TEXTURE0);
			return true;
		}
	}

	protected void unsetBrightness() {
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
		GlStateManager.enableTexture();
		GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, GLX.GL_TEXTURE0);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PRIMARY_COLOR);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 8448);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, GLX.GL_TEXTURE0);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_ALPHA, GLX.GL_PRIMARY_COLOR);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_ALPHA, 770);
		GlStateManager.activeTexture(GLX.GL_TEXTURE1);
		GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, 5890);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, 5890);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.activeTexture(GLX.GL_TEXTURE2);
		GlStateManager.disableTexture();
		GlStateManager.bindTexture(0);
		GlStateManager.texEnv(8960, 8704, GLX.GL_COMBINE);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_RGB, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND1_RGB, 768);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_RGB, 5890);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE1_RGB, GLX.GL_PREVIOUS);
		GlStateManager.texEnv(8960, GLX.GL_COMBINE_ALPHA, 8448);
		GlStateManager.texEnv(8960, GLX.GL_OPERAND0_ALPHA, 770);
		GlStateManager.texEnv(8960, GLX.GL_SOURCE0_ALPHA, 5890);
		GlStateManager.activeTexture(GLX.GL_TEXTURE0);
	}

	public void renderEffect(int scale, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		// GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR,
		// GlStateManager.DestFactor.ONE);
		this.bindTexture(ItemRenderer.RES_ITEM_GLINT);
		// GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		// GlStateManager.scalef((float)scale, (float)scale, (float)scale);
		float f = (float) (Util.milliTime() % 3000L) / 3000.0F / (float) scale;
		GlStateManager.translatef(f, 0.0F, 0.0F);
		GlStateManager.rotatef(-50.0F, 0.0F, 0.0F, 1.0F);
		renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor,
				false);
		GlStateManager.popMatrix();
		// GlStateManager.pushMatrix();
		// GlStateManager.scalef((float)scale, (float)scale, (float)scale);
		// float f1 = (float)(Util.milliTime() % 4873L) / 4873.0F / (float)scale;
		// GlStateManager.translatef(-f1, 0.0F, 0.0F);
		// GlStateManager.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
		// renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
		// netHeadYaw, headPitch, scaleFactor, false);
		// GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		// this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
	}
}
