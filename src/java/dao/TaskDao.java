
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

    List<TaskEntity> getTasksByUserId(Long userId);
    
}
