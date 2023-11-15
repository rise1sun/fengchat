package com.feng.fengchat.common.websocket.domain.vo.resp;

import com.feng.fengchat.common.websocket.domain.vo.enums.WSRespTypeEnum;
import lombok.Data;

/**
 *
 * @author jiangfeng
 * @date 2023/11/10
 */
@Data
public class WSBaseResp<T> {
    /**
     * @see WSRespTypeEnum
     */
    private Integer type;
    private T date;
}
