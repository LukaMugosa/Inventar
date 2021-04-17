package me.amplitudo.inventar.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link me.amplitudo.inventar.domain.ProblemReport} entity.
 */
public class ProblemReportDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @Lob
    private byte[] image;

    private String imageContentType;
    private String createdAt;

    private String updatedAt;


    private Long problemReportCategoryId;

    private Long employeeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getProblemReportCategoryId() {
        return problemReportCategoryId;
    }

    public void setProblemReportCategoryId(Long problemReportCategoryId) {
        this.problemReportCategoryId = problemReportCategoryId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProblemReportDTO)) {
            return false;
        }

        return id != null && id.equals(((ProblemReportDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemReportDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", problemReportCategoryId=" + getProblemReportCategoryId() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
