package dao.base;

import dao.exceptions.dailyDishExceptions.CreateDailyDishFailException;
import dao.exceptions.dailyDishExceptions.DeleteDailyDishFailException;
import dao.exceptions.dailyDishExceptions.EditDailyDishFailException;
import dao.exceptions.dailyDishExceptions.GetDailyDishFailException;
import model.DailyDishModel;
import model.enums.DailyDishStatus;
import model.enums.DaySession;

import java.util.ArrayList;
import java.util.Date;

public interface IDailyDishDao {
    DailyDishModel editDailyDish(String token, Date day, DaySession session, int dishId, DailyDishStatus newStatus, Integer newPrice) throws EditDailyDishFailException;

    void deleteDailyDish(String token, Date day, DaySession session, int dishId) throws DeleteDailyDishFailException;

    ArrayList<DailyDishModel> getAll(Integer length, Integer offset) throws GetDailyDishFailException;

    DailyDishModel createDailyDish(String token, Date day, DaySession session, int dishId, DailyDishStatus status, int price) throws CreateDailyDishFailException;

    ArrayList<DailyDishModel> getBy(Date day, Integer dishId, DaySession session, Integer length, Integer offset) throws GetDailyDishFailException;
}
