package magiccardsmod;

import java.util.List;
import java.util.Map;

import magiccardsmod.init.EnchantmentInit;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class MagiccardItemInformationUtil {
	
	public static void getInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		
		if (stack.isEnchanted()) {
			
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
			
			if (enchantments.containsKey(EnchantmentInit.CAPTURE_ENTITY_CARD_ENCHANTMENT.get())&&stack.getTag().contains("capturedentity")) {
				
				@SuppressWarnings("deprecation")
				String translationKey = Registry.ENTITY_TYPE
						.getValue(new ResourceLocation(stack.getTag().getCompound("capturedentity").getString("id"))).get().getTranslationKey();
				
				tooltip.add(new TranslationTextComponent("Captured: " ).appendSibling(new TranslationTextComponent(translationKey))
								.setStyle(new Style().setColor(TextFormatting.GRAY)));
			}
			if (enchantments.containsKey(EnchantmentInit.PLAYDISC_CARD_ENCHANTMENT.get())||
					enchantments.containsKey(EnchantmentInit.ORESEEKER_CARD_ENCHANTMENT.get())||
					enchantments.containsKey(EnchantmentInit.POTION_CARD_ENCHANTMENT.get())) {
				
				for(String s : stack.getOrCreateTag().keySet())
					if(s.contains("arg")) {
						
						ItemStack nbtStack = ItemStack.read(stack.getTag().getCompound(s));
						tooltip.add(new TranslationTextComponent(nbtStack.getTranslationKey()).setStyle(new Style().setColor(TextFormatting.GRAY)));
						}
			}
		}
		
	}
	
}
