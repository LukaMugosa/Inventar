package me.amplitudo.inventar.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import me.amplitudo.inventar.domain.enumeration.EquipmentRequestStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link me.amplitudo.inventar.domain.EquipmentRequest} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.EquipmentRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentRequestCriteria implements Serializable, Criteria {
    /**
     * Class for filtering EquipmentRequestStatus
     */
    public static class EquipmentRequestStatusFilter extends Filter<EquipmentRequestStatus> {

        public EquipmentRequestStatusFilter() {
        }

        public EquipmentRequestStatusFilter(EquipmentRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public EquipmentRequestStatusFilter copy() {
            return new EquipmentRequestStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private EquipmentRequestStatusFilter status;

    private InstantFilter createdAt;

    private LongFilter equipmentId;

    private LongFilter employeeId;

    public EquipmentRequestCriteria() {
    }

    public EquipmentRequestCriteria(EquipmentRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.equipmentId = other.equipmentId == null ? null : other.equipmentId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
    }

    @Override
    public EquipmentRequestCriteria copy() {
        return new EquipmentRequestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public EquipmentRequestStatusFilter getStatus() {
        return status;
    }

    public void setStatus(EquipmentRequestStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LongFilter getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(LongFilter equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EquipmentRequestCriteria that = (EquipmentRequestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(equipmentId, that.equipmentId) &&
            Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        status,
        createdAt,
        equipmentId,
        employeeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentRequestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }

}
