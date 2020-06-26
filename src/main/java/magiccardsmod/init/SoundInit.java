package magiccardsmod.init;

import magiccardsmod.MagicCardsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundInit {
	
	public static final DeferredRegister<SoundEvent> SOUNDEVENTS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS,
			MagicCardsMod.MODID);

	public static final RegistryObject<SoundEvent> CARD_HIT_SOUND = SOUNDEVENTS.register("cardhitsound",
			() -> new SoundEvent(new ResourceLocation(MagicCardsMod.MODID, "entity.magiccard.hit")));
	
	public static final RegistryObject<SoundEvent> CARD_RIP_SOUND = SOUNDEVENTS.register("cardripsound",
			() -> new SoundEvent(new ResourceLocation(MagicCardsMod.MODID, "entity.magiccard.rip")));
	
	public static final RegistryObject<SoundEvent> CARD_BURN_SOUND = SOUNDEVENTS.register("cardburnsound",
			() -> new SoundEvent(new ResourceLocation(MagicCardsMod.MODID, "entity.magiccard.burn")));
	
}
