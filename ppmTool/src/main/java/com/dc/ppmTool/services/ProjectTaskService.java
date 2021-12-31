package com.dc.ppmTool.services;

import com.dc.ppmTool.Exceptions.ProjectNotFoundException;
import com.dc.ppmTool.domain.Backlog;
import com.dc.ppmTool.domain.Project;
import com.dc.ppmTool.domain.ProjectTask;
import com.dc.ppmTool.repo.BacklogRepository;
import com.dc.ppmTool.repo.ProjectRepo;
import com.dc.ppmTool.repo.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepo;

    @Autowired
    private ProjectTaskRepository projectTaskRepo;

    @Autowired
    private ProjectRepo projectRepo;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //Exceptions: Project not found

        //PTs to be added to a specific project, project != null, BL exists
        try {
            Backlog backlog = backlogRepo.findByProjectIdentifier(projectIdentifier);
            //set the backlog to pt
            projectTask.setBacklog(backlog);

            // we want projectSequence to be like this IDPRO-1, IDPRO-2
            Integer backlogPTSequence = backlog.getPTSequence();
            backlogPTSequence++;

            //Add sequence to project task
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogPTSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            // update backlog pt sequence
            backlog.setPTSequence(backlogPTSequence);

            //Initial Priority when priority null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }
            // Initial status when status null
            if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
                projectTask.setStatus("TODO");
            }

            return projectTaskRepo.save(projectTask);
        }
        catch (Exception e){
            throw new ProjectNotFoundException("Project '" + projectIdentifier + "' does not exist");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id) {

        Project project = projectRepo.findByProjectIdentifier(backlog_id);
        if (project == null){
            throw new ProjectNotFoundException("Project '" + backlog_id + "' does not exist");
        }
        return projectTaskRepo.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String projectSequence){

        // make sure we are searching on right backlog
        Backlog backlog = backlogRepo.findByProjectIdentifier(backlog_id);
        if (backlog == null){
            throw new ProjectNotFoundException("Project '" + backlog_id + "' does not exist");
        }

        //make sure the task exist
        ProjectTask projectTask = projectTaskRepo.findByProjectSequence(projectSequence);
        if (projectTask == null){
            throw new ProjectNotFoundException("Project Task '" + projectSequence + "' does not exist");
        }

        // make sure that the backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '" + projectSequence + "' does not belong in the Project '" + backlog_id + "'");
        }

        return projectTask;
    }

    public ProjectTask updatePTByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){

        // find existing project Task
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        // replace it with updated task
            projectTask = updatedTask;
        //save update
        return projectTaskRepo.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTaskRepo.delete(projectTask);
    }


}
