package magiccardsmod.enchantments;

import java.util.Map;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class CaptureEntity_CardEnchantment extends Enchantment {

	public CaptureEntity_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity) {

				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();

				if (!card.isEnchanted())
					return;

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.CAPTURE_ENTITY_CARD_ENCHANTMENT.get())) {
					
					if (e.getRayTraceResult().getType() == RayTraceResult.Type.ENTITY) {
						
						if (e.getEntity().getEntityWorld().isRemote)
							return;
						
						Entity entity = ((EntityRayTraceResult) e.getRayTraceResult()).getEntity();

						((AbstractCardEntity) e.getEntity()).setCapturedEntity(entity);

						entity.remove();
						entity.getEntityWorld().setEntityState(entity, (byte) 3);

						((AbstractCardEntity) e.getEntity()).removeMe();

						card.getOrCreateTag().put("capturedentity",
								((AbstractCardEntity) e.getEntity()).getCapturedEntityNBT());

						InventoryHelper.spawnItemStack(e.getEntity().getEntityWorld(), e.getEntity().posX,
								e.getEntity().posY, e.getEntity().posZ, card.copy());
						
						e.setCanceled(true);
					} else if (e.getRayTraceResult().getType() == RayTraceResult.Type.BLOCK) {

						if (card.getTag().contains("capturedentity")) {

							((AbstractCardEntity) e.getEntity())
									.setCapturedEntityNBT(card.getTag().getCompound("capturedentity"));

							Entity entity = ((AbstractCardEntity) e.getEntity()).getCapturedEntity();
							if (entity != null) {
								BlockPos pos = ((BlockRayTraceResult) e.getRayTraceResult()).getPos();
								entity.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
								
								
									e.getEntity().getEntityWorld().getChunk(pos).addEntity(entity);
							}
							
							card.getTag().remove("capturedentity");
							((AbstractCardEntity) e.getEntity()).setCapturedEntityNBT(new CompoundNBT());
						}
					}
				}
			}
		}
	}
}
