package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TypeWorkServiceImpl implements TypeWorkServiceApi {


    @Override
    public TypeWork saveTypeWork(TypeWork savedTypeWork) {
        return null;
    }

    @Override
    public TypeWork updateTypeWork(TypeWork updatedTypeWork) {
        return null;
    }

    @Override
    public TypeWork deleteTypeWork(TypeWork deletedTypeWork) {
        return null;
    }

    @Override
    public List<TypeWork> findAll() {
        return null;
    }
}
