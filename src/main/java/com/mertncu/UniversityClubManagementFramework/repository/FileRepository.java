package com.mertncu.UniversityClubManagementFramework.repository;

import com.mertncu.UniversityClubManagementFramework.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByClubId(int clubId);
}