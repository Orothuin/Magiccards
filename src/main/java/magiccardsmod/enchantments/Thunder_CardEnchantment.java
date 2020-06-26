package magiccardsmod.enchantments;

import java.util.Map;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Thunder_CardEnchantment extends Enchantment{
	
	private static int thunderTicks=0;
	private static int tickCounter=0;
	
	public Thunder_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}
	
	@Override
	public int getMaxLevel() {

		return 2;
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
					&& e.getRayTraceResult().getType()!=RayTraceResult.Type.MISS) {
				
				if(e.getEntity().getEntityWorld().isRemote)return;
				
				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();
				
				if(!card.isEnchanted())return;
				
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);
				
				if (enchantments.containsKey(EnchantmentInit.THUNDER_CARD_ENCHANTMENT.get())) {
					
					int level = enchantments.get(EnchantmentInit.THUNDER_CARD_ENCHANTMENT.get());
					
					LightningBoltEntity newThunderEntity = new LightningBoltEntity(e.getEntity().world, e.getEntity().posX, e.getEntity().posY, e.getEntity().posZ, false);
					
					((ServerWorld)e.getEntity().world).addLightningBolt(newThunderEntity);	
					
					if(thunderTicks<11&&level<=1)
					thunderTicks++;
				}
			}
		}

		@SubscribeEvent
		public static void onTick(WorldTickEvent e) {
			
			if(e.world.isRemote)return;
			
			tickCounter++;
			
			if(tickCounter>300) {
				
				tickCounter=0;
				
				if(thunderTicks>0)
				thunderTicks--;
			}
			
			if(thunderTicks>10) {
				
				thunderTicks=0;
				
				if(((ServerWorld)e.world).getServer().getGameRules().get(GameRules.DO_WEATHER_CYCLE).get())
				setWeatherThunder((ServerWorld)e.world);
			}
		}
		
		private static void setWeatherThunder(ServerWorld world) {
			 world.getWorldInfo().setClearWeatherTime(0);
			 world.getWorldInfo().setRainTime(6000);
			 world.getWorldInfo().setThunderTime(6000);
			 world.getWorldInfo().setRaining(true);
			 world.getWorldInfo().setThundering(true);
		}
	}	
}
