/**
 * 29.10.2020 Copied from https://github.com/willh/lambda-helloworld-config/
 *
 * License: MIT
 */

package de.codeshield.cloudscan.serverlessGoatJava.apiGateway;

import java.util.Map;

public class ApiGatewayResponse {
  private final String body;
  private final Map<String, String> headers;
  private final int statusCode;
  private final boolean base64Encoded;

  /**
   * @param body The body of the response or null
   * @param headers the headers of the response or an empty map
   * @param statusCode the status code of the response
   * @param base64Encoded true if the body is base64 encoded
   */
  public ApiGatewayResponse(
      String body, Map<String, String> headers, int statusCode, boolean base64Encoded) {
    this.statusCode = statusCode;
    this.body = body;
    this.headers = headers;
    this.base64Encoded = base64Encoded;
  }

  public String getBody() {
    return body;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public boolean isBase64Encoded() {
    return base64Encoded;
  }
}
