package magiccardsmod.enchantments;

import java.util.Map;
import java.util.Random;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Drop_CardEnchantment extends Enchantment {

	public Drop_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		
		return ench!=EnchantmentInit.PIERCE_CARD_ENCHANTMENT.get();
	}
	
	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {

		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType() == RayTraceResult.Type.ENTITY) {

				if (e.getEntity().getEntityWorld().isRemote)
					return;

				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();

				if (!card.isEnchanted())
					return;

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.DROP_CARD_ENCHANTMENT.get())) {
					
					if (new Random().nextFloat() >= ((AbstractCardEntity) e.getEntity()).getVanishChance()*1.5f-0.05f)
						InventoryHelper.spawnItemStack(e.getEntity().getEntityWorld(), e.getEntity().posX,
								e.getEntity().posY, e.getEntity().posZ, card.copy());
				}
			}
		}
	}

}
