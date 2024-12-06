package com.smartexamsystem.service;

import java.util.List;

import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.ExaminationScore;
import com.smartexamsystem.entity.Question;

public interface ExaminationService {

	    public void saveExaminationScore(ExaminationScore examinationScore);

	    public List<ExaminationScore> getAllExaminationScores();
}
