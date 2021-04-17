package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.EquipmentRequest;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EquipmentRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentRequestRepository extends JpaRepository<EquipmentRequest, Long>, JpaSpecificationExecutor<EquipmentRequest> {
}
