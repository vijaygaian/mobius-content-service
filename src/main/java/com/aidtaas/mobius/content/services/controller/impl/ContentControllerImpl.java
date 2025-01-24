
/*
 * Copyright (c) 2024. Author :: developer
 *      Gaian Solutions Pvt Ltd.
 *      All rights reserved.
 */
package com.aidtaas.mobius.content.services.controller.impl;

import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.exception.CMSError;
import com.aidtaas.mobius.content.service.layer.model.entity.ColorTags;
import com.aidtaas.mobius.content.service.layer.model.entity.ContentInfo;
import com.aidtaas.mobius.content.service.layer.model.request.ContentRequest;
import com.aidtaas.mobius.content.service.layer.model.request.ContentSearchRequest;
import com.aidtaas.mobius.content.service.layer.model.request.ContentTagsRequest;
import com.aidtaas.mobius.content.service.layer.model.request.LicenceReqAndRes;
import com.aidtaas.mobius.content.service.layer.model.response.ContentMetadata;
import com.aidtaas.mobius.content.service.layer.model.response.ContentResponse;
import com.aidtaas.mobius.content.service.layer.model.response.KeyResponse;
import com.aidtaas.mobius.content.service.layer.service.IContentService;
import com.aidtaas.mobius.content.service.layer.utils.CommonMethods;
import com.aidtaas.mobius.content.services.controller.IContentController;
import com.aidtaas.mobius.error.services.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Tag(name = "Content-Controller", description = "Content APIs")
@RequiredArgsConstructor
public class ContentControllerImpl implements IContentController {


  @Qualifier("mobiusObjectMapper")
  private final ObjectMapper objectMapper;

  private final IContentService contentService;
  private final TenantIdExtractor tenantIdExtractor;
  private final CommonMethods commonMethods;


  public ResponseEntity<ContentResponse> uploadContent(MultipartFile file, String filePath,
      List<String> contentTags, String tags, String fileName, String filePathAccess,
      String description, Boolean overrideFile, HttpServletRequest httpServletRequest) {

    ColorTags colourTags = new ColorTags();
    if (!StringUtils.isEmpty(tags)) {
      try {
        colourTags = objectMapper.readValue(tags, ColorTags.class);
      } catch (JsonProcessingException e) {
        log.error("Error while parsing tags : {}", e.getMessage());
        throw new ApiException(CMSError.INVALID_TAGS);
      }
    }

    log.info("----OverrideFile : {}", overrideFile);
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.ok(contentService.uploadFile(
        ContentRequest.builder().filePath(filePath).fileName(fileName).tenantId(tenantId).file(file)
            .httpServletRequest(httpServletRequest).contentTags(contentTags)
            .filePathAccess(filePathAccess).tags(colourTags.getTags()).description(description)
            .overrideFile(overrideFile).encryptionKey("1234").build()));
  }

  @Override
  public ResponseEntity<ContentResponse> uploadContentWithEncryption(MultipartFile file,
      String filePath, List<String> contentTags, String tags, String fileName,
      String filePathAccess, String description, Boolean overrideFile, String key,
      HttpServletRequest httpServletRequest) {

    ColorTags colourTags = new ColorTags();
    if (!StringUtils.isEmpty(tags)) {
      try {
        colourTags = objectMapper.readValue(tags, ColorTags.class);
      } catch (JsonProcessingException e) {
        log.error("---- Error while parsing tags-----  : {}", e.getMessage());
        throw new ApiException(CMSError.INVALID_TAGS);
      }
    }
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.ok(contentService.uploadFileWithEncryption(
        ContentRequest.builder().filePath(filePath).fileName(fileName).tenantId(tenantId).file(file)
            .httpServletRequest(httpServletRequest).contentTags(contentTags)
            .filePathAccess(filePathAccess).tags(colourTags.getTags()).description(description)
            .overrideFile(overrideFile).encryptionKey(key).build()));
  }

  @Override
  public ResponseEntity<ContentResponse> uploadContentWithEncryptionAndLicencing(MultipartFile file,
      String filePath, List<String> contentTags, String tags, String fileName,
      String filePathAccess, String description, Boolean overrideFile,
      HttpServletRequest httpServletRequest) {

    ColorTags colourTags = new ColorTags();
    if (!StringUtils.isEmpty(tags)) {
      try {
        colourTags = objectMapper.readValue(tags, ColorTags.class);
      } catch (JsonProcessingException e) {
        log.error("---- Error while parsing tags------  : {}", e.getMessage());
        throw new ApiException(CMSError.INVALID_TAGS);
      }
    }
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.ok(contentService.uploadFileWithEncryptionAndLicence(
        ContentRequest.builder().filePath(filePath).fileName(fileName).tenantId(tenantId).file(file)
            .httpServletRequest(httpServletRequest).contentTags(contentTags)
            .filePathAccess(filePathAccess).tags(colourTags.getTags()).description(description)
            .overrideFile(overrideFile).build()));
  }


  @Override
  public ResponseEntity<Object> updateContent(String contentId, MultipartFile file, String filePath,
      List<String> tags, String fileName, String filePathAccess, Boolean overrideFile, String key,

      HttpServletRequest httpServletRequest) {
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    log.info("Updating update content controller");
    log.info("----Given tenantId : {} ", tenantId);

    if (ObjectUtils.isEmpty(file) || ObjectUtils.isEmpty(contentId)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    log.info("-----OverrideFile : {}", overrideFile);
    return ResponseEntity.ok(contentService.updateFile(
        ContentRequest.builder().filePath(filePath).fileName(fileName).tenantId(tenantId).file(file)
            .httpServletRequest(httpServletRequest).contentTags(tags).filePathAccess(filePathAccess)
            .overrideFile(overrideFile).encryptionKey(key).build(), contentId));
  }

  @Override
  public ResponseEntity<Page<ContentMetadata>> getAllUpdatedContent(String contentId,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(contentService.getAllUpdatedContent(contentId, pageable));
  }

  @Override
  public ResponseEntity<List<Long>> getVersionsByParentId(String contentID) {
    return ResponseEntity.ok(contentService.getVersionsByParentId(contentID));
  }

  @Override
  public ResponseEntity<byte[]> viewContentWithDecryption(String contentId, long version, String key,
      HttpServletResponse response, HttpServletRequest request) throws IOException {
    return contentService.viewContentWithDecryption(contentId, version, key, request);
  }

  @Override
  public ResponseEntity<byte[]> viewContentWithLicense(String contentId, long version,
      String license, String publicKey, HttpServletResponse response, HttpServletRequest request)
      throws IOException {
    log.info("inside controller view content with license contentId {} version {}", contentId,
        version);
    return contentService.viewContentWithLicense(contentId, version, license, publicKey, request);
  }

  @Override
  public ResponseEntity<byte[]> viewContentWithLicense(String id, String license, String publicKey,
      HttpServletResponse response, HttpServletRequest request) throws IOException {
    return contentService.viewContentWithLicense(id, license, publicKey, request);
  }

  @Override
  public ResponseEntity<byte[]> viewContentWithSignInUrl(String url, String license,
      String publicKey, HttpServletResponse response, HttpServletRequest request) {
    log.info("----ContentControllerImpl class, viewContentWithSignInUrl method----");
    LicenceReqAndRes licenceReqAndRes = LicenceReqAndRes.builder().signature(license)
        .publicKey(publicKey).fileName(url).build();
    return contentService.viewContentWithSignUrl(licenceReqAndRes);

  }

  @Override
  public void viewContent(String contentId, long version, HttpServletResponse response,
      HttpServletRequest request) throws IOException {
    response.sendRedirect(contentService.viewContent(contentId, version, request));
  }

  public ResponseEntity<ContentMetadata> getContentMetadata(String id, HttpServletRequest request) {
    return ResponseEntity.ok(contentService.getContentMetadata(id));
  }

  public ResponseEntity<Object> deleteContent(String id, HttpServletRequest request)
      throws IOException {
    return ResponseEntity.status(HttpStatus.OK).body(contentService.delete(id, request));
  }

  public ResponseEntity<byte[]> downloadContent(String id, HttpServletRequest request)
      throws IOException {
    return contentService.downloadContent(id, request);

  }

  @Override
  public ResponseEntity<byte[]> downloadContentByVersion(String id, long version,
      HttpServletRequest request) throws IOException {
    return contentService.downloadContentByVersion(id, version, request);
  }

  @Override
  public ResponseEntity<Object> deleteContentByVersion(String id, long version,
      HttpServletRequest request) throws IOException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(contentService.deleteByVersion(id, version, request));
  }

  @Override
  public ResponseEntity<byte[]> viewContentWithDryption(String id, String key,
      HttpServletResponse response, HttpServletRequest request) throws IOException {
    return contentService.viewContentWithDecryption(id, key, request);
  }

  public void viewContent(@Parameter(name = "id", required = true) @PathVariable("id") String id,
      HttpServletResponse response, HttpServletRequest request) throws IOException {
    response.sendRedirect(contentService.viewContent(id, request));
  }

  @Override
  public ResponseEntity<Object> deleteContentByPath(HttpServletResponse httpServletRequest,
      String userId, String filePath) throws IOException {

    log.info("-----UserId : {}, filetPath : {}", userId, filePath);
    if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(userId)) {
      log.error("-----Either filePath or userId is empty or null.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    contentService.delete(filePath, userId);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Object> deleteContentByPathOne(HttpServletRequest httpServletRequest,
      String filePath) throws IOException {

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    log.info("-----TenantId : {}, filetPath : {}", tenantId, filePath);
    if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(tenantId)) {
      log.error("-----Either filePath or tenantId is empty or null.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    contentService.delete_1(filePath, tenantId);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<Object> updateTags(String id,
      @Valid @RequestBody ContentTagsRequest contentTagsRequest) {
    log.info("----Given content id : {}", id);

    contentService.updateTags(id, contentTagsRequest);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Object> updateDescription(String id, String description) {
    log.info("Given content id : {}", id);
    return ResponseEntity.ok(contentService.updateDescription(id, description));
  }

  @Override
  public ResponseEntity<Object> updateAllDescription(String description) {
    return ResponseEntity.ok(contentService.updateAllEmptyDescription(description));
  }

  public ResponseEntity<ContentResponse> uploadAdminContent(String userId, String serviceId,
      MultipartFile file, String filePath, String fileName, HttpServletRequest httpServletRequest) {

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);

    log.info("Given tenantId : {} , userId : {}, senderApp : {}, filePath : {}, fileName : {}",
        tenantId, userId, serviceId, filePath, fileName);

    if (ObjectUtils.isEmpty(file)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(contentService.uploadFile(
        ContentRequest.builder().filePath(filePath).fileName(fileName).tenantId(tenantId).file(file)
            .httpServletRequest(httpServletRequest).build()));
  }

  public ResponseEntity<List<ContentResponse>> uploadMultipleFiles(MultipartFile[] files,
      String filePath, String filePathAccess, String description, Boolean overrideFile,

      HttpServletRequest httpServletRequest) {

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);

    log.info("Given tenantId : {}", tenantId);

    if (ObjectUtils.isEmpty(files) || files.length == 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    log.info("----Override : {}", overrideFile);
    List<ContentResponse> response = Arrays.asList(files).stream().map(
        file -> contentService.uploadFile(
            ContentRequest.builder().filePath(filePath).fileName(null).tenantId(tenantId).file(file)
                .httpServletRequest(httpServletRequest).filePathAccess(filePathAccess)
                .description(description).overrideFile(overrideFile).build())).toList();
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ContentResponse> uploadContentFromURL(String contentUrl, String filePath,
      List<String> tags, String fileName, String filePathAccess, String description,
      Boolean overrideFile, HttpServletRequest httpServletRequest) throws IOException {

    if (StringUtils.isEmpty(contentUrl) || StringUtils.isEmpty(fileName)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    log.info("-----Override : {}", overrideFile);
    return ResponseEntity.ok(contentService.uploadContentFromURLUsingPOST(
        ContentRequest.builder().filePath(filePath).fileName(fileName).tenantId(tenantId)
            .contentUrl(contentUrl).httpServletRequest(httpServletRequest).contentTags(tags)
            .filePathAccess(filePathAccess).description(description).overrideFile(overrideFile)
            .build()));
  }

  @Override
  public ResponseEntity<ModelMap> searchContentMetadata(
      @Valid @RequestBody ContentSearchRequest contentSearchRequest, Integer pageNo,
      Integer pageSize) {

    log.info("Given request : {}", contentSearchRequest);
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    List<ContentMetadata> contentByTags = contentService.searchContentMetadata(contentSearchRequest,
        pageable);
    return ResponseEntity.ok().body(new ModelMap("contentMetadata", contentByTags));
  }


  @Override
  public ResponseEntity<Page<ContentInfo>> getContentList(HttpServletRequest httpServletRequest,
      @PageableDefault Pageable pageable, String tag, String sortValue, String sortOrder) {

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);

    return ResponseEntity.ok(
        contentService.findSortedContentByTenantWithRelavantTags(tenantId, pageable, tag, sortValue,
            sortOrder));
  }

  @Override
  public ResponseEntity<Page<ContentInfo>> searchContentMetadataByFileName(String fileName,
      Pageable pageable,HttpServletRequest request) {
    log.info("-----ContentControllerImpl Class, searchContentMetadataByFileNameUsingGET method.");
    String tenantId=tenantIdExtractor.getTenantId(request);
    return ResponseEntity.status(HttpStatus.OK)
        .body(contentService.searchContentMetadataByFileName(fileName,tenantId, pageable));
  }

  @Override
  public ResponseEntity<JSONObject> searchContentMetadata(
      @Valid @RequestBody ContentSearchRequest contentSearchRequest,
      HttpServletRequest httpServletRequest, String filePath, String filePathAccess,
      @PageableDefault Pageable pageable) {

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);

    return ResponseEntity.status(HttpStatus.OK).body(
        contentService.searchContentByTags(contentSearchRequest, tenantId, filePath, filePathAccess,
            pageable));
  }

  @Override
  public ResponseEntity<JSONObject> searchContentMetadataByFileName(String fileName,
      HttpServletRequest httpServletRequest, String filePath, String filePathAccess,
      @PageableDefault Pageable pageable) {

    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);

    return ResponseEntity.status(HttpStatus.OK).body(
        contentService.searchContentByFileName(fileName, tenantId, filePath, filePathAccess,
            pageable));
  }

  @Override
  public ResponseEntity<KeyResponse> generateKey() {
    return ResponseEntity.ok().body(commonMethods.generateKey());
  }

  @Override
  public ResponseEntity<KeyResponse> retrieveKey(String kid) {
    return ResponseEntity.ok().body(commonMethods.retrieveKey(kid));
  }
}
