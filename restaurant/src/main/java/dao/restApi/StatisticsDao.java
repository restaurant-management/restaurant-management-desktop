package dao.restApi;

import com.google.firebase.database.annotations.NotNull;
import dao.base.IStatisticsDao;
import dao.exceptions.RequestFailException;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StatisticsDao implements IStatisticsDao {
    @Override
    public JSONArray countBill(@NotNull String token, @NotNull LocalDate startDate, LocalDate endDate) throws RequestFailException, IOException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String url = "/api/statistics/countBill?startDate=" + startDate.format(formatter);
        if (endDate != null) url += "&endDate=" + endDate.format(formatter);
        else url += "&endDate=" + LocalDate.now().format(formatter);
        String response = http.get(url, new Header[]{header});
        return new JSONArray(response);
    }
}
