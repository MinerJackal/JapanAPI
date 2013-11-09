package mods.japanAPI.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.japanAPI.JapanAPI;
import mods.japanAPI.items.IAutoConversionSymbol;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * プレイヤーがアイテムを拾った時のイベント
 * @author ArabikiTouhu
 * @version 0.0.2
 */
public class EntityItemPickupEventHook {

	/** キーワードリスト */
	protected static ArrayList<String> conversionKeyWordList = new ArrayList<String>();
	/** 変換済み対応表 */
	protected static Map<ItemStack, ItemStack> conversionFromToList = new HashMap<ItemStack, ItemStack>();
	/** 強制変換リスト */
	protected static Map<String, ItemStack> coercedList = new HashMap<String, ItemStack>();

	/** コンストラクタ(キーワード、対応表、強制変換リストの初期化) */
	public EntityItemPickupEventHook() {
		conversionKeyWordList.clear();
		conversionFromToList.clear();

		//<Add : ver0.0.2>
		coercedList.clear();
		//<Add : ver0.0.2>

		InitEntityItemPickupEventHook();
	}

	/**
	 * 初期化(デフォルト：キーワードに「ore」「ingot」を追加する)
	 */
	protected void InitEntityItemPickupEventHook() {

		addConversionKeyWordList("ore");
		//<Add : ver0.0.2>
		addConversionKeyWordList("ingot");
		//<Add : ver0.0.2>
	}

	/**
	 * 既に鉱石自動変換された対応表(conversionFromToList内)から検索する※時間短縮なはず
	 * @param convFrom 変換対象アイテム
	 * @return 変換後アイテム（該当なしの場合、null）
	 */
	protected ItemStack searchToList(ItemStack convFrom) {
		ItemStack toItem = null;
		for(Map.Entry<ItemStack, ItemStack> conversion : conversionFromToList.entrySet()) {
			if(conversion.getKey().isItemEqual(convFrom)) {
				toItem = conversion.getValue().copy();
				toItem.stackSize = convFrom.stackSize;
			}
		}
		return toItem;
	}

	/**
	 * キーワードの追加
	 * @param keyWork キーワード
	 */
	public void addConversionKeyWordList(String keyWork) {
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

//	/**
//	 * 鉱石自動変換イベント(ver0.0.6まで)
//	 * @param event プレイヤーがアイテムを拾った時のイベント
//	 */
//	@ForgeSubscribe
//	public void itemPickup(EntityItemPickupEvent event) {
//		EntityPlayer player = event.entityPlayer;
//		ItemStack pickupItem = event.item.getEntityItem();
//
//		if (event.isCancelable()) {
//			event.setCanceled(true);
//		}
//
//		if(player.worldObj.isRemote) return;
//
//		// インベントリ内にIAutoConversionSymbolを実装したアイテムがあるか確認
//		boolean flag = false;
//		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
//			ItemStack item = player.inventory.getStackInSlot(i);
//			if(item != null && item.getItem() instanceof IAutoConversionSymbol) {
//				flag = true;
//				break;
//			}
//		}
//
//		if(flag) {
//			//インベントリ内にある場合、自動変換開始
//			ItemStack toItem = searchToList(pickupItem);	//対応表の確認
//			if(toItem == null) {
//				//対応表不一致の場合
//				String dictName = OreDictionary.getOreName(OreDictionary.getOreID(pickupItem));	//鉱石辞書から登録名の取得
//
//				player.sendChatToPlayer("ItemPickupEvent Now! From " + pickupItem.getDisplayName() + " x" + pickupItem.stackSize + "(" + dictName + ")");	//通知
//
//				if(!dictName.matches("Unknown")) {	//鉱石辞書に登録されていない場合は「Unknown」が取得できる
//					for(String keyWord : conversionKeyWordList.toArray(new String[conversionKeyWordList.size()])) {	//キーワードリストを探査
//
//						if(dictName.startsWith(keyWord)) {		//登録名の先頭にキーワードが含まれているか？
//
//							player.sendChatToPlayer("OreDictionary Searching The HIT!! " + dictName);		//通知
//
//							toItem = OreDictionary.getOres(dictName).get(0).copy();		//デフォルトの変換
//
//							//<Add : ver0.0.2> 強制変換リストの参照
//							for(Entry<String, ItemStack> entry : coercedList.entrySet()) {		//強制変換リストの探査
//								if(dictName.matches(entry.getKey())) {		//リスト内に登録されている場合
//									toItem = entry.getValue().copy();	//強制変換
//									break;
//								}
//							}
//							//<Add : ver0.0.2> 強制変換リストの参照
//
//							addConversionFromToList(pickupItem, toItem);	//自動変換対応表へレコード登録
//
//							toItem.stackSize = pickupItem.stackSize;	//スタック数を合わせる
//
//							player.sendChatToPlayer("Conversion Success!! To" + toItem.getDisplayName() + " x" + toItem.stackSize);		//通知
//							break;
//						}
//					}
//				}
//			}
//
//			if(toItem != null) {	//変換成功しているか？
//				pickupItem = null;		//置き換え（参照の断裂）
//				pickupItem = toItem;	//置き換え（参照の設定）
//			}
//		}
//
//		if(pickupItem.stackSize > 0 && player.inventory.addItemStackToInventory(pickupItem)) {	//インベントリへアイテムを挿入
//			if(pickupItem.stackSize <= 0)
//				event.item.setDead();			//これがないと、アイテムが残る？（たぶん）
//			event.setResult(Result.ALLOW);	//イベントが成功した
//			event.setCanceled(false);		//イベント継続中止
//
//		} else if(Loader.isModLoaded("mod_StorageBox")) {
//			ItemStack[] inventory = player.inventory.mainInventory;
//			int size = inventory.length;
//
//			for (int i = 0; i < size; i++) {
//				if (inventory[i] != null && inventory[i].getItem() instanceof ItemStorageBox) {
//					if (ItemStorageBox.isAutoCollect(inventory[i])) {
//						ItemStack storageStack = ItemStorageBox.peekItemStackAll(inventory[i]);
//
//						if (storageStack != null && pickupItem.isItemEqual(storageStack)) {
//							ItemStorageBox.addItemStack(inventory[i], pickupItem);
//							pickupItem.stackSize = 0;
//							break;
//						}
//					}
//				}
//			}
//			if(pickupItem.stackSize <= 0)
//				event.item.setDead();			//これがないと、アイテムが残る？（たぶん）
//			event.setResult(Result.ALLOW);	//イベントが成功した
//			event.setCanceled(false);		//イベント継続中止
//		}
//
//	}

	/**
	 * 強制変換リストへレコードを追加・変更する。
	 * @param name 辞書登録名
	 * @param itemStack 強制変換アイテム
	 */
	public void addCoercedList(String name, ItemStack itemStack) {
		for(Entry<String, ItemStack> entry : coercedList.entrySet()) {
			if(entry.getKey().matches(name)) {
				entry.setValue(itemStack);
				return;
			}
		}
		coercedList.put(name, itemStack);
	}

	/**
	 * 鉱石自動変換イベント(ver0.0.7から)
	 * @param event プレイヤーがアイテムを拾った時のイベント
	 */
	@ForgeSubscribe
	public void itemPickup(EntityItemPickupEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack pickupItem = event.item.getEntityItem();

		// インベントリ内にIAutoConversionSymbolを実装したアイテムがあるか確認
		boolean flag = false;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack item = player.inventory.getStackInSlot(i);
			if(item != null && item.getItem() instanceof IAutoConversionSymbol) {
				flag = true;
				break;
			}
		}

		ItemStack itemStack = toAutoConversion(pickupItem);

		if (itemStack != null) {

			event.item.setEntityItemStack(itemStack.copy());
			//以下、試し
			player.worldObj.playSoundAtEntity(event.item, "random.pop", 0.2F, ((JapanAPI.RANDOM.nextFloat() - JapanAPI.RANDOM.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.worldObj.playSoundAtEntity(event.item, "random.pop", 0.2F, ((JapanAPI.RANDOM.nextFloat() - JapanAPI.RANDOM.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}




	}

	/**
	 * 変換処理
	 * @param fromItem 変換対象アイテム
	 * @return 変換後アイテム（未変換の場合は、Nullを返す）
	 */
	private ItemStack toAutoConversion(ItemStack fromItem) {
		//インベントリ内にある場合、自動変換開始
		ItemStack toItem = searchToList(fromItem);	//対応表の確認
		if(toItem == null) {
			//対応表不一致の場合
			String dictName = OreDictionary.getOreName(OreDictionary.getOreID(fromItem));	//鉱石辞書から登録名の取得

			if(!dictName.matches("Unknown")) {	//鉱石辞書に登録されていない場合は「Unknown」が取得できる
				for(String keyWord : conversionKeyWordList.toArray(new String[conversionKeyWordList.size()])) {	//キーワードリストを探査

					if(dictName.startsWith(keyWord)) {		//登録名の先頭にキーワードが含まれているか？

						toItem = OreDictionary.getOres(dictName).get(0).copy();		//デフォルトの変換

						for(Entry<String, ItemStack> entry : coercedList.entrySet()) {		//強制変換リストの探査
							if(dictName.matches(entry.getKey())) {		//リスト内に登録されている場合
								toItem = entry.getValue().copy();	//強制変換
								break;
							}
						}

						addConversionFromToList(fromItem, toItem);	//自動変換対応表へレコード登録

						toItem.stackSize = fromItem.stackSize;	//スタック数を合わせる
						break;
					}
				}
			}
		}

		return toItem;
	}

}
