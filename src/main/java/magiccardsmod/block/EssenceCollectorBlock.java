package magiccardsmod.block;

import java.util.Random;

import magiccardsmod.init.ItemInit;
import magiccardsmod.init.TileEntityInit;
import magiccardsmod.tileentitys.CollectorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
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
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EssenceCollectorBlock extends Block {

	private static final VoxelShape bN0 = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);

	private static final VoxelShape bN1 = Block.makeCuboidShape(3.0D, 2.0D, 3.0D, 13.0D, 10.0D, 13.0D);

	private static final VoxelShape v1_0N = VoxelShapes.or(bN1, bN0);

	private static final VoxelShape bN2 = Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 1.0D, 1.0D);

	private static final VoxelShape bN3 = Block.makeCuboidShape(2.0D, 0.0D, 15.0D, 14.0D, 1.0D, 16.0D);

	private static final VoxelShape v1_1N = VoxelShapes.or(bN3, bN2);

	private static final VoxelShape bN4 = Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 1.0D, 1.0D, 14.0D);

	private static final VoxelShape bN5 = Block.makeCuboidShape(15.0D, 0.0D, 2.0D, 16.0D, 1.0D, 14.0D);

	private static final VoxelShape v1_2N = VoxelShapes.or(bN5, bN4);

	private static final VoxelShape bN6 = Block.makeCuboidShape(2.0D, 10.0D, 2.0D, 14.0D, 13.0D, 14.0D);

	private static final VoxelShape bN7 = Block.makeCuboidShape(3.0D, 13.0D, 3.0D, 13.0D, 14.0D, 13.0D);

	private static final VoxelShape v1_3N = VoxelShapes.or(bN7, bN6);

	private static final VoxelShape v2_0N = VoxelShapes.or(v1_0N, v1_1N);

	private static final VoxelShape v2_1N = VoxelShapes.or(v1_2N, v1_3N);

	private static final VoxelShape v3_0N = VoxelShapes.or(v2_0N, v2_1N);

	public static final IntegerProperty SPHERE = IntegerProperty.create("sphere", 0, 3);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	static Block.Properties p;

	public EssenceCollectorBlock() {

		super(p = Properties.create(Material.ROCK).sound(SoundType.STONE).lightValue(5));

		setDefaultState(this.stateContainer.getBaseState().with(SPHERE, 0).with(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

		return v3_0N;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {

		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof CollectorTileEntity) {

			CollectorTileEntity collector = (CollectorTileEntity) tile;

			if (collector.getConcentration() < 1)
				return true;

			if (collector.getWorld().isRemote) {

				return true;
			}
			ItemStack essence = ItemStack.EMPTY;

			if (collector.getWorld().getDimension().isNether())
				essence = new ItemStack(ItemInit.HELL_ESSENCE.get());
			else if (collector.getPos().getY() > 190)
				essence = new ItemStack(ItemInit.WIND_ESSENCE.get());
			else
				essence = new ItemStack(ItemInit.NATURE_ESSENCE.get());

			collector.setConcentration(0);

			player.inventory.placeItemBackInInventory(worldIn, essence);

			worldIn.notifyBlockUpdate(pos, state, state, 2);

			return true;

		}
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		
		super.animateTick(stateIn, worldIn, pos, rand);
		
		for (int g = 0; g < 5; g++) {
			Random r = new Random();
			
			int x = (r.nextBoolean() ? 1 : -1) * r.nextInt(2), y = (r.nextBoolean() ? 1 : -1) * r.nextInt(2),
					z = (r.nextBoolean() ? 1 : -1) * r.nextInt(2);

			worldIn.addParticle(ParticleTypes.ENCHANT, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D,
					(double) pos.getZ() + 0.5D, (double) ((float) x + rand.nextFloat()) - 0.5D,
					(double) ((float) y - rand.nextFloat() - 1.0F), (double) ((float) z+ rand.nextFloat()) - 0.5D);
		}
	}

	@SuppressWarnings("deprecation")
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {

		builder.add(WATERLOGGED).add(SPHERE);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return super.getStateForPlacement(context)
				.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER)).with(SPHERE, 0);
	}

	@Override
	public int getLightValue(BlockState state, IEnviromentBlockReader world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof CollectorTileEntity) {

			if (!((CollectorTileEntity) tile).b)
				return 5;

			tile.getWorld().notifyBlockUpdate(pos, state, state, 8);

			return (int) (5 + 10 * ((CollectorTileEntity) tile).getConcentration());
		}
		return 5;
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

		return TileEntityInit.COLLECTOR_TE.get().create();
	}
}
