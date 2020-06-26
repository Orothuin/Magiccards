package magiccardsmod.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class EnchantRecipeType implements IRecipeType<EnchantRecipe>{
	
	public static final IRecipeType<EnchantRecipe> ENCHANT = register("enchant");
	
	static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
	      return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
	         public String toString() {
	            return key;
	         }
	      });
	   }
	
}
