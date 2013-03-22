package model;
import entity.TaskEntity;
import java.util.ArrayList;
import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;  
  
public class TaskDataModel extends ListDataModel<TaskModel> implements SelectableDataModel<TaskModel> {    
  
    public TaskDataModel() {  
    }  
  
    public TaskDataModel(List<TaskModel> tasks) {
        super(tasks);          
    }  
      
    @Override  
    public TaskModel getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<TaskModel> tasks = (List<TaskModel>) getWrappedData();  
          
        for(TaskModel task : tasks) {  
            if(task.getId().toString().equals(rowKey))  
                return task;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(TaskModel task) {  
        return task.getId();  
    }  
} 