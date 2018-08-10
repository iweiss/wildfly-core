/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.extension.elytron;

import static org.jboss.as.controller.PersistentResourceXMLDescription.decorator;
import static org.wildfly.extension.elytron.ElytronDescriptionConstants.CREDENTIAL_STORES;
import static org.wildfly.extension.elytron.ElytronDescriptionConstants.JASPI;
import static org.wildfly.extension.elytron.ElytronDescriptionConstants.JASPI_CONFIGURATION;
import static org.wildfly.extension.elytron.ElytronDescriptionConstants.SECURITY_PROPERTY;

import org.jboss.as.controller.AttributeMarshallers;
import org.jboss.as.controller.AttributeParsers;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PersistentResourceXMLDescription;

/**
 * The subsystem parser, which uses stax to read and write to and from xml.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @since 6.0
 */
public class ElytronSubsystemParser4_0 extends ElytronSubsystemParser3_0 {

    final PersistentResourceXMLDescription jaspiConfigurationParser = PersistentResourceXMLDescription.builder(PathElement.pathElement(JASPI_CONFIGURATION))
            .setXmlWrapperElement(JASPI)
            .addAttributes(JaspiDefinition.ATTRIBUTES)
            .build();

    @Override
    String getNameSpace() {
        return ElytronExtension.NAMESPACE_4_0;
    }

    @Override
    PersistentResourceXMLDescription getAuditLoggingParser() {
        return new AuditLoggingParser().parser4_0;
    }

    public PersistentResourceXMLDescription getParserDescription() {
        return PersistentResourceXMLDescription.builder(ElytronExtension.SUBSYSTEM_PATH, getNameSpace())
                .addAttribute(ElytronDefinition.DEFAULT_AUTHENTICATION_CONTEXT)
                .addAttribute(ElytronDefinition.INITIAL_PROVIDERS)
                .addAttribute(ElytronDefinition.FINAL_PROVIDERS)
                .addAttribute(ElytronDefinition.DISALLOWED_PROVIDERS)
                .addAttribute(ElytronDefinition.SECURITY_PROPERTIES, new AttributeParsers.PropertiesParser(null, SECURITY_PROPERTY, true), new AttributeMarshallers.PropertiesAttributeMarshaller(null, SECURITY_PROPERTY, true))
                .addChild(getAuthenticationClientParser())
                .addChild(getProviderParser())
                .addChild(getAuditLoggingParser())
                .addChild(getDomainParser())
                .addChild(getRealmParser())
                .addChild(getCredentialSecurityFactoryParser())
                .addChild(getMapperParser())
                .addChild(getPermissionSetParser())
                .addChild(getHttpParser())
                .addChild(getSaslParser())
                .addChild(getTlsParser())
                .addChild(decorator(CREDENTIAL_STORES).addChild(new CredentialStoreParser().parser))
                .addChild(getDirContextParser())
                .addChild(getPolicyParser())
                .addChild(jaspiConfigurationParser) // new
                .build();
    }

    @Override
    protected PersistentResourceXMLDescription getMapperParser() {
        return new MapperParser().getParser();
    }

    @Override
    PersistentResourceXMLDescription getTlsParser() {
        return new TlsParser().tlsParser_4_0;
    }

}
