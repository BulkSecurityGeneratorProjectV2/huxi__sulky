/*
 * sulky-modules - several general-purpose modules.
 * Copyright (C) 2007-2009 Joern Huxhorn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.huxhorn.sulky.buffers;

import de.huxhorn.sulky.codec.Codec;
import de.huxhorn.sulky.codec.SerializableCodec;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedSerializingFileBufferTest
{
	private final Logger logger = LoggerFactory.getLogger(ExtendedSerializingFileBufferTest.class);

	private File tempOutputPath;
	private File dataFile;
	private File indexFile;

	private String[] values;
	private Integer magicValue;
	private Map<String, String> metaData;
	private Codec<String> codec;

	@Before
	public void setUp()
		throws Exception
	{
		tempOutputPath = File.createTempFile("sfb-testing", "rulez");
		tempOutputPath.delete();
		tempOutputPath.mkdirs();
		dataFile = new File(tempOutputPath, "dump");
		indexFile = new File(tempOutputPath, "dump.index");

		codec = new SerializableCodec<String>();

		values = new String[]
			{
				"Null, sozusagen ganix",
				"Eins",
				"Zwei",
				"Drei",
				"Vier",
				"Fuenef",
				"Sechse",
				"Siebene",
				"Achtele",
				"Neune",
				"Zehne"
			};

		magicValue = 0xDEADBEEF;
		metaData = new HashMap<String, String>();
		metaData.put("foo1", "bar1");
		metaData.put("foo2", "bar2");
	}

	@After
	public void tearDown()
		throws Exception
	{
		dataFile.delete();
		indexFile.delete();
		tempOutputPath.delete();
	}

	@Test(expected = NullPointerException.class)
	public void readWriteNoMagic()
	{
		new ExtendedSerializingFileBuffer<String>(null, null, codec, dataFile, indexFile);
	}

	@Test
	public void readWriteNoMetaAdd()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);
		for(String current : values)
		{
			instance.add(current);
		}

		assertEquals(values.length, (int) instance.getSize());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertNull(instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		assertEquals(values.length, (int) otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertNull(otherInstance.getMetaData());
	}

	@Test
	public void readWriteMetaAdd()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		for(String current : values)
		{
			instance.add(current);
		}
		assertEquals(values.length, (int) instance.getSize());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertEquals(values.length, (int) otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void readWriteNoMetaAddAll()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);
		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertNull(instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		assertEquals(values.length, (int) otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertNull(otherInstance.getMetaData());
	}

	@Test
	public void readWriteMetaAddAll()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertEquals(values.length, (int) otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void readWriteMetaMixed()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		instance.addAll(values);
		for(String current : values)
		{
			instance.add(current);
		}
		instance.addAll(values);
		for(String current : values)
		{
			instance.add(current);
		}

		assertEquals(4 * values.length, (int) instance.getSize());

		for(int i = 0; i < 4 * values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i % values.length], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index % values.length], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertEquals(4 * values.length, (int) otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void readInvalid()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertNull(instance.get(0));

		instance.addAll(values);

		assertNull(instance.get(values.length));
		assertNotNull(instance.get(values.length - 1));
	}

	@Test
	public void readWriteNoMetaMixed()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);
		instance.addAll(values);
		for(String current : values)
		{
			instance.add(current);
		}
		instance.addAll(values);
		for(String current : values)
		{
			instance.add(current);
		}

		assertEquals(4 * values.length, (int) instance.getSize());

		for(int i = 0; i < 4 * values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i % values.length], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index % values.length], value);
			index++;
		}

		assertNull(instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		assertEquals(4 * values.length, (int) otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertNull(otherInstance.getMetaData());
	}

	@Test
	public void meta()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertEquals(0, (int) instance.getSize());

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertEquals(0, (int) instance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void noMeta()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		assertEquals(0, (int) instance.getSize());

		assertNull(instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		assertEquals(0, (int) instance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertNull(otherInstance.getMetaData());
	}

	@Test
	public void resetMeta()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());

		instance.reset();

		assertEquals(0, instance.getSize());

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);

		assertEquals(0, otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertEquals(metaData, otherInstance.getMetaData());

		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}
	}

	@Test
	public void resetNoMeta()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());

		instance.reset();
		assertEquals(0, instance.getSize());

		assertNull(instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, null, codec, dataFile, indexFile);

		assertEquals(0, otherInstance.getSize());

		assertEquals(magicValue, otherInstance.getMagicValue());

		assertNull(otherInstance.getMetaData());

		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}
	}

	@Test
	public void elementProcessorsAdd()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		List<ElementProcessor<String>> elementProcessors = new ArrayList<ElementProcessor<String>>();
		CapturingElementProcessor capture = new CapturingElementProcessor();
		elementProcessors.add(capture);
		instance.setElementProcessors(elementProcessors);
		instance.add("Example");
		assertEquals(1, capture.list.size());
		assertEquals("Example", capture.list.get(0));
	}

	@Test
	public void elementProcessorsAddAll()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		List<ElementProcessor<String>> elementProcessors = new ArrayList<ElementProcessor<String>>();
		CapturingElementProcessor capture = new CapturingElementProcessor();
		elementProcessors.add(capture);
		instance.setElementProcessors(elementProcessors);
		instance.addAll(values);
		assertEquals(values.length, capture.list.size());
		int index = 0;
		for(String value : capture.list)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}
	}

	@Test
	public void deleteDataFileAdd()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		for(String current : values)
		{
			instance.add(current);
		}
		assertEquals(values.length, (int) instance.getSize());
		dataFile.delete();
		for(String current : values)
		{
			instance.add(current);
		}

		assertEquals(8 * values.length, indexFile.length());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void deleteDataFileAddAll()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());
		dataFile.delete();
		instance.addAll(values);

		assertEquals(8 * values.length, indexFile.length());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void deleteDataFileGet()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		for(String current : values)
		{
			instance.add(current);
		}
		assertEquals(values.length, (int) instance.getSize());

		assertEquals(metaData, instance.getMetaData());

		assertNotNull(instance.get(values.length - 1));
		dataFile.delete();
		assertNull(instance.get(values.length - 1));
	}

	@Test
	public void deleteIndexFileAdd()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		for(String current : values)
		{
			instance.add(current);
		}
		assertEquals(values.length, (int) instance.getSize());
		indexFile.delete();
		for(String current : values)
		{
			instance.add(current);
		}

		assertEquals(8 * values.length, indexFile.length());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test
	public void deleteIndexFileAddAll()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());
		indexFile.delete();
		instance.addAll(values);

		assertEquals(8 * values.length, indexFile.length());

		for(int i = 0; i < values.length; i++)
		{
			String value = instance.get(i);
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
			assertEquals("Element #" + i + " differs!", values[i], value);
		}

		int index = 0;
		for(String value : instance)
		{
			if(logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
			assertEquals("Element #" + index + " differs!", values[index], value);
			index++;
		}

		assertEquals(metaData, instance.getMetaData());

		ExtendedSerializingFileBuffer<String> otherInstance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		assertEquals(metaData, otherInstance.getMetaData());
	}

	@Test(expected = IllegalArgumentException.class)
	/**
	 * This is the case that an existing file containing data is reopened. In that case, an IllegalArgumentException is
	 * thrown instead of simply overwriting the previous data (which is done if the indexFile is deleted while
	 * "in production").
	 */
	public void deleteIndexFileReopen()
	{
		ExtendedSerializingFileBuffer<String> instance = new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
		instance.addAll(values);
		assertEquals(values.length, (int) instance.getSize());
		indexFile.delete();
		new ExtendedSerializingFileBuffer<String>(magicValue, metaData, codec, dataFile, indexFile);
	}

	private static class CapturingElementProcessor
		implements ElementProcessor<String>
	{
		public List<String> list = new ArrayList<String>();

		public void processElement(String element)
		{
			list.add(element);
		}

		public void processElements(List<String> element)
		{
			list.addAll(element);
		}
	}
}
