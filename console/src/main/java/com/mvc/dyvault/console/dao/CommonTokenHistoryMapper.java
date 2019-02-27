package com.mvc.dyvault.console.dao;

import com.mvc.dyvault.common.bean.AppKline;
import com.mvc.dyvault.common.bean.CommonTokenHistory;
import com.mvc.dyvault.common.bean.vo.TickerVO;
import com.mvc.dyvault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface CommonTokenHistoryMapper extends MyMapper<CommonTokenHistory> {

}