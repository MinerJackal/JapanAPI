package mods.japanAPI.pmd.struct;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import mods.japanAPI.pmd.CommonUtil;
import mods.japanAPI.pmd.image.TgaImage;

import org.lwjgl.opengl.GL11;

/**
 * テクスチャーデータ 構造体
 * @author ArabikiTouhu
 * @version 0.01
 */
public class Texture extends CommonUtil {

	/**
	 * MMDモデル ファイル読み込み(*.pmx)
	 * @param bBuffer データ格納済みByteBuffer
	 * @param encode 読み込み時の文字コード
	 * @param rootDirectory ルートディレクトリ
	 * @return Texture
	 * @throws UnsupportedEncodingException
	 */
	public static Texture PMXFileLoad(ByteBuffer bBuffer, String encode, String rootDirectory) throws UnsupportedEncodingException {
		Texture texture = new Texture();
		//***************************************************************************************
		//	１テクスチャーデータの取得
		//***************************************************************************************

		try {
			texture.path = convString(bBuffer, bBuffer.getInt(), encode);		//モデル名の取得
			BufferedImage readImage;
			if(texture.path.endsWith(".tga"))
				readImage = TgaImage.readFile(rootDirectory + texture.path);
			else
				readImage = ImageIO.read(new File(rootDirectory + texture.path));
			int texWidth = readImage.getWidth();
			int texHeight = readImage.getHeight();
			//  glTexImage2D() の対象となるテクスチャー ID をバインドする
			texture.bindID = GL11.glGenTextures();	//バインドIDの生成
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.bindID);

			//一時ファイル群
			int srcPixelFormat;
			WritableRaster raster;
			BufferedImage texImage;
			ColorModel glAlphaColorModel = new ComponentColorModel(
					ColorSpace.getInstance(ColorSpace.CS_sRGB),
					new int[] {8,8,8,8},
					true,
					false,
					ComponentColorModel.TRANSLUCENT,
					DataBuffer.TYPE_BYTE);
			ColorModel glColorModel = new ComponentColorModel(
					ColorSpace.getInstance(ColorSpace.CS_sRGB),
					new int[] {8,8,8,0},
					false,
					false,
					ComponentColorModel.OPAQUE,
					DataBuffer.TYPE_BYTE);
			//一時ファイル群

			//テクスチャーの元となるデータを作成する
			//変換する画像がアルファ値を含むかどうかを、テクスチャーのデータ形式に反映させる
			if (readImage.getColorModel().hasAlpha()) {
				raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
				texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
	            srcPixelFormat = GL11.GL_RGBA;
			} else {
				raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
				texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
	            srcPixelFormat = GL11.GL_RGB;
			}

			//変換する画像のアルファモードを調べる
			texture.modeAlpha = readImage.getType() == BufferedImage.TYPE_4BYTE_ABGR_PRE;

			//テクスチャーの元データに、変換する画像を描画する
			Graphics g = texImage.getGraphics();

			g.setColor(new Color(0f, 0f, 0f, 0f));
			g.fillRect(0, 0, texWidth, texHeight);  //  画像は最初に透明色で塗りつぶす
			g.drawImage(readImage, 0, 0, null);
			g.dispose();

			//読み込んだ画像を破棄する
			readImage.flush();

			//テクスチャーの元データをバイト配列に変換する
			byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
			texImage.flush();

			//ByteBufferに格納
			ByteBuffer imageBuffer = ByteBuffer.allocateDirect(data.length);
			imageBuffer.order(ByteOrder.nativeOrder());
			imageBuffer.put(data, 0, data.length);
			imageBuffer.flip();

			//画像の拡大・縮小時の補間方法を設定する
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			//バイト配列と色情報のフォーマットからテクスチャーを生成する
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
					0,
					GL11.GL_RGBA,
					texWidth,
					texHeight,
					0,
					srcPixelFormat,
					GL11.GL_UNSIGNED_BYTE,
					imageBuffer);

			imageBuffer.clear();

		} catch (Exception e) {
			System.out.println("*********************************************************************");
			System.out.println("対象ファイル「 " + rootDirectory + texture.path + " 」");
			e.printStackTrace();
		}

		return texture;
	}

	protected String path;	//テクスチャの参照パス
	protected int bindID;	//バインドID
	protected boolean modeAlpha;	//アルファモード

	protected Texture() {
	}

}
