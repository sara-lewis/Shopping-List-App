package us.ait.android.shoppinglist.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GroceryDao {
    @Query("SELECT * FROM Grocery")
    List<Grocery> getAllGroceries();

    @Insert
    long insertGrocery(Grocery grocery);

    @Delete
    void deleteGrocery(Grocery grocery);

    @Update
    void updateGrocery(Grocery grocery);
}
