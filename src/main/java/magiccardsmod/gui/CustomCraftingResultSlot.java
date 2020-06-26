package magiccardsmod.gui;

import magiccardsmod.init.EnchantmentInit;
import magiccardsmod.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;

public class CustomCraftingResultSlot extends CraftingResultSlot {

	public CustomCraftingResultSlot(PlayerEntity player, CraftingInventory craftingInventory, IInventory inventoryIn,
			int slotIndex, int xPosition, int yPosition) {

		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);

	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return EnchantmentInit.isMagicCard(stack.getItem()) || stack.getItem() == ItemInit.MAGIC_CARD_DIAMOND.get()
				|| stack.getItem() == ItemInit.MAGIC_CARD_EMERALD.get();
	}

	@Override
	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {

		return stack;
	}
}
