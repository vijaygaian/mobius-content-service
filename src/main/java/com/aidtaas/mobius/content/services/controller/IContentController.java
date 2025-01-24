
/*
 * Copyright (c) 2024. Author :: developer
 *      Gaian Solutions Pvt Ltd.
 *      All rights reserved.
 */
package com.aidtaas.mobius.content.services.controller;

import com.aidtaas.mobius.content.service.layer.model.entity.ContentInfo;
import com.aidtaas.mobius.content.service.layer.model.request.ContentSearchRequest;
import com.aidtaas.mobius.content.service.layer.model.request.ContentTagsRequest;
import com.aidtaas.mobius.content.service.layer.model.response.ContentMetadata;
import com.aidtaas.mobius.content.service.layer.model.response.ContentResponse;
import com.aidtaas.mobius.content.service.layer.model.response.KeyResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Content-Controller", description = "Content APIs")
public interface IContentController {

  @Operation(summary = "Api to upload single content file", description = "Api to upload single content file", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Content response", content = {
          @Content(schema = @Schema(implementation = ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401000", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "400", description = "Bad Request: Required part is missing", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "type": "about:blank",
                      "title": "Bad Request",
                      "status": 400,
                      "detail": "Required part 'file' is not present.",
                      "instance": "/v1.0/content/upload",
                      "properties": null
                  }
              """)), @Content(mediaType = "application/json", schema = @Schema(example = """
              {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Required part 'filePath' is not present.",
                  "instance": "/v1.0/content/upload",
                  "properties": null
              }
          """))}), @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v1.0/content/upload", consumes = {"multipart/form-data"}, produces = {
      "application/json"})
  ResponseEntity<ContentResponse> uploadContent(
      @Parameter(name = "filedetail") @RequestPart("file") MultipartFile file,
      @Parameter(name = "filePath") @RequestParam(value = "filePath", required = true) String filePath,
      @Parameter(name = "contentTags") @RequestParam(value = "contentTags", required = false) List<String> contentTags,
      @Parameter(name = "tags") @RequestParam(value = "tags", required = false) String tags,
      @Parameter(name = "fileName") @RequestParam(value = "fileName", required = false) String fileName,
      @Parameter(name = "filePathAccess") @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(name = "description") @RequestParam(value = "description", required = false) String description,
      @Parameter(name = "overrideFile") @RequestParam(value = "overrideFile", defaultValue = "false") Boolean overrideFile,
      HttpServletRequest httpServletRequest);



  @Operation(summary = "Api to upload single content file in Encrypted format", description = "Api to upload single content file", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Content response", content = {
          @Content(schema = @Schema(implementation = ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401000", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "400", description = "Bad Request: Required part is missing", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "type": "about:blank",
                      "title": "Bad Request",
                      "status": 400,
                      "detail": "Required part 'file' is not present.",
                      "instance": "/v2.0/content/upload",
                      "properties": null
                  }
              """)), @Content(mediaType = "application/json", schema = @Schema(example = """
              {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Required part 'filePath' is not present.",
                  "instance": "/v2.0/content/upload",
                  "properties": null
              }
          """)), @Content(mediaType = "application/json", schema = @Schema(example = """
              {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Required part 'key' is not present.",
                  "instance": "/v2.0/content/upload",
                  "properties": null
              }
          """))}), @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v2.0/content/upload", consumes = {"multipart/form-data"}, produces = {
      "application/json"})
  ResponseEntity<ContentResponse> uploadContentWithEncryption(

      @Parameter(name = "file detail") @RequestPart("file") MultipartFile file,
      @Parameter(name = "filePath") @RequestParam(value = "filePath", required = true) String filePath,
      @Parameter(name = "contentTags") @RequestParam(value = "contentTags", required = false) List<String> contentTags,
      @Parameter(name = "tags") @RequestParam(value = "tags", required = false) String tags,
      @Parameter(name = "file name without extension") @RequestParam(value = "fileName", required = false) String fileName,
      @Parameter(name = "filePathAccess") @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(name = "description") @RequestParam(value = "description", required = false) String description,
      @Parameter(name = "overrideFile") @RequestParam(value = "overrideFile", defaultValue = "false") Boolean overrideFile,
      @Parameter(name = "key") @RequestParam(value = "key", required = true) String key,
      HttpServletRequest httpServletRequest);

  @Operation(summary = "Api to upload single content file in Encrypted format with licence", description = "Api to upload single content file", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Content response", content = {
          @Content(schema = @Schema(implementation = ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401000", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "400", description = "Bad Request: Required part is missing", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "type": "about:blank",
                      "title": "Bad Request",
                      "status": 400,
                      "detail": "Required part 'file' is not present.",
                      "instance": "/v3.0/content/upload",
                      "properties": null
                  }
              """)), @Content(mediaType = "application/json", schema = @Schema(example = """
              {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Required part 'filePath' is not present.",
                  "instance": "/v3.0/content/upload",
                  "properties": null
              }
          """))}), @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v3.0/content/upload", consumes = {"multipart/form-data"}, produces = {
      "application/json"})
  ResponseEntity<ContentResponse> uploadContentWithEncryptionAndLicencing(
      @Parameter(name = "file detail") @RequestPart("file") MultipartFile file,
      @Parameter(name = "filePath") @RequestParam(value = "filePath", required = true) String filePath,
      @Parameter(name = "contentTags") @RequestParam(value = "contentTags", required = false) List<String> contentTags,
      @Parameter(name = "tags") @RequestParam(value = "tags", required = false) String tags,
      @Parameter(name = "file name without extension") @RequestParam(value = "fileName", required = false) String fileName,
      @Parameter(name = "filePathAccess") @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(name = "description") @RequestParam(value = "description", required = false) String description,
      @Parameter(name = "overrideFile") @RequestParam(value = "overrideFile", defaultValue = "false") Boolean overrideFile,
      HttpServletRequest httpServletRequest);


  @Operation(summary = "Api to update single content file", description = "Api to update single content file", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Content response", content = {
          @Content(schema = @Schema(implementation = ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401000", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "400", description = "Bad Request: Required part is missing", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "type": "about:blank",
                      "title": "Bad Request",
                      "status": 400,
                      "detail": "Required part 'file' is not present.",
                      "instance": "/v1.0/content/update",
                      "properties": null
                  }
              """)), @Content(mediaType = "application/json", schema = @Schema(example = """
              {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Required part 'contentId' is not present.",
                  "instance": "/v1.0/content/update",
                  "properties": null
              }
          """)), @Content(mediaType = "application/json", schema = @Schema(example = """
              {
                  "type": "about:blank",
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Required part 'filePath' is not present.",
                  "instance": "/v1.0/content/update",
                  "properties": null
              }
          """))}),
      @ApiResponse(responseCode = "403002", description = "Forbidden: Tenant does not have access to the content.", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718114115531,
                      "origin": "mobius-content-service",
                      "errorCode": 403002,
                      "errorMessage": "Tenant does not have access to the content.",
                      "cause": {
                          "message": "Tenant does not have access to the content.",
                          "timestamp": 1718114115
                      },
                      "errorObject": {
                          "response": {
                              "hasAccess": [],
                              "prohibited": [
                                  {
                                      "constructType": "CONTENT",
                                      "requesterType": "TENANT",
                                      "msg": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 does not have READ access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT",
                                      "nodeIds": [
                                          "666857225f9e3e6cd814f03d"
                                      ],
                                      "description": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 must request for read access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT to the respective owner"
                                  }
                              ]
                          },
                          "prohibited": true
                      },
                      "httpStatusCode": "FORBIDDEN"
                  }
              """))}),
      @ApiResponse(responseCode = "404", description = "Not Found: No contents found for the given ContentId.", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718042371205,
                      "origin": "mobius-content-service",
                      "errorCode": 404001,
                      "errorMessage": "Content not found",
                      "cause": {
                          "actionRequired": "Please Provide Valid ContentId",
                          "timestamp": 0
                      },
                      "httpStatusCode": "NOT_FOUND"
                  }
              """))})})
  @PostMapping(value = "/v1.0/content/update", consumes = {"multipart/form-data"}, produces = {
      "application/json"})
  ResponseEntity<Object> updateContent(
      @Parameter(name = "contentId") @RequestParam(value = "contentId", required = true) String contentId,
      @Parameter(name = "file detail") @RequestPart("file") MultipartFile file,
      @Parameter(name = "filePath") @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "tags") @RequestParam(value = "tags", required = false) List<String> tags,
      @Parameter(name = "file name without extension") @RequestParam(value = "fileName", required = false) String fileName,
      @Parameter(name = "filePathAccess") @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(name = "overrideFile") @RequestParam(value = "overrideFile", defaultValue = "false") Boolean overrideFile,
      @Parameter(name = "key") @RequestParam(value = "key", defaultValue = "false") String key,
      HttpServletRequest httpServletRequest);


  @Operation(summary = "Api to get history of content info", description = "Api to get history of content info", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "History of content metadata", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ContentMetadata.class))}),
      @ApiResponse(responseCode = "401000", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "403002", description = "Forbidden: Tenant does not have access to the content.", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718114115531,
                      "origin": "mobius-content-service",
                      "errorCode": 403002,
                      "errorMessage": "Tenant does not have access to the content.",
                      "cause": {
                          "message": "Tenant does not have access to the content.",
                          "timestamp": 1718114115
                      },
                      "errorObject": {
                          "response": {
                              "hasAccess": [],
                              "prohibited": [
                                  {
                                      "constructType": "CONTENT",
                                      "requesterType": "TENANT",
                                      "msg": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 does not have READ access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT",
                                      "nodeIds": [
                                          "666857225f9e3e6cd814f03d"
                                      ],
                                      "description": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 must request for read access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT to the respective owner"
                                  }
                              ]
                          },
                          "prohibited": true
                      },
                      "httpStatusCode": "FORBIDDEN"
                  }
              """))}),
      @ApiResponse(responseCode = "404001", description = "Not Found: No contents found for the given ContentId.", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718042371205,
                      "origin": "mobius-content-service",
                      "errorCode": 404001,
                      "errorMessage": "Content not found",
                      "cause": {
                          "actionRequired": "Please Provide Valid ContentId",
                          "timestamp": 0
                      },
                      "httpStatusCode": "NOT_FOUND"
                  }
              """))})})
  @GetMapping(value = "/v1.0/content/history/{contentId}/info", produces = {"application/json",
      "application/json"})
  ResponseEntity<Page<ContentMetadata>> getAllUpdatedContent(
      @Parameter(name = "contentId", required = true) @PathVariable("contentId") String contentId,
      @Parameter(hidden = true) @SortDefault(sort = "fileName", direction = Sort.Direction.ASC) @PageableDefault Pageable pageable);


  @Operation(summary = "Api to get all the versions of content", description = "Api to get all the versions of content", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "All versions of content", content = {
          @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = Long.class, example = "[1, 2, 3]"))}),
      // Added example for 200 response
      @ApiResponse(responseCode = "401000", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "403002", description = "Forbidden: Tenant does not have access to the content.", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718114115531,
                      "origin": "mobius-content-service",
                      "errorCode": 403002,
                      "errorMessage": "Tenant does not have access to the content.",
                      "cause": {
                          "message": "Tenant does not have access to the content.",
                          "timestamp": 1718114115
                      },
                      "errorObject": {
                          "response": {
                              "hasAccess": [],
                              "prohibited": [
                                  {
                                      "constructType": "CONTENT",
                                      "requesterType": "TENANT",
                                      "msg": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 does not have READ access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT",
                                      "nodeIds": [
                                          "666857225f9e3e6cd814f03d"
                                      ],
                                      "description": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 must request for read access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT to the respective owner"
                                  }
                              ]
                          },
                          "prohibited": true
                      },
                      "httpStatusCode": "FORBIDDEN"
                  }
              """))}),
      @ApiResponse(responseCode = "404001", description = "Not Found: No versions found for the given contentId.", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718042371205,
                      "origin": "mobius-content-service",
                      "errorCode": 404001,
                      "errorMessage": "Content not found",
                      "cause": {
                          "actionRequired": "Please Provide Valid ContentId",
                          "timestamp": 0
                      },
                      "httpStatusCode": "NOT_FOUND"
                  }
              """))})})
  @GetMapping(value = "/v1.0/content/{contentId}/versions", produces = {"application/json",
      "application/json"})
  ResponseEntity<List<Long>> getVersionsByParentId(
      @Parameter(name = "contentId", required = true) @PathVariable("contentId") String contentID);

  @Operation(summary = "Api to view content with given id", description = "Api to view content with given id", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Content retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized: Missing or incorrect token", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718007228738,
                      "origin": "mobius-content-service",
                      "errorCode": 401000,
                      "errorMessage": "The token provided is invalid. Please ensure you enter a valid token.",
                      "subErrors": [
                          {
                              "rootMessage": "The token you've entered is not valid, meaning it either does not match the expected format or has expired. Please provide a valid token to access the requested service.",
                              "timestamp": 0
                          }
                      ],
                      "actionsRequired": [
                          "The token you provided is not recognized. Kindly supply a correct token to proceed."
                      ],
                      "httpStatusCode": "BAD_REQUEST"
                  }
              """))}),
      @ApiResponse(responseCode = "403", description = "Forbidden: You do not have permission to view this content", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718114115531,
                      "origin": "mobius-content-service",
                      "errorCode": 403002,
                      "errorMessage": "Tenant does not have access to the content.",
                      "cause": {
                          "message": "Tenant does not have access to the content.",
                          "timestamp": 1718114115
                      },
                      "errorObject": {
                          "response": {
                              "hasAccess": [],
                              "prohibited": [
                                  {
                                      "constructType": "CONTENT",
                                      "requesterType": "TENANT",
                                      "msg": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 does not have READ access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT",
                                      "nodeIds": [
                                          "666857225f9e3e6cd814f03d"
                                      ],
                                      "description": "TENANT : 8e80c5c3-b852-462d-baa9-f19a3e209cc5 must request for read access to nodeId : 666857225f9e3e6cd814f03d of type CONTENT to the respective owner"
                                  }
                              ]
                          },
                          "prohibited": true
                      },
                      "httpStatusCode": "FORBIDDEN"
                  }
              """))}),
      @ApiResponse(responseCode = "404", description = "Not Found: The requested content was not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(example = """
                  {
                      "timestamp": 1718042371205,
                      "origin": "mobius-content-service",
                      "errorCode": 404001,
                      "errorMessage": "Content not found",
                      "cause": {
                          "actionRequired": "Please Provide Valid ContentId",
                          "timestamp": 0
                      },
                      "httpStatusCode": "NOT_FOUND"
                  }
              """))})})
  @GetMapping(value = "/v1.0/content/{contentId}/version/{version}", produces = "application/json")
  void viewContent(
      @Parameter(name = "contentId", required = true) @PathVariable("contentId") String contentId,
      @Parameter(name = "version", required = false) @PathVariable("version") long version,
      HttpServletResponse response, HttpServletRequest request) throws IOException;

  @Hidden
  @GetMapping(value = "/v2.0/content/{contentId}/version/{version}", produces = "application/json")
  ResponseEntity<byte[]> viewContentWithDecryption(
      @Parameter(name = "contentId", required = true) @PathVariable("contentId") String contentId,
      @Parameter(name = "version", required = false) @PathVariable("version") long version,
      @Parameter(name = "key", required = false) @RequestParam("key") String key,
      HttpServletResponse response, HttpServletRequest request) throws IOException;

  @Hidden
  @GetMapping(value = "/v3.0/content/{contentId}/version/{version}", produces = "application/json")
  ResponseEntity<byte[]> viewContentWithLicense(
      @Parameter(name = "contentId", required = true) @PathVariable("contentId") String contentId,
      @Parameter(name = "version", required = false) @PathVariable("version") long version,
      @Parameter(name = "license", required = false) @RequestParam("license") String license,
      @Parameter(name = "publicKey", required = false) @RequestParam("publicKey") String publicKey,
      HttpServletResponse response, HttpServletRequest request) throws IOException;

  @Hidden
  @GetMapping(value = "/v3.0/content/{id}", produces = "application/json")
  ResponseEntity<byte[]> viewContentWithLicense(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      @Parameter(name = "license", required = false) @RequestParam("license") String license,
      @Parameter(name = "publicKey", required = false) @RequestParam("publicKey") String publicKey,
      HttpServletResponse response, HttpServletRequest request) throws IOException;

  @Hidden
  @GetMapping(value = "/v3.0/verify-content", produces = "application/json")
  ResponseEntity<byte[]> viewContentWithSignInUrl(
      @Parameter(name = "fileName", required = true) @RequestParam("fileName") String fileName,
      @Parameter(name = "signature") @RequestParam("signature") String signature,
      @Parameter(name = "publicKey") @RequestParam("publicKey") String publicKey,
      HttpServletResponse response, HttpServletRequest request);


  @Operation(summary = "Api to get content metadata", description = "Api to get content metadata", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content metadata", content = {
          @Content(schema = @Schema(implementation = ContentMetadata.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/{id}/info", produces = {"application/json",
      "application/json"})
  ResponseEntity<ContentMetadata> getContentMetadata(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      HttpServletRequest request);

  @Hidden
  @Operation(summary = "Api to upload single content file from URL", description = "Api to upload single content file from URL", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/upload/contentUrl", produces = "application/json")
  ResponseEntity<ContentResponse> uploadContentFromURL(
      @Parameter(name = "file detail") @RequestParam("contentUrl") String contentUrl,
      @Parameter(name = "file path") @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "tags") @RequestParam(value = "tags", required = false) List<String> tags,
      @Parameter(name = "file name without extension") @RequestParam(value = "fileName", required = false) String fileName,
      @Parameter(name = "file path access") @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(name = "description") @RequestParam(value = "description", required = false) String description,
      @Parameter(name = "overrideFile") @RequestParam(value = "overrideFile", defaultValue = "false") Boolean overrideFile,
      HttpServletRequest httpServletRequest) throws IOException;


  @Operation(summary = "Api to upload multiple content files", description = "Api to upload multiple content files", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(array = @ArraySchema(uniqueItems = false, schema = @Schema(implementation = ContentResponse.class)))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v1.0/content/upload/multiple", produces = "application/json", consumes = "multipart/form-data")
  ResponseEntity<List<ContentResponse>> uploadMultipleFiles(
      @Parameter(name = "content files", required = true) @RequestPart(value = "files", required = true) MultipartFile[] files,
      @Parameter(name = "file path") @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "file path access") @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(name = "description") @RequestParam(value = "description", required = false) String description,
      @Parameter(name = "overrideFile") @RequestParam(value = "overrideFile", defaultValue = "false") Boolean overrideFile,
      HttpServletRequest httpServletRequest);

  /**
   * @param file
   * @param filePath
   * @param fileName
   * @param httpServletRequest
   * @return
   * @throws Exception
   */
  @Operation(summary = "Api to upload content in specified path", description = "Api to upload content in specified path", tags = {})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "content response", content = {
          @Content(schema = @Schema(implementation = ContentResponse.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v1.0/content/admin/upload/{serviceId}/{userId}", produces = {
      "application/json"}, consumes = {"multipart/form-data"})
  ResponseEntity<ContentResponse> uploadAdminContent(
      @Parameter(name = "id of a user", required = true) @PathVariable("userId") String userId,
      @Parameter(name = "sender application name", required = true) @PathVariable("serviceId") String serviceId,
      @Parameter(name = "file detail") @RequestPart("file") MultipartFile file,
      @Parameter(name = "filepath", required = true) @RequestParam(value = "filePath", required = true) String filePath,
      @Parameter(name = "file name without extension", required = true) @RequestParam(value = "fileName", required = true) String fileName,
      HttpServletRequest httpServletRequest);


  /**
   * @param id
   * @param request
   * @return
   * @throws Exception
   */
  @Operation(summary = "Api to download content with given id", description = "Api to download content with given id", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = byte[].class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/download/{id}", produces = {"application/json"})
  ResponseEntity<byte[]> downloadContent(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      HttpServletRequest request) throws IOException;


  @Operation(summary = "Api to download content with given id", description = "Api to download content with given id", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = byte[].class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/download/{id}/versions/{version}", produces = {
      "application/json"})
  ResponseEntity<byte[]> downloadContentByVersion(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      @Parameter(name = "version", required = true) @PathVariable("version") long version,
      HttpServletRequest request) throws IOException;

  /**
   * @param id
   * @return
   * @throws Exception
   */
  @Operation(summary = "Api to delete content by content id and version", description = "Api to delete content by content id and version", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")})
  @DeleteMapping(value = "/v1.0/content/{id}/versions/{version}", produces = {"application/json"})
  ResponseEntity<Object> deleteContentByVersion(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      @Parameter(name = "version", required = true) @PathVariable("version") long version,
      HttpServletRequest request) throws IOException;

  @Hidden
  @Operation(summary = "Api to delete content by content id", description = "Api to delete content by content id", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")})
  @DeleteMapping(value = "/v1.0/content/{id}", produces = {"application/json"})
  ResponseEntity<Object> deleteContent(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      HttpServletRequest request) throws IOException;


  /**
   * @param id
   * @param response
   * @throws Exception
   */

  @Operation(summary = "Api to view content with given id", description = "Api to view content with given id", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/{id}", produces = "application/json")
  void viewContent(@Parameter(name = "id", required = true) @PathVariable("id") String id,
      HttpServletResponse response, HttpServletRequest request) throws IOException;

  @GetMapping(value = "/v2.0/content/{id}", produces = "application/json")
  ResponseEntity<byte[]> viewContentWithDryption(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      @Parameter(name = "key") @RequestParam("key") String key, HttpServletResponse response,
      HttpServletRequest request) throws IOException;

  /**
   * Old code for delete content by path
   *
   * @param userId
   * @param filePath
   * @return
   * @throws Exception
   */

  @Hidden
  @Operation(summary = "Api to delete content for the specified path", description = "Api to delete content for the specified path", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")})
  @DeleteMapping(value = "/v1.0/content/delete/{userId}", produces = {"application/json",
      "application/json"})
  ResponseEntity<Object> deleteContentByPath(HttpServletResponse httpServletRequest,
      @Parameter(name = "id of a user", required = true) @PathVariable("userId") String userId,
      @Parameter(name = "filename to delete with respective path", required = true) @RequestParam(value = "filePath", required = true) String filePath)
      throws IOException;

  /**
   * Latest code for deleteContentByPath
   *
   * @param filePath
   * @return
   * @throws Exception
   */
  @Hidden
  @Operation(summary = "Api to delete content for the specified path", description = "Api to delete content for the specified path", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")})
  @DeleteMapping(value = "/v1.1/content/delete", produces = {"application/json"})
  ResponseEntity<Object> deleteContentByPathOne(HttpServletRequest httpServletRequest,
      @Parameter(name = "filename to delete with respective path", required = true) @RequestParam(value = "filePath", required = true) String filePath)
      throws IOException;

  /**
   * @param id
   * @param contentTagsRequest
   * @return
   * @throws Exception
   */
  @Operation(summary = "Api to update tags of a video file", description = "Api to update tags of a video file", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PutMapping(value = "/v1.0/content/tags/{id}", produces = "application/json", consumes = "application/json")
  ResponseEntity<Object> updateTags(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request payload to update tags of a video file", required = true, content = @Content(mediaType = "application/json", examples = {
          @ExampleObject(name = "Update tags with a single tag", value = "{ \"tags\": [\"tutorial\"] }"),
          @ExampleObject(name = "Update tags with multiple tags", value = "{ \"tags\": [\"education\", \"science\", \"technology\"] }")})) @Parameter(name = "contentTagsRequest", required = true) @Valid @RequestBody ContentTagsRequest contentTagsRequest);

  @Hidden
  @Operation(summary = "Api to update description of a content ", description = "Api to update description of a content", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PutMapping(value = "/v1.0/content/description/{id}", produces = "application/json", consumes = "application/json")
  ResponseEntity<Object> updateDescription(
      @Parameter(name = "id", required = true) @PathVariable("id") String id,
      @Parameter(name = "description", required = true) @RequestParam("description") String description);

  @Hidden
  @Operation(summary = "Api to update all Empty  description of contents ", description = "Api to update all Empty  description of contents", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PutMapping(value = "/v1.0/content/descriptions", produces = "application/json", consumes = "application/json")
  ResponseEntity<Object> updateAllDescription(
      @Parameter(name = "description", required = true) @RequestParam("description") String description);


  @Operation(summary = "Api to search contents by fileName.", description = "Api to search contents by fileName.", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/searchByFileName", produces = "application/json")
  ResponseEntity<Page<ContentInfo>> searchContentMetadataByFileName(
      @Parameter(name = "fileName", required = true) @RequestParam(value = "fileName", required = true) String fileName,
      Pageable pageable,HttpServletRequest request);

  @Operation(summary = "Api to search contents by fileName.", description = "Api to search contents by fileName", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.1/content/searchByFileName", produces = "application/json")
  ResponseEntity<JSONObject> searchContentMetadataByFileName(
      @Parameter(name = "fileName", required = true) @RequestParam(value = "fileName", required = true) String fileName,
      HttpServletRequest httpServletRequest,
      @Parameter(name = "filePath", required = false) @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "filePathAccess", required = false) @RequestParam(value = "filePathAccess", required = false, defaultValue = "PRIVATE") String filePathAccess,
      @Parameter(hidden = true) @SortDefault(sort = "fileName", direction = Sort.Direction.ASC) @PageableDefault Pageable pageable);

  @Hidden
  @Operation(summary = "Api to search contents.", description = "Api to search contents.", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v1.0/content/search", produces = "application/json", consumes = "application/json")
  ResponseEntity<ModelMap> searchContentMetadata(
      @Parameter(name = "contentSearchRequest", required = true) @Valid @RequestBody ContentSearchRequest contentSearchRequest,
      @Parameter(name = "pageNo") @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
      @Parameter(name = "pageSize") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

  @Operation(summary = "Api to search contents.", description = "Api to search contents", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @PostMapping(value = "/v1.1/content/search", produces = "application/json", consumes = "application/json")
  ResponseEntity<JSONObject> searchContentMetadata(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request payload to search contents based on tags", required = true, content = @Content(mediaType = "application/json", examples = {
          @ExampleObject(name = "Search with a single tag", value = "{ \"tags\": [\"tutorial\"] }"),
          @ExampleObject(name = "Search with multiple tags", value = "{ \"tags\": [\"education\", \"science\", \"technology\"] }")})) @Parameter(name = "contentSearchRequest", required = true) @Valid @RequestBody ContentSearchRequest contentSearchRequest,
      HttpServletRequest httpServletRequest,
      @Parameter(name = "file path", required = false) @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "file path access", required = false) @RequestParam(value = "filePathAccess", required = false, defaultValue = "PRIVATE") String filePathAccess,
      @Parameter(hidden = true) @SortDefault(sort = "fileName", direction = Sort.Direction.ASC) @PageableDefault Pageable pageable);

  @Operation(summary = "Api to view all content list", description = "Api to view all content list", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = Object.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/content/list", produces = "application/json")
  ResponseEntity<Page<ContentInfo>> getContentList(HttpServletRequest httpServletRequest,
      @PageableDefault Pageable pageable,
      @Parameter(name = "tag to search") @RequestParam(value = "tag", required = false) String tag,
      @Parameter(name = "sort by") @RequestParam(value = "sortValue", required = false, defaultValue = "createdDate") String sortValue,
      @Parameter(name = "sort order") @RequestParam(value = "sortOrder", required = false, defaultValue = "ascending") String sortOrder);

  @Operation(summary = "Api to generate the key ", description = "Api to generate the key ", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = KeyResponse.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/generate-key", produces = "application/json")
  ResponseEntity<KeyResponse> generateKey();

  @Operation(summary = "Api to retrieve the key ", description = "Api to retrieve the key ", tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = KeyResponse.class))}),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  @GetMapping(value = "/v1.0/retrieve-key", produces = "application/json")
  ResponseEntity<KeyResponse> retrieveKey(
      @Parameter(name = "kid") @RequestParam(value = "kid") String kid);
}
