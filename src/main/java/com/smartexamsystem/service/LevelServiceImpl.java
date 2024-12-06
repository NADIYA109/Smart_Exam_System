package com.smartexamsystem.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartexamsystem.entity.Level;
import com.smartexamsystem.repository.LevelRepository;

@Service
public class LevelServiceImpl implements LevelService {

	@Autowired
	private LevelRepository levelRepo;

	@Override
	public List<Level> getAllLevelByCourseId(long id) {

		return levelRepo.findByCourseId(id);

	}

	@Override
	public Level getLevelById(long id) {
		return levelRepo.findById(id).get();
	}

	@Override
	public Level createLevel(Level level) {
		Level newLevel = levelRepo.save(level);
		return newLevel;
	}

	public Long getNextLevelId(Long courseId, Long currentLevelId) {
		Level currentLevel = levelRepo.findById(currentLevelId).orElse(null);
		if (currentLevel == null) {
			return null; // Handle the case where current level is not found
		}

		List<Level> levels = levelRepo.findByCourseId(courseId);

		int currentLevelIndex = levels.indexOf(currentLevel);
		if (currentLevelIndex == -1 || currentLevelIndex >= levels.size() - 1) {
			return null; // Handle the case where current level is not found or is the last level
		}

		Level nextLevel = levels.get(currentLevelIndex + 1);
		return nextLevel.getId();
	}

}
