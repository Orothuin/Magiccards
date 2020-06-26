package magiccardsmod.container;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import magiccardsmod.gui.CustomCraftingResultSlot;
import magiccardsmod.init.BlockInit;
import magiccardsmod.init.ContainerInit;
import magiccardsmod.networking.SetEnchantmentResultMessage;
import magiccardsmod.networking.UpdateEnchantedItemMessage;
import magiccardsmod.recipe.EnchantRecipe;
import magiccardsmod.recipe.EnchantRecipeType;
import magiccardsmod.tileentitys.CardTableTileEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class CardTableContainer extends Container {

	private final IWorldPosCallable canInteractWithCallable;
	private final CardTableTileEntity tile;
	private final CraftingInventory craftingInv = new CraftingInventory(this, 3, 3);
	public final CraftResultInventory resultInv = new CraftResultInventory() {
		@Override
		public ItemStack removeStackFromSlot(int index) {
			ItemStack stack = super.removeStackFromSlot(index);
			onCraftMatrixChanged(craftingInv);
			return stack;
		}
		
		@Override
		public ItemStack decrStackSize(int index, int count) {
			ItemStack stack = super.decrStackSize(index, count);
			onCraftMatrixChanged(craftingInv);
			return stack;
		}
		
		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			
			super.setInventorySlotContents(index, stack);
			onCraftMatrixChanged(craftingInv);
		}
	};
	private PlayerEntity player;

	public Enchantment selectedEnchantment;
	public int level;

	public CardTableContainer(int GuiId, final CardTableTileEntity tile, PlayerEntity player) {

		super(ContainerInit.CARDTABLE_TYPE.get(), GuiId);

		this.canInteractWithCallable = IWorldPosCallable.of(tile.getWorld(), tile.getPos());

		setPlayer(player);
		initSlots(player.inventory);

		this.tile = tile;
	}

	private void applyEnchantment() {

		if (tile.getWorld().isRemote)
			return;

		Optional<EnchantRecipe> optional = tile.getWorld().getServer().getRecipeManager()
				.getRecipe(EnchantRecipeType.ENCHANT, craftingInv, tile.getWorld());

		if (optional.isPresent()) {

			EnchantRecipe enchantmentrecipe = optional.get();
			
			if(enchantmentrecipe.getTableLevel()>tile.getTableLevel())return;
			
			Map<Enchantment, Integer> enchantmentsPre = EnchantmentHelper.getEnchantments(resultInv.getStackInSlot(0));
			int levelPre = enchantmentsPre.containsKey(enchantmentrecipe.enchantment) ? enchantmentsPre.get(enchantmentrecipe.enchantment) : 0;
			
			resultInv.setInventorySlotContents(0, enchantmentrecipe.getCraftingResult(resultInv));

			Map<Enchantment, Integer> enchantmentsPost = EnchantmentHelper.getEnchantments(resultInv.getStackInSlot(0));
			int levelPost =  enchantmentsPost.containsKey(enchantmentrecipe.enchantment) ? enchantmentsPost.get(enchantmentrecipe.enchantment) : 0;
			
			if (!enchantmentsPre.keySet().containsAll(enchantmentsPost.keySet())||levelPre!=levelPost) {
				for (int i = 0; i < craftingInv.getSizeInventory(); i++)
					craftingInv.decrStackSize(i, 1);

				detectAndSendChanges();
				
				magiccardsmod.networking.PacketHandler.INSTANCE.send(
						PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.player),
						new UpdateEnchantedItemMessage(resultInv.getStackInSlot(0)));
			}
		}
	}

	@Override
	public boolean enchantItem(PlayerEntity playerIn, int id) {

		if (id == 10)
			applyEnchantment();
		return super.enchantItem(playerIn, id);
	}

	protected static void checkRecipe(int windowID, World world, PlayerEntity player, CraftingInventory craftingInv,
			CraftResultInventory resultInf, CardTableContainer container) {

		if (!world.isRemote) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
			Optional<EnchantRecipe> optional = world.getServer().getRecipeManager().getRecipe(EnchantRecipeType.ENCHANT,
					craftingInv, world);
			Enchantment ench = null;
			int level = 0;
			if (optional.isPresent()) {
				EnchantRecipe enchantmentrecipe = optional.get();
				if (resultInf.canUseRecipe(world, serverplayerentity, enchantmentrecipe)) {
					ench = enchantmentrecipe.enchantment;
					level = enchantmentrecipe.getLevel(resultInf.getStackInSlot(0));
				}
				
				if(enchantmentrecipe.getTableLevel()>container.tile.getTableLevel())level =-1;
			}
			container.selectedEnchantment = ench;
			container.level = level;
			
			
			
			magiccardsmod.networking.PacketHandler.INSTANCE.send(
					PacketDistributor.PLAYER.with(() -> serverplayerentity),
					new SetEnchantmentResultMessage(ench, windowID, level));
		}
	}

	/**
	 * called in CraftingInventory
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {

		this.canInteractWithCallable.consume((p_217069_1_, p_217069_2_) -> {
			checkRecipe(this.windowId, p_217069_1_, this.player, craftingInv, resultInv, this);
		});

	}

	public CardTableContainer(int GuiId, final PlayerInventory playerInv, final PacketBuffer buffer) {

		this(GuiId, getTileEntity(playerInv, buffer), playerInv.player);
	}

	private void initSlots(PlayerInventory playerInv) {

		this.addSlot(new CustomCraftingResultSlot(playerInv.player, craftingInv, resultInv, 0, 14, 17));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(craftingInv, j + i * 3, 110 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInv, i1 + k * 9 + 9, 8 + i1 * 18, 96 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInv, l, 8 + l * 18, 154));
		}
	}

	public static CardTableTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer buffer) {

		Objects.requireNonNull(playerInv, "playerinventory must not be null");
		Objects.requireNonNull(buffer, "Packetbuffer must not be null");

		final TileEntity tileAtPos = playerInv.player.world.getTileEntity(buffer.readBlockPos());

		if (tileAtPos instanceof CardTableTileEntity)
			return (CardTableTileEntity) tileAtPos;
		throw new IllegalStateException(
				"TileEntity at" + tileAtPos.getPos() + " is no instance of " + CardTableTileEntity.class);
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);

		this.canInteractWithCallable.consume((world, pos) -> {
			this.clearContainer(playerIn, world, craftingInv);
		});
		this.canInteractWithCallable.consume((world, pos) -> {
			this.clearContainer(playerIn, world, resultInv);
		});
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {
				this.canInteractWithCallable.consume((p_217067_2_, p_217067_3_) -> {
					itemstack1.getItem().onCreated(itemstack1, p_217067_2_, playerIn);
				});
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != craftingInv && super.canMergeSlot(stack, slotIn);
	}

	@Override
	public boolean getCanCraft(PlayerEntity player) {

		return true;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {

		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.CARD_TABLE.get());
	}

	public CardTableTileEntity getTile() {
		return tile;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}
}
