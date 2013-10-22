package mods.japanAPI.items;

import net.minecraft.item.Item;

/**
 * シンボルアイテム
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public class AutoConversionSymbolItem extends Item implements IAutoConversionSymbol {

	public static int itemID = -1;

	/**
	 * @param itemID 設定したい数値（内部で-256される）
	 */
	public AutoConversionSymbolItem(int itemID) {
		super(itemID - 256);

		if(AutoConversionSymbolItem.itemID == -1)
			AutoConversionSymbolItem.itemID = itemID;
	}

}
