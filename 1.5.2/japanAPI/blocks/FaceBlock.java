package mods.japanAPI.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 向きが設定できるブロック
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public class FaceBlock extends Block {

	public FaceBlock(int blockID, Material material) {
		super(blockID, material);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta) {
		return super.getIcon(side, meta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister) {
		super.registerIcons(iconRegister);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	/**
	 * 向きの設定（設置プレイヤーを正面に捕らえる）
	 * @param world ワールド
	 * @param x ｘ座標
	 * @param y ｙ座標
	 * @param z ｚ座標
	 */
	protected void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote)
		{
			int l = world.getBlockId(x, y, z - 1);
			int i1 = world.getBlockId(x, y, z + 1);
			int j1 = world.getBlockId(x - 1, y, z);
			int k1 = world.getBlockId(x + 1, y, z);
			byte meta = 3;

			if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
				meta = 3;
			}

			if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
				meta = 2;
			}

			if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
				meta = 5;
			}

			if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
				meta = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack) {
		int l = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		} else if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		} else  if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		} else if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
	}

	/**
	 * 外部から向きの設定
	 * @param meta メタデータ値
	 * @param world ワールド
	 * @param x ｘ座標
	 * @param y ｙ座標
	 * @param z ｚ座標
	 */
	public static void updateBlockDirection(int meta, World world, int x, int y, int z) {
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}


}
