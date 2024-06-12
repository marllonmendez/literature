package com.marllonmendez.literature.repository;

public interface IConvertDataRepository {
    <T> T getData(String json, Class<T> dataClass);
}
