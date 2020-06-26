package magiccardsmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantRecipeSerializer<T extends EnchantRecipe>
		extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

	private final EnchantRecipeSerializer.IFactory<T> recipe;

	public EnchantRecipeSerializer(EnchantRecipeSerializer.IFactory<T> recipe) {
		this.recipe = recipe;

	}

	@Override
	public T read(ResourceLocation recipeId, JsonObject json) {

		NonNullList<IngredientExtension> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
		
		int stackableUntil = JSONUtils.getInt(json, "stackableMaxLevel",0);
		int canApplyAtLevel = JSONUtils.getInt(json, "canApplyAtLevel",0);
		int tableLevel = JSONUtils.getInt(json, "tablelevel",1);
		
		if (nonnulllist.isEmpty()) {
			throw new JsonParseException("No ingredients for shapeless recipe");
		} else if (nonnulllist.size() > 3 * 3) {
			throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
		} else {
			JsonObject results = JSONUtils.getJsonObject(json, "result");
			Enchantment ench = ForgeRegistries.ENCHANTMENTS
					.getValue(new ResourceLocation(JSONUtils.getString(results, "enchantment")));
			int level = JSONUtils.getInt(results, "level");

			return recipe.create(recipeId, nonnulllist, ench, level,stackableUntil,canApplyAtLevel,tableLevel);
		}
	}

	private static NonNullList<IngredientExtension> readIngredients(JsonArray p_199568_0_) {
		NonNullList<IngredientExtension> nonnulllist = NonNullList.create();

		for (int i = 0; i < p_199568_0_.size(); ++i) {

			String specialCriterion = JSONUtils.getString(p_199568_0_.get(i).getAsJsonObject(), "specialcriterion", "");
			
			boolean shouldBeInserted = JSONUtils.getBoolean(p_199568_0_.get(i).getAsJsonObject(), "insert", false);
			
			if (!specialCriterion.isEmpty()) {
				
				IngredientExtension extension = new IngredientExtension(specialCriterion,shouldBeInserted);
				
				nonnulllist.add(extension);
			} else {

				Ingredient ingredient = Ingredient.deserialize(p_199568_0_.get(i));

				IngredientExtension extension = new IngredientExtension(ingredient, shouldBeInserted);

				if (!ingredient.hasNoMatchingItems()) {

					nonnulllist.add(extension);
				}
			}
		}

		return nonnulllist;

	}

	@Override
	public T read(ResourceLocation recipeId, PacketBuffer buffer) {

		IngredientExtension empty = new IngredientExtension(Ingredient.EMPTY, false);
		
		NonNullList<IngredientExtension> nonnulllist = NonNullList.withSize(buffer.readInt(), empty);

		for (int k = 0; k < nonnulllist.size(); ++k) {
			nonnulllist.set(k, new IngredientExtension(Ingredient.read(buffer), buffer.readBoolean()));
		}

		Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(buffer.readResourceLocation());
		int level = buffer.readInt();
		
		int stackableUntil = buffer.readInt();
		int canApplyAtLevel = buffer.readInt();
		int tableLevel = buffer.readInt();
		return recipe.create(recipeId, nonnulllist, enchantment, level,stackableUntil,canApplyAtLevel,tableLevel);
	}

	@Override
	public void write(PacketBuffer buffer, T recipe) {

		buffer.writeInt(recipe.ingredients.size());

		for (IngredientExtension ingredient : recipe.ingredients) {
			ingredient.getIngredient().write(buffer);
			buffer.writeBoolean(ingredient.isShouldBeInserted());
		}

		buffer.writeResourceLocation(recipe.enchantment.getRegistryName());
		buffer.writeInt(recipe.getBaseLevel());
		buffer.writeInt(recipe.getStackableUntilLevel());
		buffer.writeInt(recipe.getCanApplyAtLevel());
		buffer.writeInt(recipe.getTableLevel());
	}

	public interface IFactory<T extends EnchantRecipe> {
		T create(ResourceLocation p_create_1_, NonNullList<IngredientExtension> ingredients, Enchantment p_create_4_,
				int level,int stackableUntil,int canApplyAtLevel,int tableLevel);
	}
}
