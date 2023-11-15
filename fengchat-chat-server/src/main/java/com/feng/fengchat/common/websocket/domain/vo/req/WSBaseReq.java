package com.feng.fengchat.common.websocket.domain.vo.req;

import com.feng.fengchat.common.websocket.domain.vo.enums.WSReqTypeEnum;
import lombok.Data;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@Data
public class WSBaseReq {
    /**
     * @see WSReqTypeEnum
     */
    private Integer type;
    private String date;
}
