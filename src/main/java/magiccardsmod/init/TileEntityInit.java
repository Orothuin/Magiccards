package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.tileentitys.CardPressTileEntity;
import magiccardsmod.tileentitys.CardTableTileEntity;
import magiccardsmod.tileentitys.CollectorTileEntity;
import magiccardsmod.tileentitys.PedestalTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {

	public static final DeferredRegister<TileEntityType<?>> TILEENTITY_TYPES = new DeferredRegister<>(
			ForgeRegistries.TILE_ENTITIES, MagicCardsMod.MODID);

	public static final RegistryObject<TileEntityType<CardTableTileEntity>> CARD_TABLE_TE = TILEENTITY_TYPES.register(
			"cardtable",
			() -> TileEntityType.Builder.create(CardTableTileEntity::new, BlockInit.CARD_TABLE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL_TE = TILEENTITY_TYPES.register(
			"pedestal",
			() -> TileEntityType.Builder.create(PedestalTileEntity::new, BlockInit.PEDESTAL.get()).build(null));
	
	public static final RegistryObject<TileEntityType<CardPressTileEntity>> CARD_PRESS_TE = TILEENTITY_TYPES.register(
			"cardpress",
			() -> TileEntityType.Builder.create(CardPressTileEntity::new, BlockInit.CARD_PRESS.get()).build(null));
	
	public static final RegistryObject<TileEntityType<CollectorTileEntity>> COLLECTOR_TE = TILEENTITY_TYPES.register(
			"collector",
			() -> TileEntityType.Builder.create(CollectorTileEntity::new, BlockInit.COLLECTOR.get()).build(null));

}
