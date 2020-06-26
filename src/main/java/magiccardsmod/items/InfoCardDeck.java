package magiccardsmod.items;

import magiccardsmod.container.InfoCardDeckContainer;
import magiccardsmod.itemgroups.ModItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class InfoCardDeck extends Item implements INamedContainerProvider{
	
	public InfoCardDeck() {
		super(new Item.Properties().group(ModItemGroup.INSTANCE));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		
		if(worldIn.isRemote)return ActionResult.newResult(ActionResultType.PASS, playerIn.getHeldItem(handIn));
		
		NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider)playerIn.getHeldItem(handIn).getItem());
		
		return ActionResult.newResult(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
		
		return new InfoCardDeckContainer(id, playerInv);
	}

	@Override
	public ITextComponent getDisplayName() {
		
		return new TranslationTextComponent("infocarddeck");
	}
}
