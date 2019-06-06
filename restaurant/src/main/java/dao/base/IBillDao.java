package dao.base;

import dao.exceptions.billExceptions.CreateBillFailException;
import dao.exceptions.billExceptions.DeleteBillFailException;
import dao.exceptions.billExceptions.EditBillFailException;
import dao.exceptions.billExceptions.GetBillFailException;
import model.BillModel;
import model.DailyDishModel;
import model.enums.BillStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public interface IBillDao {
    ArrayList<BillModel> getAll(String token, Date day, Integer length, Integer offset) throws GetBillFailException;

    ArrayList<BillModel> getAllUserBill(String token, String username, Integer length, Integer offset) throws GetBillFailException;

    BillModel createBill(String token, BillModel billModel) throws CreateBillFailException;

    BillModel editBill(String token, BillModel billModel) throws EditBillFailException;

    BillModel updateBillStatusCreated(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusPaid(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusPreparing(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusPrepareDone(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusShipping(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusDelivering(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusComplete(String token, int billId) throws EditBillFailException;

    BillModel getBill(String token, int billId) throws GetBillFailException;

    void deleteBill(String token, int billId) throws DeleteBillFailException;

    void addDishToBill(String token, int billId, int dishId, Integer price, Integer quantity) throws EditBillFailException;

    void removeDishToBill(String token, int billId, int dishId) throws EditBillFailException;
}
