package com.orioninc.statsfabric.repository;

import com.orioninc.statsfabric.entities.UserHistories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistories, Long> {
}
