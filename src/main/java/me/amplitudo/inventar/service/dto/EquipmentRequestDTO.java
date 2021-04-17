package me.amplitudo.inventar.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import me.amplitudo.inventar.domain.enumeration.EquipmentRequestStatus;

/**
 * A DTO for the {@link me.amplitudo.inventar.domain.EquipmentRequest} entity.
 */
public class EquipmentRequestDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private EquipmentRequestStatus status;

    private Instant createdAt;


    private Long equipmentId;

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

    public EquipmentRequestStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentRequestStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
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
        if (!(o instanceof EquipmentRequestDTO)) {
            return false;
        }

        return id != null && id.equals(((EquipmentRequestDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentRequestDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", equipmentId=" + getEquipmentId() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
