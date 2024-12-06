package com.smartexamsystem.service;

import java.util.List;

import com.smartexamsystem.entity.Course;
import com.smartexamsystem.entity.Level;

public interface LevelService {


	public List<Level> getAllLevelByCourseId(long id);
	
	public Level createLevel(Level level);
	
	public Level getLevelById(long id);
	
	public Long getNextLevelId(Long courseId, Long currentLevelId);
	

}
