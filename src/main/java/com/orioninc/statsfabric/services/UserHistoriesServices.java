package com.orioninc.statsfabric.services;

import com.orioninc.statsfabric.entities.InformationSchemaColumns;
import com.orioninc.statsfabric.repositories.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHistoriesServices
{
    private CustomRepository customRepository;

    @Autowired
    public UserHistoriesServices(CustomRepository customRepository) {
        this.customRepository = customRepository;
    }

    public List<InformationSchemaColumns> getStructure() {
        return customRepository.metodoPersonalizado();
    }
}
