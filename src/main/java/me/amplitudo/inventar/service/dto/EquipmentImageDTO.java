package me.amplitudo.inventar.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link me.amplitudo.inventar.domain.EquipmentImage} entity.
 */
public class EquipmentImageDTO implements Serializable {
    
    private Long id;

    
    @Lob
    private byte[] image;

    private String imageContentType;

    private Long equipmentId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentImageDTO)) {
            return false;
        }

        return id != null && id.equals(((EquipmentImageDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentImageDTO{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", equipmentId=" + getEquipmentId() +
            "}";
    }
}
