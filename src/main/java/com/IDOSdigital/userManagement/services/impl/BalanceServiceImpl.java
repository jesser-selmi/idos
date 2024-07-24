package com.IDOSdigital.userManagement.services.impl;

import com.IDOSdigital.userManagement.entities.Balance;
import com.IDOSdigital.userManagement.repositories.BalanceRepository;
import com.IDOSdigital.userManagement.services.BalanceService;
import com.IDOSdigital.userManagement.utils.EntityResponse;
import com.IDOSdigital.userManagement.utils.ErrorResponse;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    public Response getAllBalances() {
        List<Balance> allBalances = balanceRepository.findAll();
        List<Balance> balances = new ArrayList<>();

        for (Balance b : allBalances) {
            if (!b.isDeleted()) {
                balances.add(b);
            }
        }
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse(200, "Balances found");
        response.setEntityResponse(entityResponse);
        response.setData(balances);
        return response;
    }


    @Override
    public Response getBalanceById(String id) {
        Response response = new Response();
        Optional<Balance> optionalBalance = balanceRepository.findById(id);
        if (optionalBalance.isPresent()) {
            EntityResponse entityResponse = new EntityResponse(200, "Balance found");
            response.setEntityResponse(entityResponse);
            response.setData(optionalBalance.get());
        } else {
            EntityResponse entityResponse = new EntityResponse(404, "Balance not found");
            response.setEntityResponse(entityResponse);
        }
        return response;
    }

    @Override
    public Response createBalance(Balance balance) {
        Balance createdBalance = balanceRepository.save(balance);
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse(201, "Balance created successfully");
        response.setEntityResponse(entityResponse);
        response.setData(createdBalance);
        return response;
    }

    @Override
    public Response updateBalance(Balance balance, String id) {
        Response res = new Response();
        EntityResponse entityResponse;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Optional<Balance> optionalBalance = balanceRepository.findById(id);
            if (optionalBalance.isPresent()) {
                Balance existingBalance = optionalBalance.get();

                // Update only the attributes from balance that are not null or not empty
                if (balance.getUserId() != null && !balance.getUserId().isEmpty()) {
                    existingBalance.setUserId(balance.getUserId());
                }

                // Save the updated balance
                Balance updatedBalance = balanceRepository.save(existingBalance);

                res.setData(updatedBalance);
                entityResponse = new EntityResponse(HttpStatus.OK.value(), "Balance updated successfully");
            } else {
                entityResponse = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Balance not found");
            }
        } catch (Exception e) {
            errors.add(new ErrorResponse.ValidationError("Exception", e.getMessage()));
            entityResponse = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
            entityResponse.setErrors(errors);
        }

        res.setEntityResponse(entityResponse);
        return res;
    }

    @Override
    public Response deleteBalance(String id) {
        Response response = new Response();
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        EntityResponse entityResponse;

        try {
            Optional<Balance> optionalBalance = balanceRepository.findById(id);
            if (optionalBalance.isPresent()) {
                Balance existingBalance = optionalBalance.get();
                existingBalance.setDeleted(true); // Soft delete by setting deleted to true
                Balance updatedBalance = balanceRepository.save(existingBalance); // Save the updated balance
                response.setData(updatedBalance); // Return the deleted data
                entityResponse = buildResponseValidator("Balance deleted successfully", HttpStatus.OK, errors);
            } else {
                entityResponse = buildResponseValidator("Balance not found", HttpStatus.NOT_FOUND, errors);
            }
        } catch (Exception e) {
            entityResponse = buildResponseValidator("Error deleting balance", HttpStatus.INTERNAL_SERVER_ERROR, errors);
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            entityResponse.setErrors(errors);
        }

        response.setEntityResponse(entityResponse);
        return response;
    }

    private EntityResponse buildResponseValidator(String message, HttpStatus status, List<ErrorResponse.ValidationError> errors) {
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setMessage(message);
        entityResponse.setStatus(status.value());
        entityResponse.setErrors(errors);
        return entityResponse;
    }
}
