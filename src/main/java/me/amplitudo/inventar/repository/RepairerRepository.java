package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.Repairer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Repairer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepairerRepository extends JpaRepository<Repairer, Long>, JpaSpecificationExecutor<Repairer> {
}
