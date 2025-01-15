package com.mertncu.UniversityClubManagementFramework.service.file;

import com.mertncu.UniversityClubManagementFramework.entity.file.File;
import com.mertncu.UniversityClubManagementFramework.exception.ResourceNotFoundException;
import com.mertncu.UniversityClubManagementFramework.repository.file.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Value("${file.storage.location}") // Inject the property value
    private String fileStorageLocation;

    public File uploadFile(int clubId, String userId, String givenFileName, MultipartFile file) throws IOException {
        // 1. Validate the file (e.g., check file type, size)
        // ... (Add your validation logic here)

        // 2. Sanitize the given file name (important for security)
        String sanitizedFileName = sanitizeFileName(givenFileName);

        // 3. Generate a unique file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = sanitizedFileName + "-" + UUID.randomUUID() + fileExtension;

        // 4. Create the directory if it doesn't exist
        Path storageDirectory = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        Files.createDirectories(storageDirectory);

        // 5. Store the file on the local file system
        Path targetLocation = storageDirectory.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 6. Create a File entity and save it to the database
        File newFile = new File();
        newFile.setClubId(clubId);
        newFile.setFileName(uniqueFileName);
        newFile.setFilePath(targetLocation.toString()); // Store the absolute path
        newFile.setFileType(file.getContentType());
        newFile.setUploadedBy(userId);
        newFile.setUploadedAt(LocalDateTime.now());

        // Set the file title if provided
        if (givenFileName != null && !givenFileName.trim().isEmpty()) {
            newFile.setFileTitle(sanitizedFileName); // Use sanitized name as title
        } else {
            newFile.setFileTitle(originalFileName); // Use original file name as title
        }

        return fileRepository.save(newFile);
    }

    // Helper function to sanitize the given file name
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "file"; // Default name if none is provided
        }

        // Remove any potentially unsafe characters from the file name
        String sanitizedName = fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        // Limit the file name length (adjust as needed)
        return sanitizedName.length() > 255 ? sanitizedName.substring(0, 255) : sanitizedName;
    }

    public byte[] downloadFile(int fileId) throws IOException {
        File file = getFileById(fileId); // Fetch the File entity from the database
        Path filePath = Paths.get(file.getFilePath()); // Get the file path

        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File", "path", file.getFilePath());
        }

        return Files.readAllBytes(filePath); // Read and return the file content as a byte array
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public File getFileById(int fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId));
    }

    public List<File> getFilesByClubId(int clubId) {
        return fileRepository.findByClubId(clubId);
    }

    public void deleteFile(int fileId) throws IOException {
        File file = getFileById(fileId);
        Path filePath = Paths.get(file.getFilePath());

        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File", "path", file.getFilePath());
        }

        Files.delete(filePath);
        fileRepository.delete(file);
    }

}