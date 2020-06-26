package magiccardsmod.enchantments;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Teleport_CardEnchantment extends Enchantment {

	public Teleport_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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
					&& e.getRayTraceResult().getType() == RayTraceResult.Type.BLOCK) {

				if (e.getEntity().getEntityWorld().isRemote)
					return;

				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();

				if (!card.isEnchanted())
					return;

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.TELEPORT_CARD_ENCHANTMENT.get())) {

					BlockPos pos = ((BlockRayTraceResult) e.getRayTraceResult()).getPos();

					Entity entityIn = e.getEntity();
					
					Entity player = ((AbstractCardEntity) entityIn).getShooter();
					
					if (player instanceof ServerPlayerEntity) {
						
						ChunkPos chunkpos = new ChunkPos(pos);
						((ServerWorld) player.getEntityWorld()).getChunkProvider()
								.func_217228_a(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());
						
						player.stopRiding();
						if (((ServerPlayerEntity) player).isSleeping()) {
							((ServerPlayerEntity) player).wakeUpPlayer(true, true, false);
						}

						Set<SPlayerPositionLookPacket.Flags> set = EnumSet
								.noneOf(SPlayerPositionLookPacket.Flags.class);
						((ServerPlayerEntity) player).connection.setPlayerLocation(pos.getX(), pos.getY() + 1,
								pos.getZ(), entityIn.rotationYaw, entityIn.rotationPitch, set);
						
						((AbstractCardEntity) entityIn).sheduleVanishChanceException(0.9f,1);
						
					}
				}
			}
		}
	}
}
