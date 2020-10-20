package com.baizhi.dao;

import com.baizhi.entity.Stock;

public interface StockDAO {


    Stock checkStock(Integer id);


    int updateSale(Stock stock);
}
