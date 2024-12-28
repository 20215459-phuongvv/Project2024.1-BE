package com.hust.project3.services.impl;

import com.hust.project3.configuration.VNPAYConfig;
import com.hust.project3.dtos.payment.PaymentDTO;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.Setting;
import com.hust.project3.enums.SettingKeyEnum;
import com.hust.project3.repositories.SettingRepository;
import com.hust.project3.services.PaymentService;
import com.hust.project3.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final VNPAYConfig vnPayConfig;
    private final SettingRepository settingRepository;
    public PaymentDTO.VNPayResponse createVnPayPayment(ReadingCardRequestDTO dto) {
        Setting setting = settingRepository.findByKey(
                (dto.getType() == 0) ? SettingKeyEnum.MONTHLY_CARD_PRICE.name() : SettingKeyEnum.YEARLY_CARD_PRICE.name()
        ).get();
        long amount = dto.getNumberOfPeriod() * Long.parseLong(setting.getValue()) * 100L;
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(null));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    @Override
    public PaymentDTO.VNPayResponse createVipVnPayPayment() {
        Setting setting = settingRepository.findByKey(
                SettingKeyEnum.UPGRADE_VIP_PRICE.name()
        ).get();
        long amount = Long.parseLong(setting.getValue()) * 100L;
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(null));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }
}
