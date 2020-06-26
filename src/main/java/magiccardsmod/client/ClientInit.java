package magiccardsmod.client;

import java.awt.Color;

import magiccardsmod.client.renderer.entitys.MagicCardDiamondRenderer;
import magiccardsmod.client.renderer.entitys.MagicCardEmeraldRenderer;
import magiccardsmod.client.renderer.entitys.MagicCardGoldRenderer;
import magiccardsmod.client.renderer.entitys.MagicCardIronRenderer;
import magiccardsmod.client.renderer.entitys.MagicCardRenderer;
import magiccardsmod.client.tileentityrenderer.CardPressRenderer;
import magiccardsmod.client.tileentityrenderer.CollectorRenderer;
import magiccardsmod.client.tileentityrenderer.PedestalRenderer;
import magiccardsmod.entity.MagicCardDiamondEntity;
import magiccardsmod.entity.MagicCardEmeraldEntity;
import magiccardsmod.entity.MagicCardEntity;
import magiccardsmod.entity.MagicCardGoldEntity;
import magiccardsmod.entity.MagicCardIronEntity;
import magiccardsmod.gui.CardTableGui;
import magiccardsmod.gui.InfoCardDeckGui;
import magiccardsmod.init.ContainerInit;
import magiccardsmod.init.ItemInit;
import magiccardsmod.tileentitys.CardPressTileEntity;
import magiccardsmod.tileentitys.CollectorTileEntity;
import magiccardsmod.tileentitys.PedestalTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientInit {
	
	public static void init(final FMLClientSetupEvent event) {
		
		RenderingRegistry.registerEntityRenderingHandler(MagicCardEntity.class, MagicCardRenderer<MagicCardEntity>::new);
		
		RenderingRegistry.registerEntityRenderingHandler(MagicCardIronEntity.class, MagicCardIronRenderer::new);
		
		RenderingRegistry.registerEntityRenderingHandler(MagicCardGoldEntity.class, MagicCardGoldRenderer::new);
		
		RenderingRegistry.registerEntityRenderingHandler(MagicCardDiamondEntity.class, MagicCardDiamondRenderer::new);
		
		RenderingRegistry.registerEntityRenderingHandler(MagicCardEmeraldEntity.class, MagicCardEmeraldRenderer::new);
		
		ScreenManager.registerFactory(ContainerInit.CARDTABLE_TYPE.get(), CardTableGui::new);
		ScreenManager.registerFactory(ContainerInit.INFOCARDDECK_TYPE.get(), InfoCardDeckGui::new);
		
		ClientRegistry.bindTileEntitySpecialRenderer(PedestalTileEntity.class, new PedestalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(CardPressTileEntity.class, new CardPressRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(CollectorTileEntity.class, new CollectorRenderer());
		
		Minecraft.getInstance().getItemColors().register((stack, tintindex )-> {return tintindex > 0 ? -1 : new Color(26,17,9).getRGB();}, ItemInit.STAIN.get());
	}
	
}
