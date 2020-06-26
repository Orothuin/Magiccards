package magiccardsmod.enchantments;

import java.util.Map;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class BoneMeal_CardEnchantment extends Enchantment {

	public BoneMeal_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMaxLevel() {

		return 8;
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

				if (enchantments.containsKey(EnchantmentInit.BONEMEAL_CARD_ENCHANTMENT.get())) {

					BlockPos pos = ((BlockRayTraceResult) e.getRayTraceResult()).getPos();

					for (int i = 0; i < enchantments.get(EnchantmentInit.BONEMEAL_CARD_ENCHANTMENT.get()); i++) {
						placeBonemeal(e.getEntity().getEntityWorld(), pos,
								((BlockRayTraceResult) e.getRayTraceResult()).getFace());
						placeBonemeal(e.getEntity().getEntityWorld(), pos.up(),
								((BlockRayTraceResult) e.getRayTraceResult()).getFace());
					}
				}
			}
		}

		private static void placeBonemeal(World world, BlockPos pos, Direction face) {

			BlockPos blockpos = pos;
			BlockPos blockpos1 = blockpos.offset(face);
			if (applyBonemeal(world, blockpos)) {
				if (!world.isRemote()) {
					if (world.getBlockState(blockpos).getBlock() == Blocks.GRASS_BLOCK)
						world.playEvent(2005, blockpos.up(), 0);
					else
						world.playEvent(2005, blockpos, 0);
				}

				return;
			} else {
				BlockState blockstate = world.getBlockState(blockpos);
				boolean flag = blockstate.func_224755_d(world, blockpos, face);
				if (flag && BoneMealItem.growSeagrass(new ItemStack(Items.BONE_MEAL), world, blockpos1, face)) {

					if (!world.isRemote()) {
						world.playEvent(2005, blockpos1, 0);
					}

					return;
				} else {
					return;
				}
			}
		}

		public static boolean applyBonemeal(World worldIn, BlockPos pos) {

			BlockState blockstate = worldIn.getBlockState(pos);

			if (blockstate.getBlock() instanceof IGrowable) {
				IGrowable igrowable = (IGrowable) blockstate.getBlock();
				if (igrowable.canGrow(worldIn, pos, blockstate, worldIn.isRemote)) {
					if (!worldIn.isRemote) {
						if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos, blockstate)) {
							igrowable.grow(worldIn, worldIn.rand, pos, blockstate);
							
						}
					}

					return true;
				}
			}

			return false;
		}
	}
}
