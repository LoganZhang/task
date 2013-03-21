package core;
import entity.TaskEntity;
import java.util.ArrayList;
import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;  
  
public class TaskDataModel extends ListDataModel<TaskEntity> implements SelectableDataModel<TaskEntity> {    
  
    public TaskDataModel() {  
    }  
  
    public TaskDataModel(List<TaskEntity> tasks) {
        super(tasks);          
    }  
      
    @Override  
    public TaskEntity getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<TaskEntity> tasks = (List<TaskEntity>) getWrappedData();  
          
        for(TaskEntity task : tasks) {  
            if(task.getId().toString().equals(rowKey))  
                return task;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(TaskEntity task) {  
        return task.getId();  
    }  
} 