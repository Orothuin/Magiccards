package magiccardsmod;

import magiccardsmod.client.ClientInit;
import magiccardsmod.init.BlockInit;
import magiccardsmod.init.ContainerInit;
import magiccardsmod.init.EnchantmentInit;
import magiccardsmod.init.EntityInit;
import magiccardsmod.init.ItemInit;
import magiccardsmod.init.RecipeSerializerInit;
import magiccardsmod.init.SoundInit;
import magiccardsmod.init.TileEntityInit;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("magiccards")
public class MagicCardsMod {

	public static final String MODID = "magiccards";

	public MagicCardsMod() {

		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);
		
		
		eventBus.addListener(this::doClientSetup);

		EntityInit.ENTITIES.register(eventBus);
		
		EnchantmentInit.ENCHANTMENTS.register(eventBus);
		
		BlockInit.BLOCKS.register(eventBus);
		
		ItemInit.ITEMS.register(eventBus);
		
		TileEntityInit.TILEENTITY_TYPES.register(eventBus);
		
		ContainerInit.CONTAINER_TYPES.register(eventBus);
		
		SoundInit.SOUNDEVENTS.register(eventBus);
		
		RecipeSerializerInit.SERIALIZERS.register(eventBus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		magiccardsmod.networking.PacketHandler.registerMessages();
	}

	@SubscribeEvent
	public void doClientSetup(final FMLClientSetupEvent event) {
		ClientInit.init(event);
	}
	
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {

	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

		}
	}
}
