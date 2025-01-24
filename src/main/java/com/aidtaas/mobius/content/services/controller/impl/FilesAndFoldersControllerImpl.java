
/*
 * Copyright (c) 2024. Author :: developer
 *      Gaian Solutions Pvt Ltd.
 *      All rights reserved.
 */
package com.aidtaas.mobius.content.services.controller.impl;


import com.aidtaas.mobius.acl.lib.utils.TenantIdExtractor;
import com.aidtaas.mobius.content.service.layer.service.IFilesAndFoldersService;
import com.aidtaas.mobius.content.services.controller.IFilesAndFoldersController;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Files-And-Folders-Controller", description = "Files-And-Folders APIs")
@RequiredArgsConstructor
public class FilesAndFoldersControllerImpl implements IFilesAndFoldersController {


  private final IFilesAndFoldersService filesAndFoldersService;
  private final TenantIdExtractor tenantIdExtractor;


  @Override
  public ResponseEntity<JSONObject> getFiles(HttpServletRequest httpServletRequest,
      @Parameter(name = "Content Type", required = false)
      @RequestParam(value = "contentType", required = false) String contentType,
      @Parameter(name = "file path", required = false)
      @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "file path access")
      @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(hidden = true)
      @SortDefault(sort = "fileName", direction = Sort.Direction.ASC) @PageableDefault Pageable pageable) {
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.status(HttpStatus.OK).body(
        filesAndFoldersService.getFiles(tenantId, contentType, filePath, filePathAccess, pageable));
  }

  @Override
  public ResponseEntity<JSONObject> getDirectoryAndSubDirectory(
      HttpServletRequest httpServletRequest,
      @Parameter(name = "file path", required = false)
      @RequestParam(value = "filePath", required = false) String filePath,
      @Parameter(name = "file path access")
      @RequestParam(value = "filePathAccess", required = false) String filePathAccess,
      @Parameter(hidden = true) @SortDefault(sort = "createdBy", direction = Sort.Direction.DESC)
      @PageableDefault Pageable pageable)
      throws IOException {
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(filesAndFoldersService.getDirectories(tenantId, filePathAccess, filePath, pageable));
  }

  @Override
  public ResponseEntity<List<String>> listContent(HttpServletRequest httpServletRequest,
      @Parameter(name = "file path to view list of file names", required = true)
      @RequestParam(value = "filePath", required = true) String filePath) {
    String tenantId = tenantIdExtractor.getTenantId(httpServletRequest);
    if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(tenantId)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(filesAndFoldersService.getFileNames(tenantId, filePath));
  }
}
