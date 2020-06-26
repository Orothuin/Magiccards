package magiccardsmod.block;

import magiccardsmod.init.ItemInit;
import magiccardsmod.init.TileEntityInit;
import magiccardsmod.tileentitys.PedestalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class Pedestal extends Block implements IWaterLoggable {

	private static final VoxelShape bE0 = Block.makeCuboidShape(15.0D, 2.0D, 5.0D, 13.0D, 7.0D, 11.0D);
	private static final VoxelShape bS0 = Block.makeCuboidShape(5.0D, 2.0D, 15.0D, 11.0D, 7.0D, 13.0D);
	private static final VoxelShape bW0 = Block.makeCuboidShape(1.0D, 2.0D, 5.0D, 3.0D, 7.0D, 11.0D);
	private static final VoxelShape bN0 = Block.makeCuboidShape(5.0D, 2.0D, 1.0D, 11.0D, 7.0D, 3.0D);
	private static final VoxelShape bE1 = Block.makeCuboidShape(13.0D, 1.0D, 3.0D, 3.0D, 15.0D, 13.0D);
	private static final VoxelShape bS1 = Block.makeCuboidShape(3.0D, 1.0D, 13.0D, 13.0D, 15.0D, 3.0D);
	private static final VoxelShape bW1 = Block.makeCuboidShape(3.0D, 1.0D, 3.0D, 13.0D, 15.0D, 13.0D);
	private static final VoxelShape bN1 = Block.makeCuboidShape(3.0D, 1.0D, 3.0D, 13.0D, 15.0D, 13.0D);
	private static final VoxelShape v1_0E = VoxelShapes.or(bE1, bE0);
	private static final VoxelShape v1_0S = VoxelShapes.or(bS1, bS0);
	private static final VoxelShape v1_0W = VoxelShapes.or(bW1, bW0);
	private static final VoxelShape v1_0N = VoxelShapes.or(bN1, bN0);
	private static final VoxelShape bE2 = Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 0.0D, 2.0D, 16.0D);
	private static final VoxelShape bS2 = Block.makeCuboidShape(0.0D, 0.0D, 16.0D, 16.0D, 2.0D, 0.0D);
	private static final VoxelShape bW2 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	private static final VoxelShape bN2 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	private static final VoxelShape bE3 = Block.makeCuboidShape(11.0D, 2.0D, 1.0D, 5.0D, 7.0D, 3.0D);
	private static final VoxelShape bS3 = Block.makeCuboidShape(1.0D, 2.0D, 11.0D, 3.0D, 7.0D, 5.0D);
	private static final VoxelShape bW3 = Block.makeCuboidShape(5.0D, 2.0D, 1.0D, 11.0D, 7.0D, 3.0D);
	private static final VoxelShape bN3 = Block.makeCuboidShape(1.0D, 2.0D, 5.0D, 3.0D, 7.0D, 11.0D);
	private static final VoxelShape v1_1E = VoxelShapes.or(bE3, bE2);
	private static final VoxelShape v1_1S = VoxelShapes.or(bS3, bS2);
	private static final VoxelShape v1_1W = VoxelShapes.or(bW3, bW2);
	private static final VoxelShape v1_1N = VoxelShapes.or(bN3, bN2);
	private static final VoxelShape bE4 = Block.makeCuboidShape(11.0D, 2.0D, 13.0D, 5.0D, 7.0D, 15.0D);
	private static final VoxelShape bS4 = Block.makeCuboidShape(13.0D, 2.0D, 11.0D, 15.0D, 7.0D, 5.0D);
	private static final VoxelShape bW4 = Block.makeCuboidShape(5.0D, 2.0D, 13.0D, 11.0D, 7.0D, 15.0D);
	private static final VoxelShape bN4 = Block.makeCuboidShape(13.0D, 2.0D, 5.0D, 15.0D, 7.0D, 11.0D);
	private static final VoxelShape bE5 = Block.makeCuboidShape(3.0D, 2.0D, 5.0D, 1.0D, 6.0D, 11.0D);
	private static final VoxelShape bS5 = Block.makeCuboidShape(5.0D, 2.0D, 3.0D, 11.0D, 6.0D, 1.0D);
	private static final VoxelShape bW5 = Block.makeCuboidShape(13.0D, 2.0D, 5.0D, 15.0D, 6.0D, 11.0D);
	private static final VoxelShape bN5 = Block.makeCuboidShape(5.0D, 2.0D, 13.0D, 11.0D, 6.0D, 15.0D);
	private static final VoxelShape v1_2E = VoxelShapes.or(bE5, bE4);
	private static final VoxelShape v1_2S = VoxelShapes.or(bS5, bS4);
	private static final VoxelShape v1_2W = VoxelShapes.or(bW5, bW4);
	private static final VoxelShape v1_2N = VoxelShapes.or(bN5, bN4);
	private static final VoxelShape bE6 = Block.makeCuboidShape(5.5D, 8.75D, 5.0D, 0.5D, 13.75D, 11.0D);
	private static final VoxelShape bS6 = Block.makeCuboidShape(5.0D, 8.75D, 5.5D, 11.0D, 13.75D, 0.5D);
	private static final VoxelShape bW6 = Block.makeCuboidShape(10.5D, 8.75D, 5.0D, 15.5D, 13.75D, 11.0D);
	private static final VoxelShape bN6 = Block.makeCuboidShape(5.0D, 8.75D, 10.5D, 11.0D, 13.75D, 15.5D);
	private static final VoxelShape bE7 = Block.makeCuboidShape(15.0D, 10.75D, 4.0D, 2.0D, 17.75D, 12.0D);
	private static final VoxelShape bS7 = Block.makeCuboidShape(4.0D, 10.75D, 15.0D, 12.0D, 17.75D, 2.0D);
	private static final VoxelShape bW7 = Block.makeCuboidShape(1.0D, 10.75D, 4.0D, 14.0D, 17.75D, 12.0D);
	private static final VoxelShape bN7 = Block.makeCuboidShape(4.0D, 10.75D, 1.0D, 12.0D, 17.75D, 14.0D);
	private static final VoxelShape v1_3E = VoxelShapes.or(bE7, bE6);
	private static final VoxelShape v1_3S = VoxelShapes.or(bS7, bS6);
	private static final VoxelShape v1_3W = VoxelShapes.or(bW7, bW6);
	private static final VoxelShape v1_3N = VoxelShapes.or(bN7, bN6);
	private static final VoxelShape bE8 = Block.makeCuboidShape(11.0D, 11.0D, 1.0D, 5.0D, 17.0D, 4.0D);
	private static final VoxelShape bS8 = Block.makeCuboidShape(1.0D, 11.0D, 11.0D, 4.0D, 17.0D, 5.0D);
	private static final VoxelShape bW8 = Block.makeCuboidShape(5.0D, 11.0D, 1.0D, 11.0D, 17.0D, 4.0D);
	private static final VoxelShape bN8 = Block.makeCuboidShape(1.0D, 11.0D, 5.0D, 4.0D, 17.0D, 11.0D);
	private static final VoxelShape bE9 = Block.makeCuboidShape(11.0D, 11.0D, 12.0D, 5.0D, 17.0D, 15.0D);
	private static final VoxelShape bS9 = Block.makeCuboidShape(12.0D, 11.0D, 11.0D, 15.0D, 17.0D, 5.0D);
	private static final VoxelShape bW9 = Block.makeCuboidShape(5.0D, 11.0D, 12.0D, 11.0D, 17.0D, 15.0D);
	private static final VoxelShape bN9 = Block.makeCuboidShape(12.0D, 11.0D, 5.0D, 15.0D, 17.0D, 11.0D);
	private static final VoxelShape v1_4E = VoxelShapes.or(bE9, bE8);
	private static final VoxelShape v1_4S = VoxelShapes.or(bS9, bS8);
	private static final VoxelShape v1_4W = VoxelShapes.or(bW9, bW8);
	private static final VoxelShape v1_4N = VoxelShapes.or(bN9, bN8);
	private static final VoxelShape bE10 = Block.makeCuboidShape(9.0D, 7.0D, 2.0D, 7.0D, 11.0D, 3.0D);
	private static final VoxelShape bS10 = Block.makeCuboidShape(2.0D, 7.0D, 9.0D, 3.0D, 11.0D, 7.0D);
	private static final VoxelShape bW10 = Block.makeCuboidShape(7.0D, 7.0D, 2.0D, 9.0D, 11.0D, 3.0D);
	private static final VoxelShape bN10 = Block.makeCuboidShape(2.0D, 7.0D, 7.0D, 3.0D, 11.0D, 9.0D);
	private static final VoxelShape bE11 = Block.makeCuboidShape(9.0D, 7.0D, 13.0D, 7.0D, 11.0D, 14.0D);
	private static final VoxelShape bS11 = Block.makeCuboidShape(13.0D, 7.0D, 9.0D, 14.0D, 11.0D, 7.0D);
	private static final VoxelShape bW11 = Block.makeCuboidShape(7.0D, 7.0D, 13.0D, 9.0D, 11.0D, 14.0D);
	private static final VoxelShape bN11 = Block.makeCuboidShape(13.0D, 7.0D, 7.0D, 14.0D, 11.0D, 9.0D);
	private static final VoxelShape v1_5E = VoxelShapes.or(bE11, bE10);
	private static final VoxelShape v1_5S = VoxelShapes.or(bS11, bS10);
	private static final VoxelShape v1_5W = VoxelShapes.or(bW11, bW10);
	private static final VoxelShape v1_5N = VoxelShapes.or(bN11, bN10);
	private static final VoxelShape bE12 = Block.makeCuboidShape(14.0D, 7.0D, 6.0D, 13.0D, 15.0D, 10.0D);
	private static final VoxelShape bS12 = Block.makeCuboidShape(6.0D, 7.0D, 14.0D, 10.0D, 15.0D, 13.0D);
	private static final VoxelShape bW12 = Block.makeCuboidShape(2.0D, 7.0D, 6.0D, 3.0D, 15.0D, 10.0D);
	private static final VoxelShape bN12 = Block.makeCuboidShape(6.0D, 7.0D, 2.0D, 10.0D, 15.0D, 3.0D);
	private static final VoxelShape bE13 = Block.makeCuboidShape(3.0D, 6.0D, 6.0D, 2.0D, 9.0D, 10.0D);
	private static final VoxelShape bS13 = Block.makeCuboidShape(6.0D, 6.0D, 3.0D, 10.0D, 9.0D, 2.0D);
	private static final VoxelShape bW13 = Block.makeCuboidShape(13.0D, 6.0D, 6.0D, 14.0D, 9.0D, 10.0D);
	private static final VoxelShape bN13 = Block.makeCuboidShape(6.0D, 6.0D, 13.0D, 10.0D, 9.0D, 14.0D);
	private static final VoxelShape v1_6E = VoxelShapes.or(bE13, bE12);
	private static final VoxelShape v1_6S = VoxelShapes.or(bS13, bS12);
	private static final VoxelShape v1_6W = VoxelShapes.or(bW13, bW12);
	private static final VoxelShape v1_6N = VoxelShapes.or(bN13, bN12);

	private static final VoxelShape v2_0E = VoxelShapes.or(v1_0E, v1_1E);
	private static final VoxelShape v2_0S = VoxelShapes.or(v1_0S, v1_1S);
	private static final VoxelShape v2_0W = VoxelShapes.or(v1_0W, v1_1W);
	private static final VoxelShape v2_0N = VoxelShapes.or(v1_0N, v1_1N);
	private static final VoxelShape v2_1E = VoxelShapes.or(v1_2E, v1_3E);
	private static final VoxelShape v2_1S = VoxelShapes.or(v1_2S, v1_3S);
	private static final VoxelShape v2_1W = VoxelShapes.or(v1_2W, v1_3W);
	private static final VoxelShape v2_1N = VoxelShapes.or(v1_2N, v1_3N);
	private static final VoxelShape v2_2E = VoxelShapes.or(v1_4E, v1_5E);
	private static final VoxelShape v2_2S = VoxelShapes.or(v1_4S, v1_5S);
	private static final VoxelShape v2_2W = VoxelShapes.or(v1_4W, v1_5W);
	private static final VoxelShape v2_2N = VoxelShapes.or(v1_4N, v1_5N);

	private static final VoxelShape v3_0E = VoxelShapes.or(v2_0E, v2_1E);
	private static final VoxelShape v3_0S = VoxelShapes.or(v2_0S, v2_1S);
	private static final VoxelShape v3_0W = VoxelShapes.or(v2_0W, v2_1W);
	private static final VoxelShape v3_0N = VoxelShapes.or(v2_0N, v2_1N);

	private static final VoxelShape cV0E = VoxelShapes.or(v1_6E, v3_0E);
	private static final VoxelShape cV0S = VoxelShapes.or(v1_6S, v3_0S);
	private static final VoxelShape cV0W = VoxelShapes.or(v1_6W, v3_0W);
	private static final VoxelShape cV0N = VoxelShapes.or(v1_6N, v3_0N);
	private static final VoxelShape cV1E = VoxelShapes.or(v2_2E, cV0E);
	private static final VoxelShape cV1S = VoxelShapes.or(v2_2S, cV0S);
	private static final VoxelShape cV1W = VoxelShapes.or(v2_2W, cV0W);
	private static final VoxelShape cV1N = VoxelShapes.or(v2_2N, cV0N);

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public Pedestal() {
		super(Properties.create(Material.ROCK));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {

		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof PedestalTileEntity) {

			if (((PedestalTileEntity) tile).getItem() != null
					&& ((PedestalTileEntity) tile).getItem() != ItemStack.EMPTY) {

				ItemHandlerHelper.giveItemToPlayer(player, ((PedestalTileEntity) tile).removeItem());

				if (worldIn.isRemote)
					return true;

				worldIn.notifyBlockUpdate(pos, state, state, 2);
				return true;
			} else {
				ItemStack stack = player.getHeldItem(handIn);

				if (stack.getItem() == ItemInit.MAGIC_CARD.get() || stack.getItem() == ItemInit.MAGIC_CARD_GOLD.get()
						|| stack.getItem() == ItemInit.MAGIC_CARD_IRON.get()
						|| stack.getItem() == ItemInit.MAGIC_CARD_DIAMOND.get()
						|| stack.getItem() == ItemInit.MAGIC_CARD_EMERALD.get()) {

					if (worldIn.isRemote)
						return true;

					ItemStack newStack = stack.copy();
					stack.shrink(1);
					newStack.setCount(1);
					((PedestalTileEntity) tile).setItem(newStack);

					worldIn.notifyBlockUpdate(pos, state, state, 2);

					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		
		if(worldIn.isRemote)return;
		
		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((PedestalTileEntity)worldIn.getTileEntity(pos)).removeItem());
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {

		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {

		return TileEntityInit.PEDESTAL_TE.get().create();
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {

		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

		switch ((Direction) state.get(FACING)) {
		case NORTH:
			return cV1N;
		case SOUTH:
			return cV1S;
		case EAST:
			return cV1E;
		case WEST:
			return cV1W;
		default:
			return cV1N;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {

		switch ((Direction) state.get(FACING)) {
		case NORTH:
			return cV1N;
		case SOUTH:
			return cV1S;
		case EAST:
			return cV1E;
		case WEST:
			return cV1W;
		default:
			return cV1N;
		}
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {

		switch ((Direction) state.get(FACING)) {
		case NORTH:
			return cV1N;
		case SOUTH:
			return cV1S;
		case EAST:
			return cV1E;
		case WEST:
			return cV1W;
		default:
			return cV1N;
		}
	}

	@SuppressWarnings("deprecation")
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {

		builder.add(WATERLOGGED).add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return super.getStateForPlacement(context)
				.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER))
				.with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {

		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return stateIn;
	}
}
