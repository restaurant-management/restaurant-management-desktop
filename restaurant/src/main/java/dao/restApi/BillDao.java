package dao.restApi;

import dao.base.IBillDao;
import dao.exceptions.RequestFailException;
import dao.exceptions.billExceptions.CreateBillFailException;
import dao.exceptions.billExceptions.DeleteBillFailException;
import dao.exceptions.billExceptions.EditBillFailException;
import dao.exceptions.billExceptions.GetBillFailException;
import model.BillDetailModel;
import model.BillModel;
import model.exceptions.IsNotABillStatusException;
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

public class BillDao implements IBillDao {
    @Override
    public ArrayList<BillModel> getAll(String token, Date day, Integer length, Integer offset) throws GetBillFailException {
        String uri = "/api/bills?";
        if (day != null) uri += "day=" + new SimpleDateFormat("yyyy-MM-dd").format(day);
        return getBillModels(token, length, offset, uri);
    }

    @Override
    public ArrayList<BillModel> getAllUserBill(String token, String username, Integer length, Integer offset) throws GetBillFailException {
        String uri = "/api/bills/user/" + username + "?";
        return getBillModels(token, length, offset, uri);
    }

    private ArrayList<BillModel> getBillModels(String token, Integer length, Integer offset, String uri) throws GetBillFailException {
        if (length != null) uri += "length=" + length;
        if (offset != null) uri += "&offset=" + offset;
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);
            String response = http.get(uri, new Header[]{header});
            ArrayList<BillModel> result = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            for (Object json : jsonArray) {
                result.add(new BillModel((JSONObject) json));
            }
            return result;
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new GetBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel createBill(String token, BillModel billModel) throws CreateBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            if (billModel.get_billDetails().size() <= 0)
                throw new CreateBillFailException("Yêu cầu có danh sách món ăn.");

            ArrayList<BillDetailModel> details = billModel.get_billDetails();

            // Create body for request
            List<NameValuePair> body = new ArrayList<>();
            for (int i = 0; i < details.size(); i++) {
                body.add(new BasicNameValuePair("dishIds[" + i + "]", Integer.toString(details.get(i).get_dish().get_dishId())));
                body.add(new BasicNameValuePair("quantities[" + i + "]", Integer.toString(details.get(i).get_quantity())));
                body.add(new BasicNameValuePair("prices[" + i + "]", Integer.toString(details.get(i).get_price())));
            }
            if (billModel.get_day() != null) body.add(new BasicNameValuePair("day", billModel.get_day().toString()));
            if (billModel.get_status() != null)
                body.add(new BasicNameValuePair("status", billModel.get_status().toString()));
            if (billModel.get_user() != null)
                body.add(new BasicNameValuePair("user", billModel.get_user().get_username()));
            if (billModel.get_managerUsername() != null)
                body.add(new BasicNameValuePair("manager", billModel.get_managerUsername()));

            String response = http.post("/api/bills/custom", new Header[]{header}, body);
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new CreateBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel editBill(String token, BillModel billModel) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);
            if (billModel.get_billDetails().size() <= 0)
                throw new EditBillFailException("Yêu cầu có danh sách món ăn.");

            ArrayList<BillDetailModel> details = billModel.get_billDetails();
            // Create body for request
            List<NameValuePair> body = new ArrayList<>();
            for (int i = 0; i < details.size(); i++) {
                body.add(new BasicNameValuePair("dishIds[" + i + "]", Integer.toString(details.get(i).get_dish().get_dishId())));
                body.add(new BasicNameValuePair("quantities[" + i + "]", Integer.toString(details.get(i).get_quantity())));
                body.add(new BasicNameValuePair("prices[" + i + "]", Integer.toString(details.get(i).get_price())));
            }
            if (billModel.get_day() != null) body.add(new BasicNameValuePair("day", billModel.get_day().toString()));
            if (billModel.get_status() != null)
                body.add(new BasicNameValuePair("status", billModel.get_status().toString()));
            if (billModel.get_user() != null)
                body.add(new BasicNameValuePair("user", billModel.get_user().get_username()));
            if (billModel.get_managerUsername() != null)
                body.add(new BasicNameValuePair("manager", billModel.get_managerUsername()));

            String response = http.put("/api/bills/" + billModel.get_billId(), new Header[]{header}, body);
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusCreated(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/created", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusPaid(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/paid", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusPreparing(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/preparing", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusPrepareDone(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/prepare-done", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusShipping(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/shipping", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusDelivering(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/delivering", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel updateBillStatusComplete(String token, int billId) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.put("/api/bills/" + billId + "/complete", new Header[]{header}, new ArrayList<>());
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public BillModel getBill(String token, int billId) throws GetBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.get("/api/bills/" + billId, new Header[]{header});
            return new BillModel(new JSONObject(response));
        } catch (IsNotABillStatusException | IOException | RequestFailException e) {
            throw new GetBillFailException(e.getMessage());
        }
    }

    @Override
    public void deleteBill(String token, int billId) throws DeleteBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            http.delete("/api/bills/" + billId, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new DeleteBillFailException(e.getMessage());
        }
    }

    @Override
    public void addDishToBill(String token, int billId, int dishId, Integer price, Integer quantity) throws EditBillFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            // Create body for request
            List<NameValuePair> body = new ArrayList<>();
            if (price != null && price > 0)
                body.add(new BasicNameValuePair("price", Integer.toString(price)));
            if (quantity != null)
                body.add(new BasicNameValuePair("quantity", Integer.toString(quantity)));

            http.post("/api/bills/" + billId + "/dishes/" + dishId, new Header[]{header}, body);
        } catch (IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }

    @Override
    public void removeDishToBill(String token, int billId, int dishId) throws EditBillFailException {

        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            http.delete("/api/bills/" + billId + "/dishes/" + dishId, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new EditBillFailException(e.getMessage());
        }
    }
}
