package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.block.CardPressBlock;
import magiccardsmod.block.CardTableBlock;
import magiccardsmod.block.EssenceCollectorBlock;
import magiccardsmod.block.Pedestal;
import magiccardsmod.block.StainedWoodButton;
import magiccardsmod.block.StainedWoodDoor;
import magiccardsmod.block.StainedWoodFence;
import magiccardsmod.block.StainedWoodFencegate;
import magiccardsmod.block.StainedWoodPlanks;
import magiccardsmod.block.StainedWoodPressureplate;
import magiccardsmod.block.StainedWoodSlab;
import magiccardsmod.block.StainedWoodStairs;
import magiccardsmod.block.StainedWoodTrapdoor;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MagicCardsMod.MODID);
	
	public static final RegistryObject<Block> CARD_TABLE = BLOCKS.register("cardtable", ()-> new CardTableBlock());
	
	public static final RegistryObject<Block> STAINEDWOOD_PLANKS = BLOCKS.register("stainedwoodplanks", ()-> new StainedWoodPlanks());
	public static final RegistryObject<Block> STAINEDWOOD_FENCE = BLOCKS.register("stainedwoodfence", ()-> new StainedWoodFence());
	public static final RegistryObject<Block> STAINEDWOOD_PRESSUREPLATE = BLOCKS.register("stainedwoodpressureplate", ()-> new StainedWoodPressureplate());
	public static final RegistryObject<Block> STAINEDWOOD_SLAB = BLOCKS.register("stainedwoodslab", ()-> new StainedWoodSlab());
	public static final RegistryObject<Block> STAINEDWOOD_FENCEGATE = BLOCKS.register("stainedwoodfencegate", ()-> new StainedWoodFencegate());
	public static final RegistryObject<Block> STAINEDWOOD_BUTTON = BLOCKS.register("stainedwoodbutton", ()-> new StainedWoodButton());
	public static final RegistryObject<Block> STAINEDWOOD_DOOR = BLOCKS.register("stainedwooddoor", ()-> new StainedWoodDoor());
	public static final RegistryObject<Block> STAINEDWOOD_STAIRS = BLOCKS.register("stainedwoodstairs", ()-> new StainedWoodStairs());
	public static final RegistryObject<Block> STAINEDWOOD_TRAPDOOR = BLOCKS.register("stainedwoodtrapdoor", ()-> new StainedWoodTrapdoor());
	public static final RegistryObject<Block> PEDESTAL = BLOCKS.register("pedestal", ()-> new Pedestal());
	public static final RegistryObject<Block> CARD_PRESS = BLOCKS.register("cardpress", ()-> new CardPressBlock());
	public static final RegistryObject<Block> COLLECTOR = BLOCKS.register("collector", ()-> new EssenceCollectorBlock());
}

