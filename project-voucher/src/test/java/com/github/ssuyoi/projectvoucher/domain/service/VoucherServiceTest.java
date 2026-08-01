package com.github.ssuyoi.projectvoucher.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ssuyoi.projectvoucher.common.type.VoucherStatusType;
import com.github.ssuyoi.projectvoucher.storage.voucher.VoucherEntity;
import com.github.ssuyoi.projectvoucher.storage.voucher.VoucherRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherServiceTest {

    @Autowired private VoucherService voucherService;
    @Autowired private VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 code로 조회할 수 있다.")
    @Test
    public void getVoucherByCode() {
        //given
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final Long amount = 10000L;

        final String code = voucherService.publish(validFrom, validTo, amount);

        //when
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
    }
}