package magiccardsmod.enchantments;

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

public class Fly_CardEnchantment extends Enchantment {

	public Fly_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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

	public static void applyFly(AbstractCardEntity entity) {
		if (entity instanceof AbstractCardEntity && !((AbstractCardEntity) entity).isInGround()) {

			if (entity.getEntityWorld().isRemote)
				return;

			ItemStack card = ((AbstractCardEntity) entity).getThrownCard();

			if (!card.isEnchanted())
				return;

			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

			if (enchantments.containsKey(EnchantmentInit.FLY_CARD_ENCHANTMENT.get())) {
				
				Entity player = entity.getShooter();

				if (!(player instanceof PlayerEntity))
					return;

				player.startRiding(entity);
			}
		}
	}
	
	public static void stoppFly(AbstractCardEntity entity) {
		if (entity instanceof AbstractCardEntity && !((AbstractCardEntity) entity).isInGround()) {

			if (entity.getEntityWorld().isRemote)
				return;

			ItemStack card = ((AbstractCardEntity) entity).getThrownCard();

			if (!card.isEnchanted())
				return;

			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

			if (enchantments.containsKey(EnchantmentInit.FLY_CARD_ENCHANTMENT.get())) {
				
				Entity player = entity.getShooter();
				
				Entity ride = player.getRidingEntity();
				
				if (!(player instanceof PlayerEntity)||(ride!=null&&!ride.equals(entity)))
					return;
				
				player.stopRiding();
			}
		}
	}
}
