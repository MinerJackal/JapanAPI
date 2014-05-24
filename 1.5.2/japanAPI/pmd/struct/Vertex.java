package mods.japanAPI.pmd.struct;

import java.nio.ByteBuffer;

import mods.japanAPI.pmd.CommonUtil;

/**
 * 頂点データ 構造体
 * @author ArabikiTouhu
 * @version 0.01
 */
public class Vertex extends CommonUtil {

	/**
	 * MMDモデル ファイル読み込み(*.pmx)
	 * @param bBuffer データ格納済みByteBuffer
	 * @param appendTexUVCount 追加UV数
	 * @param boneIndexSize ボーン最大数
	 * @return Vertex
	 */
	public static Vertex PMXFileLoad(ByteBuffer bBuffer, byte appendTexUVCount, byte boneIndexSize) {

		Vertex vertex = new Vertex( );
		//***************************************************************************************
		//	１頂点データの取得
		//***************************************************************************************

		//位置
		vertex.posX = bBuffer.getFloat();
		vertex.posY = bBuffer.getFloat();
		vertex.posZ = bBuffer.getFloat();

		//法線
		vertex.norX = bBuffer.getFloat();
		vertex.norY = bBuffer.getFloat();
		vertex.norZ = bBuffer.getFloat();

		//UV
		vertex.texU = bBuffer.getFloat();
		vertex.texV = bBuffer.getFloat();

		//追加UV
		for(int i = 0; i < appendTexUVCount; i++) {
			vertex.texA[i / 4][0] = bBuffer.getFloat();
			vertex.texA[i / 4][1] = bBuffer.getFloat();
			vertex.texA[i / 4][2] = bBuffer.getFloat();
			vertex.texA[i / 4][3] = bBuffer.getFloat();
		}

		//ウェイト種類
		vertex.waitType = bBuffer.get();

		if(vertex.waitType == 0) {	//BDEF1
			vertex.boneIndex0 = convInt(bBuffer, boneIndexSize);
			vertex.boneWait0 = 1f;
		} else if(vertex.waitType == 1) {	//BDEF2
			vertex.boneIndex0 = convInt(bBuffer, boneIndexSize);
			vertex.boneIndex1 = convInt(bBuffer, boneIndexSize);
			vertex.boneWait0 = bBuffer.getFloat();
		} else if(vertex.waitType == 2) {	//BDEF4
			vertex.boneIndex0 = convInt(bBuffer, boneIndexSize);
			vertex.boneIndex1 = convInt(bBuffer, boneIndexSize);
			vertex.boneIndex2 = convInt(bBuffer, boneIndexSize);
			vertex.boneIndex3 = convInt(bBuffer, boneIndexSize);
			vertex.boneWait0 = bBuffer.getFloat();
			vertex.boneWait1 = bBuffer.getFloat();
			vertex.boneWait2 = bBuffer.getFloat();
			vertex.boneWait3 = bBuffer.getFloat();
		} else if(vertex.waitType == 3) {	//SDEF
			vertex.boneIndex0 = convInt(bBuffer, boneIndexSize);
			vertex.boneIndex1 = convInt(bBuffer, boneIndexSize);
			vertex.boneWait0 = bBuffer.getFloat();

			vertex.Sdef_cX = bBuffer.getFloat();
			vertex.Sdef_cY = bBuffer.getFloat();
			vertex.Sdef_cZ = bBuffer.getFloat();

			vertex.Sdef_r0X = bBuffer.getFloat();
			vertex.Sdef_r0Y = bBuffer.getFloat();
			vertex.Sdef_r0Z = bBuffer.getFloat();

			vertex.Sdef_r1X = bBuffer.getFloat();
			vertex.Sdef_r1X = bBuffer.getFloat();
			vertex.Sdef_r1X = bBuffer.getFloat();
		}

		vertex.edgeScale = bBuffer.getFloat();

		return vertex;
	}

	protected float posX, posY, posZ;	//位置
	protected float norX, norY, norZ;	//法線
	protected float texU, texV;			//UV
	protected float[][] texA = new float[4][4];	//追加UV

	protected byte waitType = -1;	//ウェイト種類

	protected int boneIndex0 = -1;	//ボーン1Index
	protected int boneIndex1 = -1;	//ボーン2Index
	protected int boneIndex2 = -1;	//ボーン3Index
	protected int boneIndex3 = -1;	//ボーン4Index

	protected float boneWait0 = -1;	//ボーン1Wait
	protected float boneWait1 = -1;	//ボーン2Wait
	protected float boneWait2 = -1;	//ボーン3Wait
	protected float boneWait3 = -1;	//ボーン4Wait

	protected float Sdef_cX, Sdef_cY, Sdef_cZ;		//SDEF-C
	protected float Sdef_r0X, Sdef_r0Y, Sdef_r0Z;	//SDEF-R0
	protected float Sdef_r1X, Sdef_r1Y, Sdef_r1Z;	//SDEF-R1

	protected float edgeScale;	//エッジ倍率

	protected Vertex() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
