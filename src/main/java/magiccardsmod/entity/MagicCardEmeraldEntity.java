package magiccardsmod.entity;

import magiccardsmod.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicCardEmeraldEntity extends AbstractCardEntity{
	
	public static final float DEFAULT_DAMAGE =0.25f;
		
	public MagicCardEmeraldEntity(EntityType<? extends MagicCardEmeraldEntity> type, World p_i48546_2_) {
		super(type, p_i48546_2_);
		
	}
	
	public MagicCardEmeraldEntity(World worldIn, LivingEntity throwerIn, ItemStack thrownCard) {
		super(EntityInit.MAGICCARDEMERALD_ENTTIY.get(), throwerIn,thrownCard,DEFAULT_DAMAGE,DEFAULT_CARD_VANISH_CHANCE, worldIn);
		
	}

	public MagicCardEmeraldEntity(World worldIn, double x, double y, double z) {
		super(EntityInit.MAGICCARDEMERALD_ENTTIY.get(), x, y, z, worldIn);
		
	}
}
