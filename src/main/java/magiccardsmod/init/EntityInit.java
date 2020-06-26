package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.MagicCardDiamondEntity;
import magiccardsmod.entity.MagicCardEmeraldEntity;
import magiccardsmod.entity.MagicCardEntity;
import magiccardsmod.entity.MagicCardGoldEntity;
import magiccardsmod.entity.MagicCardIronEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MagicCardsMod.MODID);
	
	public static final RegistryObject<EntityType<MagicCardEntity>> MAGICCARD_ENTTIY = ENTITIES.register("magiccard", ()->EntityType.Builder.<MagicCardEntity>create(MagicCardEntity::new, EntityClassification.MISC)
					.size(0.25f, 0.25f).build(new ResourceLocation(MagicCardsMod.MODID + ":magiccard").toString()));
	
	public static final RegistryObject<EntityType<MagicCardIronEntity>> MAGICCARDIRON_ENTTIY = ENTITIES.register("magiccardiron", ()->EntityType.Builder.<MagicCardIronEntity>create(MagicCardIronEntity::new, EntityClassification.MISC)
			.size(0.25f, 0.25f).build(new ResourceLocation(MagicCardsMod.MODID + ":magiccardiron").toString()));
	
	public static final RegistryObject<EntityType<MagicCardGoldEntity>> MAGICCARDGOLD_ENTTIY = ENTITIES.register("magiccardgold", ()->EntityType.Builder.<MagicCardGoldEntity>create(MagicCardGoldEntity::new, EntityClassification.MISC)
			.size(0.25f, 0.25f).build(new ResourceLocation(MagicCardsMod.MODID + ":magiccardgold").toString()));
	
	public static final RegistryObject<EntityType<MagicCardDiamondEntity>> MAGICCARDDIAMOND_ENTTIY = ENTITIES.register("magiccarddiamond", ()->EntityType.Builder.<MagicCardDiamondEntity>create(MagicCardDiamondEntity::new, EntityClassification.MISC)
			.size(0.25f, 0.25f).build(new ResourceLocation(MagicCardsMod.MODID + ":magiccarddiamond").toString()));
	
	public static final RegistryObject<EntityType<MagicCardEmeraldEntity>> MAGICCARDEMERALD_ENTTIY = ENTITIES.register("magiccardemerald", ()->EntityType.Builder.<MagicCardEmeraldEntity>create(MagicCardEmeraldEntity::new, EntityClassification.MISC)
			.size(0.25f, 0.25f).build(new ResourceLocation(MagicCardsMod.MODID + ":magiccardemerald").toString()));

	
}
