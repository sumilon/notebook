package com.xm.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xm.entity.Author;
import com.xm.entity.Story;
import com.xm.entity.StoryType;

@Repository
public interface StoryRepository extends CrudRepository<Story, Long> {

	Story findByTitle(String title);
	
	List<Story> findFirst10ByStoryTypeOrderByIdDesc(StoryType storyType);
	
	List<Story> findFirst10ByStoryTypeAndAuthorOrderByIdDesc(StoryType storyType, Author author);
}
