package magiccardsmod.block;

import magiccardsmod.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class StainedWoodStairs extends StairsBlock{

	public StainedWoodStairs() {
		super(()-> BlockInit.STAINEDWOOD_PLANKS.get().getDefaultState(),Block.Properties.from(BlockInit.STAINEDWOOD_PLANKS.get()));
	}
}
