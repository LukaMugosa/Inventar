package me.amplitudo.inventar.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import me.amplitudo.inventar.domain.enumeration.EquipmentStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link me.amplitudo.inventar.domain.EquipmentEmployee} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.EquipmentEmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment-employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentEmployeeCriteria implements Serializable, Criteria {
    /**
     * Class for filtering EquipmentStatus
     */
    public static class EquipmentStatusFilter extends Filter<EquipmentStatus> {

        public EquipmentStatusFilter() {
        }

        public EquipmentStatusFilter(EquipmentStatusFilter filter) {
            super(filter);
        }

        @Override
        public EquipmentStatusFilter copy() {
            return new EquipmentStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateOfRent;

    private BooleanFilter active;

    private EquipmentStatusFilter status;

    private LongFilter equipmentId;

    private LongFilter employeeId;

    public EquipmentEmployeeCriteria() {
    }

    public EquipmentEmployeeCriteria(EquipmentEmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateOfRent = other.dateOfRent == null ? null : other.dateOfRent.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.equipmentId = other.equipmentId == null ? null : other.equipmentId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
    }

    @Override
    public EquipmentEmployeeCriteria copy() {
        return new EquipmentEmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(InstantFilter dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public EquipmentStatusFilter getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatusFilter status) {
        this.status = status;
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
        final EquipmentEmployeeCriteria that = (EquipmentEmployeeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateOfRent, that.dateOfRent) &&
            Objects.equals(active, that.active) &&
            Objects.equals(status, that.status) &&
            Objects.equals(equipmentId, that.equipmentId) &&
            Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateOfRent,
        active,
        status,
        equipmentId,
        employeeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentEmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateOfRent != null ? "dateOfRent=" + dateOfRent + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }

}
