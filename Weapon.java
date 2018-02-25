/**
 * Represents a weapon (gun) that can inflict damage.
 */
class Weapon extends InventoryItem {
   private String name;
   private int damageInflicted;
   private double critMultiplier;
   private double critPercentage;
   private double accuracyPercentage;
   
   public Weapon(String name, int damageInflicted, double critMultiplier, double critPercentage, double accuracyPercentage) {
      this.name = name;
      this.damageInflicted = damageInflicted;
      this.critMultiplier = critMultiplier;
      this.critPercentage = critPercentage;
      this.accuracyPercentage = accuracyPercentage;
   }
   
   /**
    * Calculates the damage to be given based on certain random factors.
    */
   public int getDamageInflicted() {
      int totalDamage = 0;
      for (int i = 0; i < 4; i++) {
         if (Math.random() < accuracyPercentage) {
            if (Math.random() < critPercentage) {
               totalDamage += critMultiplier * damageInflicted;
            }
            else {
               totalDamage += damageInflicted;
            }
         }
      }
      return totalDamage;
   }
   
   public String getName() {
      return name;
   }
   
   public String toString() {
      return "W";
   }
}