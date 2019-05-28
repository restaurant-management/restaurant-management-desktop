package dao.base;

import dao.exceptions.dishExceptions.CreateDishFailException;
import dao.exceptions.dishExceptions.DeleteDishFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.dishExceptions.GetDishFailException;
import model.DishModel;

import java.util.ArrayList;

public interface IDishDao {
    DishModel createDish(String token, String name, String description, ArrayList<String> images, int defaultPrice) throws CreateDishFailException;

    void deleteDish(String token, int dishId) throws DeleteDishFailException;

    DishModel getDish(String token, int dishId) throws GetDishFailException;

    DishModel editDish(String token, int dishId, String name, String description, ArrayList<String> images, int defaultPrice) throws EditDishFailException;

    ArrayList<DishModel> getAll(String token) throws GetDishFailException;

    ArrayList<DishModel> getAll(String token, int length, int offset) throws GetDishFailException;
}
