package com.enjoyhome.entity;

import com.enjoyhome.base.BaseEntity;
import lombok.Data;

@Data
public class NursingElder extends BaseEntity {

    /**
     *  护理员ID
     */
    private Long nursingId;

    /**
     * 老人ID
     */
    private Long elderId;
}