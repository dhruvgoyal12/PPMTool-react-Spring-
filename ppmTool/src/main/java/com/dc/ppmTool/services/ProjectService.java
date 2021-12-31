package com.dc.ppmTool.services;

import com.dc.ppmTool.Exceptions.ProjectIdException;
import com.dc.ppmTool.domain.Backlog;
import com.dc.ppmTool.domain.Project;
import com.dc.ppmTool.repo.BacklogRepository;
import com.dc.ppmTool.repo.ProjectRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private BacklogRepository backlogRepo;

    public Project saveOrUpdateProject(Project project)
    {
        try {
            String identifier = project.getProjectIdentifier().toUpperCase();
            project.setProjectIdentifier(identifier);
            if (project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(identifier);
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepo.findByProjectIdentifier(identifier));
            }
            return projectRepo.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID "+project.getProjectIdentifier().toUpperCase()+ " already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId)
    {
        Project project = projectRepo.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null)
            throw new ProjectIdException("Project ID "+projectId+" does not exist");

        return project;
    }

    public Iterable<Project> findAllProjects()
    {
        return projectRepo.findAll();
    }

    public void deleteProjectById(String projectId)
    {
        Project project = projectRepo.findByProjectIdentifier(projectId);
        if(project==null)
        {
            throw new ProjectIdException("Cannot find Project with ID "+ projectId);
        }

        projectRepo.delete(project);
    }

}
