package magiccardsmod.enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import magiccardsmod.entity.AbstractCardEntity;
import magiccardsmod.init.EnchantmentInit;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class OreSeeker_CardEnchantment extends Enchantment {

	public static final int defaultRadius = 10;

	public OreSeeker_CardEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMaxLevel() {

		return 1;
	}

	@Override
	public int getMinLevel() {

		return 1;
	}

	private static Map<Entity, BlockPos> targets = new HashMap<>();

	public static void applyOreSeeker(Entity entity) {

		if (entity instanceof AbstractCardEntity ) {

			ItemStack card = ((AbstractCardEntity) entity).getThrownCard();

			if (!card.isEnchanted())
				return;

			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(card);

			if (enchantments.containsKey(EnchantmentInit.ORESEEKER_CARD_ENCHANTMENT.get())) {

				int level = enchantments.get(EnchantmentInit.ORESEEKER_CARD_ENCHANTMENT.get());

				Block targetedBlock = Block.getBlockFromItem(((AbstractCardEntity) entity).getOre().getItem());
				
				ArrayList<BlockPos> possibleTargets = new ArrayList<>();
				
				BlockPos target = targets.get(entity);
				
				if (target == null && !entity.world.isRemote && targetedBlock != null) {

					for (BlockPos pos : BlockPos.getAllInBoxMutable(
							
							entity.getPosition().west(defaultRadius + 2 * level).south(defaultRadius + 2 * level)
									.down(entity.getPosition().getY()),
							entity.getPosition().east(defaultRadius + 2 * level).north(defaultRadius + 2 * level)
									)) {

						if (entity.world.getBlockState(pos).getBlock() == targetedBlock) {
							
							possibleTargets.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
						}

					}
					
					double lenght= Double.MAX_VALUE;
					
					BlockPos nearestPos=null;
					
					for(BlockPos pos : possibleTargets)
						
						if(getDistance(entity.getPosition(), pos)<lenght) {
							
							lenght=getDistance(entity.getPosition(), pos);
							nearestPos=pos;
						}
					targets.put(entity, nearestPos);
				}
				if (target != null) {

					Vec2f oreDir = new Vec2f(target.getX() - (float) entity.posX + 0.5f,
							target.getZ() - (float) entity.posZ + 0.5f);

					Vec3d cardMotion = entity.getMotion();
					
					float length = (float) Math.sqrt(oreDir.x * oreDir.x + oreDir.y * oreDir.y);
					
					float cMLength = (float) Math.sqrt(cardMotion.x * cardMotion.x + cardMotion.z * cardMotion.z);
					
					Vec2f dif = new Vec2f(oreDir.x/length - (float) cardMotion.x/cMLength, oreDir.y/length - (float) cardMotion.z/cMLength);

					double multCurve =  1.3f*Math.pow(Math.E, -0.02f * length)+1.2;
					
					Vec2f newDir = new Vec2f((float) ((float) cardMotion.x + 0.09f * multCurve * dif.x),
							(float) ((float) cardMotion.z + 0.09f * multCurve * dif.y));

					length = (float) Math.sqrt(newDir.x * newDir.x + newDir.y * newDir.y);

					newDir = new Vec2f(newDir.x / length, newDir.y / length);

					length = (float) Math.sqrt(oreDir.x * oreDir.x + oreDir.y * oreDir.y);

					double mult =cMLength* Math.pow(Math.E, -2.1f / length) + 0.1f;
					
					double xMotion =newDir.x * (mult);
					double zMotion = newDir.y * (mult);
					double yMotion =entity.getMotion().getY()* (entity.getMotion().getY()<0 ? 0.7f:1);
					
					float yaw = (float) (MathHelper.atan2(newDir.x*mult, newDir.y*mult)
							* (double) (180F / (float) Math.PI));
					
					if(length<1&&yMotion<0) {
						yMotion*=1.4;
						xMotion*=0.8;
						zMotion*=0.8;
						
						yaw=entity.rotationYaw;
					}
		
					entity.setMotion(xMotion,yMotion,zMotion);

					entity.rotationYaw = yaw;
				}
			}
		}
	}
	
	private static double getDistance(BlockPos start,BlockPos end) {
		return Math.sqrt(Math.pow(end.getX()-start.getX()+0.5f,2)+Math.pow(end.getY()-start.getY()+0.5f,2)+Math.pow(end.getZ()-start.getZ()+0.5f,2));
	}
}
