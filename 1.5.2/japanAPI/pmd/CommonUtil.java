package mods.japanAPI.pmd;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class CommonUtil {

	protected static int convInt(ByteBuffer buffer, byte size) {
		if(size == 1) return buffer.get();
		if(size == 2) return buffer.getShort();
		return buffer.getInt();
	}
	protected static byte[] convByteArray(ByteBuffer buffer, int length) {
		byte[] buf = new byte[length];
		buffer.get(buf);
		return buf;
	}
	protected static String convString(ByteBuffer buffer, int length) {
		return new String(convByteArray(buffer, length));
	}
	protected static String convString(ByteBuffer buffer, int length, String encodeName) throws UnsupportedEncodingException {
		return new String(convByteArray(buffer, length), encodeName);
	}

}
