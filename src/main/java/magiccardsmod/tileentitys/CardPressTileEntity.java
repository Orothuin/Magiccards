package magiccardsmod.tileentitys;

import magiccardsmod.init.ItemInit;
import magiccardsmod.init.TileEntityInit;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CardPressTileEntity extends TileEntity implements ITickableTileEntity {

	public double renderTicks = 0;

	private boolean working = false;

	private ItemStackHandler input = new ItemStackHandler(1) {
		
		protected void onContentsChanged(int slot) {markDirty();}
		
		public int getSlotLimit(int slot) {
			
			if(slot==0)return 3;
			
			else return super.getSlotLimit(slot);
		};
		
	};
	private ItemStackHandler output = new ItemStackHandler(1) {
		protected void onContentsChanged(int slot) {markDirty();
		
		if(world!=null&&!world.isRemote)world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		
		};
	};

	public CardPressTileEntity() {
		super(TileEntityInit.CARD_PRESS_TE.get());

	}

	@Override
	public void read(CompoundNBT compound) {

		input.setStackInSlot(0, ItemStack.read(compound.getCompound("input")));
		output.setStackInSlot(0, ItemStack.read(compound.getCompound("output")));
		
		working = compound.getBoolean("working");
		
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {

		CompoundNBT nbt = new CompoundNBT();

		input.getStackInSlot(0).copy().write(nbt);
		
		compound.put("input", nbt);

		nbt = new CompoundNBT();

		output.getStackInSlot(0).copy().write(nbt);

		compound.put("output", nbt);

		compound.putBoolean("working", working);

		return super.write(compound);
	}

	public int ticks = 0;

	@Override
	public void tick() {
		
		if (world.isRemote) {
			
			if(working)ticks++;
			
			return;
			
		}
		
		if (!input.getStackInSlot(0).isEmpty()&&output.getStackInSlot(0).isEmpty()) {

			if (working == false) {
				setWorking(true);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
			}
		}
		
		if(working&&world.getBlockState(pos.up()).getBlock()==Blocks.AIR) {
			
			ticks++;
			
			if (ticks > 70) {
				
				ticks = 0;
				
				setWorking(false);
				
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
			}
			
			if (ticks == 35) {
				
				if(input.getStackInSlot(0).getItem()==Items.PAPER)
				output.setStackInSlot(0, new ItemStack(ItemInit.MAGIC_CARD.get(), input.getStackInSlot(0).getCount() * 2));
				
				else if(input.getStackInSlot(0).getItem()==Items.DIAMOND)
					output.setStackInSlot(0, new ItemStack(ItemInit.MAGIC_CARD_DIAMOND.get(), input.getStackInSlot(0).getCount()));
				
				else if(input.getStackInSlot(0).getItem()==Items.EMERALD)
					output.setStackInSlot(0, new ItemStack(ItemInit.MAGIC_CARD_EMERALD.get(), input.getStackInSlot(0).getCount()));
				
				input.setStackInSlot(0, ItemStack.EMPTY);
				
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
			}
		} 
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public ItemStackHandler getInputInventory() {
		return input;
	}

	public ItemStackHandler getOutputInventory() {
		return output;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == Direction.DOWN)
			return LazyOptional.of(() -> getOutputInventory()).cast();

		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&!isWorking())
			return LazyOptional.of(() -> getInputInventory()).cast();

		return super.getCapability(cap, side);
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
