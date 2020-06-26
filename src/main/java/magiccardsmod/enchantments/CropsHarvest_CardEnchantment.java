package magiccardsmod.enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class CropsHarvest_CardEnchantment extends Enchantment {

	private static ArrayList<Map<BlockPos,Boolean>> cropsQue = new ArrayList<>();

	public CropsHarvest_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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

		public static final int LISTCAP = 300;
		public static final int MAX_RADIUS = 8;

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

				if (enchantments.containsKey(EnchantmentInit.CROPSHARVEST_CARD_ENCHANTMENT.get())) {

					BlockPos pos = ((BlockRayTraceResult) e.getRayTraceResult()).getPos().up();

					Block blockType = e.getEntity().getEntityWorld().getBlockState(pos).getBlock();

					if (blockType instanceof CropsBlock) {

						IWorld world = e.getEntity().getEntityWorld();

						Map<BlockPos,Boolean> leaveRoof = new HashMap<BlockPos, Boolean>();
						
						int level = enchantments.get(EnchantmentInit.CROPSHARVEST_CARD_ENCHANTMENT.get());
						
						getCrops(blockType, pos, pos, world, leaveRoof, level == 2);

						cropsQue.add(leaveRoof);
					}
				}
			}
		}

		@SubscribeEvent
		public static void onTick(WorldTickEvent e) {

			if (e.world.isRemote)
				return;

			ArrayList<Map<BlockPos,Boolean>> temp = new ArrayList<>();

			for (Map<BlockPos,Boolean> que : cropsQue) {

				if (que.isEmpty()) {
					temp.add(que);
					continue;
				}

				if (new Random().nextFloat() < 0.5)
					return;
				
				BlockPos pos = que.keySet().iterator().next();
				
				CropsBlock block = ((CropsBlock) e.world.getBlockState(pos).getBlock());
				if (block.getMaxAge() == e.world.getBlockState(pos).get(block.getAgeProperty())) {
					e.world.destroyBlock(pos, true);
					if(que.get(pos))e.world.setBlockState(pos, block.getDefaultState());
				}
				
				que.remove(pos);
			}
			cropsQue.removeAll(temp);
		}

		private static void getCrops(Block blockType, BlockPos start, BlockPos initial, IWorld world,
				Map<BlockPos,Boolean> list,boolean replace) {

			if (list.size() >= LISTCAP)
				return;

			if (Math.sqrt(Math.pow(start.getX() - initial.getX(), 2) + Math.pow(start.getY() - initial.getY(), 2)
					+ Math.pow(start.getZ() - initial.getZ(), 2)) > MAX_RADIUS)
				return;

			list.put(new BlockPos(start.getX(), start.getY(), start.getZ()),replace);

			for (BlockPos pos : BlockPos.getAllInBoxMutable(start.down().west().south(), start.up().east().north())) {

				if (!list.keySet().contains(pos) && world.getBlockState(pos).getBlock() == blockType) {
					getCrops(blockType, pos, initial, world, list,replace);
				}
			}
		}
	}
}
