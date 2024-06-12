package com.marllonmendez.literature.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.marllonmendez.literature.repository.IConvertDataRepository;
import com.marllonmendez.literature.requestDTO.BookRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConvertData implements IConvertDataRepository {
    private final ObjectMapper mapper = new ObjectMapper();

    public ObjectMapper getMapper() {
        return mapper;
    }

    public <T> T getData(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookRequestDTO> getListData(String json) {
        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, BookRequestDTO.class);
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
