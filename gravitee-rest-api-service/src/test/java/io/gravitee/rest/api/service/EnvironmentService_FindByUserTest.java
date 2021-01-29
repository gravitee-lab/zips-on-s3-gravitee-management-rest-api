/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.rest.api.service;

import io.gravitee.repository.exceptions.TechnicalException;
import io.gravitee.repository.management.api.EnvironmentRepository;
import io.gravitee.repository.management.model.Environment;
import io.gravitee.rest.api.model.EnvironmentEntity;
import io.gravitee.rest.api.service.common.GraviteeContext;
import io.gravitee.rest.api.service.impl.EnvironmentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Yann TAVERNIER (yann.tavernier at graviteesource.com)
 * @author GraviteeSource Team
 */
@RunWith(MockitoJUnitRunner.class)
public class EnvironmentService_FindByUserTest {

    @InjectMocks
    private EnvironmentServiceImpl environmentService = new EnvironmentServiceImpl();

    @Mock
    private EnvironmentRepository mockEnvironmentRepository;

    @Mock
    private OrganizationService mockOrganizationService;

    @Mock
    private ApiHeaderService mockAPIHeaderService;

    @Mock
    private PageService mockPageService;

    @After
    public void tearDown() {
        GraviteeContext.cleanContext();
    }

    @Test
    public void shouldFindByUserFilteredByEnvId() throws TechnicalException {

        when(mockEnvironmentRepository.findByOrganization(any())).thenReturn(getEnvironments());

        List<EnvironmentEntity> environments = environmentService.findByUser(null, "fakeEnvId", null);

        assertThat(environments).isEmpty();
    }


    @Test
    public void shouldFindByUserFilteredByEnvHrid() throws TechnicalException {

        when(mockEnvironmentRepository.findByOrganization(any())).thenReturn(getEnvironments());

        List<EnvironmentEntity> environments = environmentService.findByUser(null, null, "fakeHrid");

        assertThat(environments).isEmpty();
    }

    @Test
    public void shouldFindByUserFilteredByEnvHridAndId_different() throws TechnicalException {

        when(mockEnvironmentRepository.findByOrganization(any())).thenReturn(getEnvironments());

        List<EnvironmentEntity> environments = environmentService.findByUser(null, "envId", "env2");

        assertThat(environments).hasSize(2);
    }

    @Test
    public void shouldFindByUserFilteredByEnvHridAndId_same() throws TechnicalException {

        when(mockEnvironmentRepository.findByOrganization(any())).thenReturn(getEnvironments());

        List<EnvironmentEntity> environments = environmentService.findByUser(null, "env1", "env1");

        assertThat(environments).hasSize(1);
    }

    public Set<Environment> getEnvironments() {
        Environment env1 = new Environment();
        env1.setId("envId");
        env1.setHrids(Arrays.asList("env1", "1env"));

        Environment env2 = new Environment();
        env2.setId("env2Id");
        env2.setHrids(Arrays.asList("env2", "2env"));

        HashSet<Environment> envSet = new HashSet<>();
        envSet.add(env1);
        envSet.add(env2);

        return envSet;
    }
}
