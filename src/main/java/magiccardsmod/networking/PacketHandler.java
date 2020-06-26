package magiccardsmod.networking;

import magiccardsmod.MagicCardsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	
	private static final String PROTOCOL_VERSION = "1";

	public static SimpleChannel INSTANCE;

	public static void registerMessages() {
		
		INSTANCE = NetworkRegistry.newSimpleChannel(
				new ResourceLocation(MagicCardsMod.MODID, "magiccardschannel"), () -> PROTOCOL_VERSION, PacketHandler::comp,
				PacketHandler::comp);
		int i=0;
		
		INSTANCE.<SetEnchantmentResultMessage>registerMessage(i++, SetEnchantmentResultMessage.class, SetEnchantmentResultMessage::encode,
				SetEnchantmentResultMessage::decode, SetEnchantmentResultMessage::handle);
		
		INSTANCE.<UpdateEnchantedItemMessage>registerMessage(i++, UpdateEnchantedItemMessage.class, UpdateEnchantedItemMessage::encode,
				UpdateEnchantedItemMessage::decode, UpdateEnchantedItemMessage::handle);
		
		INSTANCE.<SyncPageMessage>registerMessage(i++, SyncPageMessage.class, SyncPageMessage::encode,
				SyncPageMessage::decode, SyncPageMessage::handle);
	}
	
	private static boolean comp(String s) {
		
		return PROTOCOL_VERSION.equals(s);
	}
}