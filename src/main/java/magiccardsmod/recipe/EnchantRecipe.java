package magiccardsmod.recipe;

import java.util.ArrayList;
import java.util.Map;

import magiccardsmod.init.RecipeSerializerInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EnchantRecipe implements IRecipe<IInventory> {

	public NonNullList<IngredientExtension> ingredients;
	public Enchantment enchantment;
	private int level;
	private ResourceLocation id;
	private int stackableUntilLevel=0;
	private int canApplyAtLevel = 0;
	private int tableLevel=1;
	
	public EnchantRecipe(ResourceLocation id, NonNullList<IngredientExtension> ingredients, Enchantment p_create_4_,
			int level,int stackableUntilLevel,int canApplyAtLevel, int tableLevel) {
		this(id, ingredients, p_create_4_, level,tableLevel);
		this.stackableUntilLevel=stackableUntilLevel;
		this.setCanApplyAtLevel(canApplyAtLevel);
		this.setTableLevel(tableLevel);
	}
	
	public EnchantRecipe(ResourceLocation id, NonNullList<IngredientExtension> ingredients, Enchantment p_create_4_,
			int level, int tableLevel) {
		this.id = id;
		this.ingredients = ingredients;
		this.enchantment = p_create_4_;
		this.level = level;
		this.setTableLevel(tableLevel);
	}

	private ArrayList<ItemStack> toInsert = new ArrayList<>();

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		
		toInsert.clear();
		int i = 0;
		for (int j = 0; j < inv.getSizeInventory(); ++j) {
			ItemStack itemstack = inv.getStackInSlot(j);
			if (!itemstack.isEmpty()) {
				if (!belongsToRecipe(itemstack))
					return false;
				else
					i++;
			}
		}

		return ingredients.size() == i;
	}

	private boolean belongsToRecipe(ItemStack stack) {

		for (IngredientExtension i : ingredients)

			if (i.test(stack)) {
				if (i.isShouldBeInserted())
					toInsert.add(stack);
				return true;
			}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {

		ItemStack itemstack = inv.getStackInSlot(0);
		
		if (!itemstack.isEmpty()) {
			
			Map<Enchantment,Integer> enchs = EnchantmentHelper.getEnchantments(itemstack);
			Integer curlevel = enchs.get(enchantment);
			
			if(curlevel!=null&&curlevel<canApplyAtLevel) return itemstack.copy();
			
			if(curlevel!=null&&curlevel>0&&curlevel<stackableUntilLevel) {
				
				insertItemsInStack(itemstack);
				enchs.put(enchantment, curlevel+1);
				EnchantmentHelper.setEnchantments(enchs, itemstack);
								
			} else if ((enchantment.canApply(itemstack)) && EnchantmentHelper
					.areAllCompatibleWith(EnchantmentHelper.getEnchantments(itemstack).keySet(), enchantment)) {
				
				insertItemsInStack(itemstack);
				
				itemstack.addEnchantment(enchantment, level);
			}
		}
		return itemstack.copy();
	}
	private void insertItemsInStack(ItemStack itemstack) {
		
		CompoundNBT nbt = itemstack.getOrCreateTag();
		
		for(int i=0;i<toInsert.size();i++) {
			CompoundNBT compund = new CompoundNBT();
			toInsert.get(i).write(compund);
			nbt.put("arg"+i+nbt.keySet().size(), compund);
		}
	}

	@Override
	public boolean canFit(int width, int height) {

		if (width * height == 9)
			return true;

		return false;
	}
	
	public int getLevel(ItemStack itemstack) {
		
		if (itemstack.isEmpty())return Math.max(level, canApplyAtLevel);
		
		Integer curlevel = EnchantmentHelper.getEnchantments(itemstack).get(enchantment);
		
		if(curlevel!=null&&curlevel<canApplyAtLevel)return -1;
		
		if(curlevel!=null&&curlevel>0&&curlevel<stackableUntilLevel) {
			
			return curlevel+1;
		}
		
		if(curlevel==null)return canApplyAtLevel== 0 ? level : -1;
		
		return -1;
	}
	
	public int getBaseLevel() {
		return level;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {

		return null;
	}

	@Override
	public ResourceLocation getId() {

		return id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {

		return RecipeSerializerInit.ENCHANT.get();
	}

	@Override
	public IRecipeType<?> getType() {
		return EnchantRecipeType.ENCHANT;
	}
	
	public int getStackableUntilLevel() {
		return stackableUntilLevel;
	}

	public int getCanApplyAtLevel() {
		return canApplyAtLevel;
	}

	public void setCanApplyAtLevel(int canApplyAtLevel) {
		this.canApplyAtLevel = canApplyAtLevel;
	}

	public int getTableLevel() {
		return tableLevel;
	}

	public void setTableLevel(int tableLevel) {
		this.tableLevel = tableLevel;
	}
}
