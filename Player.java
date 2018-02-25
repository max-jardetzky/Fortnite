import java.util.ArrayList;

/**
 * Represents a single player, with health, location, and an inventory.
 */
class Player extends FortniteEntity {
   private String name;
   private int health;
   private int xLocation;
   private int yLocation;
   private ArrayList<InventoryItem> inventory;
   
   public Player(String name, int health, int xLocation, int yLocation) {
      this.name = name;
      this.health = health;
      this.xLocation = xLocation;
      this.yLocation = yLocation;
      inventory = new ArrayList<InventoryItem>();
   }
   
   public void setLocation(int x, int y) {
      xLocation = x;
      yLocation = y;
   }
   
   public void addItem(InventoryItem item) {
      inventory.add(item);
   }
   
   public int getXLocation() {
      return xLocation;
   }
   
   public int getYLocation() {
      return yLocation;
   }
   
   public void setHealth(int health) {
      this.health = health;
   }
   
   public int getHealth() {
      return health;
   }
   
   public String getName() {
      return name;
   }
   
   public String getInventory() {
      return inventory.toString();
   }
   
   public InventoryItem getInventoryItem(int itemIndex) {
      return inventory.get(itemIndex);
   }
   
   public void deleteItem(int itemIndex) {
      inventory.remove(itemIndex);
   }
   
   public int getIndexOfFirstMedkit() {
      for (int i = 0; i < inventory.size(); i++) {
         if (inventory.get(i) instanceof Medkit) {
            return i;
         }
      }
      return -1;
   }
   
   public int getIndexOfFirstWeapon() {
      for (int i = 0; i < inventory.size(); i++) {
         if (inventory.get(i) instanceof Weapon) {
            return i;
         }
      }
      return -1;
   }
   
   public String toString() {
      return "P";
   }
}