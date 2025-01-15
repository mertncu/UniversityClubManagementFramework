package com.mertncu.UniversityClubManagementFramework.entity.club;


import jakarta.persistence.*;
import lombok.Data;

@Table(name = "Clubs")
@Entity
@Data
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long clubId;
    private String name;
    private String description;
    @Column(name = "club_picture")
    private String clubPicture;

    // Getter ve Setter metodları
    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClubPicture() {
        return clubPicture;
    }

    public void setClubPicture(String clubPicture) {
        this.clubPicture = clubPicture;
    }
}
