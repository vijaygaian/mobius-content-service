package com.aidtaas.mobius.content.services.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.model.entity.Base64;
import com.aidtaas.mobius.content.service.layer.model.request.Base64ContentRequest;
import com.aidtaas.mobius.content.service.layer.model.response.Base64ContentResponse;
import com.aidtaas.mobius.content.service.layer.service.IBase64Service;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class Base64ControllerImplTest {

  @InjectMocks
  private Base64ControllerImpl base64ControllerImpl;

  @Mock
  private IBase64Service iBase64Service;

  @Mock
  private TenantIdExtractor tenantIdExtractor;

  /**
   * Method under test:
   * {@link Base64ControllerImpl#uploadBase64Content(Base64ContentRequest, HttpServletRequest)}
   */
  @Test
  void testUploadBase64Content() {
    // Arrange
    Base64ContentResponse buildResult = Base64ContentResponse.builder()
        .id("42")
        .method("Method")
        .msg("Msg")
        .url("https://example.org/example")
        .build();
    when(iBase64Service.uploadBase64Content(Mockito.<Base64ContentRequest>any(),
        Mockito.<HttpServletRequest>any()))
        .thenReturn(buildResult);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");
    Base64ContentRequest base64UploadRequest = new Base64ContentRequest();

    // Act
    ResponseEntity<Base64ContentResponse> actualUploadBase64ContentResult = base64ControllerImpl
        .uploadBase64Content(base64UploadRequest, new MockHttpServletRequest());

    // Assert
    verify(tenantIdExtractor).getTenantId(isA(HttpServletRequest.class));
    verify(iBase64Service).uploadBase64Content(isA(Base64ContentRequest.class),
        isA(HttpServletRequest.class));
    assertEquals(200, actualUploadBase64ContentResult.getStatusCodeValue(),"msg");
    assertTrue(actualUploadBase64ContentResult.hasBody(),"msg");
    assertTrue(actualUploadBase64ContentResult.getHeaders().isEmpty(),"msg");
  }

  /**
   * Method under test:
   * {@link Base64ControllerImpl#uploadBase64Content(Base64ContentRequest, HttpServletRequest)}
   */
  @Test
  void testUploadBase64Content2() throws UnsupportedEncodingException {
    // Arrange
    Base64ContentResponse buildResult = Base64ContentResponse.builder()
        .id("42")
        .method("Method")
        .msg("Msg")
        .url("https://example.org/example")
        .build();
    when(iBase64Service.uploadBase64Content(Mockito.<Base64ContentRequest>any(),
        Mockito.<HttpServletRequest>any()))
        .thenReturn(buildResult);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");
    Base64ContentRequest base64UploadRequest = mock(Base64ContentRequest.class);
    when(base64UploadRequest.getBase64Content()).thenReturn("AXAXAXAX".getBytes("UTF-8"));

    // Act
    ResponseEntity<Base64ContentResponse> actualUploadBase64ContentResult = base64ControllerImpl
        .uploadBase64Content(base64UploadRequest, new MockHttpServletRequest());

    // Assert
    verify(tenantIdExtractor).getTenantId(isA(HttpServletRequest.class));
    verify(base64UploadRequest).getBase64Content();
    verify(iBase64Service).uploadBase64Content(isA(Base64ContentRequest.class),
        isA(HttpServletRequest.class));
    assertEquals(200, actualUploadBase64ContentResult.getStatusCodeValue(),"msg");
    assertTrue(actualUploadBase64ContentResult.hasBody(),"msg");
    assertTrue(actualUploadBase64ContentResult.getHeaders().isEmpty(),"msg");
  }

  /**
   * Method under test: {@link Base64ControllerImpl#getBase64Content(HttpServletRequest, String)}
   */
  @Test
  void testGetBase64Content() throws UnsupportedEncodingException {
    // Arrange
    when(iBase64Service.getBase64Content(Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn("AXAXAXAX".getBytes("UTF-8"));
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");

    // Act
    ResponseEntity<byte[]> actualBase64Content = base64ControllerImpl.getBase64Content(
        new MockHttpServletRequest(),
        "42");

    // Assert
    verify(tenantIdExtractor).getTenantId(isA(HttpServletRequest.class));
    verify(iBase64Service).getBase64Content(("42"), ("42"));
    assertEquals(200, actualBase64Content.getStatusCodeValue(),"msg");
    assertTrue(actualBase64Content.hasBody(),"msg");
    assertTrue(actualBase64Content.getHeaders().isEmpty(),"msg");
  }

  /**
   * Method under test:
   * {@link Base64ControllerImpl#updateBase64Content(Base64ContentRequest, String,
   * HttpServletRequest)}
   */
  @Test
  void testUpdateBase64Content() throws UnsupportedEncodingException {
    // Arrange
    Base64 base64 = new Base64();
    base64.setBase64Content("AXAXAXAX".getBytes("UTF-8"));
    base64.setId("42");
    base64.setServiceId("42");
    base64.setTenantId("42");
    base64.setUserId("42");
    when(iBase64Service.updateBase64Content(Mockito.<Base64ContentRequest>any(),
        Mockito.<String>any()))
        .thenReturn(base64);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");
    Base64ContentRequest base64UploadRequest = new Base64ContentRequest();

    // Act
    ResponseEntity<Base64> actualUpdateBase64ContentResult = base64ControllerImpl
        .updateBase64Content(base64UploadRequest, "42", new MockHttpServletRequest());

    // Assert
    verify(tenantIdExtractor).getTenantId(isA(HttpServletRequest.class));
    verify(iBase64Service).updateBase64Content(isA(Base64ContentRequest.class), eq("42"));
    assertEquals(200, actualUpdateBase64ContentResult.getStatusCodeValue(),"msg");
    assertTrue(actualUpdateBase64ContentResult.hasBody(),"msg");
    assertTrue(actualUpdateBase64ContentResult.getHeaders().isEmpty(),"msg");
  }

  /**
   * Method under test:
   * {@link Base64ControllerImpl#updateBase64Content(Base64ContentRequest, String,
   * HttpServletRequest)}
   */
  @Test
  void testUpdateBase64Content2() throws UnsupportedEncodingException {
    // Arrange
    Base64 base64 = new Base64();
    base64.setBase64Content("AXAXAXAX".getBytes("UTF-8"));
    base64.setId("42");
    base64.setServiceId("42");
    base64.setTenantId("42");
    base64.setUserId("42");
    when(iBase64Service.updateBase64Content(Mockito.<Base64ContentRequest>any(),
        Mockito.<String>any()))
        .thenReturn(base64);
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");
    Base64ContentRequest base64UploadRequest = mock(Base64ContentRequest.class);
    when(base64UploadRequest.getBase64Content()).thenReturn("AXAXAXAX".getBytes("UTF-8"));

    // Act
    ResponseEntity<Base64> actualUpdateBase64ContentResult = base64ControllerImpl
        .updateBase64Content(base64UploadRequest, "42", new MockHttpServletRequest());

    // Assert
    verify(tenantIdExtractor).getTenantId(isA(HttpServletRequest.class));
    verify(base64UploadRequest).getBase64Content();
    verify(iBase64Service).updateBase64Content(isA(Base64ContentRequest.class), eq("42"));
    assertEquals(200, actualUpdateBase64ContentResult.getStatusCodeValue(),"msg");
    assertTrue(actualUpdateBase64ContentResult.hasBody(),"msg");
    assertTrue(actualUpdateBase64ContentResult.getHeaders().isEmpty(),"msg");
  }

  /**
   * Method under test:
   * {@link Base64ControllerImpl#deleteBase64Content(HttpServletRequest, String)}
   */
  @Test
  void testDeleteBase64Content() {
    // Arrange
    doNothing().when(iBase64Service)
        .deleteBase64Content(Mockito.<String>any(), Mockito.<String>any());
    when(tenantIdExtractor.getTenantId(Mockito.<HttpServletRequest>any())).thenReturn("42");

    // Act
    ResponseEntity<Void> actualDeleteBase64ContentResult = base64ControllerImpl
        .deleteBase64Content(new MockHttpServletRequest(), "42");

    // Assert
    verify(tenantIdExtractor).getTenantId(isA(HttpServletRequest.class));
    verify(iBase64Service).deleteBase64Content(("42"), ("42"));
    assertNull(actualDeleteBase64ContentResult.getBody(),"msg");
    assertEquals(204, actualDeleteBase64ContentResult.getStatusCodeValue(),"Unexpected status code");
    assertTrue(actualDeleteBase64ContentResult.getHeaders().isEmpty(),"msg");
  }
}
