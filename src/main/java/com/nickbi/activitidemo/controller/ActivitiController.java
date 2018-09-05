package com.nickbi.activitidemo.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author nickbi
 * @date 2018-09-05
 */
@RestController
public class ActivitiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiController.class);
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    /**
     * 部署流程
     */
    @RequestMapping("/deploy")
    public void deploy() {
        File file = new File("E:\\workspace\\gd-activiti" + "\\src\\main\\java\\com\\lingrit\\gd\\diagrams\\test.bpmn");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Deployment deploy = repositoryService.createDeployment().addInputStream(file.getName(), fileInputStream)
                    .deploy();
            LOGGER.debug("deploy name {} id: {}" + deploy.getName(), deploy.getId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 开始流程
     */
    @RequestMapping("/process/start/{key}")
    public void startProcess(@PathVariable String key) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        LOGGER.debug("deploy name {} id: {}" + processInstance.getName(), processInstance.getId());
    }


    /**
     * 任务列表
     */
    @RequestMapping("/task/list")
    public void queryTaskList() {

        List<Task> list = taskService.createTaskQuery().list();
        Task task = list.get(list.size() - 1);
        System.out.println("第一环节：" + task);
        System.out.println("推动流程到下一环节：" + task);
        taskService.complete(task.getId());
        task = taskService.createTaskQuery().executionId(task.getExecutionId()).singleResult();
        System.out.println("第二环节：" + task);
    }

}
