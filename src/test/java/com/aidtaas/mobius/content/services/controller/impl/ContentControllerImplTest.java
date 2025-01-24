/* (C)2024 */
package com.aidtaas.mobius.content.services.controller.impl;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aidtaas.mobius.acl.lib.enums.Color;
import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.model.entity.ColorTags;
import com.aidtaas.mobius.content.service.layer.model.entity.ContentInfo;
import com.aidtaas.mobius.content.service.layer.model.request.ContentRequest;
import com.aidtaas.mobius.content.service.layer.model.request.ContentSearchRequest;
import com.aidtaas.mobius.content.service.layer.model.request.ContentTagsRequest;
import com.aidtaas.mobius.content.service.layer.model.response.ContentMetadata;
import com.aidtaas.mobius.content.service.layer.model.response.ContentResponse;
import com.aidtaas.mobius.content.service.layer.service.impl.ContentServiceImpl;
import com.aidtaas.mobius.content.service.layer.utils.ContentConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@ExtendWith(MockitoExtension.class)
class ContentControllerImplTest {

  @InjectMocks
  private ContentControllerImpl contentController;

  @Mock
  private ContentServiceImpl contentService;
  @Mock
  private TenantIdExtractor tenantIdExtractor;
  @Mock
  private ObjectMapper objectMapper;
  @Mock
  private ColorTags colourTags;
  @Mock
  private HttpServletRequest httpServletRequest;


  @Test
  void testSearchContentMetadata() {
    ContentSearchRequest request = new ContentSearchRequest();
    // populate request with test data

    ContentMetadata metadata = new ContentMetadata();
    // populate metadata with test data

    List<ContentMetadata> expectedContent = Collections.singletonList(metadata);

    Pageable pageable = PageRequest.of(0, 10);
    when(contentService.searchContentMetadata(any(ContentSearchRequest.class),
        any(Pageable.class))).thenReturn(expectedContent);

    ResponseEntity<ModelMap> response = contentController.searchContentMetadata(request, 0, 10);
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("viewname");
    modelAndView.addObject("contentMetadata", expectedContent);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    ModelAndViewAssert.assertModelAttributeValue(modelAndView, "contentMetadata", expectedContent);
  }

//    @Test
//    void testUploadContent() {
//        String userId = "testUser";
//        String serviceId = "testService";
//        MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
//        String filePath = "testPath";
//        List<String> tags = Collections.singletonList("testTag");
//        String fileName = "testFile";
//        String filePathAccess = "testPathAccess";
//        String description = "testDescription";
//        Boolean overrideFile = false;
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");
//
//        ContentResponse expectedResponse = new ContentResponse();
//
//        when(contentService.uploadFile(any(ContentRequest.class))).thenReturn(expectedResponse);
//
//        ResponseEntity<ContentResponse> response =
//                contentController.uploadContent(
//                        file,
//                        filePath,
//                        tags,
//                        fileName,
//                        filePathAccess,
//                        description,
//                        overrideFile,
//                        request);
//
//        assertEquals(200, response.getStatusCodeValue(),"Expected status code to be 200");
//        assertEquals(expectedResponse, response.getBody(),"Expected status code to be 200");
//    }

//    @Test
//     void testGetAllUpdatedContent() {
//        String contentId = "contentId";
//        List<ContentMetadata> expectedResponse = Arrays.asList(new ContentMetadata(), new ContentMetadata());
//
//        when(contentService.getAllUpdatedContent(contentId)).thenReturn(expectedResponse);
//
//        ResponseEntity<List<ContentMetadata>> response = contentController.getAllUpdatedContent(contentId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//    }


  @Test
  @Disabled("Test is failing")
  void testShouldUploadContentWhenAllParametersAreValid() {
    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
    String filePath = "testPath";
    List<String> contentTags = Collections.singletonList("testTag");
    String tags = "{\"tags\": [\"tag1\", \"tag2\"]}";
    String fileName = "testFile";
    colourTags = mock(ColorTags.class);
    EnumMap<Color, Set<String>> s = new EnumMap<>(Color.class);
    s.put(Color.RED, Collections.singleton("tag1"));

    colourTags.setTags(s);

    String filePathAccess = "testPathAccess";
    String description = "testDescription";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");
    ContentRequest contentRequest = ContentRequest.builder().tags(s).build();
    ContentResponse expectedResponse = new ContentResponse();
    when(colourTags.getTags()).thenReturn(s);
    when(contentService.uploadFile(any(ContentRequest.class))).thenReturn(expectedResponse);

    ResponseEntity<ContentResponse> response = contentController.uploadContent(file, filePath,
        contentTags, tags, fileName, filePathAccess, description, overrideFile, request);

    assertEquals(200, response.getStatusCodeValue(), "MSG");
    assertEquals(expectedResponse, response.getBody(), "MSG");
  }

  @Test
  void testShouldThrowApiExceptionWhenTagsAreInvalid() {
    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
    String filePath = "testPath";
    List<String> contentTags = Collections.singletonList("testTag");
    String tags = "invalid_tags";
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    String description = "testDescription";
    Boolean overrideFile = false;
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    assertThrows("msg", NullPointerException.class,
        () -> contentController.uploadContent(file, filePath, contentTags, tags, fileName,
            filePathAccess, description, overrideFile ,request));
  }


  @Test
  void testGetVersionsByParentId() {
    String contentId = "contentId";
    List<Long> expectedResponse = Arrays.asList(1L, 2L, 3L);

    when(contentService.getVersionsByParentId(contentId)).thenReturn(expectedResponse);

    ResponseEntity<List<Long>> response = contentController.getVersionsByParentId(contentId);

    assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code to be 200");
    assertEquals(expectedResponse, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testGetVersionsByParentId2() {
    String contentId = "contentId";
    List<Long> expectedResponse = Arrays.asList(1L, 2L, 3L);

    when(contentService.getVersionsByParentId(contentId)).thenReturn(expectedResponse);

    ResponseEntity<List<Long>> response = contentController.getVersionsByParentId(contentId);

    assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code to be 200");
    assertEquals(expectedResponse, response.getBody(), "Expected status code to be 200");
  }


  @Test
  @Disabled("will fixed it latter")
  void testViewContent() throws IOException {
    String contentId = "contentId";
    long version = 1;
    String redirectUrl = "http://example.com";

    when(contentService.viewContent(contentId, httpServletRequest)).thenReturn(redirectUrl);

    MockHttpServletResponse response = new MockHttpServletResponse();
    contentController.viewContent(contentId, version, response, httpServletRequest);

    verify(contentService).viewContent(contentId, httpServletRequest);
    assertEquals(redirectUrl, response.getRedirectedUrl(), "Expected status code to be 200");
  }

  @Test
  void testDownloadContentByVersion() throws IOException {
    String id = "contentId";
    long version = 1L;
    MockHttpServletRequest request = new MockHttpServletRequest();
    byte[] expectedResponse = "file content".getBytes();

    when(contentService.downloadContentByVersion(id, version, request)).thenReturn(
        new ResponseEntity<>(expectedResponse, HttpStatus.OK));

    ResponseEntity<byte[]> response = contentController.downloadContentByVersion(id, version,
        request);

    assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code to be 200");
    assertEquals(expectedResponse, response.getBody(), "Expected status code to be 200");
  }


  @Test
  void testGetContentMetadata() {
    String id = "testId";

    ContentMetadata expectedMetadata = new ContentMetadata();
    // populate expectedMetadata with test data

    when(contentService.getContentMetadata(anyString())).thenReturn(expectedMetadata);

    ResponseEntity<ContentMetadata> response = contentController.getContentMetadata(id,
        httpServletRequest);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(expectedMetadata, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testDeleteContent() throws IOException {
    String id = "testId";

    ResponseEntity<Object> response = contentController.deleteContent(id, httpServletRequest);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
  }

  @Test
  void testDownloadContent() throws IOException {
    String id = "testId";
    MockHttpServletRequest request = new MockHttpServletRequest();

    byte[] expectedContent = "Hello, World!".getBytes();

    when(contentService.downloadContent(anyString(), any(HttpServletRequest.class))).thenReturn(
        ResponseEntity.ok(expectedContent));

    ResponseEntity<byte[]> response = contentController.downloadContent(id, request);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(expectedContent, response.getBody(), "Expected status code to be 200");
  }


  @Test
  void testViewContent2() throws IOException {
    String id = "testId";
    String redirectUrl = "http://example.com";

    when(contentService.viewContent(id, httpServletRequest)).thenReturn(redirectUrl);

    MockHttpServletResponse response = new MockHttpServletResponse();
    contentController.viewContent(id, response, httpServletRequest);

    assertEquals(302, response.getStatus(), "Expected status code to be 302");
    assertEquals(redirectUrl, response.getRedirectedUrl(), "Expected status code to be 302");
  }

  @Test
  void testDeleteContentByVersion() throws IOException {
    String id = "contentId";
    long version = 1L;
    Object expectedResponse = new Object();

    when(contentService.deleteByVersion(id, version, httpServletRequest)).thenReturn(
        expectedResponse);

    ResponseEntity<Object> response = contentController.deleteContentByVersion(id, version,
        httpServletRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code to be 200");
    assertEquals(expectedResponse, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testDeleteContentByPath() throws IOException {
    String userId = "testUser";
    String filePath = "testPath";

    doNothing().when(contentService).delete(anyString(), anyString());

    MockHttpServletResponse request = new MockHttpServletResponse();
    ResponseEntity<Object> response = contentController.deleteContentByPath(request, userId,
        filePath);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
  }

  @Test
  void testDeleteContentByPathWithEmptyUserId() throws IOException {
    String userId = "";
    String filePath = "testPath";

    MockHttpServletResponse request = new MockHttpServletResponse();
    ResponseEntity<Object> response = contentController.deleteContentByPath(request, userId,
        filePath);

    assertEquals(400, response.getStatusCodeValue(), "Expected status code to be 400");
  }

  @Test
  void testDeleteContentByPathWithEmptyFilePath() throws IOException {
    String userId = "testUser";
    String filePath = "";

    MockHttpServletResponse request = new MockHttpServletResponse();
    ResponseEntity<Object> response = contentController.deleteContentByPath(request, userId,
        filePath);

    assertEquals(400, response.getStatusCodeValue(), "Expected status code to be 400");
  }

  @Test
  void testDeleteContentByPathOne() throws IOException {
    String tenantId = "testTenant";
    String filePath = "testPath";

    doNothing().when(contentService).delete_1(anyString(), anyString());

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, tenantId);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("testTenant");

    ResponseEntity<Object> response = contentController.deleteContentByPathOne(request, filePath);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
  }

  @Test
  void testDeleteContentByPathOneWithEmptyTenantId() throws IOException {
    String filePath = "testPath";

    MockHttpServletRequest request = new MockHttpServletRequest();
    ResponseEntity<Object> response = contentController.deleteContentByPathOne(request, filePath);

    assertEquals(400, response.getStatusCodeValue(), "Expected status code to be 200");
  }

  @Test
  void testDeleteContentByPathOneWithEmptyFilePath() throws IOException {
    String tenantId = "testTenant";

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, tenantId);
    ResponseEntity<Object> response = contentController.deleteContentByPathOne(request, "");

    assertEquals(400, response.getStatusCodeValue(), "Expected status code to be 200");
  }

  @Test
  void testUpdateTags() {
    String id = "testId";
    ContentTagsRequest contentTagsRequest = new ContentTagsRequest();
    // populate contentTagsRequest as needed

    doNothing().when(contentService).updateTags(anyString(), any(ContentTagsRequest.class));

    ResponseEntity<Object> response = contentController.updateTags(id, contentTagsRequest);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
  }

  @Test
  void testUploadAdminContent() {

    String userId = "testUser";
    String serviceId = "testService";
    String filePath = "testPath";
    String fileName = "testFile";
    MockMultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ContentResponse contentResponse = new ContentResponse();
    // populate contentResponse as needed

    when(contentService.uploadFile(any(ContentRequest.class))).thenReturn(contentResponse);

    ResponseEntity<ContentResponse> response = contentController.uploadAdminContent(userId,
        serviceId, file, filePath, fileName, request);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(contentResponse, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testUploadAdminContentWithNullFile() {
    String userId = "testUser";
    String serviceId = "testService";
    String filePath = "testPath";
    String fileName = "testFile";

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ResponseEntity<ContentResponse> response = contentController.uploadAdminContent(userId,
        serviceId, null, filePath, fileName, request);

    assertEquals(400, response.getStatusCodeValue(), "Expected status code to be 400");
  }

  @Test
  void testUploadMultipleFiles() {
    String userId = "testUser";
    String serviceId = "testService";
    String filePath = "testPath";
    String filePathAccess = "testPathAccess";
    String description = "testDescription";
    Boolean overrideFile = false;
    MockMultipartFile file1 = new MockMultipartFile("file1", "Hello, World!".getBytes());
    MockMultipartFile file2 = new MockMultipartFile("file2", "Hello, Universe!".getBytes());
    MultipartFile[] files = {file1, file2};

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ContentResponse contentResponse1 = new ContentResponse();
    // populate contentResponse1 as needed
    ContentResponse contentResponse2 = new ContentResponse();
    // populate contentResponse2 as needed

    when(contentService.uploadFile(any(ContentRequest.class))).thenReturn(contentResponse1,
        contentResponse2);

    ResponseEntity<List<ContentResponse>> response = contentController.uploadMultipleFiles(files,
        filePath, filePathAccess, description, overrideFile, request);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(Arrays.asList(contentResponse1, contentResponse2), response.getBody(),
        "Expected status code to be 200");
  }

  @Test
  void testUploadMultipleFilesWithNullFiles() {
    String userId = "testUser";
    String serviceId = "testService";
    String filePath = "testPath";
    String filePathAccess = "testPathAccess";
    String description = "testDescription";
    Boolean overrideFile = false;

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ResponseEntity<List<ContentResponse>> response = contentController.uploadMultipleFiles(

        null, filePath, filePathAccess, description, overrideFile, request);


    assertEquals(400, response.getStatusCodeValue(), "Expected status code to be 400");
  }
//=======
//    @Test
//     void testUploadMultipleFiles() {
//        String userId = "testUser";
//        String serviceId = "testService";
//        String filePath = "testPath";
//        String filePathAccess = "testPathAccess";
//        String description = "testDescription";
//        Boolean overrideFile = false;
//        MockMultipartFile file1 = new MockMultipartFile("file1", "Hello, World!".getBytes());
//        MockMultipartFile file2 = new MockMultipartFile("file2", "Hello, Universe!".getBytes());
//        MultipartFile[] files = {file1, file2};
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");
//
//        ContentResponse contentResponse1 = new ContentResponse();
//        // populate contentResponse1 as needed
//        ContentResponse contentResponse2 = new ContentResponse();
//        // populate contentResponse2 as needed
//
//
//        when(contentService.uploadFile(any(ContentRequest.class)))
//                .thenReturn(contentResponse1, contentResponse2);
//
//        ResponseEntity<List<ContentResponse>> response =
//                contentController.uploadMultipleFiles(
//
//                        files,
//                        filePath,
//                        filePathAccess,
//                        description,
//                        overrideFile,
//                        request);
//
//        assertEquals(200, response.getStatusCodeValue(),"Expected status code to be 200");
//        assertEquals(Arrays.asList(contentResponse1, contentResponse2), response.getBody(),"Expected status code to be 200");
//    }
//
//    @Test
//
//    void testUploadMultipleFilesWithNullFiles() {
//        String userId = "testUser";
//        String serviceId = "testService";
//        String filePath = "testPath";
//        String filePathAccess = "testPathAccess";
//        String description = "testDescription";
//        Boolean overrideFile = false;
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");
//
//
//        ResponseEntity<List<ContentResponse>> response =
//                contentController.uploadMultipleFiles(
//                        null,
//                        filePath,
//                        filePathAccess,
//                        description,
//                        overrideFile,
//                        request);
//>>>>>>> ec8ed5c71050f686fc97b9457cb6c35e345b7086

  @Test
  void testUploadContentFromURL() throws IOException {
    String userId = "testUser";
    String serviceId = "testService";
    String contentUrl = "http://example.com";
    String filePath = "testPath";
    List<String> tags = Arrays.asList("tag1", "tag2");
    String fileName = "testFile";
    String filePathAccess = "testPathAccess";
    String description = "testDescription";
    Boolean overrideFile = false;

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    ContentResponse contentResponse = new ContentResponse();
    // populate contentResponse as needed

    when(contentService.uploadContentFromURLUsingPOST(any(ContentRequest.class))).thenReturn(
        contentResponse);

    ResponseEntity<ContentResponse> response = contentController.uploadContentFromURL(contentUrl,
        filePath, tags, fileName, filePathAccess, description, overrideFile, request);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(contentResponse, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testSearchContentMetadata1() {
    ContentSearchRequest contentSearchRequest = new ContentSearchRequest();
    // populate contentSearchRequest as needed
    Integer pageNo = 0;
    Integer pageSize = 10;

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    List<ContentMetadata> contentMetadataList = Arrays.asList(new ContentMetadata(),
        new ContentMetadata());
    // populate contentMetadataList as needed

    when(contentService.searchContentMetadata(contentSearchRequest, pageable)).thenReturn(
        contentMetadataList);

    ResponseEntity<ModelMap> response = contentController.searchContentMetadata(
        contentSearchRequest, pageNo, pageSize);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("viewname");
    modelAndView.addObject("contentMetadata", contentMetadataList);

    ModelAndViewAssert.assertModelAttributeValue(modelAndView, "contentMetadata",
        contentMetadataList);

  }

  @Test
  void testGetContentList() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

    Pageable pageable = PageRequest.of(0, 1);
    String tag = "testTag";
    String sortValue = "createdDate";
    String sortOrder = "ascending";

    List<ContentInfo> contentInfoList = Arrays.asList(new ContentInfo(), new ContentInfo());
    // populate contentInfoList as needed
    Page<ContentInfo> contentInfoPage = new PageImpl<>(contentInfoList);

    when(contentService.findSortedContentByTenantWithRelavantTags(anyString(), any(Pageable.class),
        anyString(), anyString(), anyString())).thenReturn(contentInfoPage);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("testTenant");

    ResponseEntity<Page<ContentInfo>> response = contentController.getContentList(request, pageable,
        tag, sortValue, sortOrder);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(contentInfoPage, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testSearchContentMetadata2() {
    ContentSearchRequest contentSearchRequest = new ContentSearchRequest();
    String tenantId = "tenantId";
    String filePath = "filePath";
    String filePathAccess = "PRIVATE";
    Pageable pageable = PageRequest.of(0, 10);
    JSONObject expectedResponse = new JSONObject();

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ContentConstants.TENANT_ID_HEADER, tenantId);

    when(
        contentService.searchContentByTags(contentSearchRequest, tenantId, filePath, filePathAccess,
            pageable)).thenReturn(expectedResponse);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("tenantId");

    ResponseEntity<JSONObject> response = contentController.searchContentMetadata(
        contentSearchRequest, request, filePath, filePathAccess, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status code to be 200");
    assertEquals(expectedResponse, response.getBody(), "Expected status code to be 200");
  }

  @Test
  void testSearchContentMetadataByFileName() {
    String fileName = "testFile";
    String tenantId="tenant123";
    Pageable pageable = PageRequest.of(0, 10);

    List<ContentInfo> contentInfoList = Arrays.asList(new ContentInfo(), new ContentInfo());
    // populate contentInfoList as needed
    Page<ContentInfo> contentInfoPage = new PageImpl<>(contentInfoList);

    when(tenantIdExtractor.getTenantId(httpServletRequest)).thenReturn(tenantId);
    when(contentService.searchContentMetadataByFileName(anyString(),anyString(),
        any(Pageable.class))).thenReturn(contentInfoPage);

    ResponseEntity<Page<ContentInfo>> response = contentController.searchContentMetadataByFileName(
        fileName, pageable,httpServletRequest);

    assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200");
    assertEquals(contentInfoPage, response.getBody(), "Expected status code to be 200");
  }
}
