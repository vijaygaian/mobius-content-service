
/*
 * Copyright (c) 2024. Author :: developer
 *      Gaian Solutions Pvt Ltd.
 *      All rights reserved.
 */
package com.aidtaas.mobius.content.services.controller.impl;

import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.model.entity.Base64;
import com.aidtaas.mobius.content.service.layer.model.request.Base64ContentRequest;
import com.aidtaas.mobius.content.service.layer.model.response.Base64ContentResponse;
import com.aidtaas.mobius.content.service.layer.service.IBase64Service;
import com.aidtaas.mobius.content.services.controller.IBase64Controller;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Base64-Controller", description = "Base64 APIs")
@RequiredArgsConstructor
public class Base64ControllerImpl implements IBase64Controller {

  private final IBase64Service base64Service;
  private final TenantIdExtractor tenantIdExtractor;


  @Override
  public ResponseEntity<Base64ContentResponse> uploadBase64Content(
      @Parameter(name = "base64UploadRequest", required = true) @Valid @RequestBody Base64ContentRequest base64UploadRequest,
      HttpServletRequest httpServletRequest) {
    log.info("-----Base64ControllerImpl Class, uploadBase64Content method");

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.ok(base64Service.uploadBase64Content(
        Base64ContentRequest.builder().tenantId(tenantId)
            .base64Content(base64UploadRequest.getBase64Content()).build(), httpServletRequest));
  }

  @Override
  public ResponseEntity<byte[]> getBase64Content(HttpServletRequest httpServletRequest,
      @Parameter(name = "id of a base64", required = true) @PathVariable("id") String id) {
    log.info("-----Base64ControllerImpl Class, getBase64Content method");
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.status(HttpStatus.OK).body(base64Service.getBase64Content(tenantId, id));
  }

  @Override
  public ResponseEntity<Base64> updateBase64Content(
      @Parameter(name = "base64UploadRequest", required = true) @Valid @RequestBody Base64ContentRequest base64UploadRequest,
      @Parameter(name = "id of a base64", required = true) @PathVariable("id") String id,
      HttpServletRequest httpServletRequest) {
    log.info("-----Base64ControllerImpl Class, updateBase64Content method");
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.ok(base64Service.updateBase64Content(
        Base64ContentRequest.builder().tenantId(tenantId)
            .base64Content(base64UploadRequest.getBase64Content()).build(), id));
  }

  @Override
  public ResponseEntity<Void> deleteBase64Content(HttpServletRequest httpServletRequest,

      @Parameter(name = "id of a base64", required = true) @PathVariable("id") String id) {
    log.info("-----Base64ControllerImpl Class, deleteBase64Content method");
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    base64Service.deleteBase64Content(tenantId, id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Override
  public ResponseEntity<Object> retrieveContentEncryptedKey(String contentId, long version,
      HttpServletRequest httpServletRequest) {
    return ResponseEntity.ok().body(base64Service.retrieveContentEncryptedKey(contentId, version));
  }
}
