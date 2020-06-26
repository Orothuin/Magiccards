package magiccardsmod.enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import magiccardsmod.MagicCardsMod;
import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class LeaveDecay_CardEnchantment extends Enchantment{
	
	private static Map<ArrayList<BlockPos>,Integer> leaveQue = new HashMap<>();
	
	public LeaveDecay_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}
	
	@Override
	public int getMaxLevel() {

		return 2;
	}

	@Override
	public int getMinLevel() {

		return 1;
	}
	
	@Mod.EventBusSubscriber(modid = MagicCardsMod.MODID, bus = Bus.FORGE)
	public static class ApplyEnchantment {
		
		public static final int LISTCAP = 300;
		public static final int MAX_RADIUS=8;
		
		@SubscribeEvent
		public static void onImpact(ProjectileImpactEvent e) {

			if (!e.isCanceled() && e.getEntity() instanceof AbstractCardEntity
					&& e.getRayTraceResult().getType()==RayTraceResult.Type.BLOCK) {
				
				if(e.getEntity().getEntityWorld().isRemote)return;
				
				ItemStack card = ((AbstractCardEntity) e.getEntity()).getThrownCard();
				
				if(!card.isEnchanted())return;
				
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

				if (enchantments.containsKey(EnchantmentInit.LEAVEDECAY_CARD_ENCHANTMENT.get())) {
					
					BlockPos pos = ((BlockRayTraceResult)e.getRayTraceResult()).getPos();
					
					Block blockType = e.getEntity().getEntityWorld().getBlockState(pos).getBlock();
					
					if(blockType instanceof LeavesBlock) {
						
						IWorld world = e.getEntity().getEntityWorld();
						
						ArrayList<BlockPos> leaveRoof = new ArrayList<>();
						getLeaves(blockType,pos,pos,world,leaveRoof);
						
						leaveQue.put(leaveRoof,enchantments.get(EnchantmentInit.LEAVEDECAY_CARD_ENCHANTMENT.get()));
					}
				}
			}
		}
		
		@SubscribeEvent
		public static void onTick(WorldTickEvent e) {
			
			if(e.world.isRemote)return;
			
			ArrayList<ArrayList<BlockPos>> temp = new ArrayList<>();
			
			for(ArrayList<BlockPos> que : leaveQue.keySet()) {
				
				if(que.isEmpty()) {
					temp.add(que);
					continue;
				}
				
				if(new Random().nextFloat()<0.1+0.4*leaveQue.get(que))return;
				
				e.world.destroyBlock(que.get(0),leaveQue.get(que)==2);
				que.remove(0);
			}
			for(ArrayList<BlockPos> a : temp)
			leaveQue.remove(a);
		}
	
		
		private static void getLeaves(Block blockType,BlockPos start,BlockPos initial,IWorld world,ArrayList<BlockPos> list){
			
			if(list.size()>=LISTCAP)return;
			
			if(Math.sqrt(Math.pow(start.getX()-initial.getX(), 2)+Math.pow(start.getY()-initial.getY(), 2)+Math.pow(start.getZ()-initial.getZ(), 2))>MAX_RADIUS)return;
			
			list.add(new BlockPos(start.getX(), start.getY(), start.getZ()));
			
			for(BlockPos pos : BlockPos.getAllInBoxMutable(start.down().west().south(),start.up().east().north())){
				
				if(!list.contains(pos)&&world.getBlockState(pos).getBlock()==blockType) {
					getLeaves(blockType, pos,initial, world, list);
				}
			}
		}
	}
}
