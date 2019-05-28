package dao.restApi;

import dao.base.IDailyDishDao;
import dao.exceptions.RequestFailException;
import dao.exceptions.dailyDishExceptions.CreateDailyDishFailException;
import dao.exceptions.dailyDishExceptions.DeleteDailyDishFailException;
import dao.exceptions.dailyDishExceptions.EditDailyDishFailException;
import dao.exceptions.dailyDishExceptions.GetDailyDishFailException;
import model.DailyDishModel;
import model.enums.DailyDishStatus;
import model.enums.DaySession;
import model.exceptions.IsNotADailyDishStatusException;
import model.exceptions.IsNotADaySessionException;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyDishDao implements IDailyDishDao {
    @Override
    public DailyDishModel editDailyDish(String token, Date day, DaySession session, int dishId, DailyDishStatus newStatus, Integer newPrice) throws EditDailyDishFailException {
        if (day == null || session == null) throw new EditDailyDishFailException("Day and session is required.");

        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (newStatus != null)
            body.add(new BasicNameValuePair("status", newStatus.toString()));
        if (newPrice != null)
            body.add(new BasicNameValuePair("price", newPrice.toString()));

        try {
            String uri = "/api/dailyDishes?day=" + new SimpleDateFormat("yyyy-MM-dd").format(day) + "&session="
                    + session.toString() + "&dishId=" + dishId;
            String response = http.put(uri, new Header[]{header}, body);
            return new DailyDishModel(new JSONObject(response));
        } catch (IOException | RequestFailException | IsNotADaySessionException | ParseException | IsNotADailyDishStatusException e) {
            throw new EditDailyDishFailException(e.getMessage());
        }
    }

    @Override
    public void deleteDailyDish(String token, Date day, DaySession session, int dishId) throws DeleteDailyDishFailException {
        if (day == null || session == null) throw new DeleteDailyDishFailException("Day and session is required.");

        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        try {
            String uri = "/api/dailyDishes?day=" + new SimpleDateFormat("yyyy-MM-dd").format(day) + "&session="
                    + session.toString() + "&dishId=" + dishId;
            http.delete(uri, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new DeleteDailyDishFailException(e.getMessage());
        }
    }

    @Override
    public ArrayList<DailyDishModel> getAll(Integer length, Integer offset) throws GetDailyDishFailException {
        String uri = "/api/dailyDishes?";
        if (length != null) uri += "length=" + length;
        if (offset != null) uri += "&offset=" + offset;
        try {
            return getManyDailyDish(uri);
        } catch (IOException | RequestFailException | IsNotADaySessionException | ParseException | IsNotADailyDishStatusException e) {
            throw new GetDailyDishFailException(e.getMessage());
        }
    }

    @Override
    public DailyDishModel createDailyDish(String token, Date day, DaySession session, int dishId, DailyDishStatus status, int price) throws CreateDailyDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (day != null)
            body.add(new BasicNameValuePair("day", new SimpleDateFormat("yyyy-MM-dd").format(day)));
        if (session != null)
            body.add(new BasicNameValuePair("session", session.toString()));
        body.add(new BasicNameValuePair("dishId", Integer.toString(dishId)));
        if (price > 0)
            body.add(new BasicNameValuePair("price", Integer.toString(price)));
        if (status != null)
            body.add(new BasicNameValuePair("status", status.toString()));

        try {
            String response = http.post("/api/dailyDishes", new Header[]{header}, body);
            return new DailyDishModel(new JSONObject(response));
        } catch (IOException | RequestFailException | IsNotADaySessionException | ParseException | IsNotADailyDishStatusException e) {
            throw new CreateDailyDishFailException(e.getMessage());
        }
    }

    @Override
    public ArrayList<DailyDishModel> getBy(Date day, Integer dishId, DaySession session, Integer length, Integer offset) throws GetDailyDishFailException {
        try {
            String uri = "/api/dailyDishes/getBy";
            if (day != null || dishId != null || session != null) uri += "?";
            if (day != null) uri += "day=" + new SimpleDateFormat("yyyy-MM-dd").format(day);
            if (dishId != null) uri += "&dishId=" + dishId.toString();
            if (session != null) uri += "&session" + session.toString();
            if (length != null) uri += "&length" + length.toString();
            if (offset != null) uri += "&offset" + offset.toString();

            return getManyDailyDish(uri);
        } catch (IOException | RequestFailException | IsNotADaySessionException | ParseException | IsNotADailyDishStatusException e) {
            throw new GetDailyDishFailException(e.getMessage());
        }
    }

    private ArrayList<DailyDishModel> getManyDailyDish(String uri) throws IOException, RequestFailException, ParseException, IsNotADaySessionException, IsNotADailyDishStatusException {
        HttpConnection http = new HttpConnection();
        String response = http.get(uri, new Header[]{});
        ArrayList<DailyDishModel> result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for (Object json : jsonArray) {
            result.add(new DailyDishModel((JSONObject) json));
        }
        return result;
    }
}
