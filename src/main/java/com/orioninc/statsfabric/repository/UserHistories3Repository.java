package com.orioninc.statsfabric.repository;

import com.orioninc.statsfabric.entities.UserHistories3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistories3Repository extends JpaRepository<UserHistories3, Long> {
}
