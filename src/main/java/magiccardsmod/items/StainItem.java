package magiccardsmod.items;

import magiccardsmod.itemgroups.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class StainItem extends Item{

	public StainItem() {
		super(new Properties().containerItem(Items.GLASS_BOTTLE).maxStackSize(16).group(ModItemGroup.INSTANCE));
	}
}
