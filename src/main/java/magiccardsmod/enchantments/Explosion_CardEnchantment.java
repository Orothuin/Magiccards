package magiccardsmod.enchantments;

import java.util.Map;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion.Mode;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Explosion_CardEnchantment extends Enchantment {

	public Explosion_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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
	
	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		
		if(ench == EnchantmentInit.PLAYDISC_CARD_ENCHANTMENT.get())return false;
		
		return super.canApplyTogether(ench);
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

				if (enchantments.containsKey(EnchantmentInit.EXPLOSION_CARD_ENCHANTMENT.get())) {

					boolean causesFire = enchantments.containsKey(EnchantmentInit.FIREASPECT_CARD_ENCHANTMENT.get());

					e.getEntity().getEntityWorld().createExplosion(e.getEntity(), e.getEntity().posX,
							e.getEntity().posY+e.getEntity().getHeight()/2, e.getEntity().posZ,
							2 + enchantments.get(EnchantmentInit.EXPLOSION_CARD_ENCHANTMENT.get()), causesFire,
							Mode.BREAK);
					
					((AbstractCardEntity) e.getEntity()).sheduleVanishChanceException(1.1f,11);
				}
			}
		}
	}

}
