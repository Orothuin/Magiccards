package magiccardsmod.enchantments;

import java.util.Map;

import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class IceWalk_CardEnchantment extends Enchantment {

	public IceWalk_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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
	
	public static void applyIceWalk(Entity entity) {

		if (entity instanceof AbstractCardEntity&&!((AbstractCardEntity) entity).isInGround()) {

			if (entity.getEntityWorld().isRemote)
				return;

			ItemStack card = ((AbstractCardEntity) entity).getThrownCard();

			if (!card.isEnchanted())
				return;

			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

			if (enchantments.containsKey(EnchantmentInit.ICEWALK_CARD_ENCHANTMENT.get())) {
				
				for(BlockPos pos : BlockPos.getAllInBoxMutable(entity.getPosition().west().south(),entity.getPosition().east().north())){
					rayTraceAnPlace(pos, entity.getEntityWorld());
				}
			}
		}
	}
	
	private static void rayTraceAnPlace(BlockPos pos,IWorld world) {
		
		for(int y=0;y<10;y++) {
			
			if(world.getFluidState(pos.down(y)).getFluid()==Fluids.WATER) {
				world.setBlockState(pos.down(y), Blocks.FROSTED_ICE.getDefaultState(), 2);
				break;
			}		
		}
	}
}
