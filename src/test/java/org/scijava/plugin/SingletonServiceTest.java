/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.plugin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConvertService;
import org.scijava.convert.Converter;
import org.scijava.plugin.event.PluginsAddedEvent;
import org.scijava.plugin.event.PluginsRemovedEvent;

/**
 * Tests {@link AbstractSingletonService}.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public class SingletonServiceTest {

	private PluginService pluginService;
	private ConvertService convertService;

	@Before
	public void setUp() {
		final Context context = new Context(PluginService.class,
			ConvertService.class);
		pluginService = context.service(PluginService.class);
		convertService = context.service(ConvertService.class);
	}

	@After
	public void tearDown() {
		pluginService.context().dispose();
	}

	/**
	 * Tests that the {@link AbstractSingletonService} properly handles
	 * {@link PluginsAddedEvent}s originating from the {@link PluginService}.
	 */
	@Test
	public void testSingletonServicePluginsAddedHandling() {
		@SuppressWarnings("rawtypes")
		PluginInfo<Converter> converterInfo = new PluginInfo<>(
			FoodConverter.class, Converter.class);

		pluginService.addPlugin(converterInfo);

		assertNotNull(pluginService.getPlugin(FoodConverter.class));
		assertTrue(convertService.supports(new Apple() {}, Peach.class));
	}

	/**
	 * Tests that the {@link AbstractSingletonService} properly handles
	 * {@link PluginsAddedEvent}s that replace an instance.
	 */
	@Test
	public void testSingletonServicePluginsAddedHandlingDuplicates() {
		@SuppressWarnings("rawtypes")
		PluginInfo<Converter> converterInfo = new PluginInfo<>(
			FoodConverter.class, Converter.class);

		pluginService.addPlugin(converterInfo);
		FoodConverter firstInstance = convertService.getInstance( FoodConverter.class );

		pluginService.addPlugin(converterInfo);
		FoodConverter secondInstance = convertService.getInstance( FoodConverter.class );

		assertNotSame(firstInstance, secondInstance);
		assertTrue(convertService.supports(new Apple() {}, Peach.class));
	}

	/**
	 * Tests that the {@link AbstractSingletonService} properly handles
	 * {@link PluginsRemovedEvent}s originating from the {@link PluginService}.
	 */
	@Test
	public void testSingletonServiceManuallyAddedPluginsRemovedHandling() {
		@SuppressWarnings("rawtypes")
		PluginInfo<Converter> converterInfo = new PluginInfo<>(
			FoodConverter.class, Converter.class);

		pluginService.addPlugin(converterInfo);

		// De-register DummyStringConverter
		pluginService.removePlugin(converterInfo);

		assertNull(pluginService.getPlugin(FoodConverter.class));
		assertFalse(convertService.supports(new Apple() {}, Peach.class));
	}

	/**
	 * Tests that the {@link AbstractSingletonService} properly handles
	 * {@link PluginsRemovedEvent}s originating from the {@link PluginService}.
	 */
	@Test
	public void testSingletonServiceCompileTimePluginsRemovedHandling() {
		PluginInfo<SciJavaPlugin> pluginInfo = pluginService.getPlugin(DiscoveredFoodConverter.class);

		// De-register DiscoveredFoodConverter
		pluginService.removePlugin(pluginInfo);

		assertNull(pluginService.getPlugin(DiscoveredFoodConverter.class));
		assertFalse(convertService.supports(new Orange() {}, Peach.class));
	}

	/**
	 * Dummy {@link Converter}.
	 */
	public static class FoodConverter extends
		AbstractConverter<Apple, Peach>
	{

		@Override
		public <T> T convert(Object src, Class<T> dest) {
			return null;
		}

		@Override
		public Class<Peach> getOutputType() {
			return Peach.class;
		}

		@Override
		public Class<Apple> getInputType() {
			return Apple.class;
		}
	}

	/**
	 * Dummy {@link Converter} that is added automatically.
	 */
	@Plugin( type = Converter.class )
	public static class DiscoveredFoodConverter extends
		AbstractConverter<Orange, Peach>
	{

		@Override
		public <T> T convert(Object src, Class<T> dest) {
			return null;
		}

		@Override
		public Class<Peach> getOutputType() {
			return Peach.class;
		}

		@Override
		public Class<Orange> getInputType() {
			return Orange.class;
		}
	}

	/**
	 * Type interface for conversion
	 */
	public interface Apple
	{
		// NB
	}

	/**
	 * Type interface for conversion
	 */
	public interface Orange
	{
		// NB
	}

	/**
	 * Type interface for conversion
	 */
	public interface Peach
	{
		// NB
	}
}