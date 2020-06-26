package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.recipe.AccessCookingSerializerUtil;
import magiccardsmod.recipe.EnchantRecipe;
import magiccardsmod.recipe.EnchantRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = new DeferredRegister<>(
			ForgeRegistries.RECIPE_SERIALIZERS, MagicCardsMod.MODID);

	public static RegistryObject<IRecipeSerializer<?>> DRY_CARD = SERIALIZERS.register("drycard", () -> AccessCookingSerializerUtil.getSerializer());
	
	public static RegistryObject<IRecipeSerializer<?>> ENCHANT = SERIALIZERS.register("enchant", () -> new EnchantRecipeSerializer<EnchantRecipe>(EnchantRecipe::new));

}
