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
public class DueDateSort implements Comparator {

    public int compare(Object arg0, Object arg1) {
        TaskEntity task0 = (TaskEntity) arg0;
        TaskEntity task1 = (TaskEntity) arg1;

        if ((task1.getDueDate().getTime() - task0.getDueDate().getTime()) > 0) {
            return -1;
        }


        if ((task1.getDueDate().getTime() - task0.getDueDate().getTime()) == 0) {
            return 0;
        }

        return 1;

    }
}