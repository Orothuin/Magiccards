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
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Pierce_CardEnchantment extends Enchantment{

	public Pierce_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}
	
	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		
		return ench!=EnchantmentInit.DROP_CARD_ENCHANTMENT.get();
	}
	
	@Override
	public int getMaxLevel() {

		return 10;
	}

	@Override
	public int getMinLevel() {

		return 1;
	}
	
	private static Map<Entity, Integer> pierced = new HashMap<>();
	
	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {

		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType()==RayTraceResult.Type.ENTITY) {
				
				if(e.getEntity().getEntityWorld().isRemote)return;
				
				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();
				
				if(!card.isEnchanted())return;
				
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.PIERCE_CARD_ENCHANTMENT.get())) {
						
					int level = enchantments.get(EnchantmentInit.PIERCE_CARD_ENCHANTMENT.get());
					
					if(pierced.containsKey(e.getEntity())) {
						if(pierced.get(e.getEntity())>=level)return;
						
						pierced.put(e.getEntity(), pierced.get(e.getEntity())+1);
					}else
						pierced.put(e.getEntity(), 1);
					
					((AbstractCardEntity) e.getEntity()).setPierce(true);
				}
			}
		}
	}
	
}
