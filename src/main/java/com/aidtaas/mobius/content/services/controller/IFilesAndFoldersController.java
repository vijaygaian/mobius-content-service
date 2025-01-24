
package com.aidtaas.mobius.content.services.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Files-And-Folders-Controller", description = "Files-And-Folders APIs")
public interface IFilesAndFoldersController {

  @Operation(summary = "Api to get all the public files and also the private files of the given tenantId",
      description = "Api to get all the public files and also the private files of the given tenantId", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(array = @ArraySchema(uniqueItems = false), schema = @Schema(implementation = JSONObject.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/files", produces = {"application/json"})
  ResponseEntity<JSONObject> getFiles(
      HttpServletRequest httpServletRequest,
      @Parameter(name = "Content Type", required = true, example = "image")
      @RequestParam(value = "contentType", required = true, defaultValue = "image") String contentType,
      @Parameter(name = "file path", required = false)
      @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "file path access")
      @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(hidden = true) @SortDefault(sort = "fileName", direction = Sort.Direction.ASC)
      @PageableDefault Pageable pageable);

  @Operation(summary = "Api to get all the directory / subDirectory of the given tenantId",
      description = "Api to get all the directory / subDirectory of the given tenantId", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(array = @ArraySchema(uniqueItems = false), schema = @Schema(implementation = JSONObject.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/directories", produces = {
      "application/json"})
  ResponseEntity<JSONObject> getDirectoryAndSubDirectory(
      HttpServletRequest httpServletRequest,
      @Parameter(name = "file path", required = false)
      @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "file path access", required = false)
      @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(hidden = true)
      @SortDefault(sort = "createdBy", direction = Sort.Direction.DESC)
      @PageableDefault Pageable pageable)
      throws IOException;


  @Operation(summary = "Api to list content for the specified path",
      description = "Api to list content for the specified path", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK", content = {
          @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/list/files", produces = {"application/json",
      "application/json"})
  ResponseEntity<List<String>> listContent(
      HttpServletRequest httpServletRequest,
      @Parameter(name = "file path to view list of file names", required = true)
      @RequestParam(value = "filePath", required = true) String filePath);

}
