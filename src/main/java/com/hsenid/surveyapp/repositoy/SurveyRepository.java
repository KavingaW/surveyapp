package com.hsenid.surveyapp.repositoy;

import com.hsenid.surveyapp.model.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends MongoRepository<Survey,String> {
}
