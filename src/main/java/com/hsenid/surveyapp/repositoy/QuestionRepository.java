package com.hsenid.surveyapp.repositoy;

import com.hsenid.surveyapp.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question,String > {
}
