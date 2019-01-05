package us.ait.android.shoppinglist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import us.ait.android.shoppinglist.R;

@Entity
public class Grocery implements Serializable {

    public enum GroceryType {
        BEVERAGES(0, R.drawable.beverages),
        BREAD_BAKERY(1, R.drawable.bread),
        CANNED_GOODS(2, R.drawable.can),
        DAIRY(3, R.drawable.dairy),
        BAKING_GOODS(4, R.drawable.baking),
        MEAT(5, R.drawable.meat),
        PRODUCE(6, R.drawable.produce),
        CLEANERS(7, R.drawable.clean),
        PAPER_GOODS(8, R.drawable.paper),
        PERSONAL_CARE(9, R.drawable.personal),
        OTHER(10, R.drawable.other);

        private int value;
        private int iconId;

        private GroceryType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static GroceryType fromInt(int value) {
            for (GroceryType p : GroceryType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return BEVERAGES;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private long groceryId;

    private String groceryName;
    private double estimatedPrice;
    private String groceryDescription;
    private boolean alreadyPurchased;
    private int groceryType;


    public Grocery(String groceryName, double estimatedPrice, String groceryDescription, boolean alreadyPurchased, int groceryType) {
        this.groceryName = groceryName;
        this.estimatedPrice = estimatedPrice;
        this.groceryDescription = groceryDescription;
        this.alreadyPurchased = alreadyPurchased;
        this.groceryType = groceryType;
    }

    public long getGroceryId() {
        return groceryId;
    }

    public void setGroceryId(long groceryId) {
        this.groceryId = groceryId;
    }

    public String getGroceryName() {
        return groceryName;
    }

    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getGroceryDescription() {
        return groceryDescription;
    }

    public void setGroceryDescription(String groceryDescription) {
        this.groceryDescription = groceryDescription;
    }

    public boolean isAlreadyPurchased() {
        return alreadyPurchased;
    }

    public void setAlreadyPurchased(boolean alreadyPurchased) {
        this.alreadyPurchased = alreadyPurchased;
    }

    public int getGroceryType() {
        return groceryType;
    }

    public void setGroceryType(int groceryType) {
        this.groceryType = groceryType;
    }

    public GroceryType getGroceryTypeAsEnum() {
        return GroceryType.fromInt(groceryType);
    }
}
