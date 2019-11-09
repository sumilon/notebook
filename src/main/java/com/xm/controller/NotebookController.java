package com.xm.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dao.StoryRepository;
import com.xm.entity.Author;
import com.xm.entity.Story;
import com.xm.entity.StoryType;

@RestController
public class NotebookController {

	@Autowired
	private StoryRepository storyRepository;

	@RequestMapping(value = "/notebook/add", method = RequestMethod.POST)
	public Long addNote(@RequestParam(value = "Subject", required = true) String subject,
			@RequestParam(value = "BodyType", required = true) final StoryType storyType,
			@RequestParam(value = "Body", required = true) String body,
			@RequestParam(value = "Author", required = true) Author author) {

		Story story = new Story();
		story.setTitle(subject);
		story.setStoryType(storyType);
		story.setStoryBook(body);
		story.setAuthor(author);
		story.setCreatedDate(new Date());

		Story story1 = storyRepository.save(story);
		return story1.getId();
	}

	@RequestMapping(value = "/notebook/update", method = RequestMethod.PUT)
	public String updateNote(@RequestParam(value = "Id", required = true) String Id,
			@RequestParam(value = "Body", required = true) String body) {
		
		if(!checkValidId(Id)) {
			return "Please enter valid Id";
		}

		Story story = storyRepository.findOne(Long.parseLong(Id));
		story.setStoryBook(body);
		story.setCreatedDate(new Date());

		storyRepository.save(story);
		
		return "Notebook updated successfully";
	}

	@RequestMapping(value = "/notebook/delete", method = RequestMethod.DELETE)
	public String deleteNote(@RequestParam(value = "Id", required = true) String Id) {

		if(!checkValidId(Id)) {
			return "Please enter valid Id";
		}

		storyRepository.delete(Long.parseLong(Id));
		return "Deleted Successfully";
	}

	@RequestMapping(value = "/notebook/get", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<Story> getNotes() {

		return (List<Story>) storyRepository.findAll();
	}

	@RequestMapping(value = "/notebook/searchId/{Id}", method = RequestMethod.GET)
	public String searchNoteWithId(@PathVariable("Id") String storyId) {
		return storyRepository.findOne(Long.parseLong(storyId)).getStoryBook();
	}

	@RequestMapping(value = "/notebook/searchsubject/{subject}", method = RequestMethod.GET)
	public String searchNoteWithSubject(@PathVariable("subject") String subject) {
		return storyRepository.findByTitle(subject).getStoryBook();
	}

	@RequestMapping(value = "/notebook/filterNotebook", method = RequestMethod.GET)
	public List<Story> filterNotebook(@RequestParam(value = "BodyType", required = true) final StoryType storyType,
			@RequestParam(value = "Author", required = true) Author author) {
		return storyRepository.findFirst10ByStoryTypeAndAuthorOrderByIdDesc(storyType, author);
	}

	public boolean checkValidId(String Id) {

		Story story = storyRepository.findOne(Long.parseLong(Id));

		if (story == null) {
			return false;
		}
		return true;
	}
}
