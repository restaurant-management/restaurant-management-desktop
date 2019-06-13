package bus;

import com.google.firebase.database.annotations.NotNull;
import dao.Repository;
import dao.exceptions.RequestFailException;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDate;

public class StatisticsBus {
    private Repository _repository = Repository.getInstance();

    public JSONArray countBill(@NotNull LocalDate startDate, LocalDate endDate) throws IOException, RequestFailException {
        return _repository.countBill(startDate, endDate);
    }

    public JSONArray countDish(@NotNull Integer dishId,@NotNull LocalDate startDate, LocalDate endDate) throws IOException, RequestFailException {
        return _repository.countDish(dishId, startDate, endDate);
    }
}
