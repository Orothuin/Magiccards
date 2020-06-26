package magiccardsmod.itemgroups;

import magiccardsmod.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup{
	
	public static final ModItemGroup INSTANCE = new ModItemGroup(ItemGroup.GROUPS.length,"moditemgroup");
	
	public ModItemGroup(int index, String label) {
		super(index, label);
	}

	@Override
	public ItemStack createIcon() {
		
		return new ItemStack(ItemInit.MAGIC_CARD.get());
	}
}
