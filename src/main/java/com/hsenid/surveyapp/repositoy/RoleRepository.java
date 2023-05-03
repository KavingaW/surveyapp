package com.hsenid.surveyapp.repositoy;

import com.hsenid.surveyapp.model.Role;
import com.hsenid.surveyapp.model.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleEnum name);
}