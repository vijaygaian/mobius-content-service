
/*
 * Copyright (c) 2024. Author :: developer
 *      Gaian Solutions Pvt Ltd.
 *      All rights reserved.
 */
package com.aidtaas.mobius.content.services.controller;

import com.aidtaas.mobius.content.service.layer.model.entity.Base64;
import com.aidtaas.mobius.content.service.layer.model.request.Base64ContentRequest;
import com.aidtaas.mobius.content.service.layer.model.response.Base64ContentResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Base64-Controller", description = "Base64 APIs")
@Hidden
public interface IBase64Controller {

  @Operation(summary = "Api to upload base64 content", description = "Api to upload base64 content", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = Base64ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v1.0/base64-content/{serviceId}/{userId}", produces = {
      "application/json"}, consumes = {"application/json"})
  ResponseEntity<Base64ContentResponse> uploadBase64Content(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request payload to upload base64 content", required = true, content = @Content(mediaType = "application/json", examples = {
          @ExampleObject(name = "Tenant1 upload with single tag", value = "{ \"tenantId\": \"tenant1\", \"userId\": \"user123\", \"serviceId\": \"service123\", \"tags\": [\"profile\"], \"base64Content\": \"SGVsbG8gV29ybGQ=\" }"),
          @ExampleObject(name = "Tenant2 upload with multiple tags", value = "{ \"tenantId\": \"tenant2\", \"userId\": \"user456\", \"serviceId\": \"service456\", \"tags\": [\"report\", \"pdf\"], \"base64Content\": \"U29tZSByZXBvcnQgY29udGVudA==\" }"),
          @ExampleObject(name = "Different service upload without tags", value = "{ \"tenantId\": \"tenant3\", \"userId\": \"user789\", \"serviceId\": \"service789\", \"tags\": [], \"base64Content\": \"QnJpZWYgdGV4dCBjb250ZW50\" }"),
          @ExampleObject(name = "Single tag and empty content (error case)", value = "{ \"tenantId\": \"tenant4\", \"userId\": \"user000\", \"serviceId\": \"service000\", \"tags\": [\"avatar\"], \"base64Content\": \"\" }"),
          @ExampleObject(name = "Multiple tags with full base64 content", value = "{ \"tenantId\": \"tenant5\", \"userId\": \"user999\", \"serviceId\": \"service999\", \"tags\": [\"document\", \"archive\"], \"base64Content\": \"QXJjaGl2ZWQgc2FtcGxlIGJhc2U2NCBjb250ZW50\" }")})) @Parameter(name = "base64UploadRequest", required = true) @Valid @RequestBody Base64ContentRequest base64UploadRequest,
      HttpServletRequest httpServletRequest);


  @Operation(summary = "Api to get base64 content", description = "Api to get base64 content", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = byte.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/base64-content/{serviceId}/{userId}/{id}", produces = {
      "application/json"})
  ResponseEntity<byte[]> getBase64Content(HttpServletRequest httpServletRequest,
      @Parameter(name = "id of a base64", required = true) @PathVariable("id") String id);


  @Operation(summary = "Api to update base64 content", description = "Api to update base64 content", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = Base64.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PutMapping(value = "/v1.0/base64-content/{serviceId}/{userId}/{id}", produces = {
      "application/json"}, consumes = {"application/json"})
  ResponseEntity<Base64> updateBase64Content(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request payload to update base64 content", required = true, content = @Content(mediaType = "application/json", examples = {
          @ExampleObject(name = "Update content with a single tag", value = "{ \"tenantId\": \"tenant1\", \"userId\": \"user123\", \"serviceId\": \"service123\", \"tags\": [\"profile\"], \"base64Content\": \"U3VjY2VzcyBVcGRhdGVkIENvbnRlbnQ=\" }"),
          @ExampleObject(name = "Update content with multiple tags", value = "{ \"tenantId\": \"tenant2\", \"userId\": \"user456\", \"serviceId\": \"service456\", \"tags\": [\"report\", \"archive\"], \"base64Content\": \"VXBkYXRlZCBSZXBvcnQgQ29udGVudA==\" }"),
          @ExampleObject(name = "Update content with no tags", value = "{ \"tenantId\": \"tenant3\", \"userId\": \"user789\", \"serviceId\": \"service789\", \"tags\": [], \"base64Content\": \"Tm8gdGFncyBjb250ZW50IHVwZGF0ZWQ=\" }"),
          @ExampleObject(name = "Update empty content (error case)", value = "{ \"tenantId\": \"tenant4\", \"userId\": \"user000\", \"serviceId\": \"service000\", \"tags\": [\"avatar\"], \"base64Content\": \"\" }"),
          @ExampleObject(name = "Update with large content and multiple tags", value = "{ \"tenantId\": \"tenant5\", \"userId\": \"user999\", \"serviceId\": \"service999\", \"tags\": [\"document\", \"archive\", \"important\"], \"base64Content\": \"QWx0ZXJlZCBhbmQgZXh0ZW5zaXZlIGNvbnRlbnQgdXBkYXRlZCB3aXRoIG11bHRpcGxlIHRhZ3M=\" }")})) @Parameter(name = "base64UploadRequest", required = true) @Valid @RequestBody Base64ContentRequest base64UploadRequest,
      @Parameter(name = "id of a base64", required = true) @PathVariable("id") String id,
      HttpServletRequest httpServletRequest);


  @Operation(summary = "Api to delete base64 content", description = "Api to delete base64 content", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = Void.class))}),
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")})
  @DeleteMapping(value = "/v1.0/base64-content/{serviceId}/{userId}/{id}", produces = {
      "application/json"})
  ResponseEntity<Void> deleteBase64Content(HttpServletRequest httpServletRequest,
      @Parameter(name = "id of a base64", required = true) @PathVariable("id") String id);


  @Operation(summary = "Api to get  content key", description = "Api to get  content key", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content-key", produces = {"application/json"})
  ResponseEntity<Object> retrieveContentEncryptedKey(
      @Parameter(name = "contentId") @RequestParam("contentId") String contentId,
      @Parameter(name = "version") @RequestParam("version") long version,
      HttpServletRequest httpServletRequest);
}
