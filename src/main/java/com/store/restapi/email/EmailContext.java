package com.store.restapi.email;

import lombok.Data;

import java.util.Map;

@Data
public class EmailContext {
    private String from; // адрес откуда отправляем
    private String to; // адрес куда отправляем
    private String subject; // Тема письма - заголовок
    private String templateLocation; // Шаблон
    private Map<String, Object> templateContext;  // Переменные в шаблоне
}
