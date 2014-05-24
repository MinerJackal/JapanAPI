package mods.japanAPI.pmd;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import mods.japanAPI.pmd.struct.Bone;
import mods.japanAPI.pmd.struct.Material;
import mods.japanAPI.pmd.struct.Texture;
import mods.japanAPI.pmd.struct.Vertex;

/**
 * MMDモデル クラス
 * @author ArabikiTouhu
 * @version 0.01
 */
public class MMDModel extends CommonUtil {

	/**
	 * MMDモデル ファイル読み込み(*.pmx)
	 * @param path 絶対パス
	 * @return MMDModel
	 */
	public static MMDModel PMXFileLoad(String path) {

		//一時ファイル群
		ByteBuffer bBuffer;
		String root = ".\\";
		//一時ファイル群

		MMDModel model = new MMDModel( );
		try {

			//ファイル->全データロード(ByteBufferへ格納)
			{
				FileInputStream in = new FileInputStream(path);
				byte[] buf=new byte[in.available()];
				in.read(buf, 0, buf.length);
				bBuffer = ByteBuffer.wrap(buf);
				bBuffer.order(ByteOrder.LITTLE_ENDIAN);
				in.close();

				//パスの形成
				for(int i = path.length() - 1; i > 0; i--) {
					if(path.charAt(i) == '\\') {
						root = path.substring(0, i + 1);
						break;
					}
				}
			}

			//***************************************************************************************
			//	ヘッダー部分の取得
			//***************************************************************************************
			model.header = convString(bBuffer, 4);	//プリフィックスの取得4文字ASCII
			model.version = bBuffer.getFloat();		//バージョンの取得

			byte[] dataSizeArray = convByteArray(bBuffer, bBuffer.get());	//各種設定値の配列取得

			model.encode = dataSizeArray[0] == 0 ? "UTF-16LE" : "UTF8";		//文字コードの設定(各種設定値[0])
			model.name = convString(bBuffer, bBuffer.getInt(), model.encode);		//モデル名の取得
			model.nameEn = convString(bBuffer, bBuffer.getInt(), model.encode);		//モデル名(英)の取得
			model.comment = convString(bBuffer, bBuffer.getInt(), model.encode);	//コメントの取得
			model.commentEn = convString(bBuffer, bBuffer.getInt(), model.encode);	//コメント(英)の取得

			//***************************************************************************************
			//	頂点データ部分の取得
			//***************************************************************************************
			model.appendTexUVCount = dataSizeArray[1];	//追加UV数の設定(各種設定値[1])
			model.vertexIndexSize = dataSizeArray[2];	//頂点データ最大数の設定(各種設定値[2])
			model.boneIndexSize = dataSizeArray[5];		//ボーンデータ最大数の設定(各種設定値[5])

			model.vertexArray = new Vertex[bBuffer.getInt()];	//頂点データ配列の初期化

			for(int i = 0; i < model.vertexArray.length; i++) {
				model.vertexArray[i] = Vertex.PMXFileLoad(bBuffer, model.appendTexUVCount, model.boneIndexSize);
			}

			//***************************************************************************************
			//	面データ部分の取得
			//***************************************************************************************
			model.faceIndexArray = new int[bBuffer.getInt()];	//面データ配列の初期化

			for(int i = 0; i < model.faceIndexArray.length; i++) {
				model.faceIndexArray[i] = convInt(bBuffer, model.vertexIndexSize);
			}

			//***************************************************************************************
			//	テクスチャーデータ部分の取得
			//***************************************************************************************
			model.textureIndexSize = dataSizeArray[3];	//テクスチャーデータ最大数の設定(各種設定値[3])

			model.textureArray = new Texture[bBuffer.getInt()];	//テクスチャデータ配列の初期化
			for(int i = 0; i < model.textureArray.length; i++) {
				model.textureArray[i] = Texture.PMXFileLoad(bBuffer, model.encode, root);
			}

			//***************************************************************************************
			//	材質データ部分の取得
			//***************************************************************************************
			model.materialIndexSize = dataSizeArray[5];	//材質データ最大数の設定(各種設定値[5])

			model.materialArray = new Material[bBuffer.getInt()];	//材質データ配列の初期化
			for(int i = 0; i < model.materialArray.length; i++) {
				model.materialArray[i] = Material.PMXFileLoad(bBuffer, model.encode, model.textureIndexSize);
			}

			//***************************************************************************************
			//	ボーンデータ部分の取得
			//***************************************************************************************
			model.boneArray = new Bone[bBuffer.getInt()];	//ボーンデータ配列の初期化
			for(int i = 0; i < model.boneArray.length; i++) {
				model.boneArray[i] = Bone.PMXFileLoad(bBuffer, model.encode, model.boneIndexSize);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return model;
	}

	protected String header;	//ヘッダ
	protected float version;	//バージョン
	protected String encode;	//文字コード
	protected String name;		//名前
	protected String nameEn;	//名前(英)
	protected String comment;	//コメント
	protected String commentEn;	//コメント(英)

	protected byte appendTexUVCount;//追加UV数
	protected byte vertexIndexSize;	//頂点データ最大数
	protected Vertex[] vertexArray;	//頂点データ配列

	protected int[] faceIndexArray;	//面データ配列

	protected byte textureIndexSize;	//テクスチャーデータ最大数
	protected Texture[] textureArray;	//テクスチャーデータ配列

	protected byte materialIndexSize;	//材質データ最大数
	protected Material[] materialArray;	//材質データ配列

	protected byte boneIndexSize;	//ボーンデータ最大数
	protected Bone[] boneArray;		//ボーンデータ配列

	protected MMDModel() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
