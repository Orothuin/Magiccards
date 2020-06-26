package magiccardsmod.networking;

import java.util.function.Supplier;

import magiccardsmod.container.InfoCardDeckContainer;
import magiccardsmod.gui.InfoCardDeckGui;
import magiccardsmod.gui.Page;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncPageMessage {
	
	Page page;
	
	public SyncPageMessage(Page page) {
		this.page=page;
		System.out.println("SYNC");
	}
	
	public static void encode(SyncPageMessage msg, PacketBuffer buffer) {
		Page page = msg.page;
		System.out.println("SYNC");
		page.encode(buffer);
	}
	
	
	public static SyncPageMessage decode(PacketBuffer buffer) {
		System.out.println("SYNC");
		return new SyncPageMessage (Page.decode(buffer));
	}
	
	public static void handle(SyncPageMessage msg, Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	    	
	    	Screen screen =  Minecraft.getInstance().currentScreen;
	    	if(screen instanceof InfoCardDeckGui) {
	    	
	    		((InfoCardDeckContainer)((InfoCardDeckGui)Minecraft.getInstance().currentScreen).getContainer()).getPages().add(msg.page);
	    	}
	    });
	    ctx.get().setPacketHandled(true);
	}
}
