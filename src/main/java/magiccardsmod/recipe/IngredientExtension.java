package magiccardsmod.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;

public class IngredientExtension {

	private boolean shouldBeInserted;
	private Ingredient ingredient;

	private boolean applySpecialCriterion;
	private String criterionID;

	private static Map<String, SpecialCritierion> criterionRegistry = new HashMap<String, IngredientExtension.SpecialCritierion>();

	public static final SpecialCritierion ORE = registerSpeicalCriterion("ore", new SpecialCritierion() {
		@Override
		public boolean test(ItemStack stack) {
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block != null && block instanceof OreBlock)
				return true;
			return false;
		}

		@SuppressWarnings("deprecation")
		@Override
		public List<ItemStack> getIngredients() {

			List<ItemStack> stacks = new ArrayList<ItemStack>();

			for (Block block : ForgeRegistries.BLOCKS.getValues())
				if (block instanceof OreBlock)
					stacks.add(new ItemStack(Item.getItemFromBlock(block)));

			return stacks;
		}
	});

	public static final SpecialCritierion POTION = registerSpeicalCriterion("potion", new SpecialCritierion() {
		@Override
		public boolean test(ItemStack stack) {
			if (stack.getItem() instanceof PotionItem)
				return true;
			return false;
		}

		@Override
		public List<ItemStack> getIngredients() {
			List<ItemStack> stacks = new ArrayList<ItemStack>();

			for (Item item : ForgeRegistries.ITEMS)

					if (item instanceof PotionItem) {
						
						NonNullList<ItemStack> list = NonNullList.create();
						item.fillItemGroup(item.getGroup(), list);
						
						stacks.addAll(list);
					}
			return stacks;
		}
	});

	public IngredientExtension(Ingredient ingredient, boolean shouldBeInserted) {
		this.ingredient = ingredient;
		this.shouldBeInserted = shouldBeInserted;
	}

	public IngredientExtension(String criterionID, boolean shouldBeInserted) {
		setApplySpecialCriterion(true);
		this.setCriterionID(criterionID);
		this.shouldBeInserted = shouldBeInserted;
	}

	public static SpecialCritierion registerSpeicalCriterion(String id, SpecialCritierion criterion) {
		criterionRegistry.put(id, criterion);
		return criterion;
	}

	public boolean test(ItemStack stack) {

		if (applySpecialCriterion)
			return criterionRegistry.get(criterionID).test(stack);
		else
			return ingredient.test(stack);
	}

	public boolean isShouldBeInserted() {
		return shouldBeInserted;
	}

	public void setShouldBeInserted(boolean shouldBeInserted) {
		this.shouldBeInserted = shouldBeInserted;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public List<ItemStack> getCriterionIngridients() {
		return criterionRegistry.get(criterionID).getIngredients();
	}

	public String getCriterionID() {
		return criterionID;
	}

	public void setCriterionID(String criterionID) {
		this.criterionID = criterionID;
	}

	public boolean isApplySpecialCriterion() {
		return applySpecialCriterion;
	}

	public void setApplySpecialCriterion(boolean applySpecialCriterion) {
		this.applySpecialCriterion = applySpecialCriterion;
	}

	public interface SpecialCritierion {

		public boolean test(ItemStack stack);

		public List<ItemStack> getIngredients();
	}
}
