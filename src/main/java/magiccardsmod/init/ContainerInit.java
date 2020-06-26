package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.container.CardTableContainer;
import magiccardsmod.container.InfoCardDeckContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
	
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MagicCardsMod.MODID);
	
	public static final RegistryObject<ContainerType<CardTableContainer>> CARDTABLE_TYPE = CONTAINER_TYPES.register("cardtablecontainer", ()-> IForgeContainerType.create( CardTableContainer::new));
	
	public static final RegistryObject<ContainerType<InfoCardDeckContainer>> INFOCARDDECK_TYPE = CONTAINER_TYPES.register("infocarddeckcontainer", ()-> IForgeContainerType.create( InfoCardDeckContainer::new));
	
}
