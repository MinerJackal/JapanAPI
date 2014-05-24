package mods.japanAPI.pmd.struct;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import mods.japanAPI.pmd.CommonUtil;

/**
 * 材質データ 構造体
 * @author ArabikiTouhu
 * @version 0.01
 */
public class Material extends CommonUtil {

	/**
	 * MMDモデル ファイル読み込み(*.pmx)
	 * @param bBuffer データ格納済みByteBuffer
	 * @param encode 読み込み時の文字コード
	 * @param textureIndexSize テクスチャ最大数
	 * @return Material
	 * @throws UnsupportedEncodingException
	 */
	public static Material PMXFileLoad(ByteBuffer bBuffer, String encode, byte textureIndexSize) throws UnsupportedEncodingException {
		Material material = new Material();
		//***************************************************************************************
		//	１材質データの取得
		//***************************************************************************************

		try {
			material.name = convString(bBuffer, bBuffer.getInt(), encode);		//材質名の取得
			material.nameEn = convString(bBuffer, bBuffer.getInt(), encode);	//材質名(英)の取得

			//ディフューズ色
			material.diffuseR = bBuffer.getFloat();
			material.diffuseG = bBuffer.getFloat();
			material.diffuseB = bBuffer.getFloat();
			material.diffuseA = bBuffer.getFloat();

			//スペキュラー色
			material.specularR = bBuffer.getFloat();
			material.specularG = bBuffer.getFloat();
			material.specularB = bBuffer.getFloat();

			//スペキュラー強度
			material.specularPower = bBuffer.getFloat();

			//アンビエント色
			material.ambientR = bBuffer.getFloat();
			material.ambientG = bBuffer.getFloat();
			material.ambientB = bBuffer.getFloat();

			//描画フラグ
			material.drawFlag = bBuffer.get();

			//エッジ色
			material.edgeR = bBuffer.getFloat();
			material.edgeG = bBuffer.getFloat();
			material.edgeB = bBuffer.getFloat();
			material.edgeA = bBuffer.getFloat();

			//エッジサイズ
			material.edgeScale = bBuffer.getFloat();

			//テクスチャ番号
			material.texIndex = convInt(bBuffer, textureIndexSize);
			material.spiaIndex = convInt(bBuffer, textureIndexSize);

			//スフィアモード
			material.spiaMode = bBuffer.get();

			//共有Toonフラグ
			material.toonFlag = bBuffer.get();

			//どちらか一方
			if(material.toonFlag == 0) {
				material.toonIndex = convInt(bBuffer, textureIndexSize);
			} else {
				material.toonNum = bBuffer.get();
			}

			//メモ
			material.memo = convString(bBuffer, bBuffer.getInt(), encode);

			//面数
			material.faceCount = bBuffer.getInt();

		} catch (Exception e) {
		  e.printStackTrace();
		}

		return material;
	}

	protected String name;	//材質名
	protected String nameEn;//材質名(英)
	protected float diffuseR, diffuseG, diffuseB, diffuseA;	//ディフューズ色
	protected float specularR, specularG, specularB;		//スペキュラー色
	protected float specularPower;							//スペキュラー強度
	protected float ambientR, ambientG, ambientB;			//アンビエント色

	protected byte drawFlag;	//描画フラグ

	protected float edgeR, edgeG, edgeB, edgeA;	//エッジ色
	protected float edgeScale;	//エッジサイズ

	protected int texIndex;	//テクスチャ番号
	protected int spiaIndex;//スフィアテクスチャ番号

	protected byte spiaMode;//スフィアモード
	protected byte toonFlag;//共有Toonフラグ

	protected int toonIndex;//トゥーンテクスチャ番号
	protected byte toonNum;	//共有トゥーンテクスチャ番号

	protected String memo;	//メモ
	protected int faceCount;//面数

	protected Material() {
	}

}
