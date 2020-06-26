package magiccardsmod.entity;

import java.util.Random;

import magiccardsmod.enchantments.Fly_CardEnchantment;
import magiccardsmod.enchantments.Gather_CardEnchantment;
import magiccardsmod.enchantments.IceWalk_CardEnchantment;
import magiccardsmod.enchantments.OreSeeker_CardEnchantment;
import magiccardsmod.init.ItemInit;
import magiccardsmod.init.SoundInit;
import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class AbstractCardEntity extends AbstractArrowEntity {

	public static final float DEFAULT_CARD_VANISH_CHANCE = 0.2f;
	private float vanishChance;

	private static final DataParameter<ItemStack> ORE = EntityDataManager.createKey(AbstractCardEntity.class,
			DataSerializers.ITEMSTACK);

	private static final DataParameter<ItemStack> DISC = EntityDataManager.createKey(AbstractCardEntity.class,
			DataSerializers.ITEMSTACK);

	private static final DataParameter<ItemStack> POTION = EntityDataManager.createKey(AbstractCardEntity.class,
			DataSerializers.ITEMSTACK);

	private static final DataParameter<ItemStack> THROWN_CARD = EntityDataManager.createKey(AbstractCardEntity.class,
			DataSerializers.ITEMSTACK);

	private static final DataParameter<CompoundNBT> CAPTURED_ENTITY = EntityDataManager
			.createKey(AbstractCardEntity.class, DataSerializers.COMPOUND_NBT);

	private float vanishException;
	private boolean sheduledVanishException;
	private int curPriority;
	private final float DEFAULT_DAMAGE;

	private BlockPos soundPosition;

	private boolean pierce;

	protected AbstractCardEntity(EntityType<? extends AbstractArrowEntity> type, World p_i48546_2_) {
		super(type, p_i48546_2_);
		DEFAULT_DAMAGE = 0.25f;
		this.setDamage(DEFAULT_DAMAGE);
	}

	public AbstractCardEntity(EntityType<? extends AbstractCardEntity> entityType, double x, double y, double z,
			World worldIn) {
		super(entityType, x, y, z, worldIn);
		DEFAULT_DAMAGE = 0.25f;
		this.setDamage(DEFAULT_DAMAGE);

	}

	public AbstractCardEntity(EntityType<? extends AbstractCardEntity> entityType, LivingEntity throwerIn,
			ItemStack thrownCard, float defaultDamage, float vanishChance, World worldIn) {
		super(entityType, throwerIn, worldIn);
		
		DEFAULT_DAMAGE = defaultDamage;

		this.setDamage(DEFAULT_DAMAGE);
		this.vanishChance = vanishChance;
		ItemStack card = thrownCard.copy();
		card.setCount(1);
		
		
		
		this.getDataManager().set(THROWN_CARD, card);
	}
	
	private void insertStack(ItemStack stack) {
		
		if(stack.getItem() instanceof PotionItem)
			setPotion(stack);
		else if(stack.getItem() instanceof MusicDiscItem)
			setDisc(stack);
		else if(Block.getBlockFromItem(stack.getItem()) instanceof OreBlock)
			setOre(stack);
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(POTION, ItemStack.EMPTY);
		this.getDataManager().register(THROWN_CARD, ItemStack.EMPTY);
		this.getDataManager().register(DISC, ItemStack.EMPTY);
		this.getDataManager().register(ORE, ItemStack.EMPTY);
		this.getDataManager().register(CAPTURED_ENTITY, new CompoundNBT());
		super.registerData();
	}
	
	@Override
	public void onAddedToWorld() {
		
		for(String s : getThrownCard().getOrCreateTag().keySet())
			if(s.contains("arg"))
				insertStack(ItemStack.read(getThrownCard().getTag().getCompound(s)));
		
		super.onAddedToWorld();
	}
	
	@Override
	public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
		super.shoot(shooter, pitch, yaw, p_184547_4_, velocity, inaccuracy);
		
		Fly_CardEnchantment.applyFly(this);
	}
	
	private int ticksInRain;
	@Override
	public void tick() {
		
		Block currentBlock = (world.getBlockState(getPosition()).getBlock());
		
		if (isBurning() || currentBlock instanceof FireBlock) {
			
			this.playSound(SoundInit.CARD_BURN_SOUND.get(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			if (!this.world.isRemote) {

				removeMe();
			}
		}else if(this.isInWaterOrBubbleColumn()) {
			
			becomeWet();
		}else if(this.isInRain()) {
			ticksInRain++;
			if(ticksInRain>9)becomeWet();
		}else ticksInRain--;
		
		IceWalk_CardEnchantment.applyIceWalk(this);
		OreSeeker_CardEnchantment.applyOreSeeker(this);
		Gather_CardEnchantment.applyGathering(this);
		super.tick();
	}
	
	/**
	 * Just a copy of the same called and for some reason private method in Entity
	 * 
	 * @return
	 */
	private boolean isInRain() {
		
		boolean flag;
	      try (BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this)) {
	         flag = this.world.isRainingAt(blockpos$pooledmutableblockpos) || this.world.isRainingAt(blockpos$pooledmutableblockpos.setPos(this.posX, this.posY + (double)this.getHeight(), this.posZ));
	      }
	      return flag;
	}
	
	private void becomeWet() {
		ItemStack stack = getWetEquivalent(getThrownCard());
		
		stack.setTag(getThrownCard().getTag());
		
		if (!this.world.isRemote) {
			InventoryHelper.spawnItemStack(world, posX,
					posY, posZ, stack);
			removeMe();
		}
	}
	
	private ItemStack getWetEquivalent(ItemStack card) {
		
		if(card.getItem()==ItemInit.MAGIC_CARD.get())return new ItemStack(ItemInit.WETCARD.get());
		else if(card.getItem()==ItemInit.MAGIC_CARD_IRON.get())return new ItemStack(ItemInit.WETCARD_IRON.get());
		else if(card.getItem()==ItemInit.MAGIC_CARD_GOLD.get())return new ItemStack(ItemInit.WETCARD_GOLD.get());
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public void onHit(RayTraceResult raytraceResultIn) {

		if (pierce) {
			handlePierceException(raytraceResultIn);
			return;
		}

		if (world.isRemote) {
			super.onHit(raytraceResultIn);
			return;
		}

		if (raytraceResultIn.getType() == RayTraceResult.Type.BLOCK
				&& new Random().nextFloat() < (sheduledVanishException ? vanishException : vanishChance)) {

			this.setHitSound(SoundInit.CARD_RIP_SOUND.get());

			removeMe();
		} else
			this.setHitSound(SoundInit.CARD_HIT_SOUND.get());

		super.onHit(raytraceResultIn);

		sheduledVanishException = false;

		// After enchantments applied extra dmg reset it
		setDamage(DEFAULT_DAMAGE);

	}

	public boolean isInGround() {
		return inGround;
	}

	private void handlePierceException(RayTraceResult raytraceResultIn) {

		pierce = false;
		applyDamageWithoutRemove(raytraceResultIn);
		setDamage(DEFAULT_DAMAGE);
		sheduledVanishException = false;
	}

	private void applyDamageWithoutRemove(RayTraceResult raytraceResultIn) {

		Entity target = ((EntityRayTraceResult) raytraceResultIn).getEntity();

		Entity shooter = getShooter();

		DamageSource damagesource;

		if (shooter == null) {
			damagesource = DamageSource.causeArrowDamage((AbstractArrowEntity) this, this);
		} else {
			damagesource = DamageSource.causeArrowDamage((AbstractArrowEntity) this, shooter);
			if (shooter instanceof LivingEntity) {
				((LivingEntity) shooter).setLastAttackedEntity(target);
			}
		}

		target.attackEntityFrom(damagesource, (float) ((AbstractCardEntity) this).getDamage());
	}

	public void removeMe() {

		if (world.isRemote)
			return;
		if (soundPosition != null)
			world.playEvent(1010, getSoundPosition(), 0);
		
		Fly_CardEnchantment.stoppFly(this);
		
		this.remove();
		this.getEntityWorld().setEntityState(this, (byte) 3);
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity entityIn) {
		super.onCollideWithPlayer(entityIn);
		if (!isAlive())
			removeMe();
	}

	public void sheduleVanishChanceException(float newChance, int priority) {
		if(priority<=curPriority)return;
		
		curPriority=priority;
		vanishException = newChance;
		sheduledVanishException = true;
	}

	public ItemStack getPotion() {

		ItemStack itemstack = this.getDataManager().get(POTION);
		if (itemstack.getItem() != Items.SPLASH_POTION && itemstack.getItem() != Items.LINGERING_POTION
				&& itemstack.getItem() != Items.POTION) {

			return ItemStack.EMPTY;
		} else {
			return itemstack;
		}
	}

	public void setPotion(ItemStack stack) {
		this.getDataManager().set(POTION, stack.copy());
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);

		setItem(getThrownCard(), "card", compound);
		setItem(getPotion(), "potion", compound);
		setItem(getDisc(), "disc", compound);
		setItem(getOre(), "ore", compound);
		compound.put("capturedentity",this.getDataManager().get(CAPTURED_ENTITY));
	}

	private void setItem(ItemStack stack, String name, CompoundNBT compound) {

		if (stack != null && !stack.isEmpty()) {

			CompoundNBT nbtPotion = new CompoundNBT();

			stack.write(nbtPotion);

			compound.put(name, nbtPotion);
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		setThrownCard(ItemStack.read(compound.getCompound("card")));

		if (compound.contains("potion")) {
			setPotion(ItemStack.read(compound.getCompound("potion")));
		}

		if (compound.contains("disc")) {
			setDisc(ItemStack.read(compound.getCompound("disc")));
		}

		if (compound.contains("ore")) {
			setOre(ItemStack.read(compound.getCompound("ore")));
		}
		if (compound.contains("capturedentity")) 
		this.getDataManager().set(CAPTURED_ENTITY,compound.getCompound("capturedentity"));
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	private void setThrownCard(ItemStack stack) {

		this.getDataManager().set(THROWN_CARD, stack.copy());
	}

	public ItemStack getThrownCard() {
		return this.getDataManager().get(THROWN_CARD);
	}

	public void setDisc(ItemStack stack) {
		if (stack.getItem() instanceof MusicDiscItem)
			this.getDataManager().set(DISC, stack.copy());
	}

	public ItemStack getDisc() {
		return this.getDataManager().get(DISC);
	}

	public void setOre(ItemStack stack) {

		this.getDataManager().set(ORE, stack.copy());
	}

	public void setOre(Block ore) {

		if (ore instanceof OreBlock) {

			this.getDataManager().set(ORE, new ItemStack(ore));
		}
	}

	public ItemStack getOre() {
		return this.getDataManager().get(ORE);
	}

	@Override
	protected ItemStack getArrowStack() {
		
		ItemStack stack = getThrownCard();
		
		if(stack.getOrCreateTag().isEmpty())
			stack.setTag(null);
		
		return stack;
	}

	public float getDEFAULT_DAMAGE() {
		return DEFAULT_DAMAGE;
	}
	
	public float getDefaultVanishChance() {
		return vanishChance;
	}
	
	public float getVanishChance() {
		return (sheduledVanishException ? vanishException : vanishChance);
	}

	public BlockPos getSoundPosition() {
		return soundPosition;
	}

	public void setSoundPosition(BlockPos soundPosition) {
		this.soundPosition = soundPosition;
	}

	public boolean isPierce() {
		return pierce;
	}

	public void setPierce(boolean pierce) {
		this.pierce = pierce;
	}

	public void setCapturedEntity(Entity entity) {

		entity.writeUnlessRemoved(this.getDataManager().get(CAPTURED_ENTITY));
	}

	public Entity getCapturedEntity() {

		return EntityType.func_220335_a(this.getDataManager().get(CAPTURED_ENTITY), world, (p_222655_1_) -> {
			world.addEntity(p_222655_1_);
			return p_222655_1_;
		});
	}
	
	public void setCapturedEntityNBT(CompoundNBT nbt) {
		this.getDataManager().set(CAPTURED_ENTITY, nbt);
	}
	
	public CompoundNBT getCapturedEntityNBT() {
		return this.getDataManager().get(CAPTURED_ENTITY);
	}
}
