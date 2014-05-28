package mods.japanAPI.items;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;

public abstract class JAPI_ItemPickaxe extends ItemPickaxe
{
    public JAPI_ItemPickaxe(int itemID, EnumToolMaterial enumToolMaterial)
    {
        super(itemID, enumToolMaterial);
    }

    public  abstract void register();
}
