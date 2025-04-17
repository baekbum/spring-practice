package com.example.demo.web.item.service;

import com.example.demo.web.item.dto.InsertItemParam;
import com.example.demo.web.item.dto.ItemCondition;
import com.example.demo.web.item.dto.ItemDto;
import com.example.demo.web.item.dto.UpdateItemParam;
import com.example.demo.web.item.repository.SpringJpaItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final SpringJpaItemRepository repository;

    public ItemDto addItem(InsertItemParam param) {
        return new ItemDto(repository.addItem(param));
    }

    public ItemDto findItem(long id) {
        return new ItemDto(repository.findItem(id));
    }

    public List<ItemDto> findItems(ItemCondition condition) {
        return repository.findItems(condition)
                .stream()
                .map(ItemDto::new)
                .toList();
    }

    public ItemDto updateItem(long id, UpdateItemParam param) {
        return new ItemDto(repository.updateItem(id, param));
    }

    public ItemDto deleteItem(long id) {
        return new ItemDto(repository.deleteItem(id));
    }
}
