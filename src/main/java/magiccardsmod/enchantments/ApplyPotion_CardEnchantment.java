package magiccardsmod.enchantments;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class ApplyPotion_CardEnchantment extends Enchantment {

	public ApplyPotion_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMaxLevel() {

		return 1;
	}

	@Override
	public int getMinLevel() {

		return 1;
	}

	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {

		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType() != RayTraceResult.Type.MISS) {

				if (e.getEntity().getEntityWorld().isRemote)
					return;

				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();

				if (!card.isEnchanted())
					return;

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.POTION_CARD_ENCHANTMENT.get())) {

					runPotion(e.getRayTraceResult(), e.getEntity(), e.getEntity().getEntityWorld(),
							((AbstractCardEntity) e.getEntity()).getPotion());
				}
			}
		}

		protected static void runPotion(RayTraceResult result, Entity cardEntity, IWorld world, ItemStack potionStack) {

			if (!world.isRemote()) {
				ItemStack itemstack = potionStack;
				Potion potion = PotionUtils.getPotionFromItem(itemstack);
				List<EffectInstance> list = PotionUtils.getEffectsFromStack(itemstack);
				boolean flag = potion == Potions.WATER && list.isEmpty();
				if (result.getType() == RayTraceResult.Type.BLOCK && flag) {
					BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) result;
					Direction direction = blockraytraceresult.getFace();
					BlockPos blockpos = blockraytraceresult.getPos().offset(direction);
					extinguishFires(blockpos, direction, cardEntity);
					extinguishFires(blockpos.offset(direction.getOpposite()), direction, cardEntity);

					for (Direction direction1 : Direction.Plane.HORIZONTAL) {
						extinguishFires(blockpos.offset(direction1), direction1, cardEntity);
					}
				}

				if (flag) {
					applyWater(cardEntity);
				} else if (!list.isEmpty()) {
					if (isLingering(cardEntity)) {
						makeAreaOfEffectCloud(itemstack, potion, cardEntity);
						
						int i = potion.hasInstantEffect() ? 2007 : 2002;
						world.playEvent(i, new BlockPos(cardEntity.posX, cardEntity.posY, cardEntity.posZ), PotionUtils.getColor(itemstack));
						
					} else if (isSplash(cardEntity)) {
						applySplashPotion(list,
								result.getType() == RayTraceResult.Type.ENTITY
										? ((EntityRayTraceResult) result).getEntity()
										: null,
								cardEntity);
						
						int i = potion.hasInstantEffect() ? 2007 : 2002;
						world.playEvent(i, new BlockPos(cardEntity.posX, cardEntity.posY, cardEntity.posZ), PotionUtils.getColor(itemstack));
						
					} else if (result.getType() == RayTraceResult.Type.ENTITY)
						applyNormalPotion(list, ((EntityRayTraceResult) result).getEntity(), cardEntity);
				}

				

			}
		}

		private static void applyNormalPotion(List<EffectInstance> effects, @Nullable Entity targetEntity,
				Entity cardEntity) {

			if (((LivingEntity) targetEntity).canBeHitWithPotion()) {
				double d0 = cardEntity.getDistanceSq(targetEntity);
				if (d0 < 16.0D) {
					double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

					d1 = 1.0D;

					for (EffectInstance effectinstance : effects) {
						Effect effect = effectinstance.getPotion();
						if (effect.isInstant()) {
							effect.affectEntity(cardEntity, ((AbstractCardEntity) cardEntity).getShooter(),
									(LivingEntity) targetEntity, effectinstance.getAmplifier(), d1);
						} else {
							int i = (int) (d1 * (double) effectinstance.getDuration() + 0.5D);
							if (i > 20) {
								((LivingEntity) targetEntity)
										.addPotionEffect(new EffectInstance(effect, i, effectinstance.getAmplifier(),
												effectinstance.isAmbient(), effectinstance.doesShowParticles()));
							}
						}
					}
				}
			}
		}

		private static void applySplashPotion(List<EffectInstance> effects, @Nullable Entity targetEntity,
				Entity cardEntity) {
			AxisAlignedBB axisalignedbb = cardEntity.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = cardEntity.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
			if (!list.isEmpty()) {
				for (LivingEntity livingentity : list) {
					if (livingentity.canBeHitWithPotion()) {
						double d0 = cardEntity.getDistanceSq(livingentity);
						if (d0 < 16.0D) {
							double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
							if (livingentity == targetEntity) {
								d1 = 1.0D;
							}

							for (EffectInstance effectinstance : effects) {
								Effect effect = effectinstance.getPotion();
								if (effect.isInstant()) {
									effect.affectEntity(cardEntity, ((AbstractCardEntity) cardEntity).getShooter(),
											livingentity, effectinstance.getAmplifier(), d1);
								} else {
									int i = (int) (d1 * (double) effectinstance.getDuration() + 0.5D);
									if (i > 20) {
										livingentity.addPotionEffect(new EffectInstance(effect, i,
												effectinstance.getAmplifier(), effectinstance.isAmbient(),
												effectinstance.doesShowParticles()));
									}
								}
							}
						}
					}
				}
			}
		}

		private static void makeAreaOfEffectCloud(ItemStack potionStack, Potion potion, Entity cardEntity) {

			AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(cardEntity.world, cardEntity.posX,
					cardEntity.posY, cardEntity.posZ);

			areaeffectcloudentity.setOwner((LivingEntity) ((AbstractCardEntity) cardEntity).getShooter());
			areaeffectcloudentity.setRadius(3.0F);
			areaeffectcloudentity.setRadiusOnUse(-0.5F);
			areaeffectcloudentity.setWaitTime(10);
			areaeffectcloudentity
					.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
			areaeffectcloudentity.setPotion(potion);

			for (EffectInstance effectinstance : PotionUtils.getFullEffectsFromItem(potionStack)) {
				areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
			}

			CompoundNBT compoundnbt = potionStack.getTag();
			if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99)) {
				areaeffectcloudentity.setColor(compoundnbt.getInt("CustomPotionColor"));
			}

			cardEntity.world.addEntity(areaeffectcloudentity);
		}

		private static boolean isLingering(Entity cardEntity) {
			return ((AbstractCardEntity) cardEntity).getPotion().getItem() == Items.LINGERING_POTION;
		}

		private static boolean isSplash(Entity cardEntity) {
			return ((AbstractCardEntity) cardEntity).getPotion().getItem() == Items.SPLASH_POTION;
		}

		private static void extinguishFires(BlockPos pos, Direction p_184542_2_, Entity cardEntity) {

			BlockState blockstate = cardEntity.world.getBlockState(pos);
			Block block = blockstate.getBlock();
			if (block == Blocks.FIRE) {
				cardEntity.world.extinguishFire((PlayerEntity) null, pos.offset(p_184542_2_),
						p_184542_2_.getOpposite());
			} else if (block == Blocks.CAMPFIRE && blockstate.get(CampfireBlock.LIT)) {
				cardEntity.world.playEvent((PlayerEntity) null, 1009, pos, 0);
				cardEntity.world.setBlockState(pos, blockstate.with(CampfireBlock.LIT, Boolean.valueOf(false)));
			}
		}

		public static final Predicate<LivingEntity> WATER_SENSITIVE = ApplyEnchantment::isWaterSensitiveEntity;

		private static void applyWater(Entity cardEntity) {
			AxisAlignedBB axisalignedbb = cardEntity.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = cardEntity.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb,
					WATER_SENSITIVE);
			if (!list.isEmpty()) {
				for (LivingEntity livingentity : list) {
					double d0 = cardEntity.getDistanceSq(livingentity);
					if (d0 < 16.0D && isWaterSensitiveEntity(livingentity)) {
						livingentity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(livingentity,
								((AbstractCardEntity) cardEntity).getShooter()), 1.0F);
					}
				}
			}
		}

		private static boolean isWaterSensitiveEntity(LivingEntity p_190544_0_) {
			return p_190544_0_ instanceof EndermanEntity || p_190544_0_ instanceof BlazeEntity;
		}
	}
}
