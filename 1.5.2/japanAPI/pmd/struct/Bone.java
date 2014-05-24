package mods.japanAPI.pmd.struct;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import mods.japanAPI.pmd.CommonUtil;

/**
 * ボーンデータ 構造体
 * @author ArabikiTouhu
 * @version 0.01
 */
public class Bone extends CommonUtil {

	/**
	 * MMDモデル ファイル読み込み(*.pmx)
	 * @param bBuffer データ格納済みByteBuffer
	 * @param encode 読み込み時の文字コード
	 * @param boneIndexSize ボーン最大数
	 * @return Bone
	 * @throws UnsupportedEncodingException
	 */
	public static Bone PMXFileLoad(ByteBuffer bBuffer, String encode, byte boneIndexSize) throws UnsupportedEncodingException {
		Bone bone = new Bone();
		//***************************************************************************************
		//	１ボーンデータの取得
		//***************************************************************************************

		try {
			bone.name = convString(bBuffer, bBuffer.getInt(), encode);		//ボーン名の取得
			bone.nameEn = convString(bBuffer, bBuffer.getInt(), encode);	//ボーン名(英)の取得

			//位置
			bone.posX = bBuffer.getFloat();
			bone.posY = bBuffer.getFloat();
			bone.posZ = bBuffer.getFloat();

			bone.parentBoneIndex = convInt(bBuffer, boneIndexSize);	//親ボーン番号

			bone.deformationHierarchy = bBuffer.getInt();	//変形階層

			bone.boneFlag = bBuffer.getShort();	//ボーンフラグ2Byte

			if((bone.boneFlag & 0x0001) == 0x0001) {
				//接続先＝1
				bone.connectBoneIndex = convInt(bBuffer, boneIndexSize);
			} else {
				//接続先＝0
				bone.posOffsetX = bBuffer.getFloat();
				bone.posOffsetY = bBuffer.getFloat();
				bone.posOffsetZ = bBuffer.getFloat();
			}

			if(((bone.boneFlag & 0x0100) == 0x0100) ||
			   ((bone.boneFlag & 0x0200) == 0x0200)) {
				//回転付与=1or移動付与=1
				bone.grantParentBoneIndex = convInt(bBuffer, boneIndexSize);	//付与親ボーン番号
				bone.grantScale = bBuffer.getFloat();	//付与率
			}

			if((bone.boneFlag & 0x0400) == 0x0400) {
				//軸固定=1
				bone.axisDirectionX = bBuffer.getFloat();
				bone.axisDirectionY = bBuffer.getFloat();
				bone.axisDirectionZ = bBuffer.getFloat();
			}

			if((bone.boneFlag & 0x0800) == 0x0800) {
				//ローカル軸=1
				bone.axisLocalXX = bBuffer.getFloat();
				bone.axisLocalXY = bBuffer.getFloat();
				bone.axisLocalXZ = bBuffer.getFloat();
				bone.axisLocalZX = bBuffer.getFloat();
				bone.axisLocalZY = bBuffer.getFloat();
				bone.axisLocalZZ = bBuffer.getFloat();
			}

			if((bone.boneFlag & 0x2000) == 0x2000) {
				//外部親変形=1
				bone.outsideParentDeformationKEY = bBuffer.getInt();
			}

			if((bone.boneFlag & 0x0020) == 0x0020) {
				//IK=1
			}

		} catch (Exception e) {
		  e.printStackTrace();
		}

		return bone;
	}

	protected String name;	//ボーン名
	protected String nameEn;//ボーン名(英)
	protected float posX, posY, posZ;	//位置
	protected int parentBoneIndex;	//親ボーン番号
	protected int deformationHierarchy;	//変形階層

	protected short boneFlag;	//ボーンフラグ2Byte

	//接続先＝0
	protected float posOffsetX, posOffsetY, posOffsetZ;	//座標オフセット

	//接続先＝1
	protected int connectBoneIndex;	//接続先ボーン番号

	//回転付与=1or移動付与=1
	protected int grantParentBoneIndex;	//付与親ボーン番号
	protected float grantScale;			//付与率

	//軸固定=1
	protected float axisDirectionX, axisDirectionY, axisDirectionZ;	//軸の方向ベクトル

	//ローカル軸=1
	protected float axisLocalXX, axisLocalXY, axisLocalXZ;	//X軸の方向ベクトル
	protected float axisLocalZX, axisLocalZY, axisLocalZZ;	//Z軸の方向ベクトル

	//外部親変形=1
	protected int outsideParentDeformationKEY;	//Key値

	//IK=1
	protected int targetBoneIndex;	//IKターゲットボーン
	protected int loopCount;		//ループ回数
	protected float limitAngle;		//制限角度。※ラジアン
	protected int linkCount;		//リンク数

	protected int[] linkBoneIndexArray;	//リンクボーン番号配列
	protected byte[] linkLimitAngleFlagArray;	//角度制限フラグ配列
	protected float[] linkLimitAngleMinXArray;		//制限角度minX配列。※ラジアン
	protected float[] linkLimitAngleMinYArray;		//制限角度minY配列。※ラジアン
	protected float[] linkLimitAngleMinZArray;		//制限角度minZ配列。※ラジアン
	protected float[] linkLimitAngleMaxXArray;		//制限角度maxX配列。※ラジアン
	protected float[] linkLimitAngleMaxYArray;		//制限角度maxY配列。※ラジアン
	protected float[] linkLimitAngleMaxZArray;		//制限角度maxZ配列。※ラジアン

	protected Bone() {
	}

}
