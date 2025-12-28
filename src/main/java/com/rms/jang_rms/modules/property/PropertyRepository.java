package com.rms.jang_rms.modules.property;

import com.rms.jang_rms.enums.PropertyStatus;
import com.rms.jang_rms.modules.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    long countByStatus(PropertyStatus status);
    long countByOwner(User owner);
    long countByOwnerAndStatus(User owner, PropertyStatus status);
    List<Property> findByOwner(User owner);
}
