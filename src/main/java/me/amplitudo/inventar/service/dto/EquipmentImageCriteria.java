package me.amplitudo.inventar.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link me.amplitudo.inventar.domain.EquipmentImage} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.EquipmentImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentImageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter equipmentId;

    public EquipmentImageCriteria() {
    }

    public EquipmentImageCriteria(EquipmentImageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.equipmentId = other.equipmentId == null ? null : other.equipmentId.copy();
    }

    @Override
    public EquipmentImageCriteria copy() {
        return new EquipmentImageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(LongFilter equipmentId) {
        this.equipmentId = equipmentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EquipmentImageCriteria that = (EquipmentImageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(equipmentId, that.equipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        equipmentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentImageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
            "}";
    }

}
