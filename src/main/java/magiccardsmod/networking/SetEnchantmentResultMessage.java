package magiccardsmod.networking;

import java.util.function.Supplier;

import magiccardsmod.container.CardTableContainer;
import magiccardsmod.gui.CardTableGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SetEnchantmentResultMessage {
	
	public Enchantment enchantment;
	public int windowId;
	public int level;
	
	public SetEnchantmentResultMessage(Enchantment enchantment,int windowId,int level) {
		this.enchantment=enchantment;
		this.windowId = windowId;
		this.level=level;
		
	}
	
	public SetEnchantmentResultMessage(ResourceLocation enchantment,int windowId,int level) {
		
		this.enchantment=enchantment.getPath().equals("empty") ? null :ForgeRegistries.ENCHANTMENTS.getValue(enchantment);
		this.windowId = windowId;
		this.level=level;
	}
	
public static void encode(SetEnchantmentResultMessage msg, PacketBuffer buffer) {
	
		buffer.writeResourceLocation(msg.enchantment!=null ? msg.enchantment.getRegistryName() : new ResourceLocation("magiccards:empty"));
		buffer.writeInt(msg.windowId);
		buffer.writeInt(msg.level);
	}
	
	public static SetEnchantmentResultMessage decode(PacketBuffer buffer) {
		
		return new SetEnchantmentResultMessage(buffer.readResourceLocation(),buffer.readInt(),buffer.readInt());
	}
	
	public static void handle(SetEnchantmentResultMessage msg, Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	    	
	    	if(Minecraft.getInstance().currentScreen instanceof CardTableGui) {
	    	
	    		((CardTableContainer)((ContainerScreen<?>) Minecraft.getInstance().currentScreen).getContainer()).selectedEnchantment=msg.enchantment;
	        	((CardTableContainer)((ContainerScreen<?>) Minecraft.getInstance().currentScreen).getContainer()).level=msg.level;
	    	}
	    });
	    ctx.get().setPacketHandled(true);
	}
}
