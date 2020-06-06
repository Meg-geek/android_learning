package com.nsu.db.aircraft.api.model.accounting;

import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.product.Product;
import com.nsu.db.aircraft.api.model.tests.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accounting {
    private Product product;
    private Stage stage;
    private Site site;
    private Test test;
    private long beginTime;
    private long endTime;
}
