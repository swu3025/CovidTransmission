/*NAME: Samuel Wu
ID: A16306765
EMAIL: saw003@ucsd.edu
 */

/* The purpose is to determine the risk level of a person to the covid virus.
This is calculated based on the amount of time the person was in contact with
another person.
 */

import java.util.*;

//The purpose of my class is to create objects
public class CovidTransmission {

    //initialize risk levels
    private static final String LOW = "low";
    private static final String MEDIUM = "medium";
    private static final String HIGH = "high";
    private static final String EHIGH = "extremely high";

    //day and hour to minute conversion
    private static final int dayToMin = 1440;
    private static final int hourToMin = 60;

    public static void main(String [] args) {
        //creating a scanner object
        Scanner scan = new Scanner(System.in);

        //initialize the input into int variables
        int day1 = scan.nextInt();
        int hour1 = scan.nextInt();
        int minute1 = scan.nextInt();
        int day2 = scan.nextInt();
        int hour2 = scan.nextInt();
        int minute2 = scan.nextInt();

        //initialize total minutes and risk level
        int total = 0;
        String riskLevel;

        //calculate total minutes
        total = (dayToMin * (day2 - day1)) + (hourToMin * (hour2 - hour1)) +
                (minute2 - minute1);

        //check if int are in range and if total is bigger than or equal to 0
        if ((inRange(day1, hour1, minute1) && inRange(day2, hour2, minute2)) &&
                (total >=0)){

            //find the risk level based off of total
            if (total >= 0 && total <= 60) {
                riskLevel = LOW;
            } else if (total > 60 && total <= 180) {
                riskLevel = MEDIUM;
            } else if (total > 180 && total <= 360) {
                riskLevel = HIGH;
            } else {
                riskLevel = EHIGH;
            }
        } else {
            total = -1;
            riskLevel = "-1";
        }

        //print out the total minutes and the risk level
        System.out.println(Integer.toString(total) + " " + riskLevel);
    }

    /*Determine whether the days,hours,minutes are in the given range
    *
    * @param day, hour, minute of the person
    * @return true or false depending if they are in range
     */
    public static boolean inRange(int day, int hour, int minute){
        if(day >= 1 && day <= 31){
            if(hour >= 0 && hour <= 23){
                if(minute >= 0 && minute <= 59){
                    return true;
                }
            }
        }
        return false;
    }
}
