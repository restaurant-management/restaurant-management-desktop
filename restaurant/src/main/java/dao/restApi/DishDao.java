package dao.restApi;

import dao.FirebaseDao;
import dao.base.IDishDao;
import dao.exceptions.RequestFailException;
import dao.exceptions.dishExceptions.CreateDishFailException;
import dao.exceptions.dishExceptions.DeleteDishFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.dishExceptions.GetDishFailException;
import model.DishModel;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DishDao implements IDishDao {
    @Override
    public DishModel createDish(String token, String name, String description, ArrayList<String> images, int defaultPrice) throws CreateDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("name", name));
        if (description != null)
            body.add(new BasicNameValuePair("description", description));
        if (images != null)
            for (int i = 0; i < images.size(); i++) {
                try {
                    String uploadPath = "dishImages/" + name + i + "-" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                    String url = FirebaseDao.getInstance().create(uploadPath, images.get(i));
                    body.add(new BasicNameValuePair("images[" + i + "]", url));
                } catch (IOException ignored) {
                }
            }
        body.add(new BasicNameValuePair("defaultPrice", Integer.toString(defaultPrice)));

        try {
            String response = http.post("/api/dishes", new Header[]{header}, body);
            return new DishModel(new JSONObject(response));
        } catch (IOException | RequestFailException e) {
            throw new CreateDishFailException(e.getMessage());
        }
    }

    @Override
    public void deleteDish(String token, int dishId) throws DeleteDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            http.delete("/api/dishes/" + dishId, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new DeleteDishFailException(e.getMessage());
        }
    }

    @Override
    public DishModel getDish(String token, int dishId) throws GetDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            String response = http.get("/api/dishes/" + dishId, new Header[]{header});
            return new DishModel(new JSONObject(response));
        } catch (IOException | RequestFailException e) {
            throw new GetDishFailException(e.getMessage());
        }
    }

    /// new DishDao().editDish(token, 5, "Sườn nướng", "Nhớ em nhiều", new ArrayList<>(Arrays.asList("", "")), 0);
    @Override
    public DishModel editDish(String token, int dishId, String name, String description, ArrayList<String> images, int defaultPrice) throws EditDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (name != null)
            body.add(new BasicNameValuePair("name", name));
        if (description != null)
            body.add(new BasicNameValuePair("description", description));
        body.add(new BasicNameValuePair("defaultPrice", Integer.toString(defaultPrice)));
        if (images != null)
            for (int i = 0; i < images.size(); i++) {
                body.add(new BasicNameValuePair("images[" + i + "]", images.get(i)));
            }

        try {
            String response = http.put("/api/dishes/" + dishId, new Header[]{header}, body);
            return new DishModel(new JSONObject(response));
        } catch (IOException | RequestFailException e) {
            throw new EditDishFailException(e.getMessage());
        }
    }

    @Override
    public ArrayList<DishModel> getAll(String token, Integer length, Integer offset) throws GetDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        try {
            String uri = "/api/dishes?";
            if (length != null) uri += "length=" + length;
            if (offset != null) uri += "&offset=" + offset;
            String response = http.get(uri, new Header[]{header});

            ArrayList<DishModel> result = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            for (Object json : jsonArray) {
                result.add(new DishModel((JSONObject) json));
            }
            return result;
        } catch (IOException | RequestFailException e) {
            throw new GetDishFailException(e.getMessage());
        }
    }
}
