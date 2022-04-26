/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projectfive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Ianba
 */
public class ProjectFive {
    
    File[] files;
    String[][] maze;
    
    int topX;
    int topY;
    
    int initxLoc;
    int inityLoc;
    
    int initxHand;
    int inityHand;
    
    int moves;
    
    public ProjectFive(){
        files = new File("mazes").listFiles(); //Fetching list of mazes at launch.
    }
    
    public void mainMenu(){
        Scanner con = new Scanner(System.in);
        String choice;
        
        do{
            System.out.println();
            System.out.println("Welcome to the Maze Simulation.");
            System.out.println();
            System.out.println("Provide valid menu choice:");
            System.out.println("-------------------------------");
            System.out.println("0. Exit");
            System.out.println("1. Make new maze");
            System.out.println("2. About");
            System.out.println("-------------------------------");
            System.out.print("Choice: ");
            choice = con.next();
            switch(choice){
                case "0": break;
                case "1": selectMaze(); break;
                case "2": printAbout(); break;
                default: System.out.println("\u001B[31m" + "Invalid option." + "\u001B[0m");
            }
        }while(!choice.equals("0"));
        
    }
    
    public void printAbout(){
        System.out.println();
        System.out.println("I over-engineered this for the sake of making testing easier and because I could. Enjoy.");
    }

    public void selectMaze(){
        Scanner con = new Scanner(System.in);
        String choice = "0";
        do{
            System.out.println();
            System.out.println("Select desired maze, or 0 to return to menu.");
            System.out.println("-------------------------------");
            for(int i = 0; i < files.length; i++){
                System.out.print(i+1 + ": ");
                System.out.println(files[i]);
            }
            System.out.println("-------------------------------");
            System.out.print("Choice: ");
            choice = con.next();
            if(choice.equals("0"))
                break;
            try{
                makeMaze(files[Integer.valueOf(choice)-1]);
            }catch(FileNotFoundException e){
                System.out.println("\u001B[31m" + "File is missing. (Did you delete it ?)" + "\u001B[0m");
            }catch(Exception e){
                System.out.println("\u001B[31m" + "Invalid input." + "\u001B[0m");
            }

        }while(!choice.equals("0"));
    }
    
    public void makeMaze(File file) throws FileNotFoundException{
        topX = 0;
        topY = 0;
        moves = 0;
        String line = "";
        
        Scanner reader = new Scanner(file);
        while(reader.hasNextLine()){ //This gets how tall the maze is.
            line = reader.nextLine();
            topY++;
        }
        topX = (line.length())/2; //This gets how wide the maze is.
        
        maze = new String[topX][topY];
        reader = new Scanner(file);
        for(int j = topY-1; j >= 0; j--){ // Takes file line by line, adding every two characters(the space + symbol), and adding them as one item to the maze array to make the grid simpler.
            line = reader.nextLine();
            for(int i = 0; i < topX; i++){
                if(i==0){
                    maze[i][j] = String.valueOf(""+line.charAt(i)+(line.charAt(i+1)));
                }else{
                    maze[i][j] = String.valueOf(""+line.charAt(2*i)+(line.charAt((2*i)+1)));
                }
            }
        }
        
        reader = new Scanner(file); // Finds the starting location on the edge, starts the game with those coords.
        for(int j = topY-1; j >= 0; j--){
            line = reader.nextLine();
            for(int i = 0; i < topX; i++){
                if(maze[i][topY-1].equals(" .")){ //If on our bottom line and there's a move space
                    System.out.println(move(i, (topY-1), i, j-1));
                    break;
                }
                if(maze[i][0].equals(" .")){ // If on our time line and there's a move space
                    System.out.println(move(i, 0, i, 1));
                    break;
                }
                if(i==0 && maze[i][j].equals(" .")){ // If we're on our first column and there's a move space
                    System.out.println(move(i, j, i+1, j));
                    break;
                }
                if(i==topX-1 && maze[i][j].equals(" .")){ // If we're on our last column and there's a move space
                    System.out.println(move(i, j, i-1, j));
                    break;
                }
            }
        }
        reader.close();
    }
    
    public String move(int xLoc, int yLoc, int xHand, int yHand){
        Scanner con = new Scanner(System.in);
        String choice = "0";
        
        if(maze[xLoc][yLoc].equals(" F")){
            maze[xLoc][yLoc] = " O";
            printMaze(xLoc, yLoc, xHand, yHand);
            return("\u001B[32m" + "Maze completed in "+moves+" moves !" + "\u001B[0m");
        }
        
        maze[xLoc][yLoc] = " O";
        printMaze(xLoc, yLoc, xHand, yHand);
        maze[xLoc][yLoc] = " x";
        
        do{
            System.out.println();
            System.out.println("Press 1 to continue or 0 to cancel.");
            System.out.println("-------------------------------");
            System.out.print("Choice: ");
            choice = con.next();
            
            if(choice.equals("1")){
                
                try{
                    
                    moves++;

                    if((maze[xHand][yHand].equals(" #"))){ // If the hand is on a wall

                        if(xHand>xLoc){
                            if((!maze[xLoc][yLoc+1].equals(" #"))) // If the tile infront is not a wall (is dot or finish line).
                                return(move(xLoc, yLoc+1, xHand, yHand+1)); // Move forward one.
                            return(move(xLoc, yLoc, xHand-1, yHand+1)); // If the tile ahead is also a wall, rotate.
                        }
                        if(xHand<xLoc){
                            if(!(maze[xLoc][yLoc-1].equals(" #")))
                                return(move(xLoc, yLoc-1, xHand, yHand-1));
                            return(move(xLoc, yLoc, xHand+1, yHand-1));
                        }
                        if(yHand>yLoc){
                            if(!(maze[xLoc-1][yLoc].equals(" #")))
                                return(move(xLoc-1, yLoc, xHand-1, yHand));
                            return(move(xLoc, yLoc, xHand-1, yHand-1));
                        }
                        if(yHand<yLoc){
                            if(!(maze[xLoc+1][yLoc].equals(" #")))
                                return(move(xLoc+1, yLoc, xHand+1, yHand));
                            return(move(xLoc, yLoc, xHand+1, yHand+1));
                        }

                    }

                    if(xHand>xLoc) // If the hand is not on a wall, rotate and move forward one.
                        return(move(xLoc+1, yLoc, xHand, yHand-1));
                    if(xHand<xLoc)
                        return(move(xLoc-1, yLoc, xHand, yHand+1));
                    if(yHand>yLoc)
                        return(move(xLoc, yLoc+1, xHand+1, yHand));
                    if(yHand<yLoc)
                        return(move(xLoc, yLoc-1, xHand-1, yHand));
                
                }catch(ArrayIndexOutOfBoundsException e){
                    return("\u001B[31m" + "Escaped the maze !" + "\u001B[0m");
                }catch(Exception e){
                    System.out.println(e);
                    return("\u001B[31m" + "Something has gone horribly wrong." + "\u001B[0m");
                }
            }
            
            if(choice.equals("0"))
                break;
            
            System.out.println("\u001B[31m" + "Invalid input." + "\u001B[0m");
        
        }while(!choice.equals("0"));
        
       return("\u001B[31m" + "Cancelling maze run." + "\u001B[0m"); 
    }
    
    public void printMaze(int xLoc, int yLoc, int xHand, int yHand){
        
        System.out.println();
        System.out.println("-------------------------------");
        for(int i = topY-1; i >= 0; i--){ // Prints out the actual maze array saved to RAM rather than the file
            for(int j = 0; j < topX; j++){
                System.out.print(maze[j][i]);
            }
            System.out.println();
        }
        System.out.println("-------------------------------");
        System.out.print("Coords: ("+xLoc+ ", "+yLoc+") ");
        System.out.print("Hand Coords: ("+xHand+ ", "+yHand+") ");
        System.out.println("Moves: "+moves);
    }
    
    public static void main(String[] args) {
        new ProjectFive().mainMenu();
    }
    
}
