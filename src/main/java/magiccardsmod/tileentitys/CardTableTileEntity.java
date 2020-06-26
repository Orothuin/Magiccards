package magiccardsmod.tileentitys;

import magiccardsmod.container.CardTableContainer;
import magiccardsmod.init.ItemInit;
import magiccardsmod.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CardTableTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity{
		
	private int tableLevel=1;
	
	public CardTableTileEntity() {
		super(TileEntityInit.CARD_TABLE_TE.get());
	}

	@Override
	public Container createMenu(int id, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		
		return new CardTableContainer(id,this,p_createMenu_3_);
	}

	@Override
	public ITextComponent getDisplayName() {
		
		return new TranslationTextComponent("Card Table");
	}

	public int getTableLevel() {
		return tableLevel;
	}

	public void setTableLevel(int tableLevel) {
		this.tableLevel = tableLevel;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		tableLevel = compound.getInt("level");
		super.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		
		compound.putInt("level", tableLevel);
		
		return super.write(compound);
	}
	private int tick;
	@Override
	public void tick() {
		
		if(world.isRemote)return;
		
		if(tick>5) 
			tick=0;
		else {
			tick++;
			return;
		}
		
		int level=1;
		
		boolean lv3Possible=false;
		
		for(BlockPos bPos : BlockPos.getAllInBoxMutable(pos.south(2).west(2).down(2), pos.north(2).east(2).up(2))) {
			
			TileEntity tile = world.getTileEntity(bPos);
			
			if(tile instanceof PedestalTileEntity) {
				
				PedestalTileEntity pedestal = (PedestalTileEntity) tile;
				
				if(pedestal.getItem().getItem()==ItemInit.MAGIC_CARD_DIAMOND.get()) {
					level = level>1 ? level :2;
					if(lv3Possible)level = 3;
				}
				if(pedestal.getItem().getItem()==ItemInit.MAGIC_CARD_EMERALD.get()) {
						level = level!=2 ? level :3;
						lv3Possible=true;
				}
			}
		}
		tableLevel=level;
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 2);
		markDirty();
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
