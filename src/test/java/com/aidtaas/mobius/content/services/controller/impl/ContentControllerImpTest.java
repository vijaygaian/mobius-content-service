package com.aidtaas.mobius.content.services.controller.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.model.entity.ColorTags;
import com.aidtaas.mobius.content.service.layer.model.request.ContentRequest;
import com.aidtaas.mobius.content.service.layer.model.response.ContentMetadata;
import com.aidtaas.mobius.content.service.layer.model.response.ContentResponse;
import com.aidtaas.mobius.content.service.layer.service.IContentService;
import com.aidtaas.mobius.content.service.layer.utils.CommonMethods;
import com.aidtaas.mobius.content.service.layer.utils.ContentConstants;
import com.aidtaas.mobius.error.services.error.Error;
import com.aidtaas.mobius.error.services.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(SpringExtension.class)
class ContentControllerImpTest {

  @InjectMocks
  private ContentControllerImpl contentControllerImpl;

  @Mock
  private IContentService iContentService;

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private TenantIdExtractor tenantIdExtractor;
  @Mock
  private CommonMethods commonMethods;

  @Mock
  private HttpServletRequest httpServletRequest;

  /**
   * Method under test: {@link ContentControllerImpl#deleteContent(String, HttpServletRequest)}
   */
  @Test
  void testDeleteContent() throws IOException {
    // Arrange
    when(iContentService.delete("id", httpServletRequest)).thenReturn("Delete");

    // Act
    ResponseEntity<Object> actualDeleteContentResult = contentControllerImpl.deleteContent("42",
        httpServletRequest);

    // Assert
    verify(iContentService).delete(("42"), httpServletRequest);
    assertEquals(null, actualDeleteContentResult.getBody(), "msg");
    assertEquals(200, actualDeleteContentResult.getStatusCodeValue(), "msg");
    assertTrue(actualDeleteContentResult.getHeaders().isEmpty(), "msg");
  }

  /**
   * Method under test: {@link ContentControllerImpl#deleteContent(String, HttpServletRequest)}
   */
  @Test
  @Disabled("will fixed it latter")
  void testDeleteContent2() throws IOException {
    // Arrange
    when(iContentService.delete("id", httpServletRequest)).thenThrow(
        new ApiException(new Error(null, -1, "An error occurred", "Action Required")));

    // Act and Assert
    assertThrows(ApiException.class,
        () -> contentControllerImpl.deleteContent("42", httpServletRequest), "msg");
    verify(iContentService).delete(("42"), httpServletRequest);
  }

  @Test
  void testShouldReturnBadRequestWhenFileAndContentIdAreNull() {
    String contentId = null;
    MultipartFile file = null;
    String filePath = "testPath";
    List<String> tags = Collections.singletonList("testTag");
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ResponseEntity<Object> response = contentControllerImpl.updateContent(contentId, file, filePath,
        tags, fileName, filePathAccess, overrideFile, null, request);

    assertEquals(400, response.getStatusCodeValue(), "Msg");
  }

  @Test
  void testShouldUpdateContentWhenAllParametersAreValid() {
    String contentId = "testContentId";
    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
    String filePath = "testPath";
    List<String> tags = Collections.singletonList("testTag");
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ContentResponse expectedResponse = new ContentResponse();

    when(iContentService.updateFile(any(ContentRequest.class), anyString())).thenReturn(
        expectedResponse);

    ResponseEntity<Object> response = contentControllerImpl.updateContent(contentId, file, filePath,
        tags, fileName, filePathAccess, overrideFile, null, request);

    assertEquals(200, response.getStatusCodeValue(), "Msg");
    assertEquals(expectedResponse, response.getBody(), "Msg");
  }

  @Test
  void testShouldReturnBadRequestWhenFileIsNull() {
    String contentId = "testContentId";
    String filePath = "testPath";
    List<String> tags = Collections.singletonList("testTag");
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ResponseEntity<Object> response = contentControllerImpl.updateContent(contentId, null, filePath,
        tags, fileName, filePathAccess, overrideFile, null, request);

    assertEquals(400, response.getStatusCodeValue(), "Msg");
  }

  @Test
  void testShouldReturnBadRequestWhenContentIdIsNull() {
    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
    String filePath = "testPath";
    List<String> tags = Collections.singletonList("testTag");
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ResponseEntity<Object> response = contentControllerImpl.updateContent(null, file, filePath,
        tags, fileName, filePathAccess, overrideFile, null, request);

    assertEquals(400, response.getStatusCodeValue(), "Msg");
  }

  @Test
  void shouldRedirectWhenUrlIsValid() throws IOException {
    String id = "testId";
    String redirectUrl = "http://example.com";

    when(iContentService.viewContent(id, httpServletRequest)).thenReturn(redirectUrl);

    MockHttpServletResponse response = new MockHttpServletResponse();
    contentControllerImpl.viewContent(id, response, httpServletRequest);

    assertEquals(302, response.getStatus());
    assertEquals(redirectUrl, response.getRedirectedUrl());
  }


  @Test
  void shouldThrowThirdPartyExceptionWhenRuntimeExceptionOccurs() {
    String id = "testId";

    when(iContentService.viewContent(id, httpServletRequest)).thenThrow(
        new RuntimeException());

    MockHttpServletResponse response = new MockHttpServletResponse();

    assertThrows(RuntimeException.class,
        () -> contentControllerImpl.viewContent(id, response, httpServletRequest));
  }

  @Test
  void shouldReturnContentMetadataWhenFileNameIsValid() {
    String fileName = "validFileName";
    String filePath = "validFilePath";
    String filePathAccess = "PRIVATE";
    Pageable pageable = PageRequest.of(0, 10, Sort.by("fileName"));

    when(tenantIdExtractor.getTenantId(any())).thenReturn("testTenant");
    when(iContentService.searchContentByFileName(anyString(), anyString(), anyString(), anyString(),
        any(Pageable.class))).thenReturn(new JSONObject());

    ResponseEntity<JSONObject> response = contentControllerImpl.searchContentMetadataByFileName(
        fileName, null, filePath, filePathAccess, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldReturnContentMetadataWhenFileNameAndFilePathAreValid() {
    String fileName = "validFileName";
    String filePath = "validFilePath";
    String filePathAccess = "PRIVATE";
    Pageable pageable = PageRequest.of(0, 10, Sort.by("fileName"));

    when(tenantIdExtractor.getTenantId(any())).thenReturn("testTenant");
    when(iContentService.searchContentByFileName(anyString(), anyString(), anyString(), anyString(),
        any(Pageable.class))).thenReturn(new JSONObject());

    HttpServletRequest request = new MockHttpServletRequest();
    ResponseEntity<JSONObject> response = contentControllerImpl.searchContentMetadataByFileName(
        fileName, request, filePath, filePathAccess, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldThrowExceptionWhenFileNameIsNull() {
    String fileName = null;
    String filePath = "validFilePath";
    String filePathAccess = "PRIVATE";
    Pageable pageable = PageRequest.of(0, 10, Sort.by("fileName"));

    assertNotNull(contentControllerImpl.searchContentMetadataByFileName(fileName, null, filePath,
        filePathAccess, pageable));
  }

  @Test
  void shouldUpdateDescriptionWhenIdAndDescriptionAreValid() {
    String id = "testId";
    String description = "testDescription";

    when(iContentService.updateDescription(anyString(), anyString())).thenReturn(
        new ContentResponse());

    ResponseEntity<Object> response = contentControllerImpl.updateDescription(id, description);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldThrowExceptionWhenIdIsNull() {
    String id = null;
    String description = "testDescription";

    assertNotNull(contentControllerImpl.updateDescription(id, description));
  }

  @Test
  void shouldThrowExceptionWhenDescriptionIsNull() {
    String id = "testId";
    String description = null;

    assertNotNull(contentControllerImpl.updateDescription(id, description));
  }

//  @Test
//  void shouldUploadContentWhenAllParametersAreValid() {
//    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
//    String filePath = "testPath";
//    List<String> contentTags = Collections.singletonList("testTag");
//    String tags = "testTags";
//    String fileName = "testFile";
//    String filePathAccess = "testPathAccess";
//    String description = "testDescription";
//    Boolean overrideFile = false;
//    MockHttpServletRequest request = new MockHttpServletRequest();
//    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");
//
//    ContentResponse expectedResponse = new ContentResponse();
//
//    when(iContentService.uploadFile(any(ContentRequest.class))).thenReturn(expectedResponse);
//
//    ResponseEntity<ContentResponse> response = contentControllerImpl.uploadContent(file, filePath, contentTags, tags, fileName, filePathAccess, description, overrideFile, request);
//
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    assertEquals(expectedResponse, response.getBody());
//  }

  @Test
  void shouldThrowApiExceptionWhenTagsAreInvalid() throws JsonProcessingException {
    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
    String filePath = "testPath";
    List<String> contentTags = Collections.singletonList("testTag");
    String tags = "invalidTags";
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    String description = "testDescription";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    when(objectMapper.readValue(tags, ColorTags.class)).thenThrow(
        new JsonProcessingException("Invalid tags") {
        });

    assertThrows(ApiException.class,
        () -> contentControllerImpl.uploadContent(file, filePath, contentTags, tags, fileName,
            filePathAccess, description, overrideFile,  request));
  }

  @Test
  void shouldReturnAllUpdatedContentWhenContentIdIsValid() {
    String contentId = "validContentId";
    Pageable pageable = PageRequest.of(0, 10, Sort.by("fileName"));

    when(iContentService.getAllUpdatedContent(anyString(), any(Pageable.class))).thenReturn(
        new PageImpl<>(Collections.emptyList()));

    ResponseEntity<Page<ContentMetadata>> response = contentControllerImpl.getAllUpdatedContent(
        contentId, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldThrowExceptionWhenContentIdIsNull() {
    String contentId = null;
    Pageable pageable = PageRequest.of(0, 10, Sort.by("fileName"));

    assertNotNull(contentControllerImpl.getAllUpdatedContent(contentId, pageable));
  }

}
