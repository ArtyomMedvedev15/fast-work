package io.project.fastwork.services.implementation;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.repositories.TypeWorkRepository;
import io.project.fastwork.services.api.TypeWorkServiceApi;
import io.project.fastwork.services.exception.TypeWorkAlreadyExistsException;
import io.project.fastwork.services.exception.TypeWorkInvalidParameterException;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import io.project.fastwork.services.util.TypeWorkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TypeWorkServiceImpl implements TypeWorkServiceApi {

    private final TypeWorkRepository typeWorkRepository;

    @Override
    public TypeWork saveTypeWork(TypeWork savedTypeWork) throws TypeWorkInvalidParameterException, TypeWorkAlreadyExistsException {
        TypeWork check_type_work_exists = typeWorkRepository.findByTypeWorkName(savedTypeWork.getTypeWorkName());
        if (check_type_work_exists != null) {
            log.error("Type work with name {} already exists exception, in {}",savedTypeWork.getTypeWorkName(),new Date());
            throw new TypeWorkAlreadyExistsException("Type work already exists, try yet.");
        }
        if (TypeWorkValidator.TypeWorkValidDataValues(savedTypeWork)) {
            savedTypeWork.setTypeWorkDateCreate(Timestamp.valueOf(LocalDateTime.now()));
            log.info("Save new type work with name {} in {}",savedTypeWork.getTypeWorkName(),new Date());
            return typeWorkRepository.save(savedTypeWork);
        } else {
            log.error("Invalid parameter for type work, throw exception in {}",new Date());
            throw new TypeWorkInvalidParameterException();
        }
    }

    @Override
    public TypeWork updateTypeWork(TypeWork updatedTypeWork) throws TypeWorkInvalidParameterException, TypeWorkAlreadyExistsException {
        TypeWork typeWorkAlreadyExists = typeWorkRepository.findByTypeWorkName(updatedTypeWork.getTypeWorkName());
        if(typeWorkAlreadyExists!=null){
            log.error("Type work with name {} already exists, throw exception in {}",updatedTypeWork.getTypeWorkName(),new Date());
            throw new TypeWorkAlreadyExistsException();
        }
        if (TypeWorkValidator.TypeWorkValidDataValues(updatedTypeWork)) {
            log.info("Update type work with name {} in {}",updatedTypeWork.getTypeWorkName(),new Date());
            updatedTypeWork.setTypeWorkDateCreate(typeWorkRepository.getTypeWorkById(updatedTypeWork.getId()).getTypeWorkDateCreate());
            return typeWorkRepository.save(updatedTypeWork);
        } else {
            log.error("Invalid parameter for type work, throw exception in {}",new Date());
            throw new TypeWorkInvalidParameterException();
        }
    }

    @Override
    public TypeWork deleteTypeWork(TypeWork deletedTypeWork) throws TypeWorkNotFound {
        TypeWork check_type_work_exists = typeWorkRepository.findById(deletedTypeWork.getId()).orElse(null);
        if(check_type_work_exists!=null){
            log.warn("Delete type work with id {} in {}",deletedTypeWork.getId(),new Date());
            typeWorkRepository.delete(deletedTypeWork);
            return deletedTypeWork;
        }else{
            log.error("Type work with id {} was not found, throw exception in {}",deletedTypeWork.getId(),new Date());
            throw new TypeWorkNotFound(String.format("Type work with id %s not found",deletedTypeWork.getId()));
        }
    }

    @Override
    public TypeWork getTypeWorkById(Long id_type_work) throws TypeWorkNotFound {
        TypeWork check_type_work_exists = typeWorkRepository.findById(id_type_work).orElse(null);
        if(check_type_work_exists!=null){
            log.warn("Delete type work with id {} in {}",check_type_work_exists.getId(),new Date());
            return typeWorkRepository.getTypeWorkById(id_type_work);

        }else{
            log.error("Type work with id {} was not found, throw exception in {}",id_type_work,new Date());
            throw new TypeWorkNotFound(String.format("Type work with id %s not found",id_type_work));
        }
    }

    @Override
    public List<TypeWork> findAll() {
        log.info("Get all type work in {}",new Date());
        return typeWorkRepository.findAll();
    }
}
