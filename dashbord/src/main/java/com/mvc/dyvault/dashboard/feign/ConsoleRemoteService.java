package com.mvc.dyvault.dashboard.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.*;
import com.mvc.dyvault.common.bean.dto.*;
import com.mvc.dyvault.common.bean.vo.*;
import com.mvc.dyvault.common.dashboard.bean.dto.*;
import com.mvc.dyvault.common.dashboard.bean.vo.*;
import com.mvc.dyvault.common.sdk.dto.BusinessTxSearchDTO;
import com.mvc.dyvault.common.sdk.dto.DevDTO;
import com.mvc.dyvault.common.sdk.dto.PaymentDTO;
import com.mvc.dyvault.common.sdk.vo.BusinessOrderVO;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.common.sdk.vo.DevVO;
import com.mvc.dyvault.common.sdk.vo.ShopVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface ConsoleRemoteService {

    @GetMapping("dashboard/adminUser")
    Result<PageInfo<AdminVO>> getAdmins(@RequestParam("userId") BigInteger userId, @RequestBody PageDTO dto);

    @GetMapping("dashboard/adminUser/{id}")
    Result<AdminDetailVO> getAdminDetail(@PathVariable("id") BigInteger id);

    @DeleteMapping("dashboard/adminUser/{id}")
    Result<Boolean> deleteAdmin(@RequestParam(value = "userId", required = false) BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("dashboard/adminUser/password")
    Result<Boolean> updatePwd(@RequestParam(value = "id", required = false) BigInteger userId, @RequestBody AdminPasswordDTO adminPasswordDTO);

    @GetMapping("dashboard/adminUser/username")
    Result<AdminUser> getAdminByUsername(@RequestParam(value = "username", required = false) String username);

    @PostMapping("dashboard/adminUser")
    Result<Boolean> newAdmin(@RequestBody AdminDTO adminDTO);

    @PutMapping("dashboard/adminUser")
    Result<Boolean> updateAdmin(@RequestParam("userId") BigInteger userId, @RequestBody AdminDTO adminDTO);

    @GetMapping("dashboard/adminUser/balance")
    Result<BigDecimal> getBalance(@RequestParam(value = "tokenId", required = false) BigInteger tokenId);

    @GetMapping("dashboard/commonToken")
    Result<List<DTokenVO>> findTokens(@RequestParam(value = "tokenName", required = false) String tokenName, @RequestParam(value = "isBlock", required = false) Integer blockType);

    @PostMapping("dashboard/commonToken")
    Result<Boolean> newToken(@RequestBody DTokenDTO dTokenDTO);

    @PutMapping("dashboard/commonToken")
    Result<Boolean> updateToken(@RequestBody DTokenDTO dTokenDTO);

    @GetMapping("dashboard/commonToken/{id}")
    Result<DTokenDTO> getToken(@PathVariable("id") BigInteger id);

    @PutMapping("dashboard/commonToken/setting")
    Result<Boolean> tokenSetting(@RequestBody DTokenSettingVO dto);

    @GetMapping("dashboard/commonToken/setting")
    Result<PageInfo<DTokenSettingVO>> getTokenSettings(@RequestBody PageDTO pageDTO, @RequestParam(value = "tokenName", required = false) String tokenName);

    @GetMapping("dashboard/commonToken/setting/{id}")
    Result<DTokenSettingVO> getTokenSetting(@PathVariable("id") BigInteger id);

    @PutMapping("dashboard/blockTransaction/status")
    Result<Boolean> updateStatus(@RequestBody DBlockStatusDTO dBlockStatusDTO);

    @GetMapping("dashboard/blockTransaction")
    Result<PageInfo<DBlockeTransactionVO>> getTransactions(@RequestBody DBlockeTransactionDTO dBlockeTransactionDTO);

    @GetMapping("dashboard/appUser")
    Result<PageInfo<DUSerVO>> findUser(@RequestBody PageDTO pageDTO, @RequestParam(value = "cellphone", required = false) String cellphone, @RequestParam(value = "status", required = false) Integer status);

    @GetMapping("dashboard/appUser/{id}")
    Result<DUSerDetailVO> getUserDetail(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appUser/{id}/balance")
    Result<List<DUserBalanceVO>> getUserBalance(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appUser/{id}/log")
    Result<PageInfo<DUserLogVO>> getUserLog(@RequestBody PageDTO pageDTO, @PathVariable("id") BigInteger id);

    @PostMapping("dashboard/commonAddress")
    Result<Boolean> importAddress(@RequestBody List<CommonAddress> list, @RequestParam("fileName") String fileName);

    @PostMapping("dashboard/blockSign")
    Result<Boolean> importSign(@RequestBody List<BlockSign> list, @RequestParam("fileName") String fileName);

    @PutMapping("dashboard/appUser/{id}/status")
    Result<Boolean> updateUserStatus(@PathVariable("id") BigInteger id, @RequestParam("status") Integer status);

    @GetMapping("dashboard/appProject/{id}/partake")
    Result<List<ExportPartake>> exportPartake(@PathVariable("id") BigInteger id);

    @PostMapping("dashboard/appProject/{id}/partake")
    Result<Boolean> importPartake(@PathVariable("id") BigInteger id, @RequestBody List<ImportPartake> list, @RequestParam("fileName") String fileName);

    @GetMapping("dashboard/adminUser/wallet/{tokenId}")
    Result<AdminWalletVO> getAdminWallet(@PathVariable("tokenId") BigInteger tokenId);

    @PostMapping("dashboard/blockTransaction")
    Result<Boolean> buy(@RequestBody AdminTransactionDTO dto);

    @PostMapping("dashboard/appUser")
    Result<Boolean> importAppUser(@RequestBody List<AppUser> list, @RequestParam("fileName") String fileName);

    @PutMapping("appUserBalance/debit")
    Result<Boolean> debit(@RequestBody DebitRechargeDTO debitRechargeDTO);

    @GetMapping("dashboard/financial")
    Result<PageInfo<AppFinancial>> getFinancialList(@RequestBody PageDTO pageDTO, @RequestParam(value = "financialName", required = false) String financialName);

    @GetMapping("dashboard/financial/{id}")
    Result<AppFinancialDetailVO> getFinancialDetail(@PathVariable("id") BigInteger id);

    @PostMapping("dashboard/financial")
    Result<Boolean> saveAppFinancial(@RequestBody AppFinancialDTO appFinancialDTO);

    @PutMapping("dashboard/financial")
    Result<Boolean> updateAppFinancial(@RequestBody AppFinancialDTO appFinancialDTO);

    @GetMapping("dashboard/financial/{id}/order")
    Result<PageInfo<AppFinancialOrderVO>> getFinancialOrderList(@PathVariable("id") BigInteger id, @RequestBody PageDTO pageDTO, @RequestParam(value = "searchKey", required = false) String searchKey, @RequestParam(value = "status", required = false) Integer status);

    @GetMapping("appInfo/{appType}")
    Result<AppInfo> getApp(@PathVariable("appType") String appType);

    @GetMapping("appInfo")
    Result<List<AppInfo>> getAppList();

    @PostMapping("appInfo")
    Result<Boolean> saveApp(@RequestBody AppInfo appInfo);

    @GetMapping("dashboard/blockTransaction/collect")
    Result<List<ExportOrders>> exportCollect();

    @GetMapping("dashboard/blockTransaction/signCount")
    Result<List<SignSumVO>> exportSignCount();

    @GetMapping("dashboard/blockTransaction/sign")
    Result<List<ExportOrders>> exportSign();

    @GetMapping("dashboard/channel")
    Result<PageInfo<AppChannel>> getChannels(@RequestBody PageDTO pageDTO);

    @PostMapping("dashboard/channel")
    Result<Boolean> saveChannel(@RequestBody AppChannel appChannel);

    @DeleteMapping("dashboard/channel/{id}")
    Result<Boolean> delete(@PathVariable("id") BigInteger id);


    @GetMapping("dashboard/shop")
    Result<PageInfo<ShopVO>> getShop();

    @GetMapping("dashboard/shop/{id}/develop")
    Result<DevVO> getDevSetting(@PathVariable("id") BigInteger id);

    @PostMapping("dashboard/shop/{id}/develop")
    Result<Boolean> setDevSetting(@PathVariable("id") BigInteger id, @RequestBody DevDTO devDTO);

    @GetMapping("dashboard/shop/{id}")
    Result<PageInfo<BusinessOrderVO>> getBusinessList(@PathVariable("id") BigInteger id, @RequestBody BusinessTxSearchDTO businessTxSearchDTO);

    @GetMapping("dashboard/shop/{id}/count")
    Result<List<BusinessTxCountVO>> getBusinessCount(@PathVariable("id") BigInteger id, @RequestParam("startedAt") Long startedAt, @RequestParam("stopAt") Long stopAt);

    @PostMapping("dashboard/appUser/userType")
    Result<Boolean> updateUserType(@RequestBody UserTypeDTO userTypeDTO);

    @GetMapping("dashboard/supplier/{id}/tx")
    Result<PageInfo<BusinessTransaction>> getSupplierTxList(@PathVariable("id") BigInteger id, @RequestBody BusinessTransactionSearchDTO businessTransactionSearchDTO);

    @GetMapping("dashboard/supplier/{id}/cancel")
    Result<Boolean> cancelBusinessTx(@PathVariable("id") BigInteger id, @RequestParam("txId") BigInteger txId);

    @GetMapping("dashboard/supplier/{id}/setting")
    Result<SupplierVO> getSupplier(@PathVariable("id") BigInteger id);

    @PostMapping("dashboard/supplier/{id}/setting")
    Result<Boolean> updateSupplier(@PathVariable("id") BigInteger id, @RequestBody SupplierVO supplierVO);

    @PostMapping("dashboard/supplier/{id}")
    Result<Boolean> updatePayment(@PathVariable("id") BigInteger id, @RequestBody PaymentDTO paymentDTO);

    @GetMapping("dashboard/supplier/{id}")
    Result<PaymentDTO> getPayment(@PathVariable("id") BigInteger id, @RequestParam("paymentType") Integer paymentType);
}
