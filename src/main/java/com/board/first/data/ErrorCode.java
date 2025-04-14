package com.board.first.data;

public enum ErrorCode {

    // For Board
    BOARD_NOT_FOUND("게시판을 찾을 수 없습니다."),
    BOARD_VALIDATION_FAILED("게시판 이름을 입력해주세요."),

    // For Post
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    POSTS_NOT_FOUND("게시판에 게시글이 존재하지 않습니다."),
    POST_VALIDATION_FAILED("게시글 제목과 내용을 모두 입력해주세요."),

    // For Account
    ACCOUNT_NOT_FOUND("해당하는 계정을 찾을 수 없습니다."),
    ACCOUNT_DUPLICATED("이미 사용중인 아이디입니다."),
    ACCOUNT_UNAUTHORIZED("해당 계정으로 로그인 해주세요."),
    ACCOUNT_ADMIN_ONLY("해당 기능은 관리자만 이용할 수 있습니다."),
    ACCOUNT_NEED_LOGOUT("다른 아이디로 로그인 시에는 로그아웃 후에 로그인 해주세요."),

    // For Common
    INVALID_PARAMETER("잘못된 요청 파라미터입니다."),
    AUTHENTICATION_FAILED("인증에 실패했습니다."),
    ALREADY_LOGGED_OUT("이미 로그아웃된 상태입니다."),

    // For command
    COMMAND_NOT_URL("URL을 입력해주세요."),
    COMMAND_INVALID_URL("URL은 '/'로 시작해야 합니다."),
    COMMAND_INVALID_FORM("URL은 최소한 '/카테고리/기능'의 형태를 갖춰야 합니다."),
    COMMAND_NOT_FOUND_FUNCTION("존재하지 않는 기능의 URL입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
