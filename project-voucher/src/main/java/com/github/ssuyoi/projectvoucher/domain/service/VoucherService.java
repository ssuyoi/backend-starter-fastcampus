package com.github.ssuyoi.projectvoucher.domain.service;

import com.github.ssuyoi.projectvoucher.common.type.VoucherStatusType;
import com.github.ssuyoi.projectvoucher.storage.voucher.VoucherEntity;
import com.github.ssuyoi.projectvoucher.storage.voucher.VoucherRepository;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // 상품권 발권
    @Transactional
    public String publish(final LocalDate validFrom, final LocalDate validTo, final Long amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount);

        return voucherRepository.save(voucherEntity).getCode();
    }

    // 상품권 취소


    // 상품권 사용
}
