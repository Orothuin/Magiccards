package magiccardsmod.items;

import java.util.List;
import java.util.Map;

import magiccardsmod.MagiccardItemInformationUtil;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.entity.MagicCardEntity;
import magiccardsmod.init.EnchantmentInit;
import magiccardsmod.itemgroups.ModItemGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class MagicCardItem extends Item {

	public MagicCardItem() {

		super(new Properties().maxStackSize(16).group(ModItemGroup.INSTANCE));
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack =super.getDefaultInstance();
		stack.getOrCreateTag();
		return stack;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

		float dmg = (MagicCardEntity.DEFAULT_DAMAGE + getEnchExtraDamage(stack));

		tooltip.add(new TranslationTextComponent("When in hand").setStyle(new Style().setColor(TextFormatting.GRAY)));
		if (dmg > 1)
			tooltip.add(new TranslationTextComponent((int) dmg + " Attack Damage")
					.setStyle(new Style().setColor(TextFormatting.GREEN)));
		else
			tooltip.add(new TranslationTextComponent(dmg + " Attack Damage")
					.setStyle(new Style().setColor(TextFormatting.GREEN)));

		MagiccardItemInformationUtil.getInformation(stack, worldIn, tooltip, flagIn);
		
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	public int getEnchExtraDamage(ItemStack stack) {

		if (!stack.isEnchanted())
			return 0;

		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

		if (enchantments.containsKey(EnchantmentInit.SHARPEDGES_CARD_ENCHANTMENT.get())) {
			return enchantments.get(EnchantmentInit.SHARPEDGES_CARD_ENCHANTMENT.get());
		}
		return 0;
	}

	

	public AbstractCardEntity getProjectileEntity(World worldIn, PlayerEntity playerIn, ItemStack thrownCard) {

		return new MagicCardEntity(worldIn, playerIn, thrownCard);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (!playerIn.abilities.isCreativeMode) {
			itemstack.shrink(1);
		}

		worldIn.playSound((PlayerEntity) null, playerIn.posX, playerIn.posY, playerIn.posZ,
				SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
				0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote) {

			AbstractArrowEntity entity = getProjectileEntity(worldIn, playerIn, itemstack);

			// ((AbstractCardEntity)entity).setDisc(playerIn.getItemStackFromSlot(EquipmentSlotType.OFFHAND));

			((AbstractCardEntity) entity).setOre(playerIn.getItemStackFromSlot(EquipmentSlotType.OFFHAND));

			entity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 1.0F);
			worldIn.addEntity(entity);
		}

		playerIn.addStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	}

}
