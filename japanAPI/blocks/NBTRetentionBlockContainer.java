package mods.japanAPI.blocks;

import java.util.ArrayList;

import mods.japanAPI.JapanAPI;
import mods.japanAPI.tileEntities.NBTRetentionTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * NBT保持BlockContainer※同時にItemBlockと、NBTRetentionTileEntityを定義しなければならない。
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public abstract class NBTRetentionBlockContainer extends BlockContainer {

	public NBTRetentionBlockContainer(int blockID, Material material) {
		super(blockID, material);
	}

	public abstract TileEntity createNewTileEntity(World world);

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, x, z, entityLiving, itemStack);

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);	//タイルエンティティー取得
		if(tileEntity == null) return;

		if(tileEntity instanceof NBTRetentionTileEntity) {
			NBTRetentionTileEntity tile = (NBTRetentionTileEntity) tileEntity;	//キャスト（NBTRetentionTileEntity）
			if(tile.getItemStack() != null &&
				tile.getItemStack().isItemEqual(itemStack) &&
				itemStack.hasTagCompound()) {
				NBTTagCompound nbtTagCompound = itemStack.getTagCompound().getCompoundTag("tileEntity");

				if(nbtTagCompound != null) {
					tile.readFromNBT(nbtTagCompound);
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int flag, int meta) {

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);	//タイルエンティティー取得
		if(tileEntity instanceof NBTRetentionTileEntity) {
			NBTRetentionTileEntity tile = (NBTRetentionTileEntity) tileEntity;	//キャスト（NBTRetentionTileEntity）

			NBTTagCompound nbtTagCompound = new NBTTagCompound();	//NBTの生成
			tile.writeToNBT(nbtTagCompound);	//NBTの書き込み

			ItemStack itemStack = tile.getItemStack();	//タイルからItemStackの取得（生成）
			if(itemStack != null) {
				itemStack.setTagCompound(itemStack.writeToNBT(new NBTTagCompound()));	//ItemStack内のNBT初期化
				itemStack.getTagCompound().setCompoundTag("tileEntity", nbtTagCompound);	//NBTの書き込み

				float xx = JapanAPI.RANDOM.nextFloat() * 0.8F + 0.1F;
				float yy = JapanAPI.RANDOM.nextFloat() * 0.8F + 0.1F;
				float zz = JapanAPI.RANDOM.nextFloat() * 0.8F + 0.1F;

				EntityItem entityitem = new EntityItem(world, (double)((float)x + xx), (double)((float)y + yy), (double)((float)z + zz),
						itemStack);
				float speed = 0.05F;
				entityitem.motionX = (double)((float)JapanAPI.RANDOM.nextGaussian() * speed);
				entityitem.motionY = (double)((float)JapanAPI.RANDOM.nextGaussian() * speed + 0.2F);
				entityitem.motionZ = (double)((float)JapanAPI.RANDOM.nextGaussian() * speed);
				world.spawnEntityInWorld(entityitem);
			}
		}

		super.breakBlock(world, x, y, z, flag, meta);
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		return ret;
	}

}
