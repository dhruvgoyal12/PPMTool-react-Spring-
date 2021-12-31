package com.dc.ppmTool.repo;


import com.dc.ppmTool.domain.Backlog;
import com.dc.ppmTool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

    Backlog findByProjectIdentifier(String Identifier);
}
