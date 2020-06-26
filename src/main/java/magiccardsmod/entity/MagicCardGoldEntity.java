package magiccardsmod.entity;

import magiccardsmod.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicCardGoldEntity extends AbstractCardEntity {

	public static final float DEFAULT_DAMAGE = 4;

	public MagicCardGoldEntity(EntityType<? extends MagicCardGoldEntity> type, World p_i48546_2_) {
		super(type, p_i48546_2_);

	}

	public MagicCardGoldEntity(World worldIn, LivingEntity throwerIn, ItemStack thrownCard) {
		super(EntityInit.MAGICCARDGOLD_ENTTIY.get(), throwerIn, thrownCard, DEFAULT_DAMAGE,0.19f, worldIn);

	}

	public MagicCardGoldEntity(World worldIn, double x, double y, double z) {
		super(EntityInit.MAGICCARDGOLD_ENTTIY.get(), x, y, z, worldIn);

	}
}
