package mods.japanAPI.utils;


import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtil
{
    //文字列を取得し鉱石辞書からアイテム名を取得し、アイテムスタックを返却する
    public static ItemStack getOreDic(String name)
    {
        return getOreDic(name,1);
    }

    //文字列を取得し鉱石辞書から該当アイテムと指定された個数のスタックアイテムを返却
    public static ItemStack getOreDic(String name,int amount)
    {
        for(ItemStack ore : OreDictionary.getOres(name))
        {
            if(ore != null)
            {
                if(ore.getItemDamage() != -1 || ore.getItemDamage() != OreDictionary.WILDCARD_VALUE)
                {
                    return new ItemStack(ore.getItem(),amount,ore.getItemDamage());
                }else{
                    return new ItemStack(ore.getItem(),amount);
                }
            }

        }
        return null;
    }
}
