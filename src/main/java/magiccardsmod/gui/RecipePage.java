package magiccardsmod.gui;

import java.util.ArrayList;
import java.util.List;

import magiccardsmod.recipe.EnchantRecipe;
import magiccardsmod.recipe.EnchantRecipeType;
import magiccardsmod.recipe.IngredientExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class RecipePage implements Page {

	private RecipeEntry entry;
	private TranslationTextComponent enchantmentName;

	private int cornerX = 71 - 27, cornerY = 50;

	static {
		Page.pageTypes.put("recipe", RecipePage::decode);
	}

	public RecipePage(IRecipe<?> recipe) {

		if (recipe.getType() == EnchantRecipeType.ENCHANT) {

			EnchantRecipe eRecipe = (EnchantRecipe) recipe;

			enchantmentName = new TranslationTextComponent(eRecipe.enchantment.getName());

			List<List<ItemStack>> l = new ArrayList<>();

			for (IngredientExtension ie : eRecipe.ingredients)
				if (!ie.isApplySpecialCriterion()) {

					List<ItemStack> stacks = new ArrayList<>();

					for (ItemStack s : ie.getIngredient().getMatchingStacks())
						stacks.add(s);
					l.add(stacks);
				} else
					l.add(ie.getCriterionIngridients());

			RecipeEntry e = new RecipeEntry(l, eRecipe.getCanApplyAtLevel(), eRecipe.getStackableUntilLevel(),eRecipe.getBaseLevel());
			entry = e;
		}
	}

	public RecipePage(RecipeEntry entries, TranslationTextComponent enchantmentName) {

		this.entry = entries;
		this.enchantmentName = enchantmentName;
	}

	@Override
	public void draw(ContainerScreen<?> gui) {
		gui.getMinecraft().fontRenderer.drawString(enchantmentName.getFormattedText(),
				gui.getGuiLeft() + 71
						- gui.getMinecraft().fontRenderer.getStringWidth(enchantmentName.getFormattedText()) / 2,
				gui.getGuiTop() + 21, InfoCardDeckGui.TEXT_COLOR);

		String s = "Level " + (entry.maxLevel!=0&&entry.maxLevel-entry.minLevel>1 ? (entry.minLevel + 1)+"-" : "") +(entry.maxLevel>0 ? entry.maxLevel : entry.baseLevel);

		gui.getMinecraft().fontRenderer.drawString(s, gui.getGuiLeft() + 30, gui.getGuiTop() + 38,
				InfoCardDeckGui.TEXT_COLOR);
		
		gui.getMinecraft().getTextureManager().bindTexture(InfoCardDeckGui.CARDDECK_GUI_TEXTURES);
		gui.blit(gui.getGuiLeft() + cornerX-1, gui.getGuiTop() + cornerY-1, 92, 202,54 , 54);
		
		int xRaster = 0, yRaster = 0;

		List<List<ItemStack>> packed = entry.ingriedients;
		
		for (List<ItemStack> stack : packed) {

			ItemStack st = stack.get((ticks / 40) % stack.size());
			
			Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(st,
					gui.getGuiLeft() + cornerX + xRaster * 18, gui.getGuiTop() + cornerY + 18 * yRaster);

			int count = Math.min(st.getCount(), st.getMaxStackSize());

			Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, st,
					gui.getGuiLeft() + cornerX + xRaster * 18, gui.getGuiTop() + cornerY + 18 * yRaster,
					count <= 1 ? "" : String.valueOf(count));

			if (xRaster >= 2) {
				yRaster++;
				xRaster = 0;
			} else
				xRaster++;
		}
		ticks++;
	}

	private static int ticks = 0;

	public List<ItemStack> copy(List<ItemStack> list) {

		List<ItemStack> copy = new ArrayList<>();

		for (ItemStack stack : list)
			copy.add(stack.copy());

		return copy;

	}

	@SuppressWarnings("unused")
	private List<List<ItemStack>> getPacked(List<List<ItemStack>> list) {

		List<List<ItemStack>> packed = new ArrayList<>();

		for (List<ItemStack> stack : list) {
			List<ItemStack> temp = new ArrayList<>();

			if (!packed.isEmpty()) {
				for (List<ItemStack> stack2 : packed) {

					if (stack.isEmpty() || stack2.isEmpty())
						System.err.println("");

					if (stack.get(0).getItem().equals(stack2.get(0).getItem())) {

						for (ItemStack st : stack2)
							st.setCount(st.getCount() + 1);

						temp.clear();
						break;

					}
					temp = copy(stack);
				}
			} else
				temp = copy(stack);

			if (!temp.isEmpty())
				packed.add(temp);

		}
		return packed;

	}

	@Override
	public void encode(PacketBuffer buffer) {

		buffer.writeString(getPageType());

		buffer.writeString(enchantmentName.getKey());

		buffer.writeInt(entry.ingriedients.size());

		for (List<ItemStack> l : entry.ingriedients) {
			buffer.writeInt(l.size());
			for (ItemStack stack : l)
				buffer.writeItemStack(stack);
		}
		buffer.writeInt(entry.minLevel);
		buffer.writeInt(entry.maxLevel);
		buffer.writeInt(entry.baseLevel);

	}

	private static Page decode(PacketBuffer buffer) {

		TranslationTextComponent name = new TranslationTextComponent(buffer.readString());

		List<List<ItemStack>> stacks = new ArrayList<>();

		int xMax = buffer.readInt();

		for (int x = 0; x < xMax; x++) {
			int yMax = buffer.readInt();
			ArrayList<ItemStack> l = new ArrayList<>();
			for (int y = 0; y < yMax; y++)
				l.add(buffer.readItemStack());
			stacks.add(l);
		}
		RecipeEntry entry = new RecipePage.RecipeEntry(stacks, buffer.readInt(), buffer.readInt(),buffer.readInt());

		return new RecipePage(entry, name);
	}

	@Override
	public String getPageType() {

		return "recipe";
	}

	public static class RecipeEntry {

		public List<List<ItemStack>> ingriedients;
		public int minLevel, maxLevel;
		public int baseLevel;

		public RecipeEntry(List<List<ItemStack>> ingriedients, int minLevel, int maxLevel,int baseLevel) {
			this.ingriedients = ingriedients;
			this.minLevel = minLevel;
			this.maxLevel = maxLevel;
			this.baseLevel=baseLevel;
		}
	}

	@Override
	public ItemStack getHoveredItem(ContainerScreen<?> gui, int x, int y) {

		List<List<ItemStack>> list = entry.ingriedients;

		for (int yRaster = 0; yRaster < 3; yRaster++)
			for (int xRaster = 0; xRaster < 3; xRaster++) {

				int xPos = gui.getGuiLeft() + cornerX + xRaster * 18;
				int yPos = gui.getGuiTop() + cornerY + 18 * yRaster;

				if (x >= xPos && x <= xPos + 16 && y >= yPos && y <= yPos + 16) {

					if (3 * yRaster + xRaster >= list.size())
						return null;

					List<ItemStack> l = list.get(3 * yRaster + xRaster);

					return l.get((ticks / 40) % l.size());
				}
			}
		return null;
	}

	public RecipeEntry getEntry() {
		return entry;
	}

	public TranslationTextComponent getEnchantmentName() {
		return enchantmentName;
	}

}
