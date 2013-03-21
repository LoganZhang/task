package core;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import uk.ac.susx.inf.ianw.webApps.taskBroker.ejb.TaskBrokerBeanRemote;
import uk.ac.susx.inf.ianw.webApps.taskBroker.ejb.TaskBrokerException;
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
    TaskBrokerBeanRemote ettt;
    
    private String new_task = Internationalization.getString("add");
    private String node;
    private String time;

    public String getNew_task() {
        return new_task;
    }

    public void setNew_task(String new_task) {
        this.new_task = new_task;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void addTask(ActionEvent actionEvent) {
        TaskEntity t = new TaskEntity();
        t.setTitle(this.new_task);
        Long userID = UserService.UserBean().getId();
        t.setUerId(userID);
        tasks.add(t);
        ejbBean.addTask(t);
        
        
         String[] a = new String[1];
         a[0]= this.new_task;
        try {
            ettt.registerUsers(a);
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
            tasks = ejbBean.getTasksByUserId(userID);
            if (tasks == null) {
                tasks = new ArrayList<TaskEntity>();
            }
            taskDataModel = new TaskDataModel(tasks);
        }
    }
    private TaskEntity selectedTask;
    private TaskDataModel taskDataModel;
    List<TaskEntity> tasks = null;

    public TaskBean() {
    }

    public boolean isTaskEmpty() {
        if (tasks == null || tasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public TaskEntity getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(TaskEntity selectedTask) {
        this.selectedTask = selectedTask;
    }

    public TaskDataModel getTaskDataModel() {
        return taskDataModel;
    }

    public void onRowSelect(SelectEvent event) {
        TaskEntity t = (TaskEntity) event.getObject();

        this.node = t.getTitle();
        this.time = t.getTitle();
    }
}