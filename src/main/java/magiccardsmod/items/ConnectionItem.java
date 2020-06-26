package magiccardsmod.items;

import magiccardsmod.itemgroups.ModItemGroup;
import net.minecraft.item.Item;

public class ConnectionItem extends Item{

	public ConnectionItem() {
		super(new Properties().group(ModItemGroup.INSTANCE));	
	}
}
