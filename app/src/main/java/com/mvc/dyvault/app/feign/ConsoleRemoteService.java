package com.mvc.dyvault.app.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.*;
import com.mvc.dyvault.common.bean.dto.AppUserDTO;
import com.mvc.dyvault.common.bean.dto.AssertVisibleDTO;
import com.mvc.dyvault.common.bean.dto.TransactionDTO;
import com.mvc.dyvault.common.bean.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface ConsoleRemoteService {

    @GetMapping("appUserBalance/{userId}")
    Result<List<TokenBalanceVO>> getAsset(@PathVariable("userId") BigInteger userId);

    @GetMapping("appUserBalance/{userId}/{tokenId}")
    Result<TokenBalanceVO> getAssetByTokenId(@PathVariable("userId") BigInteger userId, @PathVariable("tokenId") BigInteger tokenId);

    @PutMapping("appUserBalance/{userId}")
    Result<Boolean> updateVisible(@PathVariable("userId") BigInteger userId, @RequestBody AssertVisibleDTO assertVisibleDTO);

    @GetMapping("appUserBalance/sum/{userId}")
    Result<BigDecimal> getBalance(@PathVariable("userId") BigInteger userId);

    @GetMapping("appOrder/user")
    Result<List<TransactionSimpleVO>> getTransactions(
            @RequestParam("userId") BigInteger userId,
            @RequestParam("transactionType") Integer transactionType,
            @RequestParam("id") BigInteger id,
            @RequestParam("type") Integer type,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("tokenId") BigInteger tokenId,
            @RequestParam("classify") Integer classify
    );

    @GetMapping("appOrder/{id}")
    Result<TransactionDetailVO> getTransaction(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("appUserAddress/{userId}")
    Result<String> getAddress(@PathVariable("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId);

    @GetMapping("commonToken/transactionInfo")
    Result<TransactionTokenVO> getTransactionInfo(@RequestParam("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId);

    @PostMapping("blockTransaction/{userId}")
    Result<Boolean> sendTransaction(@PathVariable("userId") BigInteger userId, @RequestBody TransactionDTO transactionDTO);

    @GetMapping("appMessage")
    Result<PageInfo<AppMessage>> getlist(@RequestParam("userId") BigInteger userId, @RequestParam("timestamp") BigInteger timestamp, @RequestParam("type") Integer type, @RequestParam("pageSize") Integer pageSize);

    @PutMapping("appMessage/{id}")
    Result<Boolean> read(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("commonToken")
    Result<PageInfo<CommonToken>> all(@RequestParam("visiable") Integer visiable, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("updatedStartAt") BigInteger timestamp);

    @GetMapping("commonTokenPrice")
    Result<PageInfo<CommonTokenPrice>> price();

    @GetMapping("user/{id}")
    Result<AppUser> getUserById(@PathVariable("id") BigInteger userId);

    @GetMapping("user/username")
    Result<AppUser> getUserByUsername(@RequestParam("username") String username);

    @PostMapping("user")
    Result<AppUserRetVO> register(@RequestBody AppUserDTO appUserDTO);

    @PostMapping("user/mnemonics")
    Result<Boolean> mnemonicsActive(@RequestParam("email") String email);

    @PostMapping("user/password")
    Result<Boolean> forget(@RequestParam("userId") BigInteger userId, @RequestParam("password") String password, @RequestParam("type") Integer type);

    @PutMapping("user")
    Result<Boolean> updateUser(@RequestBody AppUser user);

    @GetMapping("appInfo/{appType}")
    Result<AppInfo> getApp(@PathVariable("appType") String appType);

}
