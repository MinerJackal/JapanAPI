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

/**
 * プレイヤーがアイテムを拾った時のイベント
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public class EntityItemPickupEventHook {

	/**
	 * キーワードリスト
	 */
	protected static ArrayList<String> conversionKeyWordList = new ArrayList<String>();
	/**
	 * 変換済み対応表
	 */
	protected static Map<ItemStack, ItemStack> conversionFromToList = new HashMap<ItemStack, ItemStack>();

	/**
	 * コンストラクタ
	 * キーワード、対応表の初期化
	 */
	public EntityItemPickupEventHook() {
		conversionKeyWordList.clear();
		conversionFromToList.clear();

		InitEntityItemPickupEventHook();
	}

	/**
	 * 初期化(キーワードに「ore」を追加する)
	 */
	protected void InitEntityItemPickupEventHook() {

		addConversionKeyWordList("ore");
	}

	/**
	 * 既に鉱石自動変換された対応表(conversionFromToList内)から検索する※時間短縮なはず
	 * @param convFrom 変換対象アイテム
	 * @return 変換後アイテム（該当なしの場合、null）
	 */
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

	/**
	 * キーワードの追加
	 * @param keyWork キーワード
	 */
	protected void addConversionKeyWordList(String keyWork) {
		conversionKeyWordList.add(keyWork);
	}

	/**
	 * 対応表へのレコード追加
	 * @param from 変換前アイテム
	 * @param to 変換後アイテム
	 */
	private void addConversionFromToList(ItemStack from, ItemStack to) {
		conversionFromToList.put(from, to);
	}

	/**
	 * 鉱石自動変換イベント
	 * @param event プレイヤーがアイテムを拾った時のイベント
	 */
	@ForgeSubscribe
	public void itemPickup(EntityItemPickupEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack pickupItem = event.item.getEntityItem();

		if (event.isCancelable()) {
			event.setCanceled(true);
		}

		if(player.worldObj.isRemote) return;

		// インベントリ内にIAutoConversionSymbolを実装したアイテムがあるか確認
		boolean flag = false;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack item = player.inventory.getStackInSlot(i);
			if(item != null && item.getItem() instanceof IAutoConversionSymbol) {
				flag = true;
				break;
			}
		}

		if(flag) {
			//インベントリ内にある場合、自動変換開始
			ItemStack convTo = searchToList(pickupItem);	//対応表の確認
			if(convTo == null) {
				//対応表不一致の場合
				String dictName = OreDictionary.getOreName(OreDictionary.getOreID(pickupItem));	//鉱石辞書から登録名の取得
				player.sendChatToPlayer("ItemPickupEvent Now! From " + pickupItem.getDisplayName() + " x" + pickupItem.stackSize + "(" + dictName + ")");
				if(!dictName.matches("Unknown")) {
					//鉱石辞書に登録してある場合
					for(String keyWord : conversionKeyWordList.toArray(new String[conversionKeyWordList.size()])) {
						if(dictName.startsWith(keyWord)) {
							//登録名の先頭にキーワードが含まれている場合
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
				//変換成功ならば置き換え
				pickupItem = null;
				pickupItem = convTo;
			}
		}

		event.setResult(Result.ALLOW);	//イベントが成功した
		event.setCanceled(false);		//イベント継続中止
		player.inventory.addItemStackToInventory(pickupItem);	//インベントリへアイテムを挿入
		event.item.setDead();			//これがないと、アイテムが残る？（たぶん）
	}
}
