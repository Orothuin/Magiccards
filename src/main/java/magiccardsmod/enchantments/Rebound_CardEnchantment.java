package magiccardsmod.enchantments;

import java.util.HashMap;
import java.util.Map;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Rebound_CardEnchantment extends Enchantment {

	public Rebound_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMaxLevel() {

		return 10;
	}

	@Override
	public int getMinLevel() {

		return 1;
	}

	

	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {
		
		private static Map<Entity, Integer> bouncedC = new HashMap<Entity, Integer>();
		private static Map<Entity, Integer> bouncedS = new HashMap<Entity, Integer>();
		
		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType() == RayTraceResult.Type.BLOCK
					&& !((AbstractCardEntity) e.getEntity()).isInGround()) {

				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();

				if (!card.isEnchanted())
					return;

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);
				
				Enchantment ench = EnchantmentInit.REBOUND_CARD_ENCHANTMENT.get();
				
				if (enchantments.containsKey(ench)) {
					
					Map<Entity, Integer> bounced;
					
					if(e.getEntity().world.isRemote)
						bounced = bouncedC;
						else bounced = bouncedS;
					
					if (bounced.containsKey(e.getEntity()) && bounced.get(e.getEntity()) >= enchantments.get(ench))
						return;
					
					Direction dir = ((BlockRayTraceResult) e.getRayTraceResult()).getFace();

					Vec3d vec3d = mirror(e.getEntity().getMotion(), dir);
					vec3d = vec3d.scale(0.7f);
					e.getEntity().setMotion(vec3d);

					float f = MathHelper.sqrt(Entity.func_213296_b(vec3d));
					
					e.getEntity().rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z)
							* (double) (180F / (float) Math.PI));
					e.getEntity().rotationPitch = (float) (MathHelper.atan2(vec3d.y, (double) f)
							* (double) (180F / (float) Math.PI));
					e.getEntity().prevRotationYaw = e.getEntity().rotationYaw;
					e.getEntity().prevRotationPitch = e.getEntity().rotationPitch;

					if (!bounced.containsKey(e.getEntity()))
						bounced.put(e.getEntity(), 0);

					bounced.put(e.getEntity(), bounced.get(e.getEntity()) + 1);

					e.setCanceled(true);
				}
			}
		}

		private static Vec3d mirror(Vec3d vec, Direction dir) {
			Vec3d newVec;

			newVec = new Vec3d(0, 0, 0);

			switch (dir) {

			case DOWN:
				newVec = new Vec3d(vec.x, -1 * vec.y, vec.z);
				break;
			case UP:
				newVec = new Vec3d(vec.x, -1 * vec.y, vec.z);
				break;
			case NORTH:
				newVec = new Vec3d(vec.x, vec.y, -1 * vec.z);
				break;
			case EAST:
				newVec = new Vec3d(-1 * vec.x, vec.y, vec.z);
				break;
			case SOUTH:
				newVec = new Vec3d(vec.x, vec.y, -1 * vec.z);
				break;
			case WEST:
				newVec = new Vec3d(-1 * vec.x, vec.y, vec.z);
				break;
			default:
				break;
			}

			return newVec;
		}
	}

}
