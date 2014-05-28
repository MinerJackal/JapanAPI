package mods.japanAPI.items;

import net.minecraft.item.EnumToolMaterial;
import net.minecraftforge.common.EnumHelper;

public class JAPI_ForgeEnumMaterials
{
    public static EnumToolMaterial TOOL_COPPER = EnumHelper.addToolMaterial("銅ツール", 1, 250,4.0F, 1, 5);

    public static EnumToolMaterial TOOL_BRONZE = EnumHelper.addToolMaterial("青銅ツール", 2, 400, 6F, 2 , 14);

    public static EnumToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("鋼鉄ツール", 2, 1561, 6F, 2 , 14);

    public static EnumToolMaterial TOOL_CHROME = EnumHelper.addToolMaterial("クロムツール", 2, 400, 6F, 2 , 14);

    public static EnumToolMaterial TOOL_TUNGSTEN = EnumHelper.addToolMaterial("タングステンツール", 2, 400, 6F, 2 , 14);

    public static EnumToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("チタニウムツール", 2, 400, 6F, 2 , 14);


    public static EnumToolMaterial ENUM_gemTool  = EnumHelper.addToolMaterial("宝石ツール", 2, 1561, 6F, 5, 16);
    public static EnumToolMaterial ENUM_emeraldTool = EnumHelper.addToolMaterial("エメラルドツール", 3, 3122, 16F, 3, 20);

}
