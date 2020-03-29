package com.perivoliotis.app.eSymposium.repos;

import com.perivoliotis.app.eSymposium.entities.symposium.SymposiumUser;
import com.perivoliotis.app.eSymposium.repos.custom.SymposiumUserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymposiumUserRepository extends MongoRepository<SymposiumUser, Long>, SymposiumUserRepositoryCustom {

}
