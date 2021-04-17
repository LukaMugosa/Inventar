package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.EquipmentService;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EquipmentService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentServiceRepository extends JpaRepository<EquipmentService, Long>, JpaSpecificationExecutor<EquipmentService> {
}
