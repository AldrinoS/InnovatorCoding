package com.example.innovatorcodingchallenge.repository;

import com.example.innovatorcodingchallenge.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<List<Roles>> findByName(String name);
}
