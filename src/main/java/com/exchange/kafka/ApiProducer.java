package com.exchange.kafka;

import com.exchange.Constants;
import com.exchange.postgres.entity.BankStatement;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.service.BankStatementService;
import com.exchange.postgres.service.MemberService;
import com.exchange.postgres.service.OrderService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.bcel.Const;
import org.hibernate.type.OrderedSetType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApiProducer {

    private final MemberService memberService;
    private final OrderService orderService;
    private final BankStatementService bankStatementService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // kafka를 통해 실제 서비스 로직이 진행되는 서버로 요청 전달
    private void sendKafka(String token, Long requestId, String topic) {
        Gson reqGsonObj = new Gson();

        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("token", token);
        inputMap.put("requestId", requestId);

        String jsonStr = reqGsonObj.toJson(inputMap);
        log.debug("[ApiProducer] jsonStr {}", jsonStr);

        this.kafkaTemplate.send(topic, jsonStr);
    }

    public String normalProcess(Object obj, Constants.TOPIC topic, String token) {
        StringBuffer returnMsg = new StringBuffer();

        String objName = "logic";
        Long id = 0L;
        Constants.STATUS status;

        // 서비스 요청
        try {
            status = Constants.STATUS.REQS;
            
            // topic 명으로 요청된 서비스 구분
            switch(topic) {
                case reqDw:
                    log.info("{}", obj.toString());
                    objName = "bank statement";
                    id = bankStatementService.getNextId();
                    bankStatementService.insertBankStatement((BankStatement) obj, id, status);
                    break;

                case reqOrder:
                    objName = "order";
                    id = orderService.getNextId();
                    orderService.insertOrder((Order) obj, id, status);
                    break;

                default:
                    break;
            }

            // 메시지 대기열 전송
//            sendKafka(token, id, Constants.TOPIC.reqDw.toString());
            returnMsg.append(objName).append(" Request success");

        } catch(Exception e) {
            // 오류 발생으로 인해 서비스 요청 실패시
            status = Constants.STATUS.REQF;
            log.info("error '{}' request (request : {})", objName, obj.toString());

            // 각종 오류로 인해 정상적으로 DB에 저장되지 않았거나 kafka로 전송되지 못한 경우
            // '요청실패' 상태로 처리 (id 중복체크 실시하여 insert/update 판단)
            try{
                switch(topic) {
                    case reqDw:
                        if(bankStatementService.checkDuplicateId(id)) {
                            bankStatementService.updateBankStatement((BankStatement) obj, id, status);
                        } else {
                            id = bankStatementService.getNextId();
                            bankStatementService.insertBankStatement((BankStatement) obj, id, status);
                        }
                        break;
                    case reqOrder:
                        if(orderService.checkDuplicateId(id)) {
                            orderService.updateOrder((Order) obj, id, status);
                        } else {
                            id = orderService.getNextId();
                            orderService.insertOrder((Order) obj, id, status);
                        }
                        break;

                    default:
                        break;
                }
            } catch (Exception f) {
                // 실패 결과 상태조차 DB에 반영하지 못한 경우 : 로그로 남김
                log.info("logic status update fail");
            }

            returnMsg.delete(0, returnMsg.length());
            returnMsg.append(objName).append(" Request Fail");
        }

        return returnMsg.toString();
    }
}
