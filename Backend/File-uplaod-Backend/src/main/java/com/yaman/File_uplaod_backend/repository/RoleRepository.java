package com.yaman.File_uplaod_backend.repository;

import com.yaman.File_uplaod_backend.model.Role;
import com.yaman.File_uplaod_backend.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
