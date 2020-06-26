package magiccardsmod.entity;

import magiccardsmod.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicCardIronEntity extends AbstractCardEntity{
	
	public static final float DEFAULT_DAMAGE = 6;
	
	public MagicCardIronEntity(EntityType<? extends MagicCardIronEntity> type, World p_i48546_2_) {
		super(type, p_i48546_2_);

	}
	
	public MagicCardIronEntity(World worldIn, LivingEntity throwerIn, ItemStack thrownCard) {
		super(EntityInit.MAGICCARDIRON_ENTTIY.get(), throwerIn,thrownCard,DEFAULT_DAMAGE,0.09f, worldIn);
		
	}

	public MagicCardIronEntity(World worldIn, double x, double y, double z) {
		super(EntityInit.MAGICCARDIRON_ENTTIY.get(), x, y, z, worldIn);
		
	}
}
