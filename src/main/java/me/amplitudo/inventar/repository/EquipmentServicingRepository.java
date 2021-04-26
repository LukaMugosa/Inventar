package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.EquipmentServicing;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EquipmentServicing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentServicingRepository extends JpaRepository<EquipmentServicing, Long>, JpaSpecificationExecutor<EquipmentServicing> {
}
