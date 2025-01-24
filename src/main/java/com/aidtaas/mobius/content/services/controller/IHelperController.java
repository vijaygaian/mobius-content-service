
/*
 * Copyright (c) 2024. Author :: developer
 *      Gaian Solutions Pvt Ltd.
 *      All rights reserved.
 */
package com.aidtaas.mobius.content.services.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Helper-Controller", description = "Helper APIs")
public interface IHelperController {


  @Hidden
  @GetMapping("/v1518/{tenantId}")
  ResponseEntity<Object> deleteDirectoryAlongWithFiles(
      @PathVariable(value = "tenantId", required = true) String tenantId,
      @RequestParam(value = "dir", required = false) String dir);

  /**
   * Latest responseCode for deleteContentByPath
   *
   * @param tenantId
   * @return
   * @throws Exception
   */

  @Hidden
  @Operation(summary = "Api to delete content for the given tenant Id", description = "nn", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")})
  @DeleteMapping(value = "/v1.0/content/delete/tenantId/{tenantId}", produces = {"application/json",
      "application/json"})
  ResponseEntity<Object> deleteContentByTenantId(
      @Parameter(name = "id of a tenant", required = true) @PathVariable("tenantId") String tenantId);


}
