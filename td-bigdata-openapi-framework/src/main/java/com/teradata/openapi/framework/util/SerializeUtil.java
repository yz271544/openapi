package com.teradata.openapi.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * 序列化与反序列化对象 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月6日 上午11:50:08
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */

public class SerializeUtil {

	/**
	 * 
	 * serialize:(序列化对象).
	 * 
	 * @author houbl
	 * @param object
	 * @return
	 */
	public static <T> byte[] serialize(T object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * unserialize:(反序列化).
	 * 
	 * @author houbl
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}