package com.aer.es.vo;

import com.aer.es.entity.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemVO extends Item {
    private String highlight;
}
