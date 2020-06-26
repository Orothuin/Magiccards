package magiccardsmod.tileentitys;

import magiccardsmod.init.TileEntityInit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CollectorTileEntity extends TileEntity implements ITickableTileEntity{
	
	private float concentration;
	public boolean b;
	public float renderTicks;
	public CollectorTileEntity() {
		super(TileEntityInit.COLLECTOR_TE.get());
		
	}

	public float getConcentration() {
		return concentration;
	}

	public void setConcentration(float concentration) {
		this.concentration = concentration;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		concentration = compound.getFloat("concentration");
		super.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putFloat("concentration", concentration);
		return super.write(compound);
	}
	@Override
	public void tick() {
		
		if(concentration<1) {
		concentration+=0.001f;
		markDirty();
		}
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
