package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.itemgroups.ModItemGroup;
import magiccardsmod.items.ConnectionCubeItem;
import magiccardsmod.items.ConnectionGoldDecorationItem;
import magiccardsmod.items.ConnectionItem;
import magiccardsmod.items.ConnectionPoleItem;
import magiccardsmod.items.InfoCardDeck;
import magiccardsmod.items.MagicCardDiamondItem;
import magiccardsmod.items.MagicCardEmeraldItem;
import magiccardsmod.items.MagicCardGoldItem;
import magiccardsmod.items.MagicCardIronItem;
import magiccardsmod.items.MagicCardItem;
import magiccardsmod.items.PodestItem;
import magiccardsmod.items.PodestbottomItem;
import magiccardsmod.items.PodesttopItem;
import magiccardsmod.items.SharpEdgeGoldItem;
import magiccardsmod.items.SharpEdgeIronItem;
import magiccardsmod.items.StainItem;
import magiccardsmod.items.TopPlateItem;
import magiccardsmod.items.WetCardGoldItem;
import magiccardsmod.items.WetCardIronItem;
import magiccardsmod.items.WetCardItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS,
			MagicCardsMod.MODID);

	public static final RegistryObject<Item> MAGIC_CARD = ITEMS.register("magiccard", () -> new MagicCardItem());
	public static final RegistryObject<Item> MAGIC_CARD_IRON = ITEMS.register("magiccardiron",
			() -> new MagicCardIronItem());
	public static final RegistryObject<Item> MAGIC_CARD_GOLD = ITEMS.register("magiccardgold",
			() -> new MagicCardGoldItem());
	public static final RegistryObject<Item> MAGIC_CARD_EMERALD = ITEMS.register("magiccardemerald",
			() -> new MagicCardEmeraldItem());
	public static final RegistryObject<Item> MAGIC_CARD_DIAMOND = ITEMS.register("magiccarddiamond",
			() -> new MagicCardDiamondItem());
	public static final RegistryObject<Item> INFO_CARD_DECK = ITEMS.register("infocarddeck", () -> new InfoCardDeck());
	public static final RegistryObject<Item> SHARPEDGE_IRON = ITEMS.register("sharpedgeiron",
			() -> new SharpEdgeIronItem());
	public static final RegistryObject<Item> SHARPEDGE_GOLD = ITEMS.register("sharpedgegold",
			() -> new SharpEdgeGoldItem());
	public static final RegistryObject<Item> WETCARD = ITEMS.register("wetcard", () -> new WetCardItem());
	public static final RegistryObject<Item> WETCARD_IRON = ITEMS.register("wetcardiron", () -> new WetCardIronItem());
	public static final RegistryObject<Item> WETCARD_GOLD = ITEMS.register("wetcardgold", () -> new WetCardGoldItem());
	

	public static final RegistryObject<Item> CARD_TABLE_ITEM = ITEMS.register("cardtableitem",
			() -> new BlockItem(BlockInit.CARD_TABLE.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_PLANKS_ITEM = ITEMS.register("stainedwoodplanks",
			() -> new BlockItem(BlockInit.STAINEDWOOD_PLANKS.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_FENCE_ITEM = ITEMS.register("stainedwoodfence",
			() -> new BlockItem(BlockInit.STAINEDWOOD_FENCE.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_PRESSUREPLATE_ITEM = ITEMS.register("stainedwoodpressureplate",
			() -> new BlockItem(BlockInit.STAINEDWOOD_PRESSUREPLATE.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_SLAB_ITEM = ITEMS.register("stainedwoodslab",
			() -> new BlockItem(BlockInit.STAINEDWOOD_SLAB.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_BUTTON_ITEM = ITEMS.register("stainedwoodbutton",
			() -> new BlockItem(BlockInit.STAINEDWOOD_BUTTON.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_FENCEGATE_ITEM = ITEMS.register("stainedwoodfencegate",
			() -> new BlockItem(BlockInit.STAINEDWOOD_FENCEGATE.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_DOOR_ITEM = ITEMS.register("stainedwooddoor",
			() -> new BlockItem(BlockInit.STAINEDWOOD_DOOR.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_STAIRS_ITEM = ITEMS.register("stainedwoodstairs",
			() -> new BlockItem(BlockInit.STAINEDWOOD_STAIRS.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> STAINEDWOOD_TRAPDOOR_ITEM = ITEMS.register("stainedwoodtrapdoor",
			() -> new BlockItem(BlockInit.STAINEDWOOD_TRAPDOOR.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> PEDESTAL_ITEM = ITEMS.register("pedestal",
			() -> new BlockItem(BlockInit.PEDESTAL.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));
	
	public static final RegistryObject<Item> CARD_PRESS_ITEM = ITEMS.register("cardpress",
			() -> new BlockItem(BlockInit.CARD_PRESS.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	public static final RegistryObject<Item> COLECTOR_ITEM = ITEMS.register("collector",
			() -> new BlockItem(BlockInit.COLLECTOR.get(),
					new net.minecraft.item.Item.Properties().group(ModItemGroup.INSTANCE)));

	
	public static final RegistryObject<Item> STAIN = ITEMS.register("stain", () -> new StainItem());

	public static final RegistryObject<Item> PODEST_BOTTOM = ITEMS.register("podestbottom",
			() -> new PodestbottomItem());
	public static final RegistryObject<Item> PODEST_TOP = ITEMS.register("podesttop", () -> new PodesttopItem());
	public static final RegistryObject<Item> CONNECTIONPOLE = ITEMS.register("connectionpole",
			() -> new ConnectionPoleItem());
	public static final RegistryObject<Item> CONNECTION_CUBE = ITEMS.register("connectioncube",
			() -> new ConnectionCubeItem());
	public static final RegistryObject<Item> CONNECTION_GOLD_DECORATION = ITEMS.register("connectiongolddecoration",
			() -> new ConnectionGoldDecorationItem());
	public static final RegistryObject<Item> TOP_PLATE = ITEMS.register("topplate", () -> new TopPlateItem());
	public static final RegistryObject<Item> PODEST = ITEMS.register("podest", () -> new PodestItem());
	public static final RegistryObject<Item> CONNECTION = ITEMS.register("connection", () -> new ConnectionItem());
	public static final RegistryObject<Item> NATURE_ESSENCE = ITEMS.register("natureessence", ()-> new Item(new Properties().group(ModItemGroup.INSTANCE)));
	public static final RegistryObject<Item> WIND_ESSENCE = ITEMS.register("windessence", ()-> new Item(new Properties().group(ModItemGroup.INSTANCE)));
	public static final RegistryObject<Item> HELL_ESSENCE = ITEMS.register("hellessence", ()-> new Item(new Properties().group(ModItemGroup.INSTANCE)));
}
