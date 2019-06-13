package bus;

import dao.Repository;
import dao.exceptions.dishExceptions.CreateDishFailException;
import dao.exceptions.dishExceptions.DeleteDishFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.dishExceptions.GetDishFailException;
import model.DishModel;

import java.util.ArrayList;

public class DishBus {

    private static DishBus _instance = new DishBus();
    private Repository _repository = Repository.getInstance();
    private ArrayList<DishModel> _listDishes;

    public DishBus() {
    }

    public static DishBus get_instance() {
        return _instance;
    }

    public ArrayList<DishModel> get_listDishes() throws GetDishFailException {
        if (_listDishes == null) return getAll();
        return _listDishes;
    }

    public ArrayList<DishModel> getAll() throws GetDishFailException {
        _listDishes = _repository.getAllDish();
        return _listDishes;
    }

    public void createDish(DishModel dishModel) throws CreateDishFailException {
        _repository.createDish(dishModel);
    }

    public void editDish(DishModel dishModel) throws EditDishFailException {
        _repository.editDish(dishModel);
    }

    public void deleteDish(int dishId) throws DeleteDishFailException {
        _repository.deleteDish(dishId);
    }

    public DishModel getDishDetail(int dishId) throws GetDishFailException {
        return _repository.getDishDetail(dishId);
    }
}
