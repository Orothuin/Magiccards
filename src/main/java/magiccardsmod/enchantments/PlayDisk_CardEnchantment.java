package magiccardsmod.enchantments;

import java.util.Map;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class PlayDisk_CardEnchantment extends Enchantment{

	public PlayDisk_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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
		
		if(ench==EnchantmentInit.EXPLOSION_CARD_ENCHANTMENT.get())return false;
		
		return super.canApplyTogether(ench);
	}

	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {

		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType()!=RayTraceResult.Type.MISS) {
				
				if(e.getEntity().getEntityWorld().isRemote)return;
				
				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();
				
				if(!card.isEnchanted())return;
				
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.PLAYDISC_CARD_ENCHANTMENT.get())) {
					
					ItemStack disc = ((AbstractCardEntity) e.getEntity()).getDisc();
					
					if(disc!=null && disc.getItem() instanceof MusicDiscItem&&((AbstractCardEntity) e.getEntity()).getSoundPosition()==null&&e.getEntity().isAlive()) {
						e.getEntity().getEntityWorld().playEvent((PlayerEntity)null, 1010, e.getEntity().getPosition(), Item.getIdFromItem(disc.getItem()));
						((AbstractCardEntity) e.getEntity()).setSoundPosition(e.getEntity().getPosition());
						
						((AbstractCardEntity) e.getEntity()).sheduleVanishChanceException(0.0f,11);
					}
				}
			}
		}
	}
}
