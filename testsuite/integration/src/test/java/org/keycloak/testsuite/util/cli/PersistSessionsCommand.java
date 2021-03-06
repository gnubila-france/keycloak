/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.testsuite.util.cli;

import java.util.LinkedList;
import java.util.List;

import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientSessionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionTask;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.models.session.UserSessionPersisterProvider;
import org.keycloak.models.utils.KeycloakModelUtils;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class PersistSessionsCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "persistSessions";
    }

    @Override
    public void doRunCommand(KeycloakSession sess) {
        final int count = getIntArg(0);
        final List<String> userSessionIds = new LinkedList<>();
        final List<String> clientSessionIds = new LinkedList<>();

        // Create sessions in separate transaction first
        KeycloakModelUtils.runJobInTransaction(sessionFactory, new KeycloakSessionTask() {

            @Override
            public void run(KeycloakSession session) {
                RealmModel realm = session.realms().getRealmByName("master");
                UserModel john = session.users().getUserByUsername("admin", realm);
                ClientModel testApp = realm.getClientByClientId("security-admin-console");
                UserSessionPersisterProvider persister = session.getProvider(UserSessionPersisterProvider.class);

                for (int i = 0; i < count; i++) {
                    UserSessionModel userSession = session.sessions().createUserSession(realm, john, "john-doh@localhost", "127.0.0.2", "form", true, null, null);
                    ClientSessionModel clientSession = session.sessions().createClientSession(realm, testApp);
                    clientSession.setUserSession(userSession);
                    clientSession.setRedirectUri("http://redirect");
                    clientSession.setNote("foo", "bar-" + i);
                    userSessionIds.add(userSession.getId());
                    clientSessionIds.add(clientSession.getId());
                }
            }

        });

        log.info("Sessions created in infinispan storage");

        // Persist them now
        KeycloakModelUtils.runJobInTransaction(sessionFactory, new KeycloakSessionTask() {

            @Override
            public void run(KeycloakSession session) {
                RealmModel realm = session.realms().getRealmByName("master");
                UserSessionPersisterProvider persister = session.getProvider(UserSessionPersisterProvider.class);

                int counter = 0;
                for (String userSessionId : userSessionIds) {
                    counter++;
                    UserSessionModel userSession = session.sessions().getUserSession(realm, userSessionId);
                    persister.createUserSession(userSession, true);
                    if (counter%1000 == 0) {
                        log.infof("%d user sessions persisted. Continue", counter);
                    }
                }

                log.infof("All %d user sessions persisted", counter);

                counter = 0;
                for (String clientSessionId : clientSessionIds) {
                    counter++;
                    ClientSessionModel clientSession = session.sessions().getClientSession(realm, clientSessionId);
                    persister.createClientSession(clientSession, true);
                    if (counter%1000 == 0) {
                        log.infof("%d client sessions persisted. Continue", counter);
                    }
                }

                log.infof("All %d client sessions persisted", counter);
            }

        });

        // Persist them now
        KeycloakModelUtils.runJobInTransaction(sessionFactory, new KeycloakSessionTask() {

            @Override
            public void run(KeycloakSession session) {
                UserSessionPersisterProvider persister = session.getProvider(UserSessionPersisterProvider.class);
                log.info("Total number of sessions in persister: " + persister.getUserSessionsCount(true));
            }

        });
    }

    @Override
    public String printUsage() {
        return super.printUsage() + " <sessions-count>";
    }
}
