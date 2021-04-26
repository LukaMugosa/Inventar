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
 * Criteria class for the {@link me.amplitudo.inventar.domain.Employee} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter fullName;

    private StringFilter phoneNumber;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter userId;

    private LongFilter equipmentRequestsId;

    private LongFilter problemReportsId;

    private LongFilter equipmentServicingId;

    private LongFilter equipmentEmployeesId;

    private LongFilter tenantId;

    private LongFilter positionId;

    private LongFilter createdNotificationsId;

    private LongFilter receivedNotificationsId;

    public EmployeeCriteria() {
    }

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.equipmentRequestsId = other.equipmentRequestsId == null ? null : other.equipmentRequestsId.copy();
        this.problemReportsId = other.problemReportsId == null ? null : other.problemReportsId.copy();
        this.equipmentServicingId = other.equipmentServicingId == null ? null : other.equipmentServicingId.copy();
        this.equipmentEmployeesId = other.equipmentEmployeesId == null ? null : other.equipmentEmployeesId.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.positionId = other.positionId == null ? null : other.positionId.copy();
        this.createdNotificationsId = other.createdNotificationsId == null ? null : other.createdNotificationsId.copy();
        this.receivedNotificationsId = other.receivedNotificationsId == null ? null : other.receivedNotificationsId.copy();
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getEquipmentRequestsId() {
        return equipmentRequestsId;
    }

    public void setEquipmentRequestsId(LongFilter equipmentRequestsId) {
        this.equipmentRequestsId = equipmentRequestsId;
    }

    public LongFilter getProblemReportsId() {
        return problemReportsId;
    }

    public void setProblemReportsId(LongFilter problemReportsId) {
        this.problemReportsId = problemReportsId;
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

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public LongFilter getCreatedNotificationsId() {
        return createdNotificationsId;
    }

    public void setCreatedNotificationsId(LongFilter createdNotificationsId) {
        this.createdNotificationsId = createdNotificationsId;
    }

    public LongFilter getReceivedNotificationsId() {
        return receivedNotificationsId;
    }

    public void setReceivedNotificationsId(LongFilter receivedNotificationsId) {
        this.receivedNotificationsId = receivedNotificationsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(equipmentRequestsId, that.equipmentRequestsId) &&
            Objects.equals(problemReportsId, that.problemReportsId) &&
            Objects.equals(equipmentServicingId, that.equipmentServicingId) &&
            Objects.equals(equipmentEmployeesId, that.equipmentEmployeesId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(createdNotificationsId, that.createdNotificationsId) &&
            Objects.equals(receivedNotificationsId, that.receivedNotificationsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        fullName,
        phoneNumber,
        createdAt,
        updatedAt,
        userId,
        equipmentRequestsId,
        problemReportsId,
        equipmentServicingId,
        equipmentEmployeesId,
        tenantId,
        positionId,
        createdNotificationsId,
        receivedNotificationsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (equipmentRequestsId != null ? "equipmentRequestsId=" + equipmentRequestsId + ", " : "") +
                (problemReportsId != null ? "problemReportsId=" + problemReportsId + ", " : "") +
                (equipmentServicingId != null ? "equipmentServicingId=" + equipmentServicingId + ", " : "") +
                (equipmentEmployeesId != null ? "equipmentEmployeesId=" + equipmentEmployeesId + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
                (positionId != null ? "positionId=" + positionId + ", " : "") +
                (createdNotificationsId != null ? "createdNotificationsId=" + createdNotificationsId + ", " : "") +
                (receivedNotificationsId != null ? "receivedNotificationsId=" + receivedNotificationsId + ", " : "") +
            "}";
    }

}
