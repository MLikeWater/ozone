/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.ozone.om.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hadoop.hdds.client.DefaultReplicationConfig;
import org.apache.hadoop.hdds.protocol.StorageType;
import org.apache.hadoop.ozone.OzoneConsts;
import org.apache.hadoop.ozone.audit.Auditable;
import org.apache.hadoop.ozone.protocol.proto.OzoneManagerProtocolProtos.BucketArgs;

import com.google.common.base.Preconditions;

/**
 * A class that encapsulates Bucket Arguments.
 */
public final class OmBucketArgs extends WithMetadata implements Auditable {
  /**
   * Name of the volume in which the bucket belongs to.
   */
  private final String volumeName;
  /**
   * Name of the bucket.
   */
  private final String bucketName;
  /**
   * Bucket Version flag.
   */
  private Boolean isVersionEnabled;
  /**
   * Type of storage to be used for this bucket.
   * [RAM_DISK, SSD, DISK, ARCHIVE]
   */
  private StorageType storageType;

  private long quotaInBytes;
  private long quotaInNamespace;
  private DefaultReplicationConfig defaultReplicationConfig = null;
  /**
   * Bucket Owner Name.
   */
  private String ownerName;

  /**
   * Private constructor, constructed via builder.
   * @param volumeName - Volume name.
   * @param bucketName - Bucket name.
   * @param isVersionEnabled - Bucket version flag.
   * @param storageType - Storage type to be used.
   * @param quotaInBytes Volume quota in bytes.
   * @param quotaInNamespace Volume quota in counts.
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  private OmBucketArgs(String volumeName, String bucketName,
      Boolean isVersionEnabled, StorageType storageType,
      Map<String, String> metadata, long quotaInBytes, long quotaInNamespace,
      String ownerName) {
    this.volumeName = volumeName;
    this.bucketName = bucketName;
    this.isVersionEnabled = isVersionEnabled;
    this.storageType = storageType;
    this.metadata = metadata;
    this.quotaInBytes = quotaInBytes;
    this.quotaInNamespace = quotaInNamespace;
    this.ownerName = ownerName;
  }

  /**
   * Returns the Volume Name.
   * @return String.
   */
  public String getVolumeName() {
    return volumeName;
  }

  /**
   * Returns the Bucket Name.
   * @return String
   */
  public String getBucketName() {
    return bucketName;
  }

  /**
   * Returns true if bucket version is enabled, else false.
   * @return isVersionEnabled
   */
  public Boolean getIsVersionEnabled() {
    return isVersionEnabled;
  }

  /**
   * Returns the type of storage to be used.
   * @return StorageType
   */
  public StorageType getStorageType() {
    return storageType;
  }

  /**
   * Returns Bucket Quota in bytes.
   * @return quotaInBytes.
   */
  public long getQuotaInBytes() {
    return quotaInBytes;
  }

  /**
   * Returns Bucket Quota in key counts.
   * @return quotaInNamespace.
   */
  public long getQuotaInNamespace() {
    return quotaInNamespace;
  }

  /**
   * Returns Bucket default replication config.
   * @return
   */
  public DefaultReplicationConfig getDefaultReplicationConfig() {
    return defaultReplicationConfig;
  }

  /**
   * Sets the Bucket default replication config.
   */
  private void setDefaultReplicationConfig(
      DefaultReplicationConfig defaultReplicationConfig) {
    this.defaultReplicationConfig = defaultReplicationConfig;
  }

  /**
   * Returns Bucket Owner Name.
   *
   * @return ownerName.
   */
  public String getOwnerName() {
    return ownerName;
  }

  /**
   * Returns new builder class that builds a OmBucketArgs.
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public Map<String, String> toAuditMap() {
    Map<String, String> auditMap = new LinkedHashMap<>();
    auditMap.put(OzoneConsts.VOLUME, this.volumeName);
    auditMap.put(OzoneConsts.BUCKET, this.bucketName);
    auditMap.put(OzoneConsts.GDPR_FLAG,
        this.metadata.get(OzoneConsts.GDPR_FLAG));
    auditMap.put(OzoneConsts.IS_VERSION_ENABLED,
                String.valueOf(this.isVersionEnabled));
    if (this.storageType != null) {
      auditMap.put(OzoneConsts.STORAGE_TYPE, this.storageType.name());
    }
    if (this.ownerName != null) {
      auditMap.put(OzoneConsts.OWNER, this.ownerName);
    }
    return auditMap;
  }

  /**
   * Builder for OmBucketArgs.
   */
  public static class Builder {
    private String volumeName;
    private String bucketName;
    private Boolean isVersionEnabled;
    private StorageType storageType;
    private Map<String, String> metadata;
    private long quotaInBytes;
    private long quotaInNamespace;
    private DefaultReplicationConfig defaultReplicationConfig;
    private String ownerName;
    /**
     * Constructs a builder.
     */
    public Builder() {
      quotaInBytes = OzoneConsts.QUOTA_RESET;
      quotaInNamespace = OzoneConsts.QUOTA_RESET;
    }

    public Builder setVolumeName(String volume) {
      this.volumeName = volume;
      return this;
    }

    public Builder setBucketName(String bucket) {
      this.bucketName = bucket;
      return this;
    }

    public Builder setIsVersionEnabled(Boolean versionFlag) {
      this.isVersionEnabled = versionFlag;
      return this;
    }

    public Builder addMetadata(Map<String, String> metadataMap) {
      this.metadata = metadataMap;
      return this;
    }

    public Builder setStorageType(StorageType storage) {
      this.storageType = storage;
      return this;
    }

    public Builder setQuotaInBytes(long quota) {
      quotaInBytes = quota;
      return this;
    }

    public Builder setQuotaInNamespace(long quota) {
      quotaInNamespace = quota;
      return this;
    }

    public Builder setDefaultReplicationConfig(
        DefaultReplicationConfig defaultRepConfig) {
      this.defaultReplicationConfig = defaultRepConfig;
      return this;
    }

    public Builder setOwnerName(String owner) {
      ownerName = owner;
      return this;
    }

    /**
     * Constructs the OmBucketArgs.
     * @return instance of OmBucketArgs.
     */
    public OmBucketArgs build() {
      Preconditions.checkNotNull(volumeName);
      Preconditions.checkNotNull(bucketName);
      OmBucketArgs omBucketArgs =
          new OmBucketArgs(volumeName, bucketName, isVersionEnabled,
              storageType, metadata, quotaInBytes, quotaInNamespace, ownerName);
      omBucketArgs.setDefaultReplicationConfig(defaultReplicationConfig);
      return omBucketArgs;
    }
  }

  /**
   * Creates BucketArgs protobuf from OmBucketArgs.
   */
  public BucketArgs getProtobuf() {
    BucketArgs.Builder builder = BucketArgs.newBuilder();
    builder.setVolumeName(volumeName)
        .setBucketName(bucketName);
    if (isVersionEnabled != null) {
      builder.setIsVersionEnabled(isVersionEnabled);
    }
    if (storageType != null) {
      builder.setStorageType(storageType.toProto());
    }
    if (quotaInBytes > 0 || quotaInBytes == OzoneConsts.QUOTA_RESET) {
      builder.setQuotaInBytes(quotaInBytes);
    }
    if (quotaInNamespace > 0 || quotaInNamespace == OzoneConsts.QUOTA_RESET) {
      builder.setQuotaInNamespace(quotaInNamespace);
    }
    if (defaultReplicationConfig != null) {
      builder.setDefaultReplicationConfig(defaultReplicationConfig.toProto());
    }
    if (ownerName != null) {
      builder.setOwnerName(ownerName);
    }
    return builder.build();
  }

  /**
   * Parses BucketInfo protobuf and creates OmBucketArgs.
   * @param bucketArgs
   * @return instance of OmBucketArgs
   */
  public static OmBucketArgs getFromProtobuf(BucketArgs bucketArgs) {
    OmBucketArgs omBucketArgs =
        new OmBucketArgs(bucketArgs.getVolumeName(),
            bucketArgs.getBucketName(),
            bucketArgs.hasIsVersionEnabled() ?
                bucketArgs.getIsVersionEnabled() : null,
            bucketArgs.hasStorageType() ? StorageType.valueOf(
                bucketArgs.getStorageType()) : null,
            KeyValueUtil.getFromProtobuf(bucketArgs.getMetadataList()),
            bucketArgs.getQuotaInBytes(),
            bucketArgs.getQuotaInNamespace(),
            bucketArgs.hasOwnerName() ?
                bucketArgs.getOwnerName() : null);
    // OmBucketArgs ctor already has more arguments, so setting the default
    // replication config separately.
    omBucketArgs.setDefaultReplicationConfig(
        new DefaultReplicationConfig(bucketArgs.getDefaultReplicationConfig()));
    return omBucketArgs;
  }
}
