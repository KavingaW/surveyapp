package com.hsenid.surveyapp.repositoy;

import com.hsenid.surveyapp.model.SurveyResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResultRepository extends MongoRepository<SurveyResult, String> {
    List<SurveyResult> findAllBySurveyId(String surveyId);
}
