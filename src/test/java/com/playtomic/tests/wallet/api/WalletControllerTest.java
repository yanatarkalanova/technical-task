package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.dto.RechargeRequestDto;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    private static final String CREDIT_CARD = "4242 4242 4242 4242";
    private static final String BASE_URL = "/wallets";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WalletService walletService;

    @Test
    public void test_getAllWalletsOk() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void test_getSpecificWallet() throws Exception {
        this.mockMvc.perform(post(BASE_URL + "/1/")).andReturn();
    }

    @Test
    public void test_rechargeWalletBadRequest() throws Exception {
        String INVALID_CREDIT_CARD = "fknoedsf230rfj23j-23jo";
        RechargeRequestDto rechargeRequestDto = new RechargeRequestDto(1L, BigDecimal.valueOf(20), INVALID_CREDIT_CARD);

        MvcResult result = this.mockMvc.perform(patch(BASE_URL + "/recharge")
                        .content(objectMapper.writeValueAsString(rechargeRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Please enter valid card number."));
    }

    @Test
    public void test_rechargeWalletMethodNotAllowed() throws Exception {
        RechargeRequestDto rechargeRequestDto = new RechargeRequestDto(1L, BigDecimal.valueOf(20), CREDIT_CARD);

        this.mockMvc.perform(post(BASE_URL + "/recharge")
                        .content(objectMapper.writeValueAsString(rechargeRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void test_rechargeWalletOk() throws Exception {
        RechargeRequestDto rechargeRequestDto = new RechargeRequestDto(1L, BigDecimal.valueOf(20), CREDIT_CARD);

        this.mockMvc.perform(patch(BASE_URL + "/recharge")
                        .content(objectMapper.writeValueAsString(rechargeRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_rechargeWalletWithZeroAmount() throws Exception {
        RechargeRequestDto rechargeRequestDto = new RechargeRequestDto(1L, BigDecimal.valueOf(0), CREDIT_CARD);

        MvcResult result = this.mockMvc.perform(patch(BASE_URL + "/recharge")
                        .content(objectMapper.writeValueAsString(rechargeRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Please recharge your wallet with amount bigger than 0."));
    }

}