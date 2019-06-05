package bus;

import dao.Repository;
import dao.exceptions.dishExceptions.CreateDishFailException;
import dao.exceptions.dishExceptions.DeleteDishFailException;
import dao.exceptions.dishExceptions.GetDishFailException;
import model.DishModel;

import java.util.ArrayList;

public class DishBus {

    private static DishBus _instance = new DishBus();
    private Repository _repository = Repository.getInstance();
    private ArrayList<DishModel> _listDishes;

    private DishBus() {
    }

    public static DishBus get_instance() {
        return _instance;
    }

    public ArrayList<DishModel> getAll() throws GetDishFailException {
        _listDishes = _repository.getAllDish();
        return _listDishes;
    }

    public void createDish(DishModel dishModel) throws CreateDishFailException {
        _repository.createDish(dishModel);
    }

    public void deleteDish(int dishId) throws DeleteDishFailException {
        _repository.deleteDish(dishId);
    }
}
