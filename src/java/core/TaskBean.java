package core;

import model.TaskDataModel;
import dao.TaskDao;
import dao.UserDao;
import entity.TaskEntity;
import entity.UserEntity;
import java.io.IOException;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.TaskModel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import uk.ac.susx.inf.ianw.webApps.taskBroker.ejb.TaskBrokerBeanRemote;
import uk.ac.susx.inf.ianw.webApps.taskBroker.ejb.TaskBrokerException;
import uk.ac.susx.inf.ianw.webApps.taskBroker.entity.Task;
import utilities.DataService;
import utilities.DateService;
import utilities.EmailValidation;
import utilities.Internationalization;
import utilities.UserService;

/**
 *
 * @author 109472
 */
@ManagedBean(name = "task")
@ViewScoped
public class TaskBean implements Serializable {

    @EJB
    TaskDao ejbBean;
    @EJB
    TaskBrokerBeanRemote taskBroker;
    private String new_task = Internationalization.getString("add");
    private TaskModel viewTask = null;

    public TaskModel getViewTask() {
        return viewTask;
    }

    public void setViewTask(TaskModel viewTask) {
        this.viewTask = viewTask;
    }

    public String getNew_task() {
        return new_task;
    }

    public void setNew_task(String new_task) {
        this.new_task = new_task;
    }

    public void addTask(ActionEvent actionEvent) {
        TaskEntity t = new TaskEntity();
        t.setTitle(this.new_task);
        Long userID = UserService.UserBean().getId();
        String userName = UserService.UserBean().getUsername();
        t.setUerId(userID);

        try {
            TaskModel model = new TaskModel();
            Task task = new Task();
            Date d = DateService.getTommorrow();
            task.setDueDate(d);
            taskBroker.allocateTask(task, userName);
            
            long taskBrokerId = taskBroker.allocateTask(task, userName);
            t.setTaskBrokerID(taskBrokerId);

            taskBroker.collectTask(taskBrokerId, userName);
            ejbBean.addTask(t);

            model.setId(t.getId());
            model.setAllocated(task.getAllocated() == null ? null : task.getAllocated().getName());
            model.setCompleted(task.isCompleted());
            model.setDescription(task.getDescription());
            model.setDueDate(task.getDueDate());
            model.setPriority(t.getPriority());
            model.setTaskBrokerID(task.getId());
            model.setTitle(t.getTitle());
            model.setProposer(task.getProposer() == null ? null : task.getProposer().getName());
            model.setUerId(userID);

            tasks.add(model);

        } catch (TaskBrokerException ex) {
            Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.new_task = null;
    }

    @PostConstruct
    public void init() throws IOException {

            UserBean bean = UserService.UserBean();
            if (!bean.isStatus()) {
                utilities.Redirect.goLogin();
            }
            Long userID = UserService.UserBean().getId();
            if (userID != null) {
                tasks = DataService.getTasksByUserId(userID, taskBroker, ejbBean);
                if (tasks == null) {
                    tasks = new ArrayList<TaskModel>();
                }
                taskDataModel = new TaskDataModel(tasks);
            }
        
    }
    private TaskModel selectedTask;
    private TaskDataModel taskDataModel;
    List<TaskModel> tasks = null;

    public TaskBean() {
    }

    public boolean isTaskEmpty() {
        if (tasks == null || tasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public TaskModel getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(TaskModel selectedTask) {
        this.selectedTask = selectedTask;
    }

    public TaskDataModel getTaskDataModel() {
        return taskDataModel;
    }

    public void onRowSelect(SelectEvent event) {
        TaskModel t = (TaskModel) event.getObject();
        this.viewTask = t;
    }

    public void test() {
        System.out.println("save!!!!");
    }
}