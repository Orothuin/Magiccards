package magiccardsmod.block;

import magiccardsmod.init.TileEntityInit;
import magiccardsmod.tileentitys.CardPressTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.items.ItemStackHandler;

public class CardPressBlock extends Block {
	
	private static final VoxelShape bE0 = Block.makeCuboidShape(16.0D,0.0D,0.0D,0.0D,7.0D,16.0D);
	private static final VoxelShape bS0 = Block.makeCuboidShape(0.0D,0.0D,16.0D,16.0D,7.0D,0.0D);
	private static final VoxelShape bW0 = Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
	private static final VoxelShape bN0 = Block.makeCuboidShape(0.0D,0.0D,0.0D,16.0D,7.0D,16.0D);
	private static final VoxelShape bE1 = Block.makeCuboidShape(16.0D,7.0D,0.0D,15.0D,10.0D,15.0D);
	private static final VoxelShape bS1 = Block.makeCuboidShape(0.0D,7.0D,16.0D,15.0D,10.0D,15.0D);
	private static final VoxelShape bW1 = Block.makeCuboidShape(0.0D,7.0D,0.0D,1.0D,10.0D,15.0D);
	private static final VoxelShape bN1 = Block.makeCuboidShape(0.0D,7.0D,0.0D,15.0D,10.0D,1.0D);
	private static final VoxelShape v1_0E = VoxelShapes.or(bE1,bE0);
	private static final VoxelShape v1_0S = VoxelShapes.or(bS1,bS0);
	private static final VoxelShape v1_0W = VoxelShapes.or(bW1,bW0);
	private static final VoxelShape v1_0N = VoxelShapes.or(bN1,bN0);
	private static final VoxelShape bE2 = Block.makeCuboidShape(15.0D,7.0D,0.0D,0.0D,10.0D,1.0D);
	private static final VoxelShape bS2 = Block.makeCuboidShape(0.0D,7.0D,15.0D,1.0D,10.0D,0.0D);
	private static final VoxelShape bW2 = Block.makeCuboidShape(1.0D,7.0D,0.0D,16.0D,10.0D,1.0D);
	private static final VoxelShape bN2 = Block.makeCuboidShape(0.0D,7.0D,1.0D,1.0D,10.0D,16.0D);
	private static final VoxelShape bE3 = Block.makeCuboidShape(1.0D,7.0D,1.0D,0.0D,10.0D,16.0D);
	private static final VoxelShape bS3 = Block.makeCuboidShape(1.0D,7.0D,1.0D,16.0D,10.0D,0.0D);
	private static final VoxelShape bW3 = Block.makeCuboidShape(15.0D,7.0D,1.0D,16.0D,10.0D,16.0D);
	private static final VoxelShape bN3 = Block.makeCuboidShape(1.0D,7.0D,15.0D,16.0D,10.0D,16.0D);
	private static final VoxelShape v1_1E = VoxelShapes.or(bE3,bE2);
	private static final VoxelShape v1_1S = VoxelShapes.or(bS3,bS2);
	private static final VoxelShape v1_1W = VoxelShapes.or(bW3,bW2);
	private static final VoxelShape v1_1N = VoxelShapes.or(bN3,bN2);
	private static final VoxelShape bE4 = Block.makeCuboidShape(16.0D,7.0D,15.0D,1.0D,10.0D,16.0D);
	private static final VoxelShape bS4 = Block.makeCuboidShape(15.0D,7.0D,16.0D,16.0D,10.0D,1.0D);
	private static final VoxelShape bW4 = Block.makeCuboidShape(0.0D,7.0D,15.0D,15.0D,10.0D,16.0D);
	private static final VoxelShape bN4 = Block.makeCuboidShape(15.0D,7.0D,0.0D,16.0D,10.0D,15.0D);
	private static final VoxelShape bE5 = Block.makeCuboidShape(15.0D,7.0D,5.25D,1.0D,9.0D,5.75D);
	private static final VoxelShape bS5 = Block.makeCuboidShape(5.25D,7.0D,15.0D,5.75D,9.0D,1.0D);
	private static final VoxelShape bW5 = Block.makeCuboidShape(1.0D,7.0D,5.25D,15.0D,9.0D,5.75D);
	private static final VoxelShape bN5 = Block.makeCuboidShape(5.25D,7.0D,1.0D,5.75D,9.0D,15.0D);
	private static final VoxelShape v1_2E = VoxelShapes.or(bE5,bE4);
	private static final VoxelShape v1_2S = VoxelShapes.or(bS5,bS4);
	private static final VoxelShape v1_2W = VoxelShapes.or(bW5,bW4);
	private static final VoxelShape v1_2N = VoxelShapes.or(bN5,bN4);
	private static final VoxelShape bE6 = Block.makeCuboidShape(15.0D,7.0D,10.25D,1.0D,9.0D,10.75D);
	private static final VoxelShape bS6 = Block.makeCuboidShape(10.25D,7.0D,15.0D,10.75D,9.0D,1.0D);
	private static final VoxelShape bW6 = Block.makeCuboidShape(1.0D,7.0D,10.25D,15.0D,9.0D,10.75D);
	private static final VoxelShape bN6 = Block.makeCuboidShape(10.25D,7.0D,1.0D,10.75D,9.0D,15.0D);
	private static final VoxelShape bE7 = Block.makeCuboidShape(8.25D,7.0D,10.75D,7.75D,9.0D,15.0D);
	private static final VoxelShape bS7 = Block.makeCuboidShape(10.75D,7.0D,8.25D,15.0D,9.0D,7.75D);
	private static final VoxelShape bW7 = Block.makeCuboidShape(7.75D,7.0D,10.75D,8.25D,9.0D,15.0D);
	private static final VoxelShape bN7 = Block.makeCuboidShape(10.75D,7.0D,7.75D,15.0D,9.0D,8.25D);
	private static final VoxelShape v1_3E = VoxelShapes.or(bE7,bE6);
	private static final VoxelShape v1_3S = VoxelShapes.or(bS7,bS6);
	private static final VoxelShape v1_3W = VoxelShapes.or(bW7,bW6);
	private static final VoxelShape v1_3N = VoxelShapes.or(bN7,bN6);
	private static final VoxelShape bE8 = Block.makeCuboidShape(8.25D,7.0D,5.75D,7.75D,9.0D,10.25D);
	private static final VoxelShape bS8 = Block.makeCuboidShape(5.75D,7.0D,8.25D,10.25D,9.0D,7.75D);
	private static final VoxelShape bW8 = Block.makeCuboidShape(7.75D,7.0D,5.75D,8.25D,9.0D,10.25D);
	private static final VoxelShape bN8 = Block.makeCuboidShape(5.75D,7.0D,7.75D,10.25D,9.0D,8.25D);
	private static final VoxelShape bE9 = Block.makeCuboidShape(8.25D,7.0D,1.0D,7.75D,9.0D,5.25D);
	private static final VoxelShape bS9 = Block.makeCuboidShape(1.0D,7.0D,8.25D,5.25D,9.0D,7.75D);
	private static final VoxelShape bW9 = Block.makeCuboidShape(7.75D,7.0D,1.0D,8.25D,9.0D,5.25D);
	private static final VoxelShape bN9 = Block.makeCuboidShape(1.0D,7.0D,7.75D,5.25D,9.0D,8.25D);
	private static final VoxelShape v1_4E = VoxelShapes.or(bE9,bE8);
	private static final VoxelShape v1_4S = VoxelShapes.or(bS9,bS8);
	private static final VoxelShape v1_4W = VoxelShapes.or(bW9,bW8);
	private static final VoxelShape v1_4N = VoxelShapes.or(bN9,bN8);
	private static final VoxelShape bE10 = Block.makeCuboidShape(12.0D,0.25D,-1.25D,4.0D,19.0D,0.75D);
	private static final VoxelShape bS10 = Block.makeCuboidShape(-1.25D,0.25D,12.0D,0.75D,19.0D,4.0D);
	private static final VoxelShape bW10 = Block.makeCuboidShape(4.0D,0.25D,-1.25D,12.0D,19.0D,0.75D);
	private static final VoxelShape bN10 = Block.makeCuboidShape(-1.25D,0.25D,4.0D,0.75D,19.0D,12.0D);
	private static final VoxelShape bE11 = Block.makeCuboidShape(12.0D,0.25D,15.25D,4.0D,19.0D,17.25D);
	private static final VoxelShape bS11 = Block.makeCuboidShape(15.25D,0.25D,12.0D,17.25D,19.0D,4.0D);
	private static final VoxelShape bW11 = Block.makeCuboidShape(4.0D,0.25D,15.25D,12.0D,19.0D,17.25D);
	private static final VoxelShape bN11 = Block.makeCuboidShape(15.25D,0.25D,4.0D,17.25D,19.0D,12.0D);
	private static final VoxelShape v1_5E = VoxelShapes.or(bE11,bE10);
	private static final VoxelShape v1_5S = VoxelShapes.or(bS11,bS10);
	private static final VoxelShape v1_5W = VoxelShapes.or(bW11,bW10);
	private static final VoxelShape v1_5N = VoxelShapes.or(bN11,bN10);
	private static final VoxelShape bE12 = Block.makeCuboidShape(11.0D,17.0D,-1.0D,5.0D,20.0D,17.0D);
	private static final VoxelShape bS12 = Block.makeCuboidShape(-1.0D,17.0D,11.0D,17.0D,20.0D,5.0D);
	private static final VoxelShape bW12 = Block.makeCuboidShape(5.0D,17.0D,-1.0D,11.0D,20.0D,17.0D);
	private static final VoxelShape bN12 = Block.makeCuboidShape(-1.0D,17.0D,5.0D,17.0D,20.0D,11.0D);
	private static final VoxelShape bE13 = Block.makeCuboidShape(16.25D,0.25D,-0.25D,15.0D,10.25D,1.0D);
	private static final VoxelShape bS13 = Block.makeCuboidShape(-0.25D,0.25D,16.25D,1.0D,10.25D,15.0D);
	private static final VoxelShape bW13 = Block.makeCuboidShape(-0.25D,0.25D,-0.25D,1.0D,10.25D,1.0D);
	private static final VoxelShape bN13 = Block.makeCuboidShape(-0.25D,0.25D,-0.25D,1.0D,10.25D,1.0D);
	private static final VoxelShape v1_6E = VoxelShapes.or(bE13,bE12);
	private static final VoxelShape v1_6S = VoxelShapes.or(bS13,bS12);
	private static final VoxelShape v1_6W = VoxelShapes.or(bW13,bW12);
	private static final VoxelShape v1_6N = VoxelShapes.or(bN13,bN12);
	private static final VoxelShape bE14 = Block.makeCuboidShape(16.25D,0.25D,15.0D,15.0D,10.25D,16.25D);
	private static final VoxelShape bS14 = Block.makeCuboidShape(15.0D,0.25D,16.25D,16.25D,10.25D,15.0D);
	private static final VoxelShape bW14 = Block.makeCuboidShape(-0.25D,0.25D,15.0D,1.0D,10.25D,16.25D);
	private static final VoxelShape bN14 = Block.makeCuboidShape(15.0D,0.25D,-0.25D,16.25D,10.25D,1.0D);
	private static final VoxelShape bE15 = Block.makeCuboidShape(1.0D,0.25D,15.0D,-0.25D,10.25D,16.25D);
	private static final VoxelShape bS15 = Block.makeCuboidShape(15.0D,0.25D,1.0D,16.25D,10.25D,-0.25D);
	private static final VoxelShape bW15 = Block.makeCuboidShape(15.0D,0.25D,15.0D,16.25D,10.25D,16.25D);
	private static final VoxelShape bN15 = Block.makeCuboidShape(15.0D,0.25D,15.0D,16.25D,10.25D,16.25D);
	private static final VoxelShape v1_7E = VoxelShapes.or(bE15,bE14);
	private static final VoxelShape v1_7S = VoxelShapes.or(bS15,bS14);
	private static final VoxelShape v1_7W = VoxelShapes.or(bW15,bW14);
	private static final VoxelShape v1_7N = VoxelShapes.or(bN15,bN14);

	private static final VoxelShape v2_0E = VoxelShapes.or(v1_0E,v1_1E);
	private static final VoxelShape v2_0S = VoxelShapes.or(v1_0S,v1_1S);
	private static final VoxelShape v2_0W = VoxelShapes.or(v1_0W,v1_1W);
	private static final VoxelShape v2_0N = VoxelShapes.or(v1_0N,v1_1N);
	private static final VoxelShape v2_1E = VoxelShapes.or(v1_2E,v1_3E);
	private static final VoxelShape v2_1S = VoxelShapes.or(v1_2S,v1_3S);
	private static final VoxelShape v2_1W = VoxelShapes.or(v1_2W,v1_3W);
	private static final VoxelShape v2_1N = VoxelShapes.or(v1_2N,v1_3N);
	private static final VoxelShape v2_2E = VoxelShapes.or(v1_4E,v1_5E);
	private static final VoxelShape v2_2S = VoxelShapes.or(v1_4S,v1_5S);
	private static final VoxelShape v2_2W = VoxelShapes.or(v1_4W,v1_5W);
	private static final VoxelShape v2_2N = VoxelShapes.or(v1_4N,v1_5N);
	private static final VoxelShape v2_3E = VoxelShapes.or(v1_6E,v1_7E);
	private static final VoxelShape v2_3S = VoxelShapes.or(v1_6S,v1_7S);
	private static final VoxelShape v2_3W = VoxelShapes.or(v1_6W,v1_7W);
	private static final VoxelShape v2_3N = VoxelShapes.or(v1_6N,v1_7N);

	private static final VoxelShape v3_0E = VoxelShapes.or(v2_0E,v2_1E);
	private static final VoxelShape v3_0S = VoxelShapes.or(v2_0S,v2_1S);
	private static final VoxelShape v3_0W = VoxelShapes.or(v2_0W,v2_1W);
	private static final VoxelShape v3_0N = VoxelShapes.or(v2_0N,v2_1N);
	private static final VoxelShape v3_1E = VoxelShapes.or(v2_2E,v2_3E);
	private static final VoxelShape v3_1S = VoxelShapes.or(v2_2S,v2_3S);
	private static final VoxelShape v3_1W = VoxelShapes.or(v2_2W,v2_3W);
	private static final VoxelShape v3_1N = VoxelShapes.or(v2_2N,v2_3N);

	private static final VoxelShape v4_0E = VoxelShapes.or(v3_0E,v3_1E);
	private static final VoxelShape v4_0S = VoxelShapes.or(v3_0S,v3_1S);
	private static final VoxelShape v4_0W = VoxelShapes.or(v3_0W,v3_1W);
	private static final VoxelShape v4_0N = VoxelShapes.or(v3_0N,v3_1N);

	
	/**
	 * only for rendering
	 */
	public static final BooleanProperty STOMP = BooleanProperty.create("stomp");
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public CardPressBlock() {
		super(Properties.create(Material.WOOD).sound(SoundType.WOOD));

		setDefaultState(this.stateContainer.getBaseState().with(STOMP, false).with(FACING, Direction.NORTH)
				.with(WATERLOGGED, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch ((Direction) state.get(FACING)) {
		case NORTH:
			return v4_0N;
		case SOUTH:
			return v4_0S;
		case EAST:
			return v4_0E;
		case WEST:
			return v4_0W;
		default:
			return v4_0N;
		}
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {

		ItemStack stack = player.getHeldItem(handIn);

		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof CardPressTileEntity) {

			CardPressTileEntity press = (CardPressTileEntity) tile;

			ItemStackHandler inv = press.getInputInventory();

			ItemStack stackSlot = inv.getStackInSlot(0);

			ItemStackHandler invOut = press.getOutputInventory();

			if (!invOut.getStackInSlot(0).isEmpty()) {

				if (worldIn.isRemote)
					return true;
				ItemHandlerHelper.giveItemToPlayer(player,
						invOut.extractItem(0, invOut.getStackInSlot(0).getCount(), false));

				worldIn.notifyBlockUpdate(pos, state, state, 2);

				return true;
			}

			if (!stack.isEmpty() && stack.getItem() == stackSlot.getItem() && stackSlot.getCount() < 3) {

				if (worldIn.isRemote)
					return true;

				int shrink = Math.min(stack.getCount(), 3 - stackSlot.getCount());

				stack.shrink(shrink);

				stackSlot.grow(shrink);

				inv.setStackInSlot(0, stackSlot.copy());

				return true;

			} else if ((stack.getItem() == Items.PAPER || stack.getItem() == Items.DIAMOND
					|| stack.getItem() == Items.EMERALD) && stackSlot.isEmpty()) {
				
				if (worldIn.isRemote)
					return true;

				boolean b = stack.getItem() == Items.DIAMOND
						|| stack.getItem() == Items.EMERALD;
				
				int shrink = Math.min(stack.getCount(), b ? 1 : 3);

				inv.setStackInSlot(0, new ItemStack(stack.getItem(), shrink));

				stack.shrink(shrink);

				return true;

			}
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {

		builder.add(WATERLOGGED).add(FACING).add(STOMP);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return super.getStateForPlacement(context)
				.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER))
				.with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(STOMP, false);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {

		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return stateIn;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {

		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {

		return TileEntityInit.CARD_PRESS_TE.get().create();
	}
}
