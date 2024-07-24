package com.IDOSdigital.userManagement.services;

import com.IDOSdigital.userManagement.entities.Balance;
import com.IDOSdigital.userManagement.utils.Response;


public interface BalanceService {
    Response getAllBalances();
    Response getBalanceById(String id);
    Response createBalance(Balance balance);
    Response updateBalance(Balance balance , String id);
    Response deleteBalance(String id);
}
