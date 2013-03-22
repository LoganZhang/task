/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Date;

/**
 *
 * @author Logan
 */
public class DateService {
    public static Date getTommorrow()
    {
        Date now = new Date();
        now.setTime(now.getTime()+86400000);
        return now;
    }
}
