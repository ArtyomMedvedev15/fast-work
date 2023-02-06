package io.project.fastwork.repositories;

import io.project.fastwork.domains.TypeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeWorkRepository extends JpaRepository<TypeWork,Long> {
    TypeWork findByTypeWorkName(String type_work_name);
}
