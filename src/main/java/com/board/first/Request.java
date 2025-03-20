package com.board.first;

import com.board.first.exception.command.CommandValidationException;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String category;
    private String function;
    private Map<String, String> paramMap;
    private Account currentAccount = null;

    public Request() {
    }

    public void updateUrl(String input) {
        if (input == null || input.isBlank()) {
            throw new CommandValidationException("URL을 입력해주세요.");
        }

        if (!input.startsWith("/")) {
            throw new CommandValidationException("URL은 '/'로 시작해야 합니다");
        }
        // 2. 경로(Path)와 쿼리(Query) 분리
        String[] urlParts = input.split("\\?", 2);
        String pathSection = urlParts[0];
        String querySection = urlParts.length > 1 ? urlParts[1] : "";

        // 3. 경로 파싱 (최소 2계층)
        String[] pathSegments = pathSection.substring(1).split("/");
        if (pathSegments.length < 2) {
            throw new CommandValidationException("URL은 최소한 '/카테고리/기능'의 형태를 갖춰야 합니다.");
        }
        this.category = pathSegments[0];
        this.function = pathSegments[1];

        if (!querySection.isEmpty()) {
            String[] params = querySection.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                String key = keyValue[0].trim();
                String value = keyValue.length > 1 ? keyValue[1].trim() : "";
                paramMap.put(key, value);
            }
        }
    }

    public String getCategory() {
        return category;
    }

    public String getFunction() {
        return function;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }
}
