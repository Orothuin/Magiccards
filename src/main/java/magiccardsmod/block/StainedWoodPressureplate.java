package magiccardsmod.block;

import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;

public class StainedWoodPressureplate extends PressurePlateBlock{

	public StainedWoodPressureplate() {
		super(Sensitivity.EVERYTHING, Properties.create(Material.WOOD));
	}
}
