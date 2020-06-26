package magiccardsmod.entity;

import magiccardsmod.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicCardEntity extends AbstractCardEntity{
	
	public static final float DEFAULT_DAMAGE =0.25f;
		
	public MagicCardEntity(EntityType<? extends MagicCardEntity> type, World p_i48546_2_) {
		super(type, p_i48546_2_);
		
	}
	
	public MagicCardEntity(World worldIn, LivingEntity throwerIn, ItemStack thrownCard) {
		super(EntityInit.MAGICCARD_ENTTIY.get(), throwerIn,thrownCard,DEFAULT_DAMAGE,DEFAULT_CARD_VANISH_CHANCE, worldIn);
		
	}

	public MagicCardEntity(World worldIn, double x, double y, double z) {
		super(EntityInit.MAGICCARD_ENTTIY.get(), x, y, z, worldIn);
		
	}
}
