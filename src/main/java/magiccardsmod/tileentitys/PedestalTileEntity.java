package magiccardsmod.tileentitys;

import magiccardsmod.init.TileEntityInit;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PedestalTileEntity extends TileEntity{
	
	private IItemHandler inventory = new ItemStackHandler(1) {
		protected void onContentsChanged(int slot) {
			markDirty();
		}; 
	};
	
	public ItemStack getItem() {
		return inventory.getStackInSlot(0);
	}
	
	public ItemStack removeItem() {
		return inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);
	}
	
	public void setItem(ItemStack stack) {
		inventory.insertItem(0, stack, false);
	}
	
	public PedestalTileEntity() {
		super(TileEntityInit.PEDESTAL_TE.get());
	}	
	
	@Override
	public void read(CompoundNBT compound) {
		
		inventory.insertItem(0,ItemStack.read(compound.getCompound("item")),false);
		
		super.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		
		CompoundNBT nbt = new CompoundNBT();
		inventory.getStackInSlot(0).write(nbt);
		
		compound.put("item", nbt);
		
		return super.write(compound);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		
		return write(super.getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		
		read(tag);
		super.handleUpdateTag(tag);
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		
		return new SUpdateTileEntityPacket(this.pos, 1, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		
		read(pkt.getNbtCompound());
		
		super.onDataPacket(net, pkt);
	}
	
}
