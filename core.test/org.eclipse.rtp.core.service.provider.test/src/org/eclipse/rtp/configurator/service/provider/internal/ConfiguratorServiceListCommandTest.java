/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class ConfiguratorServiceListCommandTest {

	private DefaultConfiguratorService configuratorService;
	private P2Util p2UtilMock;

	@Before
	public void setUp() {
		configuratorService = new DefaultConfiguratorService();
		p2UtilMock = mock(P2Util.class);
		configuratorService.setP2Util(p2UtilMock);
	}

	@Test
	public void testListNoRepositoryLoaded() throws CoreException {
		URI[] knownRepositories = new URI[] {  };
		IMetadataRepository[] metadataRepositories = new IMetadataRepository[] {  };
		IMetadataRepositoryManager metadataRepositoryManagerMock = createMetadataRepositoryManagerMock(metadataRepositories, knownRepositories);
		createAgentMock(metadataRepositoryManagerMock);

		List<String> list = configuratorService.list();

		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testListFromEmptyRepository() throws CoreException {
		Iterator iteratorMock = mock(Iterator.class);
		when(iteratorMock.hasNext()).thenReturn(false);
		IQueryResult queryResultMock = createQueryResultMock(iteratorMock);
		IMetadataRepository metadataRepositoryMock = createMetadataRepositoryMock(queryResultMock);
		URI[] knownRepositories = new URI[] { URI.create("http://test.repository") };
		IMetadataRepository[] metadataRepositories = new IMetadataRepository[] { metadataRepositoryMock };
		IMetadataRepositoryManager metadataRepositoryManagerMock = createMetadataRepositoryManagerMock(metadataRepositories, knownRepositories);
		createAgentMock(metadataRepositoryManagerMock);

		List<String> list = configuratorService.list();

		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testListFromOneRepository() throws CoreException {
		String expectedIuId = "org.eclipse.test.bundle";
		String expectedIuId2 = "org.eclipse.test.bundle2";
		IInstallableUnit installableUnitMock = createIuMock(expectedIuId);
		IInstallableUnit installableUnitMock2 = createIuMock(expectedIuId2);
		Iterator iteratorMock = mock(Iterator.class);
		when(iteratorMock.hasNext()).thenReturn(true, true, false);
		when(iteratorMock.next()).thenReturn(installableUnitMock, installableUnitMock2);
		IQueryResult queryResultMock = createQueryResultMock(iteratorMock);
		IMetadataRepository metadataRepositoryMock = createMetadataRepositoryMock(queryResultMock);
		URI[] knownRepositories = new URI[] { URI.create("http://test.repository") };
		IMetadataRepository[] metadataRepositories = new IMetadataRepository[] { metadataRepositoryMock };
		IMetadataRepositoryManager metadataRepositoryManagerMock = createMetadataRepositoryManagerMock(metadataRepositories, knownRepositories);
		createAgentMock(metadataRepositoryManagerMock);

		List<String> list = configuratorService.list();

		assertTrue(list.size() == 2);
		assertTrue(list.contains(expectedIuId));
		assertTrue(list.contains(expectedIuId2));
	}
	
	@Test
	public void testListFromTwoRepositories() throws CoreException {
		String expectedIuId = "org.eclipse.test.bundle";
		String expectedIuId2 = "org.eclipse.test.bundle2";
		IInstallableUnit installableUnitMock = createIuMock(expectedIuId);
		IInstallableUnit installableUnitMock2 = createIuMock(expectedIuId2);
		Iterator iteratorMock = createIteratorMoc(installableUnitMock);
		Iterator iteratorMock2 = createIteratorMoc(installableUnitMock2);
		IQueryResult queryResultMock = createQueryResultMock(iteratorMock);
		IQueryResult queryResultMock2 = createQueryResultMock(iteratorMock2);
		IMetadataRepository metadataRepositoryMock = createMetadataRepositoryMock(queryResultMock);
		IMetadataRepository metadataRepositoryMock2 = createMetadataRepositoryMock(queryResultMock2);
		URI[] knownRepositories = new URI[] { URI.create("http://test.repository"), URI.create("http://test.repository2") };
		IMetadataRepository[] metadataRepositories = new IMetadataRepository[] { metadataRepositoryMock, metadataRepositoryMock2 };
		IMetadataRepositoryManager metadataRepositoryManagerMock = 
		  createMetadataRepositoryManagerMock(metadataRepositories, knownRepositories);
		createAgentMock(metadataRepositoryManagerMock);

		List<String> list = configuratorService.list();

		assertTrue(list.size() == 2);
		assertTrue(list.contains(expectedIuId));
		assertTrue(list.contains(expectedIuId2));
	}

	private void createAgentMock(
			IMetadataRepositoryManager metadataRepositoryManagerMock)
			throws CoreException {
		IProvisioningAgent provisioningAgent = mock(IProvisioningAgent.class);
		when(provisioningAgent.getService(IMetadataRepositoryManager.class.getName())).thenReturn(metadataRepositoryManagerMock);
	}

	private IMetadataRepositoryManager createMetadataRepositoryManagerMock(
			IMetadataRepository[] metadataRepositories, URI[] knownRepositories)
			throws ProvisionException {
		IMetadataRepositoryManager metadataRepositoryManagerMock = mock(IMetadataRepositoryManager.class);
		when(metadataRepositoryManagerMock.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL)).thenReturn(knownRepositories);
		for (int i = 0; i < metadataRepositories.length; i++) {
			when(metadataRepositoryManagerMock.loadRepository(knownRepositories[i], null)).thenReturn(metadataRepositories[i]);
		}
		return metadataRepositoryManagerMock;
	}

	private IMetadataRepository createMetadataRepositoryMock(IQueryResult queryResultMock) {
		IMetadataRepository metadataRepositoryMock = mock(IMetadataRepository.class);
		when(metadataRepositoryMock.query(QueryUtil.createIUAnyQuery(), null)).thenReturn(queryResultMock);
		return metadataRepositoryMock;
	}

	private IQueryResult createQueryResultMock(Iterator iteratorMock) {
		IQueryResult queryResultMock = mock(IQueryResult.class);
		when(queryResultMock.iterator()).thenReturn(iteratorMock);
		return queryResultMock;
	}

	private Iterator createIteratorMoc(IInstallableUnit installableUnitMock) {
		Iterator iteratorMock = mock(Iterator.class);
		when(iteratorMock.hasNext()).thenReturn(true, false);
		when(iteratorMock.next()).thenReturn(installableUnitMock);
		return iteratorMock;
	}

	private IInstallableUnit createIuMock(String expectedIuId) {
		IInstallableUnit installableUnitMock = mock(IInstallableUnit.class);
		when(installableUnitMock.getId()).thenReturn(expectedIuId);
		return installableUnitMock;
	}

}
