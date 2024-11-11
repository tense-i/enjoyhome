package com.enjoyhome.vo;

import com.enjoyhome.entity.Applications;
import lombok.Data;

/**
 * @author tensei
 */
@Data
public class ApplicationsVo extends Applications {

    /**
     * 单据流程状态
     */
    private Integer flowStatus;
}
