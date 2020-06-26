package magiccardsmod.networking;

import java.util.function.Supplier;

import magiccardsmod.container.CardTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateEnchantedItemMessage {
	
	public	ItemStack stack;
	
	public UpdateEnchantedItemMessage(ItemStack stack) {
		this.stack=stack;
		
	}
	
public static void encode(UpdateEnchantedItemMessage msg, PacketBuffer buffer) {
	
		buffer.writeItemStack(msg.stack);
	}
	
	public static UpdateEnchantedItemMessage decode(PacketBuffer buffer) {
		
		return new UpdateEnchantedItemMessage(buffer.readItemStack());
	}
	
	public static void handle(UpdateEnchantedItemMessage msg, Supplier<NetworkEvent.Context> ctx) {
	   
		ctx.get().enqueueWork(() -> {
	     
	        ((CardTableContainer)((ContainerScreen<?>) Minecraft.getInstance().currentScreen).getContainer()).resultInv.setInventorySlotContents(0, msg.stack);
	    });
	    ctx.get().setPacketHandled(true);
	}
}
