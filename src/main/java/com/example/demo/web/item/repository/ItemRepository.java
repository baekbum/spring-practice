package com.example.demo.web.item.repository;

import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.ItemCondition;
import com.example.demo.web.item.dto.UpdateItemParam;
import com.example.demo.web.item.entity.Item;

import java.util.List;

public interface ItemRepository {

    Item addItem(InsertItemParam param);

    Item findItem(long id);

    List<Item> findItems(ItemCondition condition);

    Item updateItem(long id, UpdateItemParam param);

    Item deleteItem(long id);
}
