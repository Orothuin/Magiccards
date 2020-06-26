package magiccardsmod.init;

import java.util.ArrayList;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.enchantments.ApplyPotion_CardEnchantment;
import magiccardsmod.enchantments.BoneMeal_CardEnchantment;
import magiccardsmod.enchantments.CaptureEntity_CardEnchantment;
import magiccardsmod.enchantments.CropsHarvest_CardEnchantment;
import magiccardsmod.enchantments.Drop_CardEnchantment;
import magiccardsmod.enchantments.Durablility_CardEnchantment;
import magiccardsmod.enchantments.Explosion_CardEnchantment;
import magiccardsmod.enchantments.FireAspect_CardEnchantment;
import magiccardsmod.enchantments.Fly_CardEnchantment;
import magiccardsmod.enchantments.Gather_CardEnchantment;
import magiccardsmod.enchantments.IceWalk_CardEnchantment;
import magiccardsmod.enchantments.LeaveDecay_CardEnchantment;
import magiccardsmod.enchantments.Lumber_CardEnchantment;
import magiccardsmod.enchantments.OreSeeker_CardEnchantment;
import magiccardsmod.enchantments.Pierce_CardEnchantment;
import magiccardsmod.enchantments.PlayDisk_CardEnchantment;
import magiccardsmod.enchantments.Rebound_CardEnchantment;
import magiccardsmod.enchantments.SharpedEdges_CardEnchantment;
import magiccardsmod.enchantments.Smelt_CardEnchantment;
import magiccardsmod.enchantments.Teleport_CardEnchantment;
import magiccardsmod.enchantments.Thunder_CardEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentInit {

	public static final EnchantmentType MAGICCARD = EnchantmentType.create("magiccard", item -> isMagicCard(item));

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = new DeferredRegister<>(
			ForgeRegistries.ENCHANTMENTS, MagicCardsMod.MODID);

	public static final RegistryObject<Enchantment> FIREASPECT_CARD_ENCHANTMENT = ENCHANTMENTS.register("fireaspect",
			() -> new FireAspect_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));

	public static final RegistryObject<Enchantment> LUMBER_CARD_ENCHANTMENT = ENCHANTMENTS.register("lumber",
			() -> new Lumber_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));

	public static final RegistryObject<Enchantment> LEAVEDECAY_CARD_ENCHANTMENT = ENCHANTMENTS.register("leavedecay",
			() -> new LeaveDecay_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> TELEPORT_CARD_ENCHANTMENT = ENCHANTMENTS.register("teleport",
			() -> new Teleport_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> SHARPEDGES_CARD_ENCHANTMENT = ENCHANTMENTS.register("sharpedges",
			() -> new SharpedEdges_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> POTION_CARD_ENCHANTMENT = ENCHANTMENTS.register("applypotion",
			() -> new ApplyPotion_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> THUNDER_CARD_ENCHANTMENT = ENCHANTMENTS.register("thunder",
			() -> new Thunder_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> PLAYDISC_CARD_ENCHANTMENT = ENCHANTMENTS.register("playdisc",
			() -> new PlayDisk_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> EXPLOSION_CARD_ENCHANTMENT = ENCHANTMENTS.register("explosion",
			() -> new Explosion_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> DROP_CARD_ENCHANTMENT = ENCHANTMENTS.register("dropcard",
			() -> new Drop_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> PIERCE_CARD_ENCHANTMENT = ENCHANTMENTS.register("pierce",
			() -> new Pierce_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));

	public static final RegistryObject<Enchantment> ICEWALK_CARD_ENCHANTMENT = ENCHANTMENTS.register("icewalk",
			() -> new IceWalk_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> ORESEEKER_CARD_ENCHANTMENT = ENCHANTMENTS.register("oreseeker",
			() -> new OreSeeker_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> BONEMEAL_CARD_ENCHANTMENT = ENCHANTMENTS.register("bonemeal",
			() -> new BoneMeal_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> SMELT_CARD_ENCHANTMENT = ENCHANTMENTS.register("smelt",
			() -> new Smelt_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> CROPSHARVEST_CARD_ENCHANTMENT = ENCHANTMENTS.register("cropsharvest",
			() -> new CropsHarvest_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> CAPTURE_ENTITY_CARD_ENCHANTMENT = ENCHANTMENTS.register("captureentity",
			() -> new CaptureEntity_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> GATHER_CARD_ENCHANTMENT = ENCHANTMENTS.register("gather",
			() -> new Gather_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));

	public static final RegistryObject<Enchantment> FLY_CARD_ENCHANTMENT = ENCHANTMENTS.register("fly",
			() -> new Fly_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> REBOUND_CARD_ENCHANTMENT = ENCHANTMENTS.register("rebound",
			() -> new Rebound_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));
	
	public static final RegistryObject<Enchantment> DURABILITY_CARD_ENCHANTMENT = ENCHANTMENTS.register("durability",
			() -> new Durablility_CardEnchantment(Rarity.UNCOMMON, MAGICCARD,
					new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND }));

	
	
	public static boolean isMagicCard(Item item) {
		
		ArrayList<Item> cards = new ArrayList<>();
		cards.add(ItemInit.MAGIC_CARD.get());
		cards.add(ItemInit.MAGIC_CARD_IRON.get());
		cards.add(ItemInit.MAGIC_CARD_GOLD.get());
		
		cards.add(ItemInit.WETCARD.get());
		cards.add(ItemInit.WETCARD_IRON.get());
		cards.add(ItemInit.WETCARD_GOLD.get());
		return cards.contains(item);
	}
}
