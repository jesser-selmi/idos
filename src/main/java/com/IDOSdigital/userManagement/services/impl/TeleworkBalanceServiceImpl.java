package com.IDOSdigital.userManagement.services.impl;

import com.IDOSdigital.userManagement.entities.TeleworkBalance;
import com.IDOSdigital.userManagement.repositories.TeleworkBalanceRepository;
import com.IDOSdigital.userManagement.services.TeleworkBalanceService;
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
public class TeleworkBalanceServiceImpl implements TeleworkBalanceService {
    @Autowired
    private TeleworkBalanceRepository teleworkBalanceRepository;
    @Override
    public Response getAllTeleworkBalances() {
        List<TeleworkBalance> allTeleworkBalances = teleworkBalanceRepository.findAll();
        List<TeleworkBalance> teleworkBalances = new ArrayList<>();
        for (TeleworkBalance twb : allTeleworkBalances) {
            if (!twb.isDeleted()) {
                teleworkBalances.add(twb);
            }
        }
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse(200, "Telework balances found");
        response.setEntityResponse(entityResponse);
        response.setData(teleworkBalances);
        return response;
    }
    @Override
    public Response getTeleworkBalanceById(String id) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        try {
            Optional<TeleworkBalance> optionalTeleworkBalance = teleworkBalanceRepository.findById(id);
            if (optionalTeleworkBalance.isPresent()) {
                res.setData(optionalTeleworkBalance.get());
                response = new EntityResponse(HttpStatus.OK.value(), "Telework balance found");
            } else {
                response = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Telework balance not found");
            }
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }
        res.setEntityResponse(response);
        return res;
    }
    @Override
    public Response createTeleworkBalance(TeleworkBalance teleworkBalance) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        try {
            TeleworkBalance createdTeleworkBalance = teleworkBalanceRepository.save(teleworkBalance);
            res.setData(createdTeleworkBalance);
            response = new EntityResponse(HttpStatus.CREATED.value(), "Telework balance created successfully");
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }
        res.setEntityResponse(response);
        return res;
    }
    @Override
    public Response updateTeleworkBalance(TeleworkBalance teleworkBalance, String id) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        try {
            Optional<TeleworkBalance> optionalTeleworkBalance = teleworkBalanceRepository.findById(id);
            if (optionalTeleworkBalance.isPresent()) {
                TeleworkBalance existingTeleworkBalance = optionalTeleworkBalance.get();
                // Update only the attributes from teleworkBalance that are not null
                if (teleworkBalance.getBalance() != 0) {
                    existingTeleworkBalance.setBalance(teleworkBalance.getBalance());
                }
                if (teleworkBalance.getUserId() != null && !teleworkBalance.getUserId().isEmpty()) {
                    existingTeleworkBalance.setUserId(teleworkBalance.getUserId());
                }
                TeleworkBalance updatedTeleworkBalance = teleworkBalanceRepository.save(existingTeleworkBalance);
                res.setData(updatedTeleworkBalance);
                response = new EntityResponse(HttpStatus.OK.value(), "Telework balance updated successfully");
            } else {
                response = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Telework balance not found");
            }
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }

        res.setEntityResponse(response);
        return res;
    }
    @Override
    public Response deleteTeleworkBalance(String id) {
        Response res = new Response();
        EntityResponse response;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        try {
            Optional<TeleworkBalance> optionalTeleworkBalance = teleworkBalanceRepository.findById(id);
            if (optionalTeleworkBalance.isPresent()) {
                TeleworkBalance existingTeleworkBalance = optionalTeleworkBalance.get();
                existingTeleworkBalance.setDeleted(true); // Soft delete by setting deleted to true
                TeleworkBalance updatedTeleworkBalance =teleworkBalanceRepository.save(existingTeleworkBalance);
                res.setData(updatedTeleworkBalance);
                response = new EntityResponse(HttpStatus.OK.value(), "Telework balance deleted successfully");
            } else {
                response = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Telework balance not found");
            }
        } catch (final Exception e) {
            response = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            response.setErrors(errors);
        }
        res.setEntityResponse(response);
        return res;
    }

}
