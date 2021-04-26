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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link me.amplitudo.inventar.domain.Equipment} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.EquipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter stock;

    private DoubleFilter pricePerUnit;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter equipmentRequestId;

    private LongFilter equipmentServicingId;

    private LongFilter equipmentEmployeesId;

    private LongFilter imagesId;

    private LongFilter manufacturerId;

    private LongFilter equipmentCategoryId;

    private LongFilter suplierId;

    public EquipmentCriteria() {
    }

    public EquipmentCriteria(EquipmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.equipmentRequestId = other.equipmentRequestId == null ? null : other.equipmentRequestId.copy();
        this.equipmentServicingId = other.equipmentServicingId == null ? null : other.equipmentServicingId.copy();
        this.equipmentEmployeesId = other.equipmentEmployeesId == null ? null : other.equipmentEmployeesId.copy();
        this.imagesId = other.imagesId == null ? null : other.imagesId.copy();
        this.manufacturerId = other.manufacturerId == null ? null : other.manufacturerId.copy();
        this.equipmentCategoryId = other.equipmentCategoryId == null ? null : other.equipmentCategoryId.copy();
        this.suplierId = other.suplierId == null ? null : other.suplierId.copy();
    }

    @Override
    public EquipmentCriteria copy() {
        return new EquipmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getStock() {
        return stock;
    }

    public void setStock(IntegerFilter stock) {
        this.stock = stock;
    }

    public DoubleFilter getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(DoubleFilter pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getEquipmentRequestId() {
        return equipmentRequestId;
    }

    public void setEquipmentRequestId(LongFilter equipmentRequestId) {
        this.equipmentRequestId = equipmentRequestId;
    }

    public LongFilter getEquipmentServicingId() {
        return equipmentServicingId;
    }

    public void setEquipmentServicingId(LongFilter equipmentServicingId) {
        this.equipmentServicingId = equipmentServicingId;
    }

    public LongFilter getEquipmentEmployeesId() {
        return equipmentEmployeesId;
    }

    public void setEquipmentEmployeesId(LongFilter equipmentEmployeesId) {
        this.equipmentEmployeesId = equipmentEmployeesId;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public LongFilter getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(LongFilter manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public LongFilter getEquipmentCategoryId() {
        return equipmentCategoryId;
    }

    public void setEquipmentCategoryId(LongFilter equipmentCategoryId) {
        this.equipmentCategoryId = equipmentCategoryId;
    }

    public LongFilter getSuplierId() {
        return suplierId;
    }

    public void setSuplierId(LongFilter suplierId) {
        this.suplierId = suplierId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EquipmentCriteria that = (EquipmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(equipmentRequestId, that.equipmentRequestId) &&
            Objects.equals(equipmentServicingId, that.equipmentServicingId) &&
            Objects.equals(equipmentEmployeesId, that.equipmentEmployeesId) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(manufacturerId, that.manufacturerId) &&
            Objects.equals(equipmentCategoryId, that.equipmentCategoryId) &&
            Objects.equals(suplierId, that.suplierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        stock,
        pricePerUnit,
        createdAt,
        updatedAt,
        equipmentRequestId,
        equipmentServicingId,
        equipmentEmployeesId,
        imagesId,
        manufacturerId,
        equipmentCategoryId,
        suplierId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (stock != null ? "stock=" + stock + ", " : "") +
                (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (equipmentRequestId != null ? "equipmentRequestId=" + equipmentRequestId + ", " : "") +
                (equipmentServicingId != null ? "equipmentServicingId=" + equipmentServicingId + ", " : "") +
                (equipmentEmployeesId != null ? "equipmentEmployeesId=" + equipmentEmployeesId + ", " : "") +
                (imagesId != null ? "imagesId=" + imagesId + ", " : "") +
                (manufacturerId != null ? "manufacturerId=" + manufacturerId + ", " : "") +
                (equipmentCategoryId != null ? "equipmentCategoryId=" + equipmentCategoryId + ", " : "") +
                (suplierId != null ? "suplierId=" + suplierId + ", " : "") +
            "}";
    }

}
