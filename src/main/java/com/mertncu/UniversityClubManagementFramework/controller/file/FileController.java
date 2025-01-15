package com.mertncu.UniversityClubManagementFramework.controller.file;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.dto.file.FileDTO;
import com.mertncu.UniversityClubManagementFramework.entity.file.File;
import com.mertncu.UniversityClubManagementFramework.service.club.ClubService;
import com.mertncu.UniversityClubManagementFramework.service.file.FileService;
import com.mertncu.UniversityClubManagementFramework.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")
public class FileController {

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Value("${server.base.url}")
    private String serverBaseUrl;

    @Autowired
    private FileService fileService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/files/upload")
    public ResponseEntity<AuthReqResDTO> uploadFile(
            @RequestParam("clubId") int clubId,
            @RequestParam("userId") String userId,
            @RequestParam(value = "fileName", required = false) String givenFileName,
            @RequestParam("file") MultipartFile file) {

        try {
            // Use the original file name as the given file name if the parameter is not provided
            if (givenFileName == null || givenFileName.trim().isEmpty()) {
                givenFileName = StringUtils.cleanPath(file.getOriginalFilename());
            }

            // Call the fileService to upload the file, passing the given file name
            File uploadedFile = fileService.uploadFile(clubId, userId, givenFileName, file);

            // Create FileDTO and set properties
            FileDTO fileDTO = new FileDTO();
            fileDTO.setFileId(uploadedFile.getFileId());
            fileDTO.setClubId(uploadedFile.getClubId());
            fileDTO.setFileName(uploadedFile.getFileName());
            fileDTO.setFileTitle(uploadedFile.getFileTitle()); // Set the file title
            fileDTO.setFileType(uploadedFile.getFileType());
            fileDTO.setUploadedAt(uploadedFile.getUploadedAt());
            fileDTO.setFileUrl(generateFileUrl(uploadedFile)); // Generate the URL

            var club = clubService.getClubById(uploadedFile.getClubId());
            if (club != null) {
                fileDTO.setClubName(club.getName());
            }

            var user = userManagementService.getUserById(uploadedFile.getUploadedBy());
            if (user != null) {
                fileDTO.setUploadedByName(user.getUser().getName());
            }

            AuthReqResDTO response = new AuthReqResDTO();
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("File uploaded successfully");
            response.setFileDTO(fileDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IOException e) {
            // Handle file upload exception
            AuthReqResDTO response = new AuthReqResDTO();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setError("File upload failed");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable int fileId) {
        try {
            File file = fileService.getFileById(fileId); // Fetch file metadata
            if (file == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = fileService.downloadFile(fileId);

            // Sanitize the original file name to remove any potentially unsafe characters
            String originalFileName = StringUtils.cleanPath(file.getFileName());

            // Set appropriate headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getFileType()));
            headers.setContentDispositionFormData("attachment", originalFileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);

        } catch (IOException e) {
            // Handle file download exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/files/getAllFiles")
    public ResponseEntity<AuthReqResDTO> getAllFiles() {
        List<File> files = fileService.getAllFiles();
        List<FileDTO> fileDTOs = files.stream().map(this::convertToDTO).collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setFileDTOs(fileDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/files/getFile/{fileId}")
    public ResponseEntity<AuthReqResDTO> getFileById(@PathVariable int fileId) {
        File file = fileService.getFileById(fileId);
        FileDTO fileDTO = convertToDTO(file);

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setFileDTO(fileDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/files/getFilesByClubId/{clubId}")
    public ResponseEntity<AuthReqResDTO> getFilesByClubId(@PathVariable int clubId) {
        List<File> files = fileService.getFilesByClubId(clubId);
        List<FileDTO> fileDTOs = files.stream().map(this::convertToDTO).collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setFileDTOs(fileDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/files/deleteFile/{fileId}")
    public ResponseEntity<AuthReqResDTO> deleteFile(@PathVariable int fileId) {
        try {
            fileService.deleteFile(fileId);
            AuthReqResDTO response = new AuthReqResDTO();
            response.setStatusCode(HttpStatus.NO_CONTENT.value());
            response.setMessage("File deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            // Handle file deletion exception
            AuthReqResDTO response = new AuthReqResDTO();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setError("File deletion failed");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private FileDTO convertToDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileId(file.getFileId());
        fileDTO.setClubId(file.getClubId());
        fileDTO.setFileName(file.getFileName());
        fileDTO.setFileType(file.getFileType());
        fileDTO.setUploadedAt(file.getUploadedAt());

        // Fetch and set the club name
        var club = clubService.getClubById(file.getClubId());
        if (club != null) {
            fileDTO.setClubName(club.getName());
        }

        // Fetch and set the uploader's name
        var user = userManagementService.getUserById(file.getUploadedBy());
        if (user != null) {
            fileDTO.setUploadedByName(user.getUser().getName());
        }

        return fileDTO;
    }

    private String generateFileUrl(File file) {
        return serverBaseUrl + "/public/files/download/" + file.getFileName();
    }
}