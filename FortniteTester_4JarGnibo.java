import java.util.ArrayList;
import java.util.Scanner;

/**
 * Plays battle royale with 2-4 players.
 * Turn-based combat, movement, and environment interaction.
 * Have fun!
 */
public class FortniteTester_4JarGnibo {
   private static int mapWidth;
   private static int mapHeight;
   private static int stormRadius;
   private static int stormDamage;
   private static double fireRangeHypotenuse;
   private static FortniteEntity[][] map;
   private static ArrayList<Player> players;
   private static Scanner scanner;
   private static int numPlayers;
   private static int numPlayersAlive;
   
   public static void main(String[] args) {
      mapWidth = 15;
      mapHeight = 13;
      stormRadius = 0;
      stormDamage = 15;
      fireRangeHypotenuse = 4.0;
      map = new FortniteEntity[mapHeight][mapWidth];
      players = new ArrayList<Player>();
      scanner = new Scanner(System.in);
      
      System.out.println(
         "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n| Welcome to Jargnibo's Fortnite! |\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
      
      String userInput = "";
      boolean isDigit = false;

      while (!isDigit) {
         System.out.print("Enter number of players (2-4): ");
         userInput = scanner.nextLine();
         isDigit = true;
         
         try {
               Integer.parseInt(userInput);
               if (Integer.parseInt(userInput) < 2 || Integer.parseInt(userInput) > 4) isDigit = false;
         }
         
         catch (NumberFormatException e) {
               isDigit = false;
         }
      }
      
      numPlayers = Integer.parseInt(userInput);
      numPlayersAlive = numPlayers;
      
      for (int i = 0; i < mapHeight; i++) {
         for (int j = 0; j < mapWidth; j++) {
            double randomSpawn = Math.random();
            
            if (randomSpawn < 0.05) {
               map[i][j] = new Weapon("Assault Rifle", 32, 1.5, 0.25, 0.5);
            }
            
            else if (randomSpawn >= 0.05 && randomSpawn < 0.08) {
               map[i][j] = new Medkit();
            }
            
            else {
               map[i][j] = new Ground();
            }
         }
      }

      String playerName;
      int randomLocationX;
      int randomLocationY;
      
      for (int i = 0; i < numPlayers; i++) {
         randomLocationX = (int) (Math.random() * mapWidth);
         randomLocationY = (int) (Math.random() * mapHeight);
         
         while(!(map[randomLocationY][randomLocationX] instanceof Ground)) {
            randomLocationX = (int) (Math.random() * mapWidth);
            randomLocationY = (int) (Math.random() * mapWidth);
         }
         
         System.out.print("Enter player " + (i + 1) + "'s name: ");
         playerName = scanner.nextLine();
         players.add(new Player(playerName, 100, randomLocationX, randomLocationY));
         map[randomLocationY][randomLocationX] = players.get(i);
      }
      
      int turnNumber = 0;
      int numSpaces;
      
      while (numPlayersAlive > 1) {
         turnNumber++;
         
         if (turnNumber % 2 == 0) {
            stormRadius++;
         }
         
         advanceStorm(false);
         
         for (int i = 0; i < numPlayers; i++) {
            Player p = players.get(i);
            if (p.getHealth() > 0) {
               advanceStorm(true);
               if (checkIfAlive(p) && numPlayersAlive > 1) {
                  printMap();
                  System.out.println(p.getName() + " at (" + (p.getXLocation() + 1) + ", " + (p.getYLocation() + 1) + "):");
                  System.out.println("Health: " + p.getHealth() + " Inventory: " + p.getInventory());
                  
                  if (p.getIndexOfFirstMedkit() != -1 && p.getHealth() < 100) {
                     userInput = "";
                  
                     while (!(userInput.toLowerCase().equals("y")) && !(userInput.toLowerCase().equals("n"))) {
                        System.out.print("Use Medkit? (y/n): ");
                        userInput = scanner.nextLine();
                     }
                     
                     if (userInput.equals("y")) {
                        p.setHealth(100);
                        p.deleteItem(p.getIndexOfFirstMedkit());
                        printMap();
                        System.out.println(p.getName() + " at (" + (p.getXLocation() + 1) + ", " + (p.getYLocation() + 1) + "):");
                        System.out.println("Health: " + p.getHealth() + " Inventory: " + p.getInventory());
                     }
                  }
                  
                  userInput = "";
                  isDigit = false;
               
                  while (!isDigit) {
                     System.out.print("How many total spaces do you want to move? (0-5) ");
                     userInput = scanner.nextLine();
                     isDigit = true;
                     
                     try {
                           Integer.parseInt(userInput);
                           if (Integer.parseInt(userInput) < 0 || Integer.parseInt(userInput) > 5) isDigit = false;
                     }
                     
                     catch (NumberFormatException e) {
                           isDigit = false;
                     }
                  }
                  
                  numSpaces = Integer.parseInt(userInput);
                  int numSpacesToBeMoved = 0;
                  String direction = "";
                  
                  while (numSpaces > 0) {
                     while (!(userInput.toLowerCase().equals("up") || userInput.toLowerCase().equals("down")
                        || userInput.toLowerCase().equals("left") || userInput.toLowerCase().equals("right"))) {
                        System.out.print("Spaces remaining: " + numSpaces + "\nWhich direction do you want to move now? (up/down/left/right) ");
                        userInput = scanner.nextLine();
                     }
                     
                     direction = userInput;
                     isDigit = false;
                     
                     if (numSpaces == 1) {
                        userInput = "1";
                     }
                     else {
                        while (!isDigit) {
                           System.out.print("How many spaces do you want to move " + direction + "? (1-" + numSpaces + ") ");
                           userInput = scanner.nextLine();
                           isDigit = true;
                           
                           try {
                                 Integer.parseInt(userInput);
                                 if (Integer.parseInt(userInput) < 1 || Integer.parseInt(userInput) > numSpaces) isDigit = false;
                           }
                           
                           catch (NumberFormatException e) {
                                 isDigit = false;
                           }
                        }
                     }
                     
                     numSpacesToBeMoved = Integer.parseInt(userInput);
                     numSpaces = movePlayer(p, numSpacesToBeMoved, direction, numSpaces);
                     advanceStorm(true);
                     printMap();
                     System.out.println(p.getName() + " at (" + (p.getXLocation() + 1) + ", " + (p.getYLocation() + 1) + "):");
                     System.out.println("Health: " + p.getHealth() + " Inventory: " + p.getInventory());
                  }
                  
                  if (p.getIndexOfFirstWeapon() != -1) {  
                     boolean withinFireRange = false;
                     boolean alreadyFired = false;
                     boolean rejectedFire = true;
                     boolean noPlayers = true;
                     int deltaX = 0;
                     int deltaY = 0;
                     double fireHypotenuse = 0;
                     
                     System.out.print("You can fire at: ");
                     for (Player enemy : players) {
                        if (enemy != p && enemy.getHealth() > 0) {
                           deltaX = Math.abs(p.getXLocation() - enemy.getXLocation());
                           deltaY = Math.abs(p.getYLocation() - enemy.getYLocation());
                           fireHypotenuse = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                           if (fireHypotenuse <= fireRangeHypotenuse) {
                              noPlayers = false;
                              System.out.print(enemy.getName() + " (" + (enemy.getXLocation() + 1) + ", " + (enemy.getYLocation() + 1) + ") HP=" + enemy.getHealth() + "; ");
                           }
                        }
                     }
                     
                     if (!noPlayers) {
                        System.out.print("\n");
                     }
                     
                     for (Player enemy : players) {
                        if (enemy != p) {
                           deltaX = Math.abs(p.getXLocation() - enemy.getXLocation());
                           deltaY = Math.abs(p.getYLocation() - enemy.getYLocation());
                           fireHypotenuse = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                           if (fireHypotenuse <= fireRangeHypotenuse && !alreadyFired && enemy.getHealth() > 0) {
                              userInput = "";
                  
                              while (!(userInput.toLowerCase().equals("y")) && !(userInput.toLowerCase().equals("n"))) {
                                 System.out.print("Fire at " + enemy.getName() + " (" + (enemy.getXLocation() + 1) + ", " + (enemy.getYLocation() + 1) + ")? (y/n): ");
                                 userInput = scanner.nextLine();
                              }
                              
                              if (userInput.equals("y")) {
                                 Weapon weaponUsed = (Weapon) (p.getInventoryItem(p.getIndexOfFirstWeapon()));
                                 int damageDealt = weaponUsed.getDamageInflicted();
                                 enemy.setHealth(enemy.getHealth() - damageDealt);
                                 alreadyFired = true;
                                 rejectedFire = false;
                                 printMap();
                                 System.out.println(damageDealt + " damage dealt to " + enemy.getName() + " with " + weaponUsed.getName() + ".");
                                 checkIfAlive(enemy);
                                 System.out.println(p.getName() + " at (" + (p.getXLocation() + 1) + ", " + (p.getYLocation() + 1) + "):");
                                 System.out.println("Health: " + p.getHealth() + " Inventory: " + p.getInventory());
                              }
                           }
                        }
                     }
                     
                     if (!alreadyFired && noPlayers) {
                        System.out.println("nobody");
                     }
                  }
                  
                  if (p.getIndexOfFirstMedkit() != -1 && p.getHealth() < 100 && numPlayersAlive > 1) {
                     userInput = "";
                  
                     while (!(userInput.toLowerCase().equals("y")) && !(userInput.toLowerCase().equals("n"))) {
                        System.out.print("Use Medkit? (y/n): ");
                        userInput = scanner.nextLine();
                     }
                     
                     if (userInput.equals("y")) {
                        p.setHealth(100);
                        p.deleteItem(p.getIndexOfFirstMedkit());
                        printMap();
                        System.out.println(p.getName() + " at (" + (p.getXLocation() + 1) + ", " + (p.getYLocation() + 1) + "):");
                        System.out.println("Health: " + p.getHealth() + " Inventory: " + p.getInventory());
                     }
                  }
               }
            }
            else if (p.isAlive()) {
               checkIfAlive(p);
               p.setAlive(false);
               numPlayersAlive--;
            }
         }
      }
      
      printMap();
      for (Player p : players) {
         if (p.getHealth() > 0 && numPlayersAlive == 1) {
            System.out.println("Victory Royale! " + p.getName() + " won!");
         }
      }
   }
   
   public static void printMap() {
      for (int i = 0; i < mapWidth * 2 + 3; i++) {
         System.out.print("v");
      }
      System.out.print("\n");
      for (int i = 0; i < mapHeight; i++) {
         System.out.print("| ");
         for (int j = 0; j < mapWidth; j++) {
            System.out.print(map[i][j] + " ");
         }
         System.out.print("| " + (i + 1) + "\n");
      }
      for (int i = 0; i < mapWidth * 2 + 3; i++) {
         System.out.print("^");
      }
      System.out.print("\n");
   }
   
   /**
    * Swaps the player and the FortniteEntity object at the new coordinates.
    * If the FortniteEntity is a Weapon or Medkit the player adds it to their inventory and a new Ground object is constructed at their initial position.
    */  
   public static int movePlayer(Player p, int numSpaces, String direction, int totalNumSpaces) {
      int originalXLocation = p.getXLocation();
      int originalYLocation = p.getYLocation();
      int newXLocation = 0;
      int newYLocation = 0;
      
      if (direction.equals("up")) {
         newXLocation = p.getXLocation();
         newYLocation = p.getYLocation() - numSpaces;
      }
      if (direction.equals("down")) {
         newXLocation = p.getXLocation();
         newYLocation = p.getYLocation() + numSpaces;
      }
      if (direction.equals("left")) {
         newXLocation = p.getXLocation() - numSpaces;
         newYLocation = p.getYLocation();
      }
      if (direction.equals("right")) {
         newXLocation = p.getXLocation() + numSpaces;
         newYLocation = p.getYLocation();
      }
      
      if (newXLocation < mapWidth && newYLocation < mapHeight && newXLocation >= 0 && newYLocation >= 0) {
         if (!(map[newYLocation][newXLocation] instanceof Player)) {
            FortniteEntity tempSwap = map[newYLocation][newXLocation];
            
            if (map[newYLocation][newXLocation] instanceof Weapon || map[newYLocation][newXLocation] instanceof HealthItem) {
               p.addItem((InventoryItem) (map[newYLocation][newXLocation]));
               map[newYLocation][newXLocation] = p;
               if (map[originalYLocation][originalXLocation] instanceof Storm) {
                  map[originalYLocation][originalXLocation] = new Storm();
               }
               else {
                  map[originalYLocation][originalXLocation] = new Ground();
               }
            }
            
            else if (map[newYLocation][newXLocation] instanceof Storm) {
               map[newYLocation][newXLocation] = p;
               map[originalYLocation][originalXLocation] = new Ground();
            }
            
            else {
               map[newYLocation][newXLocation] = p;
               map[originalYLocation][originalXLocation] = tempSwap;
            }
               
            map[newYLocation][newXLocation] = p;
            p.setLocation(newXLocation, newYLocation);
            return totalNumSpaces - numSpaces;
         }
         return totalNumSpaces;
      }
      return totalNumSpaces;
   }
   
   /**
    * Moves the storm forward, with the argument to do damage to players or not.
    */
   public static void advanceStorm(boolean replenish) {
      for (int i = 0; i < mapWidth; i++) {
         for (int j = 0; j <= stormRadius - 1; j++) {
            if (!(map[j][i] instanceof Player)) {
               map[j][i] = new Storm();
            }
            else if (!replenish) {
               Player p = (Player) (map[j][i]);
               p.setHealth(p.getHealth() - stormDamage);
            }
         }
         
         for (int j = mapHeight - 1; j >= mapHeight - stormRadius; j--) {
            if (!(map[j][i] instanceof Player)) {
               map[j][i] = new Storm();
            }
            else if (!replenish) {
               Player p = (Player) (map[j][i]);
               p.setHealth(p.getHealth() - stormDamage);
            }
         }
      }
      for (int i = 0; i < mapHeight; i++) {
         for (int j = 0; j <= stormRadius - 1; j++) {
            if (!(map[i][j] instanceof Player)) {
               map[i][j] = new Storm();
            }
            else if (!replenish) {
               Player p = (Player) (map[i][j]);
               p.setHealth(p.getHealth() - stormDamage);
            }
         }
         
         for (int j = mapWidth - 1; j >= mapWidth - stormRadius; j--) {
            if (!(map[i][j] instanceof Player)) {
               map[i][j] = new Storm();
            }
            else if (!replenish) {
               Player p = (Player) (map[i][j]);
               p.setHealth(p.getHealth() - stormDamage);
            }
         }
      }  
   }
   
   /**
    * Checks if a given player is alive, if not, performs necessary actions.
    */
   public static boolean checkIfAlive(Player p) {
      if (p.getHealth() > 0) {
         return true;
      }
      else {
         System.out.println(p.getName() + " died!");
         map[p.getYLocation()][p.getXLocation()] = new Ground();
         advanceStorm(true);
         return false;
      }
   }
}