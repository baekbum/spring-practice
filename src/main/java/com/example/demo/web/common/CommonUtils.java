package com.example.demo.web.common;

import jakarta.persistence.TypedQuery;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtils {

    public static Function<BindingResult, Map<String, String>> getBindingResult() {
        return bindingResult -> bindingResult.getFieldErrors()
               .stream()
               .collect(Collectors.toMap(
                       fieldError -> fieldError.getField(),
                       fieldError -> fieldError.getDefaultMessage()
               ));
    }

    public static Consumer<Map<String, Object>> setQueryParam(TypedQuery<?> query) {
        return map -> {
            Set<String> keys = map.keySet();
            keys.forEach(key -> query.setParameter(key, map.get(key)));
        };
    }
}
