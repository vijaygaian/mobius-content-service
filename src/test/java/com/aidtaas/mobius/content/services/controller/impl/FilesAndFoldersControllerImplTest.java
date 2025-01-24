/* (C)2024 */
package com.aidtaas.mobius.content.services.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.service.IFilesAndFoldersService;
import com.aidtaas.mobius.content.service.layer.utils.ContentConstants;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class FilesAndFoldersControllerImplTest {

    @InjectMocks private FilesAndFoldersControllerImpl filesAndFoldersController;

    @Mock private IFilesAndFoldersService filesAndFoldersService;
    @Mock private TenantIdExtractor tenantIdExtractor;


    @Test
    void testGetFiles() throws ExecutionException, InterruptedException {
        Pageable pageable = PageRequest.of(0, 10);
        JSONObject jsonObject = new JSONObject();

        when(filesAndFoldersService.getFiles(
                        anyString(), anyString(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(jsonObject);

        String tenantId = "testTenant";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ContentConstants.TENANT_ID_HEADER, tenantId);

        when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");

        ResponseEntity<JSONObject> response =
                filesAndFoldersController.getFiles(
                        request, "contentType", "filePath", "filePathAccess", pageable);

        assertEquals(200, response.getStatusCodeValue(), "Unexpected status code");
        assertEquals(jsonObject, response.getBody(), "Unexpected response body");
    }

    @Test
    void testGetDirectoryAndSubDirectory() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ContentConstants.TENANT_ID_HEADER, "testTenant");

        Pageable pageable = PageRequest.of(0, 10);
        JSONObject jsonObject = new JSONObject();
        // populate jsonObject as needed

        when(filesAndFoldersService.getDirectories(
                        anyString(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(jsonObject);
        when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");


        ResponseEntity<JSONObject> response =
                filesAndFoldersController.getDirectoryAndSubDirectory(
                        request, "filePath", "filePathAccess", pageable);

        assertEquals(200, response.getStatusCodeValue(), "Expected status code to be 200 but was not");
        assertEquals(jsonObject, response.getBody(), "Expected response body to match the provided jsonObject but did not");
    }

    @Test
    void testListContent() {
        List<String> fileNames = Collections.singletonList("testFile");

        when(filesAndFoldersService.getFileNames(anyString(), anyString())).thenReturn(fileNames);

        String tenantId = "testTenant";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ContentConstants.TENANT_ID_HEADER, tenantId);
        when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");


        ResponseEntity<List<String>> response =
                filesAndFoldersController.listContent(request, "filePath");
        assertEquals(200, response.getStatusCodeValue(), "Failure - expected HTTP status code to be 200");
        assertEquals(fileNames, response.getBody(), "Failure - expected fileNames to match the response body");
    }
}
