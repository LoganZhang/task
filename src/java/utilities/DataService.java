/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dao.TaskDao;
import entity.TaskEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import model.TaskModel;
import uk.ac.susx.inf.ianw.webApps.taskBroker.ejb.TaskBrokerBeanRemote;
import uk.ac.susx.inf.ianw.webApps.taskBroker.entity.Task;
import uk.ac.susx.inf.ianw.webApps.taskBroker.entity.Username;

/**
 *
 * @author Logan
 */
public class DataService {

    public static List<TaskModel> getTasksByUserId(Long userId, TaskBrokerBeanRemote taskBroker, TaskDao ejbBean) {
        List<TaskModel> result = new ArrayList<TaskModel>();

        
        Username usernameB = taskBroker.getUsername(UserService.UserBean().getUsername());
        List<Task> taskBlist= new  ArrayList<Task>();
        taskBlist.addAll(usernameB.getAllocatedTasks());
        for(Task t : usernameB.getProposedTasks())
        {
            if(!t.getAllocated().equals(t.getProposer()))
            {
                taskBlist.add(t);
            }
        }
        
        for(Task t:taskBlist)
        {
            TaskEntity te = ejbBean.getTaskByTaskBrokerId(t.getId());
            TaskModel model = new TaskModel();
            model.setId(te.getId());
            model.setAllocated(t.getAllocated()==null? null:t.getAllocated().getName());
            model.setCompleted(t.isCompleted());
            model.setDescription(t.getDescription());
            model.setDueDate(t.getDueDate());
            model.setPriority(te.getPriority());
            model.setTaskBrokerID(t.getId());
            model.setTitle(te.getTitle());
            model.setProposer(t.getProposer()==null? null:t.getProposer().getName());
            model.setUerId(userId);
        }

        return result;
    }
}
