package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mvc.dyvault.common.bean.AppMessage;
import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.dto.AppUserDTO;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.dto.UserTypeDTO;
import com.mvc.dyvault.common.bean.vo.AppUserRetVO;
import com.mvc.dyvault.common.bean.vo.TokenBalanceVO;
import com.mvc.dyvault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DUserLogVO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.common.util.InviteUtil;
import com.mvc.dyvault.common.util.MessageConstants;
import com.mvc.dyvault.common.util.MnemonicUtil;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.AppUserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

import static com.mvc.dyvault.common.constant.RedisConstant.APP_USER_USERNAME;

@Service
public class AppUserService extends AbstractService<AppUser> implements BaseService<AppUser> {
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("sign-pool-%d").build();

    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppMessageService appMessageService;
    @Autowired
    AppUserMapper appUserMapper;
    @Autowired
    AppUserInviteService appUserInviteService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    ShopService shopService;

    public PageInfo<DUSerVO> findUser(PageDTO pageDTO, String cellphone, Integer status) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), "id desc");
        Condition condition = new Condition(AppUser.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "cellphone = ", cellphone);
        ConditionUtil.andCondition(criteria, "status = ", status);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<AppUser> list = findByCondition(condition);
        List<DUSerVO> vos = new ArrayList<>(list.size());
        for (AppUser appUser : list) {
            DUSerVO vo = new DUSerVO();
            BeanUtils.copyProperties(appUser, vo);
            List<TokenBalanceVO> data = appUserBalanceService.getAsset(appUser.getId(), true);
            BigDecimal sum = data.stream().map(obj -> obj.getRatio().multiply(obj.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setBalance(sum);
            vo.setInviteNum(appUser.getInviteNum());
            vo.setUserType(null != appUser.getIsBusinesses() && appUser.getIsBusinesses() == 1 ? 2 : null != appUser.getIsProxy() && appUser.getIsProxy() == 1 ? 1 : 0);
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public PageInfo<DUserLogVO> getUserLog(BigInteger id, PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        Condition condition = new Condition(AppMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", id);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<AppMessage> list = appMessageService.findByCondition(condition);
        List<DUserLogVO> vos = new ArrayList<>(list.size());
        for (AppMessage appUser : list) {
            DUserLogVO vo = new DUserLogVO();
            BeanUtils.copyProperties(appUser, vo);
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public AppUserRetVO register(AppUserDTO appUserDTO) {
        AppUser userTemp = findOneBy("email", appUserDTO.getEmail());
        Assert.isNull(userTemp, MessageConstants.getMsg("USER_EXIST"));
        long time = System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setStatus(4);
        appUser.setUpdatedAt(time);
        appUser.setCreatedAt(time);
        appUser.setNickname(appUserDTO.getNickname());
        appUser.setInviteNum(0);
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setIsBusinesses(0);
        appUser.setPassword(appUserDTO.getPassword());
        appUser.setSalt(appUserDTO.getSalt());
        appUser.setInviteLevel(0);
        appUser.setTransactionPassword(appUserDTO.getTransactionPassword());
        String code = getCode();
        appUser.setPvKey(code);
        save(appUser);
        AppUserRetVO vo = new AppUserRetVO();
        vo.setMnemonics(MnemonicUtil.getWordsList(code));
        vo.setPrivateKey(code);
        //更新邀请人数量
        Long id = InviteUtil.codeToId(appUserDTO.getInviteCode());
        appUserMapper.updateInvite(BigInteger.valueOf(id));
        //添加邀请记录
        appUserInviteService.insert(BigInteger.valueOf(id), appUser.getId());
        updateCache(appUser.getId());
        String key = APP_USER_USERNAME + appUserDTO.getEmail();
        redisTemplate.opsForHash().put(key, key, String.valueOf(appUser.getId()));
        return vo;
    }

    private String getCode() {
        String result = null;
        while (true) {
            result = MnemonicUtil.getRandomCode();
            List<AppUser> list = findBy("pvKey", result);
            if (list.size() == 0) {
                break;
            }
        }
        return result;
    }

    public void mnemonicsActive(String email) {
        AppUser appUser = findOneBy("email", email);
        appUser.setStatus(1);
        appUser.setUpdatedAt(System.currentTimeMillis());
        update(appUser);
        updateCache(appUser.getId());
    }

    public AppUser regUser(String cellphone) {
        AppUser user = new AppUser();
        user.setCellphone(cellphone);
        user.setIsBusinesses(0);
        user.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setStatus(5);
        user.setUpdatedAt(System.currentTimeMillis());
        user.setPassword(md5Encrypt(user.getSalt(), user.getSalt()));
        user.setTransactionPassword(user.getPassword());
        user.setCreatedAt(System.currentTimeMillis());
        user.setInviteLevel(0);
        user.setInviteNum(0);
        user.setNickname(cellphone);
        user.setPvKey(getCode());
        save(user);
        return user;
    }

    private String md5Encrypt(String pass, String salt) {
        return DigestUtils.md5Hex((salt + DigestUtils.md5Hex(pass).toUpperCase())).toUpperCase();
    }

    public Boolean updateUserType(UserTypeDTO userTypeDTO) {
        AppUser user = findById(userTypeDTO.getUserId());
        if (user == null) {
            return false;
        }
        Long time = System.currentTimeMillis();
        switch (userTypeDTO.getUserType()) {
            case 0:
                user.setIsBusinesses(0);
                user.setIsProxy(0);
                user.setUpdatedAt(time);
                shopService.deleteShop(user.getId());
                break;
            case 1:
                user.setIsBusinesses(0);
                user.setIsProxy(1);
                user.setUpdatedAt(time);
                shopService.addShop(user.getId(), userTypeDTO.getName());
                break;
            case 2:
                user.setIsBusinesses(1);
                user.setIsProxy(0);
                user.setUpdatedAt(time);
                shopService.deleteShop(user.getId());
                break;
        }
        update(user);
        updateCache(user.getId());
        return true;
    }
}