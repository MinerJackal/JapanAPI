package mods.japanAPI.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mods.japanAPI.items.IAutoConversionSymbol;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.oredict.OreDictionary;

public class EntityItemPickupEventHook {

	protected static ArrayList<String> conversionKeyWordList = new ArrayList<String>();
	protected static Map<ItemStack, ItemStack> conversionFromToList = new HashMap<ItemStack, ItemStack>();

	public EntityItemPickupEventHook() {
		conversionKeyWordList.clear();
		conversionFromToList.clear();

		InitEntityItemPickupEventHook();
	}

	protected void InitEntityItemPickupEventHook() {

		addConversionKeyWordList("ore");
	}

	protected ItemStack searchToList(ItemStack convFrom) {
		ItemStack convTo = null;
		for(Map.Entry<ItemStack, ItemStack> conversion : conversionFromToList.entrySet()) {
			if(conversion.getKey().isItemEqual(convFrom)) {
				convTo = conversion.getValue().copy();
				convTo.stackSize = convFrom.stackSize;
			}
		}
		return convTo;
	}

	protected void addConversionKeyWordList(String keyWork) {
		conversionKeyWordList.add(keyWork);
	}

	private void addConversionFromToList(ItemStack from, ItemStack to) {
		conversionFromToList.put(from, to);
	}

	@ForgeSubscribe
	public void itemPickup(EntityItemPickupEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack pickupItem = event.item.getEntityItem();

		if (event.isCancelable()) {
			event.setCanceled(true);
		}

		if(player.worldObj.isRemote) return;

		boolean flag = false;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack item = player.inventory.getStackInSlot(i);
			if(item != null && item.getItem() instanceof IAutoConversionSymbol) {
				flag = true;
				break;
			}
		}

		if(flag) {
			ItemStack convTo = searchToList(pickupItem);
			if(convTo == null) {
				String dictName = OreDictionary.getOreName(OreDictionary.getOreID(pickupItem));
				player.sendChatToPlayer("ItemPickupEvent Now! From " + pickupItem.getDisplayName() + " x" + pickupItem.stackSize + "(" + dictName + ")");
				if(!dictName.matches("Unknown")) {
					for(String keyWord : conversionKeyWordList.toArray(new String[conversionKeyWordList.size()])) {
						if(dictName.startsWith(keyWord)) {
							player.sendChatToPlayer("OreDictionary Searching The HIT!! " + dictName);
							convTo = OreDictionary.getOres(dictName).get(0).copy();
							addConversionFromToList(pickupItem, convTo);
							convTo.stackSize = pickupItem.stackSize;
							player.sendChatToPlayer("Conversion Success!! To" + convTo.getDisplayName() + " x" + convTo.stackSize);
							break;
						}
					}
				}
			}
			if(convTo != null) {
				pickupItem = null;
				pickupItem = convTo;
			}
		}
		// 処理
		event.setResult(Result.ALLOW);
		event.setCanceled(false);
		player.inventory.addItemStackToInventory(pickupItem);
		event.item.setDead();
	}
}
