/*
 * Copyright (c) 2016 Dimension Data, Inc.
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

package com.cloudera.director.dimensiondata;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.cloudera.director.dimensiondata.compute.DimensionDataComputeProvider;
import com.cloudera.director.dimensiondata.compute.DimensionDataComputeProviderConfigurationValidator;
import com.cloudera.director.dimensiondata.internal.DimensionDataCredentials;
import com.cloudera.director.spi.v1.model.ConfigurationProperty;
import com.cloudera.director.spi.v1.model.ConfigurationValidator;
import com.cloudera.director.spi.v1.model.Configured;
import com.cloudera.director.spi.v1.model.LocalizationContext;
import com.cloudera.director.spi.v1.model.util.CompositeConfigurationValidator;
import com.cloudera.director.spi.v1.provider.CloudProviderMetadata;
import com.cloudera.director.spi.v1.provider.ResourceProvider;
import com.cloudera.director.spi.v1.provider.ResourceProviderMetadata;
import com.cloudera.director.spi.v1.provider.util.AbstractCloudProvider;
import com.cloudera.director.spi.v1.provider.util.SimpleCloudProviderMetadataBuilder;
import com.typesafe.config.Config;

public class DimensionDataCloudProvider extends AbstractCloudProvider {

  public static final String ID = "dimensiondata";
    private static final List<ResourceProviderMetadata> RESOURCE_PROVIDER_METADATA = 
		  Collections.singletonList(DimensionDataComputeProvider.METADATA);

  private DimensionDataCredentials credentials;
  private Config applicationProperties;
  private Config dimensiondataConfig;

  protected static final CloudProviderMetadata METADATA = new SimpleCloudProviderMetadataBuilder()
      .id(ID)
      .name("Dimension Data Managed Cloud Platform (MCP 2.0)")
      .description("A provider implementation that provisions virtual resources on Dimension Data MCP.")
      .configurationProperties(Collections.<ConfigurationProperty>emptyList())
      .credentialsProviderMetadata(DimensionDataCredentialsProvider.METADATA)
      .resourceProviderMetadata(RESOURCE_PROVIDER_METADATA)
      .build();

  public DimensionDataCloudProvider(DimensionDataCredentials credentials, Config applicationProperties, Config dimensiondataConfig,
      LocalizationContext rootLocalizationContext) {
    super(METADATA, rootLocalizationContext);

    this.credentials = credentials;
    this.applicationProperties = applicationProperties;
    this.dimensiondataConfig = dimensiondataConfig;
  }

  @Override
  protected ConfigurationValidator getResourceProviderConfigurationValidator(
      ResourceProviderMetadata resourceProviderMetadata) {
    ConfigurationValidator providerSpecificValidator;
    if (resourceProviderMetadata.getId().equals(DimensionDataComputeProvider.METADATA.getId())) {
      providerSpecificValidator = new DimensionDataComputeProviderConfigurationValidator(credentials);
    } else {
      throw new NoSuchElementException("Invalid provider id: " + resourceProviderMetadata.getId());
    }
    return new CompositeConfigurationValidator(METADATA.getProviderConfigurationValidator(),
        providerSpecificValidator);
  }

  @Override
  public ResourceProvider createResourceProvider(String resourceProviderId, Configured configuration) {

    if (DimensionDataComputeProvider.METADATA.getId().equals(resourceProviderId)) {
      return new DimensionDataComputeProvider(configuration, credentials, applicationProperties, dimensiondataConfig,
          getLocalizationContext());
    }

    throw new NoSuchElementException("Invalid provider id: " + resourceProviderId);
  }
}
