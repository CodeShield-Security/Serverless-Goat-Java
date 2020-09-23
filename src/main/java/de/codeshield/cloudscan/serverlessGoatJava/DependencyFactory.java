package de.codeshield.cloudscan.serverlessGoatJava;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/** The module containing all dependencies required by the {@link App}. */
public class DependencyFactory {

  private DependencyFactory() {}

  /** @return an instance of S3Client */
  public static AmazonS3 s3Client() {
    return AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
  }
}
