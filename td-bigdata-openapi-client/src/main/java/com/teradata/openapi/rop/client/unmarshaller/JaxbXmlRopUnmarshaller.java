package com.teradata.openapi.rop.client.unmarshaller;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.teradata.openapi.rop.RopException;
import com.teradata.openapi.rop.client.RopUnmarshaller;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class JaxbXmlRopUnmarshaller implements RopUnmarshaller {

	private static Map<Class, JAXBContext> jaxbContextHashMap = new ConcurrentHashMap<Class, JAXBContext>();

	private Unmarshaller buildUnmarshaller(Class<?> objectType) throws JAXBException {
		if (!jaxbContextHashMap.containsKey(objectType)) {
			JAXBContext context = JAXBContext.newInstance(objectType);
			jaxbContextHashMap.put(objectType, context);
		}
		JAXBContext context = jaxbContextHashMap.get(objectType);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// unmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// unmarshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
		return unmarshaller;
	}

	@Override
	public <T> T unmarshaller(String content, Class<T> objectType) {
		try {
			Unmarshaller unmarshaller = buildUnmarshaller(objectType);
			StringReader reader = new StringReader(content);
			new InputSource(reader);
			return (T) unmarshaller.unmarshal(reader);
		}
		catch (JAXBException e) {
			throw new RopException(e);
		}

	}
}
