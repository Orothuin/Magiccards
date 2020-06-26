package magiccardsmod.block;

import magiccardsmod.init.TileEntityInit;
import magiccardsmod.tileentitys.CardTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraftforge.fml.network.NetworkHooks;

public class CardTableBlock extends Block implements IWaterLoggable {

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private static final VoxelShape pedestalBottom = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
	
	private static final VoxelShape pedestalPoleN = Block.makeCuboidShape(5.0D, 2.0D, 4.0D, 11.0D, 10.0D, 13.0D);
	private static final VoxelShape pedestalPoleE = Block.makeCuboidShape(4.0D, 2.0D, 5.0D, 13.0D, 10.0D, 11.0D);
	private static final VoxelShape pedestalPoleS = Block.makeCuboidShape(5.0D, 2.0D, 12.0D, 11.0D, 10.0D, 3.0D);
	private static final VoxelShape pedestalPoleW = Block.makeCuboidShape(4.0D, 2.0D, 11.0D, 13.0D, 10.0D, 5.0D);
	
	private static final VoxelShape rendershapeN = VoxelShapes.or(pedestalBottom,pedestalPoleN);
	private static final VoxelShape rendershapeE = VoxelShapes.or(pedestalBottom,pedestalPoleE);
	private static final VoxelShape rendershapeS = VoxelShapes.or(pedestalBottom,pedestalPoleS);
	private static final VoxelShape rendershapeW = VoxelShapes.or(pedestalBottom,pedestalPoleW);
	private static final VoxelShape invisibleWalkTop = Block.makeCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 11.0D, 16.0D);
	private static final VoxelShape hitbox = VoxelShapes.or(rendershapeN,invisibleWalkTop);
	private static final VoxelShape pedestalN = VoxelShapes.or(Block.makeCuboidShape(0.0D, 9.0D, 2.0D, 16.0D, 13.0D, 8.0D),Block.makeCuboidShape(0.0D, 11.0D, 8.0D, 16.0D, 15.0D, 14.0D),rendershapeN);
	private static final VoxelShape pedestalE = VoxelShapes.or(Block.makeCuboidShape(2.0D, 15.0D, 0.0D, 8.0D, 11.0D, 16.0D),Block.makeCuboidShape(8.0D, 13.0D, 0.0D, 14.0D, 9.0D, 16.0D),rendershapeE);
	private static final VoxelShape pedestalS = VoxelShapes.or(Block.makeCuboidShape(16.0D, 9.0D, 14.0D, 0.0D, 13.0D, 8.0D),Block.makeCuboidShape(16.0D, 11.0D, 8.0D, 0.0D, 15.0D, 2.0D),rendershapeS);
	private static final VoxelShape pedestalW = VoxelShapes.or(Block.makeCuboidShape(14.0D, 15.0D, 16.0D, 8.0D, 11.0D, 0.0D),Block.makeCuboidShape(8.0D, 13.0D, 16.0D, 2.0D, 9.0D, 0.0D),rendershapeW);
	
	
	public CardTableBlock() {
		super(Properties.create(Material.WOOD).sound(SoundType.WOOD));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		
		TileEntity tile = worldIn.getTileEntity(pos);
		
		if(tile instanceof CardTableTileEntity) {
			if(worldIn.isRemote)return true;
			NetworkHooks.openGui((ServerPlayerEntity)player, (CardTableTileEntity)tile,pos);
			return true;
		}
		return true;
	}
	
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		
		switch((Direction)state.get(FACING)) {
	      case NORTH:
	         return pedestalN;
	      case SOUTH:
	         return pedestalS;
	      case EAST:
	         return pedestalE;
	      case WEST:
	         return pedestalW;
	      default:
	         return pedestalN;
	      }
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {
		
		return hitbox;
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		
		switch((Direction)state.get(FACING)) {
	      case NORTH:
	         return rendershapeN;
	      case SOUTH:
	         return rendershapeS;
	      case EAST:
	         return rendershapeE;
	      case WEST:
	         return rendershapeW;
	      default:
	         return pedestalN;
	      }
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
		return false;
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
		return super.getStateForPlacement(context).with(WATERLOGGED,
				Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER)).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
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
		
		return TileEntityInit.CARD_TABLE_TE.get().create() ;
	}
}
