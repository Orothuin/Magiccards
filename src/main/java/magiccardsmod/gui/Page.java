package magiccardsmod.gui;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public interface Page {
	
	public	static Map<String,Decoder> pageTypes=new HashMap<String, Decoder>();
	
	public void draw(ContainerScreen<?> gui);
	
	public ItemStack getHoveredItem(ContainerScreen<?> gui,int x,int y);
	
	public void encode(PacketBuffer buffer) ;
		
	public static Page decode(PacketBuffer buffer) {
		
		return pageTypes.get(buffer.readString()).decode(buffer);
	}
	
	public String getPageType();
	
	public interface Decoder{
		
		public Page decode(PacketBuffer buffer);
	}
}
