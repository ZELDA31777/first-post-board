package com.board.first;

import com.board.first.data.ErrorCode;
import com.board.first.exception.BoardAppException;

import java.util.Scanner;

public class RequestUtils {

    public static int getIntParameterFromRequest(Request request, String paramName) {
        requireParam(request, paramName);
        String accountIdString = request.getParamMap().get(paramName);
        if (accountIdString == null || accountIdString.isBlank()) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
        }
        try {
            return Integer.parseInt(accountIdString);
        } catch (NumberFormatException e) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public static String parameterForInput(Scanner sc, String fieldName) {
        System.out.print(fieldName + ": ");
        return sc.nextLine().trim();
    }

    public static void requireParam(Request request, String paramName) {
        if (!request.getParamMap().containsKey(paramName)) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER, paramName + " 파라미터를 입력해주세요,");
        }
    }
}
