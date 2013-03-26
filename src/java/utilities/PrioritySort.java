/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import entity.TaskEntity;
import java.util.Comparator;

/**
 *
 * @author Logan
 */
public class PrioritySort implements Comparator {

    public int compare(Object arg0, Object arg1) {
        TaskEntity task0 = (TaskEntity) arg0;
        TaskEntity task1 = (TaskEntity) arg1;

        
        return task1.getPriority()-task0.getPriority();

    }
}