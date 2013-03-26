package dao;

import entity.TaskEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ianw
 */
@Local
public interface TaskDao {

    boolean addTask(TaskEntity task);

    List<TaskEntity> getTasksByUserId(String username);
    
        List<TaskEntity> getUnallocatedTasks();


    TaskEntity getTaskByTaskBrokerId(long taskBrokerId);

    boolean update(TaskEntity task);

    boolean deleteById(Long taskID);
}
