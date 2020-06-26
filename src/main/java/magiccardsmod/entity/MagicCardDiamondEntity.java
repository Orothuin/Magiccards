package magiccardsmod.entity;

import magiccardsmod.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicCardDiamondEntity extends AbstractCardEntity{
	
	public static final float DEFAULT_DAMAGE =0.25f;
		
	public MagicCardDiamondEntity(EntityType<? extends MagicCardDiamondEntity> type, World p_i48546_2_) {
		super(type, p_i48546_2_);
		
	}
	
	public MagicCardDiamondEntity(World worldIn, LivingEntity throwerIn, ItemStack thrownCard) {
		super(EntityInit.MAGICCARDDIAMOND_ENTTIY.get(), throwerIn,thrownCard,DEFAULT_DAMAGE,DEFAULT_CARD_VANISH_CHANCE, worldIn);
		
	}

	public MagicCardDiamondEntity(World worldIn, double x, double y, double z) {
		super(EntityInit.MAGICCARDDIAMOND_ENTTIY.get(), x, y, z, worldIn);
		
	}
}
