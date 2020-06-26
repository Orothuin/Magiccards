package magiccardsmod.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import magiccardsmod.gui.Page;
import magiccardsmod.gui.RecipePage;
import magiccardsmod.init.ContainerInit;
import magiccardsmod.networking.PacketHandler;
import magiccardsmod.networking.SyncPageMessage;
import magiccardsmod.recipe.EnchantRecipe;
import magiccardsmod.recipe.EnchantRecipeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class InfoCardDeckContainer extends Container {

	private PlayerEntity player;

	private ArrayList<Page> pages;

	public InfoCardDeckContainer(int id, final PlayerInventory playerInv, final PacketBuffer buffer) {

		super(ContainerInit.INFOCARDDECK_TYPE.get(), id);
		setPlayer(playerInv.player);
		pages = new ArrayList<>();
		
	}

	public InfoCardDeckContainer(int id, final PlayerInventory playerInv) {

		super(ContainerInit.INFOCARDDECK_TYPE.get(), id);
		setPlayer(playerInv.player);
		pages = new ArrayList<>();
		initPages();
		syncPagesToClient((ServerPlayerEntity) player);
	}
	
	@Override
	public boolean enchantItem(PlayerEntity playerIn, int id) {
		
		if(id==111&&!player.world.isRemote)syncPagesToClient((ServerPlayerEntity)player);
		
		return super.enchantItem(playerIn, id);
	}
	
	private void syncPagesToClient(ServerPlayerEntity entity) {
		
		for(Page page : pages)
			PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.player), new SyncPageMessage(page));
	}
	
	private void initPages() {
		
		ArrayList<Page> pages = new ArrayList<>();
		
		Map<TranslationTextComponent,ArrayList<Page>> sortedPages = new HashMap<TranslationTextComponent, ArrayList<Page>>();
		
		for (IRecipe<?> recipe : player.getServer().getRecipeManager().getRecipes()) {

			if (recipe.getType() == EnchantRecipeType.ENCHANT) {

				EnchantRecipe eRecipe = (EnchantRecipe) recipe;
				
				TranslationTextComponent name = new TranslationTextComponent(eRecipe.enchantment.getName());
				
				if(sortedPages.containsKey(name)) {
					
					ArrayList<Page> l = sortedPages.get(name);
					
					int index=l.size();
					
					for(Page p : l) {
						
						RecipePage rP = (RecipePage)p;
						
							int indexP = l.indexOf(p);;
						
							int v1= rP.getEntry().maxLevel!=0 ? rP.getEntry().maxLevel : rP.getEntry().baseLevel;
							int v2 = eRecipe.getStackableUntilLevel()!=0 ? eRecipe.getStackableUntilLevel() : eRecipe.getBaseLevel();
							
							if(v2>=v1) {
								if(index<indexP)
									index=indexP+1;
							}else {
								if(index>indexP)
									index=indexP;
							}
					}
					l.add(index,new RecipePage(eRecipe));
					
				}else {
					ArrayList<Page> l = new ArrayList<>();
					l.add(new RecipePage(eRecipe));
					sortedPages.put(name,l );
				}
			}
		}
		
		for(TranslationTextComponent name : sortedPages.keySet())
			pages.addAll(sortedPages.get(name));
		
		for(Page p: pages) {
			
			System.out.println(((RecipePage)p).getEnchantmentName()+""+((RecipePage)p).getEntry().maxLevel);
		}
		
		this.pages.addAll(pages);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {

		return true;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}
	public ArrayList<Page> getPages() {
		return pages;
	}

	public void setPages(ArrayList<Page> pages) {
		this.pages = pages;
	}

}
