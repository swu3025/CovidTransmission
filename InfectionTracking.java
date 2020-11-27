/**
 * Name: Samuel Wu
 * ID: A16306765
 * EMAIL: saw003@ucsd.edu
 *
 * Sources used: None
 *
 * The purpose of this file is to calculate the movements of each student
 * and updating their infection status given their locations. This simulation
 * also determines the average people affected and the student who
 * caused the most infections.
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * The purpose of this class is to create the many methods that will assist in
 * the functions that will take part in the simulation,
 * such as finding the locations of each students and their status
 * after every movement.
 */
public class InfectionTracking {

    //creating the deliminator for the input
    private static final String DELIMINATOR_FOR_INPUT = "\\s*[,\\n]\\s*";

    /**
     * Checks whether the lengths of the array parameters are all the same
     *
     * @param names the array of students' names
     * @param locations the array of students' locations
     * @param movements the array of students' movements
     * @param infections the array of students' infections
     * @return true or false depending on if the lengths are the same
     */
    public static boolean isSameLength(String[] names, int[]
            locations, int[] movements, int[] infections){
        int lengthOfNames = names.length;
        return ((locations.length == lengthOfNames) && (movements.length ==
                lengthOfNames) && (infections.length == lengthOfNames));
    }

    /**
     * Read each line of the file and sort each comma separated
     * section into its array and finding the largest location value + 1
     *
     * @param pathToFile the file that will be read
     * @param names the names of the students
     * @param locations the locations of the students
     * @param movements the movements of the students
     * @param infections the infections of the students
     * @return the largest location value + 1
     * @throws IOException if the file cannot be opened or read,
     * the IOException will be thrown
     */

    public static int populateArrays(String pathToFile, String[] names, int[]
            locations, int[] movements, int[] infections) throws IOException{

        //check the validity of the parameters
        if(pathToFile == null || names == null || locations == null ||
                movements == null || infections == null ||
                locations.length == 0 ||
                !isSameLength(names,locations,movements,infections)){
            return -1;
        }

        //create a source file for the file parameter
        File sourceFile = new File(pathToFile);
        Scanner input = new Scanner(sourceFile);

        //delimiter for inputs separated by commas
        input.useDelimiter(DELIMINATOR_FOR_INPUT);

        int index = 0;

        /* while there is a next input and the index is less than location's
        length, we will separate the input into different arrays */
        while(input.hasNext() && index < locations.length){
            names[index] = input.next();
            locations[index] = input.nextInt();
            movements[index] = input.nextInt();
            infections[index] = input.nextInt();
            index++;
        }

        int max = locations[0];

        //go through the array locations and find the biggest number
        for(int element : locations){
            if(element > max){
                max = element;
            }
        }

        return max + 1;

    }

    /**
     * updates the locations of the students by moving them
     * by their movement values
     *
     * @param worldSize the size of the 1-dimensional world
     * @param locations the locations of the students
     * @param movements the movements of the students
     */
    public static void updateLocations(int worldSize, int [] locations, int []
            movements){

        //check the validity of the parameters
        if(locations == null || movements == null || worldSize <= 0 ||
                locations.length != movements.length){
            return;
        }

        //check if the elements of locations is in the range [0,worldSize-1]
        for(int element: locations){
            if(element < 0 || element > worldSize-1){
                return;
            }
        }

        //updating the locations element with the movement values
        for(int i = 0; i<locations.length;i++){

            int newLocation = locations[i] + movements[i];

            //check if the new location is bigger than worldSize
            if(newLocation >= worldSize){
                newLocation %= worldSize;
            }
            //check if the new location is smaller than 0
            else if(newLocation < 0){
                newLocation %= worldSize;
                newLocation += worldSize;

            }

            //make the new location 0 if it is equal to the world size
            if(newLocation == worldSize){
                newLocation = 0;
            }

            //update the new location in the locations array
            locations[i] = newLocation;
        }

    }

    /**
     * Updates the infections status of each student and
     * calculates the number of infections
     * caused by each student
     *
     * @param worldSize the size of the 1-dimensional world
     * @param infections infection status of the students
     * @param locations the location of the students
     * @return an array of the number of infections caused by each student
     */
    public static int [] updateInfections(int worldSize, int[] infections,
            int [] locations){

        //check the validity of the parameters
        if(locations == null || infections == null || worldSize <= 0 ||
                locations.length != infections.length){
            return null;
        }

        //new array for the number of infections caused by each student
        int [] numStudentsInfected = new int[infections.length];
        int lengthOfLocations = locations.length;

        // Calculate the number of infections caused by each each student
        for(int i = 0; i<lengthOfLocations;i++){
            int locationOfStudent = locations[i];

            //check if the locations and infections array are valid
            if(locationOfStudent < 0 || locationOfStudent >= worldSize ||
                    infections[i] < 0 || infections[i] > 1){
                return null;
            }

            int numOfInfected = 0;
            if(infections[i]== 1) {

                /*check if there are any other students with the same
                location that hasn't been infected, if so, increment
                the number infected of the infected student */
                for (int j = 0; j < lengthOfLocations; j++) {
                    if(i == j){
                        continue;
                    }
                    if(locationOfStudent == locations[j]){
                        if(infections[j] == 0){
                            numOfInfected++;
                        }
                    }
                }
            }
            numStudentsInfected[i]=numOfInfected;
        }

        //update the infection status of the students
        for(int i = 0; i<lengthOfLocations;i++){
            int locationOfStudent = locations[i];
            if(infections[i] == 1){
                /*check if any uninfected students are in the same location as
                an infected student, if so, change the infection status of
                the uninfected student */
                for (int j = 0; j < lengthOfLocations; j++) {
                    if(i == j){
                        continue;
                    }
                    if(locationOfStudent == locations[j]){
                        if(infections[j] == 0){
                            infections[j] = 1;
                        }
                    }
                }
            }
        }

        return numStudentsInfected;

    }

    /**
     * for each day of the days give, update the locations and
     * infections status and update the infection records, infections
     * caused by each student, of each student
     *
     * @param days the number of days for the simulation
     * @param worldSize the size of the 1-dimensional world
     * @param locations the locations of the students
     * @param movements the movements of the students
     * @param infections the infection status of the students
     * @return the infection records of each student
     */
    public static int[] countInfectionsByStudent(int days, int worldSize,
            int[] locations, int[] movements, int[] infections){

        //check the validity of the parameters
        if(days < 0 || locations == null || infections == null ||
                movements == null || worldSize <= 0 || locations.length !=
                infections.length || locations.length != movements.length){
            return null;
        }

        int lengthOfLocations = locations.length;
        int [] infectionRecord = new int[lengthOfLocations];
        int[] infectionRecordEachDay;

        /* for loop to simulate each day, updating the location,infection
        status, and the infection record for each day */
        for(int i = 0;i<days;i++){
            updateLocations(worldSize,locations,movements);
            infectionRecordEachDay = updateInfections(worldSize,
                    infections,locations);

            if(infectionRecordEachDay == null){
                return null;
            }

            //update the infection record with the infection record of each day
            for(int j = 0; j<lengthOfLocations;j++){
                infectionRecord[j] += infectionRecordEachDay[j];
            }
        }
        
        return infectionRecord;
    }

    /**
     * find the average number of people who will contract a disease
     * from one person given the infection record of the students
     *
     * @param infectionRecord the infection record of the students that
     *                        contains the amount of infections were
     *                        caused by each student
     * @return the average number of people who will contract a disease
     */
    public static int findRNaught(int [] infectionRecord){
        //check the validity of the parameter
        if(infectionRecord == null || infectionRecord.length == 0){
            return -1;
        }

        int sum = 0;

        //add up all the elements in the infectionRecord array
        for (int element : infectionRecord) {
            if (element < 0) {
                return -1;
            } else {
                sum += element;
            }
        }
        //calculate the average and find the ceiling of the value
        return (int)Math.ceil((double)sum/infectionRecord.length);
    }

    /**
     * find the student who caused the most infections
     *
     * @param infectionRecord the infections caused by each student
     * @param names the names of each student
     * @return the name of the student who caused the most infections
     */
    public static String findSuperSpreader(int [] infectionRecord,
            String [] names){
        //check the validity of the parameters
        if(infectionRecord == null || names == null || names.length == 0 ||
                infectionRecord.length != names.length){
            return null;
        }

        int max = 0;

        //find the index of the highest infection number in infectionRecord
        for(int i = 1; i<infectionRecord.length;i++){

            if(infectionRecord[i] < 0){
                return null;
            }

            if(infectionRecord[i]>infectionRecord[max]){
                max = i;
            }
        }

        //match the index with that of names to find the name of the student
        return names[max];
    }

}
