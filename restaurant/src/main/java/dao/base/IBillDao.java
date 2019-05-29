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

public interface IBillDao {
    ArrayList<BillModel> getAll(String token, Integer length, Integer offset) throws GetBillFailException;

    ArrayList<BillModel> getAllUserBill(String token, String username, Integer length, Integer offset) throws GetBillFailException;

    BillModel createBill(String token, ArrayList<DailyDishModel> dishes, ArrayList<Integer> quantities, LocalDateTime day, BillStatus billStatus) throws CreateBillFailException;

    BillModel editBill(String token, int billId, LocalDateTime day) throws EditBillFailException;

    BillModel updateBillStatusCreated(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusPaid(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusPreparing(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusPrepareDone(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusShipping(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusDelivering(String token, int billId) throws EditBillFailException;

    BillModel updateBillStatusComplete(String token, int billId) throws EditBillFailException;

    BillModel getBill(String token, int billId) throws GetBillFailException;

    void deleteBill(String token, int billId) throws DeleteBillFailException;

    BillModel addDishToBill(String token, int billId, DailyDishModel dish) throws EditBillFailException;

    BillModel removeDishToBill(String token, int billId, int dishId) throws EditBillFailException;
}
