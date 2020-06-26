package magiccardsmod.enchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class Smelt_CardEnchantment extends Enchantment {
	
	private static Map<Entity,Boolean> hasSmeltedOnce = new HashMap<>();
	
	public Smelt_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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

	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {

		private static Block currentTarget;
		private static ItemStack result;
		
		private static Map<BlockPos,Integer> targeted = new HashMap<>();
		
		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType() == RayTraceResult.Type.BLOCK) {

				if (e.getEntity().getEntityWorld().isRemote)
					return;
				
				if(hasSmeltedOnce.containsKey(e.getEntity()))return;
				
				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();

				if (!card.isEnchanted())
					return;

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.SMELT_CARD_ENCHANTMENT.get())) {

					BlockPos pos = ((BlockRayTraceResult) e.getRayTraceResult()).getPos();
					
					int level = enchantments.get(EnchantmentInit.SMELT_CARD_ENCHANTMENT.get());
					
					hasSmeltedOnce.put(e.getEntity(), true);
					
					targeted.put(pos,level);
				}
			}
		}
		
		@SubscribeEvent
		public static void onTick(WorldTickEvent e) {
			
			if(e.world.isRemote)return;
			
			for(BlockPos pos : targeted.keySet())
				smeltBlock(e.world, pos);
			
			targeted.clear();
		}
		
		private static void smeltBlock(World world, BlockPos pos) {
			
			currentTarget = world.getBlockState(pos).getBlock();

			for (IRecipe<?> recipe : world.getRecipeManager().getRecipes())
				if (searchRecepies(recipe))
					break;

			if (result != null && !result.isEmpty()) {
				
				int level = targeted.get(pos);
				
				if(new Random().nextFloat()<0.1*level)
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(),
							pos.getZ(), result.copy());
				
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(),
						pos.getZ(), result.copy());

				world.destroyBlock(pos, false);

				world.addParticle(ParticleTypes.FLAME, pos.getX(), pos.getY(),
						pos.getZ(), 0.0D, 0.0D, 0.0D);

				world.playSound(pos.getX(), pos.getY(), pos.getZ(),
						SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
				
				world.notifyBlockUpdate(pos, currentTarget.getDefaultState(), Blocks.AIR.getDefaultState(), 2);
			}

			result = null;
		}
		
		@SuppressWarnings({ "deprecation" })
		private static boolean searchRecepies(IRecipe<?> recepie) {

			if (recepie.getType() != IRecipeType.SMELTING)
				return false;

			for (Ingredient ingredient : recepie.getIngredients())

				for (ItemStack stack : ingredient.getMatchingStacks())

					if (Item.getItemFromBlock(currentTarget) == stack.getItem()
							&& recepie.getIngredients().size() == 1) {
						result = recepie.getRecipeOutput();
						return true;
					}
			return false;
		}
		
		
	}
}
