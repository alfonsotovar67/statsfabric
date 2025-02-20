package com.orioninc.statsfabric.repository;

import com.orioninc.statsfabric.entities.UserHistories2_alf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistories2Repository extends JpaRepository<UserHistories2_alf, Long> {
}
