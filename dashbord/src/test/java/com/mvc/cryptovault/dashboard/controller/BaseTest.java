package com.mvc.dyvault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mvc.dyvault.common.bean.dto.UserDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TokenVO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author qiyichen
 * @create 2018/11/16 14:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

    protected final static String host = "http://localhost:10083";
    protected final static Integer SEARCH_DIRECTION_UP = 0;
    protected final static Integer SEARCH_DIRECTION_DOWN = 1;

    @Autowired
    public WebApplicationContext wac;
    public MockMvc mockMvc;
    //性能测试时开启
//    @Rule
//    public ContiPerfRule rule = new ContiPerfRule();

    private static TokenVO vo = null;

    @Before
    public void setup() {
        //让每个测试用例启动之前都构建这样一个启动项
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    public <T> T parseObject(MvcResult result, Class<T> clazz) throws Exception {
        Result resultObj = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        String resultData = JSON.toJSONString(resultObj.getData());
        T data = JSON.parseObject(resultData, clazz);
        return data;
    }

    public <T> T parseObject(MvcResult result, TypeReference<T> type) throws Exception {
        Result resultObj = JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        String resultData = JSON.toJSONString(resultObj.getData());
        T data = JSON.parseObject(resultData, type);
        return data;
    }

    public TokenVO getToken() throws Exception {
        if (vo == null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setPassword("admin");
            userDTO.setUsername("admin");
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/admin/login")
                    .content(JSON.toJSONString(userDTO))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.userId").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.refreshToken").exists())
                    .andDo(print())
                    .andReturn();
            vo = parseObject(result, TokenVO.class);
        }
        return vo;
    }

    protected void addNullActionTest(ResultActions action, String str, Class clazz, String... ignoreFields) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName()) || Arrays.asList(ignoreFields).contains(field.getName())) {
                continue;
            }
            String jsonPath = str + "." + field.getName();
            action.andExpect(MockMvcResultMatchers.jsonPath(jsonPath).exists());
        }
    }
}
