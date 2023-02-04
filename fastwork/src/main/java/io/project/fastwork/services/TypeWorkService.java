package io.project.fastwork.services;

import io.project.fastwork.domains.TypeWork;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TypeWorkService {
    TypeWork saveTypeWork(TypeWork savedTypeWork);
    TypeWork updateTypeWork(TypeWork updatedTypeWork);
    TypeWork deleteTypeWork(TypeWork deletedTypeWork);
    List<TypeWork>findAll();
}
