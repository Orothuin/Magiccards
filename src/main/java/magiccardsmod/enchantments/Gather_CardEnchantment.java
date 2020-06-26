package magiccardsmod.enchantments;

import java.util.List;
import java.util.Map;

import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class Gather_CardEnchantment extends Enchantment {

	public Gather_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMaxLevel() {

		return 6;
	}

	@Override
	public int getMinLevel() {

		return 1;
	}
	
	public static void applyGathering(AbstractCardEntity cardEntity) {
		
		if (cardEntity instanceof AbstractCardEntity) {

			ItemStack card = ((AbstractCardEntity) cardEntity).getThrownCard();

			if (!card.isEnchanted())
				return;

			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

			if (enchantments.containsKey(EnchantmentInit.GATHER_CARD_ENCHANTMENT.get())) {

				double radius = 5+2*enchantments.get(EnchantmentInit.GATHER_CARD_ENCHANTMENT.get());

				List<Entity> entities = cardEntity.world.getEntitiesWithinAABB(Entity.class,
						new AxisAlignedBB(cardEntity.posX - radius, cardEntity.posY - radius, cardEntity.posZ - radius,
								cardEntity.posX + radius, cardEntity.posY + radius, cardEntity.posZ + radius));

				for (Entity e : entities)
					if (e instanceof PlayerEntity||e.equals(cardEntity))
						continue;
					else if (e.getDistance(cardEntity) <= radius) {

						Vec3d center = new Vec3d(cardEntity.posX + cardEntity.getWidth() / 2,
								cardEntity.posY + cardEntity.getHeight() , cardEntity.posZ + cardEntity.getWidth());

						Vec3d ePos = new Vec3d(e.posX + e.getWidth() / 2, e.posY + e.getHeight() / 2,
								e.posZ + e.getWidth());

						Vec3d dir = center.subtract(ePos);

						dir = dir.normalize();
						
						float maxVelocity = 0.06f;
						float minVelocity = 0.03f;
						double velocity =e.getDistance(cardEntity)/radius* maxVelocity+ minVelocity;
						
						Vec3d motionChange = dir.scale(velocity);
						
						Vec3d newMotion = e.getMotion().add(motionChange);
						
						e.setMotion(newMotion.x, newMotion.y, newMotion.z);
										
					}
			}
		}
	}
}
