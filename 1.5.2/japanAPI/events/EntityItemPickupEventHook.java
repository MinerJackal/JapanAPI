package mods.japanAPI.events;

import mods.japanAPI.JapanAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * プレイヤーがアイテムを拾った時のイベント
 * @author ArabikiTouhu
 * @version 0.0.2
 */
public class EntityItemPickupEventHook
{
	/** キーワードリスト */
	protected static ArrayList<String> conversionKeyWordList = new ArrayList<String>();
	/** 変換済み対応表 */
	protected static Map<ItemStack, ItemStack> conversionFromToList = new HashMap<ItemStack, ItemStack>();
	/** 強制変換リスト */
	protected static Map<String, ItemStack> coercedList = new HashMap<String, ItemStack>();

	/** 対象外キーワードリスト */
	protected static ArrayList<String> excludedKeyWordList = new ArrayList<String>();

	/** コンストラクタ(キーワード、対応表、強制変換リストの初期化) */
	public EntityItemPickupEventHook() {
		conversionKeyWordList.clear();
		conversionFromToList.clear();

		//<Add : ver0.0.2>
		coercedList.clear();
		//<Add : ver0.0.2>

		//<Add : ver0.0.9>
		excludedKeyWordList.clear();
		//<Add : ver0.0.9>

		InitEntityItemPickupEventHook();
	}

	/**
	 * 初期化(デフォルト：キーワードに「ore」「ingot」を追加する)
	 */
	protected void InitEntityItemPickupEventHook()
    {

		addConversionKeyWordList("ore");
		//<Add : ver0.0.2>
		addConversionKeyWordList("ingot");
		//<Add : ver0.0.2>

		//<Add : ver0.0.9>
		addExcludedKeyWordList("Unknown");
		//<Add : ver0.0.9>

	}

	/**
	 * 既に鉱石自動変換された対応表(conversionFromToList内)から検索する※時間短縮なはず
	 * @param convFrom 変換対象アイテム
	 * @return 変換後アイテム（該当なしの場合、null）
	 */
	protected ItemStack searchToList(ItemStack convFrom)
    {
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

		if( ! JapanAPI.CONFIG_itemConversion) return;

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
	private ItemStack toAutoConversion(ItemStack fromItem)
    {
		//インベントリ内にある場合、自動変換開始
		ItemStack toItem = searchToList(fromItem);	//対応表の確認
		if(toItem == null)
        {
			//対応表不一致の場合
			String dictName = OreDictionary.getOreName(OreDictionary.getOreID(fromItem));	//鉱石辞書から登録名の取得

			//対象外キーワード検索
			for(int i = 0; i < excludedKeyWordList.size(); i++) {	//対象外キーワードリストを探査
				if(dictName.matches(excludedKeyWordList.get(i))) return toItem;	//一致の場合は、変換なし
			}

			//対象キーワード
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

		return toItem;
	}

	/**
	 * 対象外キーワードへのレコード追加
	 * @param keyWord キーワード
	 */
	public void addExcludedKeyWordList(String keyWord) {
		if(keyWord == null) return;
		excludedKeyWordList.add(keyWord);
	}
}
