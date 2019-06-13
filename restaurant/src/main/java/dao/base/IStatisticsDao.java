package dao.base;

import dao.exceptions.RequestFailException;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDate;

public interface IStatisticsDao {
    JSONArray countBill(String token, LocalDate startDate, LocalDate endDate) throws RequestFailException, IOException;

    JSONArray countDish(String token, Integer dishId, LocalDate startDate, LocalDate endDate) throws RequestFailException, IOException;
}
