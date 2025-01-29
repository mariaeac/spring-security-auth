package com.meac.springsecurity.repositories;

import com.meac.springsecurity.entities.Role;
import com.meac.springsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


}
