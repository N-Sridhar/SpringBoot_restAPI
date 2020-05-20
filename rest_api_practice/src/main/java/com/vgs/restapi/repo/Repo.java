package com.vgs.restapi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vgs.restapi.model.Tutorial;

public interface Repo extends JpaRepository<Tutorial, Long> {
	List<Tutorial> findByPublished(boolean published);
	List<Tutorial> findByTitle(String title);
}
