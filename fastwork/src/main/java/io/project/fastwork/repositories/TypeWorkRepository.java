package io.project.fastwork.repositories;

import io.project.fastwork.domains.TypeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeWorkRepository extends JpaRepository<TypeWork,Long> {
    @Query("select t from TypeWork t where t.typeWorkName =:nametypework")
    TypeWork findByTypeWorkName(@Param("nametypework") String type_work_name);

    @Query("select t from TypeWork t where t.id =:type_work_id")
    TypeWork getTypeWorkById(@Param("type_work_id") Long type_work_id);

}
