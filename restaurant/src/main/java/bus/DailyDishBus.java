package bus;

import dao.Repository;
import dao.exceptions.dailyDishExceptions.CreateDailyDishFailException;
import dao.exceptions.dailyDishExceptions.DeleteDailyDishFailException;
import dao.exceptions.dailyDishExceptions.EditDailyDishFailException;
import dao.exceptions.dailyDishExceptions.GetDailyDishFailException;
import model.DailyDishModel;
import model.enums.DaySession;

import java.util.ArrayList;
import java.util.Date;

public class DailyDishBus {
    private Repository _repository = Repository.getInstance();

    public ArrayList<DailyDishModel> getDailyDishes(Date date) throws GetDailyDishFailException {
        return _repository.getDailyDish(date);
    }

    public DailyDishModel getDailyDish(Date day, DaySession session, int dishId) throws GetDailyDishFailException {
        return _repository.getDailyDish(day, session, dishId);
    }

    public void createDailyDish(DailyDishModel dailyDishModel) throws CreateDailyDishFailException {
        _repository.createDailyDish(dailyDishModel);
    }

    public void deleteDailyDish(Date day, DaySession session, int dishId) throws DeleteDailyDishFailException {
        _repository.deleteDailyDish(day, session, dishId);
    }

    public void editDailyDish(DailyDishModel dailyDishModel) throws EditDailyDishFailException {
        _repository.editDailyDish(dailyDishModel);
    }
}
