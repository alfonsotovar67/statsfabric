package com.orioninc.statsfabric.repositories;

import com.orioninc.statsfabric.entities.InformationSchemaColumns;
import com.orioninc.statsfabric.repositories.interfaces.CustomRepositoryInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRepository implements CustomRepositoryInterface {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InformationSchemaColumns>  metodoPersonalizado() {
         return entityManager.createQuery("select COLUMN_NAME, COLUMN_COMMENT from information_schema.columns where table_name='userhistories'", InformationSchemaColumns.class).getResultList();
    }

}
