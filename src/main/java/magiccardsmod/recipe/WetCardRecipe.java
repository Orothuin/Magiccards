package magiccardsmod.recipe;

import java.util.Map;

import magiccardsmod.init.ItemInit;
import magiccardsmod.init.RecipeSerializerInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WetCardRecipe extends FurnaceRecipe {

	public WetCardRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn,
			float experienceIn, int cookTimeIn) {

		super(idIn, groupIn, Ingredient.fromItems(ItemInit.WETCARD.get(),ItemInit.WETCARD_IRON.get(),ItemInit.WETCARD_GOLD.get()), new ItemStack(ItemInit.MAGIC_CARD.get()),
				experienceIn, 70);
	}

	private static ItemStack ingredientItemStack;

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		boolean ingriedientsMatches;
		
		if (ingriedientsMatches = super.matches(inv, worldIn))
			ingredientItemStack = inv.getStackInSlot(0);

		boolean enchantmenstMatches = true;

		if (!inv.getStackInSlot(0).isEmpty() && !inv.getStackInSlot(2).isEmpty()) {

			Map<Enchantment, Integer> enchantments1 = EnchantmentHelper.getEnchantments(inv.getStackInSlot(0));
			Map<Enchantment, Integer> enchantments2 = EnchantmentHelper.getEnchantments(inv.getStackInSlot(2));

			if (!enchantments1.keySet().containsAll(enchantments2.keySet())
					|| !enchantments2.keySet().containsAll(enchantments1.keySet()))
				enchantmenstMatches = false;

		}
		return ingriedientsMatches && enchantmenstMatches;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(IInventory inv) {

		return super.getRemainingItems(inv);
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {

		return super.getCraftingResult(inv);
	}

	@Override
	public ItemStack getRecipeOutput() {

		ItemStack stack = ingredientItemStack!=null ? getDryEquivalent(ingredientItemStack) : ItemStack.EMPTY;

		if (ingredientItemStack != null && !ingredientItemStack.equals(ItemStack.EMPTY)) {

			stack.setTag(ingredientItemStack.getTag());
		}

		return stack;
	}
	
	private ItemStack getDryEquivalent(ItemStack stack) {
		
		if(stack.getItem()==ItemInit.WETCARD.get())return new ItemStack(ItemInit.MAGIC_CARD.get());
		else if(stack.getItem()==ItemInit.WETCARD_IRON.get())return new ItemStack(ItemInit.MAGIC_CARD_IRON.get());
		else if(stack.getItem()==ItemInit.WETCARD_GOLD.get())return new ItemStack(ItemInit.MAGIC_CARD_GOLD.get());
		
		return ItemStack.EMPTY;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {

		return RecipeSerializerInit.DRY_CARD.get();
	}
}
