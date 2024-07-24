package com.IDOSdigital.userManagement.services;

import com.IDOSdigital.userManagement.entities.TeleworkBalance;
import com.IDOSdigital.userManagement.utils.Response;

public interface TeleworkBalanceService {
    Response getAllTeleworkBalances();


    Response getTeleworkBalanceById(String id);
    Response createTeleworkBalance(TeleworkBalance teleworkBalance);
    Response updateTeleworkBalance(TeleworkBalance teleworkBalance , String id);
    Response deleteTeleworkBalance(String id);
}
