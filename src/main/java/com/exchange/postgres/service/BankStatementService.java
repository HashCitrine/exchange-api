package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.BankStatement;
import com.exchange.postgres.repository.BankStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankStatementService {

    private final BankStatementRepository bankStatementRepository;

    public Long getNextId() {
        return bankStatementRepository.getNextId();
    }

    public void insertBankStatement(BankStatement bankStatement, Long id, Constants.STATUS status) {
        bankStatement.setTransactionId(id);
        bankStatementRepository.insertBankStatement(bankStatement, bankStatement.getTransactionType().toString(), status.toString());
    }

    public void updateBankStatement(BankStatement bankStatement, Long id, Constants.STATUS status) {
        bankStatement.setTransactionId(id);
        bankStatementRepository.updateBankStatement(bankStatement, status.toString());
    }

    public boolean checkDuplicateId(Long id) {
        boolean result = false;

        // id가 0이 아닌 경우 = 시퀸스를 통해 값을 받아옴 → 중복 체크 필요
        if(id != 0L && bankStatementRepository.checkDuplicateId(id) != 0) {
            // 해당 id로 생성된 행이 없는 경우 = insert
            result = true;
        }

        return result;
    }
}
