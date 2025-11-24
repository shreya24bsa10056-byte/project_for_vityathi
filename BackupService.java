package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class BackupService {
    private static final String BACKUP_DIR = "backups";
    
    public String createBackup() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupName = "ccrm_backup_" + timestamp;
        Path backupPath = backupDir.resolve(backupName);
        
        Files.createDirectories(backupPath);
        
        // In a real application, we would copy all data files to the backup directory
        // For this example, we'll just create an info file
        Path infoFile = backupPath.resolve("backup_info.txt");
        String infoContent = "CCRM Backup created at: " + LocalDateTime.now() + "\n";
        Files.writeString(infoFile, infoContent);
        
        return backupPath.toAbsolutePath().toString();
    }
    
    public long calculateBackupSize() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);
        if (!Files.exists(backupDir)) {
            return 0;
        }
        
        try (Stream<Path> paths = Files.walk(backupDir)) {
            return paths.filter(Files::isRegularFile)
                       .mapToLong(p -> {
                           try {
                               return Files.size(p);
                           } catch (IOException e) {
                               return 0L;
                           }
                       })
                       .sum();
        }
    }
    
    public void listBackupFiles() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);
        if (!Files.exists(backupDir)) {
            System.out.println("No backups found.");
            return;
        }
        
        try (Stream<Path> paths = Files.list(backupDir)) {
            paths.filter(Files::isDirectory)
                 .forEach(p -> {
                     try {
                         long size = Files.walk(p)
                                 .filter(Files::isRegularFile)
                                 .mapToLong(path -> {
                                     try {
                                         return Files.size(path);
                                     } catch (IOException e) {
                                         return 0L;
                                     }
                                 })
                                 .sum();
                         System.out.println(p.getFileName() + " - " + size + " bytes");
                     } catch (IOException e) {
                         System.out.println(p.getFileName() + " - Error calculating size");
                     }
                 });
        }
    }
}
