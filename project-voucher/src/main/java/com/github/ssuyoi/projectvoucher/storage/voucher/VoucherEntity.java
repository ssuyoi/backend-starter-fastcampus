package com.github.ssuyoi.projectvoucher.storage.voucher;

import com.github.ssuyoi.projectvoucher.common.type.VoucherStatusType;
import com.github.ssuyoi.projectvoucher.storage.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "voucher")
@Entity
public class VoucherEntity extends BaseEntity {

    private String code;
    private VoucherStatusType status;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long amount;

    public VoucherEntity() {
    }

    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, Long amount) {
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;
    }


}
