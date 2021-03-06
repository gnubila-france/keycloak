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
package org.keycloak.testsuite.broker.provider;

import org.keycloak.broker.provider.AbstractIdentityProvider;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.IdentityProviderModel;

import javax.ws.rs.core.Response;

/**
 * @author pedroigor
 */
public class CustomIdentityProvider extends AbstractIdentityProvider<IdentityProviderModel> {

    public CustomIdentityProvider(IdentityProviderModel config) {
        super(config);
    }

    @Override
    public Response performLogin(AuthenticationRequest request) {
        return null;
    }

    @Override
    public Response retrieveToken(FederatedIdentityModel identity) {
        return null;
    }
}
