/*
 * Copyright 2017 Otavio Santana and others
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jnosql.artemis.orientdb.document;

import org.jnosql.artemis.document.DocumentEntityConverter;
import org.jnosql.artemis.document.DocumentEventPersistManager;
import org.jnosql.artemis.document.DocumentWorkflow;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.orientdb.document.OrientDBDocumentCollectionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(WeldJUnit4Runner.class)
public class DefaultOrientDBDocumentRepositoryTest {

    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private DocumentWorkflow flow;

    @Inject
    private DocumentEventPersistManager persistManager;

    private OrientDBDocumentCollectionManager manager;

    private OrientDBDocumentRepository repository;


    @Before
    public void setup() {
        manager = Mockito.mock(OrientDBDocumentCollectionManager.class);
        Instance instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(manager);
        repository = new DefaultOrientDBDocumentRepository(instance, converter, flow, persistManager);

        DocumentEntity entity = DocumentEntity.of("Person");
        entity.add(Document.of("name", "Ada"));
        entity.add(Document.of("age", 10));
        when(manager.find(Mockito.anyString(), Mockito.any(String[].class)))
                .thenReturn(Collections.singletonList(entity));
    }

    @Test
    public void shouldFindQuery() {
        List<Person> people = repository.find("select * from Person where name = ?", "Ada");

        assertThat(people, contains(new Person("Ada", 10)));
        verify(manager).find(Mockito.eq("select * from Person where name = ?"), Mockito.eq("Ada"));
    }

    @Test
    public void shouldLive() {
        DocumentQuery query = DocumentQuery.of("Person");
        Consumer<Person> callBack = p -> {
        };
        repository.live(query, callBack);
        verify(manager).live(Mockito.eq(query), Mockito.any(Consumer.class));
    }

    @Test
    public void shouldLiveQuery() {
        Consumer<Person> callBack = p -> {
        };
        repository.live("select from Person where name = ?", callBack, "Ada");
        verify(manager).live(Mockito.eq("select from Person where name = ?"),
                Mockito.any(Consumer.class), Mockito.eq("Ada"));
    }
}