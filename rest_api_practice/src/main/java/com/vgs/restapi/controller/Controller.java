package com.vgs.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vgs.restapi.model.Tutorial;
import com.vgs.restapi.repo.Repo;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class Controller {

	@Autowired
	Repo repo;

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAll(@RequestParam(required = false) String title) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			if (title == null) {
				repo.findAll().forEach(tutorials::add);;
			} else {
				repo.findByName(title).forEach(tutorials::add);
			} 	

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getById(@PathVariable("id") long id) {
		Optional<Tutorial> tutorial = repo.findById(id);
		if(tutorial.isPresent()) {
			return new ResponseEntity<>(tutorial.get(), HttpStatus.OK);
		} else {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> getByPublished() {
		try {
			List<Tutorial> t = repo.findByPublished(true);
			if(t.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(t, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> create(@RequestBody Tutorial t) {
		try {
			Tutorial tutorial = repo.save(new Tutorial(t.getName(), t.getDescription(), false));
			return new ResponseEntity<>(tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> update(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tdata = repo.findById(id);
		if(tdata.isPresent()) {
			Tutorial t = tdata.get();
			t.setName(tutorial.getName());
			t.setDescription(tutorial.getDescription());
			t.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(repo.save(t), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> delete(@PathVariable("id") long id) {
		try {
			repo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@DeleteMapping("/tutorials")
	public ResponseEntity<Tutorial> deleteAll() {
		try {
			repo.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}
