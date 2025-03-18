package com.board.first;

import com.board.first.exception.*;
import com.board.first.exception.account.AccountStatusException;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.account.InvalidAccountIdException;
import com.board.first.exception.board.*;
import com.board.first.exception.command.CommandValidationException;
import com.board.first.exception.command.InvalidCommandException;
import com.board.first.exception.post.PostValidationException;
import com.board.first.service.AccountServiceImpl;
import com.board.first.service.BoardServiceImpl;
import com.board.first.service.PostServiceImpl;

import java.util.*;

public class Main {
    private static Account currentLoginAccount = null;
    private static final Scanner scanner = new Scanner(System.in);
    private static final AccountServiceImpl accountService = new AccountServiceImpl();
    private static final BoardServiceImpl boardService = new BoardServiceImpl();
    private static final PostServiceImpl postService = new PostServiceImpl();

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.print("a");
                String input = scanner.nextLine().trim();

                if (input.equals("종료")) {
                    System.out.println("프로그램이 종료됩니다.");
                    return;
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
                    throw new CommandValidationException(
                            "[오류] URL 형식: /카테고리/기능?파라미터"
                    );
                }
                String category = pathSegments[0];
                String function = pathSegments[1];

                Map<String, String> paramMap = new HashMap<>();
                if (!querySection.isEmpty()) {
                    String[] params = querySection.split("&");
                    for (String param : params) {
                        String[] keyValue = param.split("=", 2);
                        String key = keyValue[0].trim();
                        String value = keyValue.length > 1 ? keyValue[1].trim() : "";
                        paramMap.put(key, value);
                    }
                }

                switch (category) {
                    case "boards":
                        boardsFunctions(function, paramMap);
                        break;
                    case "posts":
                        postsFunctions(function, paramMap);
                        break;
                    case "accounts":
                        accountsFunctions(function, paramMap);
                }
            } catch (BoardAppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // TODO :
    private static void accountsFunctions(String function, Map<String, String> paramMap) {
        try {
            switch (function) {
                case "signup":
                    createAccount();
                    break;
                case "signin":
                    loginAccount();
                    break;
                case "signout":
                    logoutAccount();
                    break;
                case "detail":
                    requireParam(paramMap, "accountId");
                    displayAccount(paramMap);
                    break;
                case "edit":
                    requireParam(paramMap, "accountId");
                    editAccount(paramMap);
                    break;
                case "remove":
                    requireParam(paramMap, "accountId");
                    deleteAccount(paramMap);
                    break;
                default:
                    throw new InvalidCommandException(function);
            }
        } catch (AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void postsFunctions(String function, Map<String, String> paramMap){
        try {
            switch (function) {
                case "add":
                    requireParam(paramMap, "boardId");
                    insertPost(paramMap);
                    break;
                case "view":
                    requireParam(paramMap, "postId");
                    displayPostByPostId(paramMap);
                    break;
                case "remove":
                    requireParam(paramMap, "postId");
                    deletePostByPostId(paramMap);
                    break;
                case "edit":
                    requireParam(paramMap, "postId");
                    updatePostByPostId(paramMap);
                    break;
                default:
                    throw new InvalidCommandException(function);
            }
        } catch (PostValidationException | AccountValidationException | BoardValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void boardsFunctions(String function, Map<String, String> paramMap){
        try {
            switch (function) {
                case "add":
                    insertBoard();
                    break;
                case "edit":
                    requireParam(paramMap, "boardId");
                    updateBoardByBoardId(paramMap);
                    break;
                case "remove":
                    requireParam(paramMap, "boardId");
                    deleteBoardByBoardId(paramMap);
                    break;
                case "view":
                    requireParam(paramMap, "boardId");
                    displayPostsByBoardName(paramMap);
                    break;
                default:
                    throw new InvalidCommandException(function);
            }
        } catch (BoardValidationException | AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void requireParam(Map<String, String> paramMap, String paramName) {
        if (!paramMap.containsKey(paramName)) {
            throw new CommandValidationException(paramName + " 파라미터를 입력해주세요.");
        }
    }

    // 게시글 작성
    // TODO : 게시글 작성 서비스로 분리 완료. (BoardService 관련 고민 중)
    private static void insertPost(Map<String, String> paramMap) throws BoardValidationException {
        if (!paramMap.containsKey("boardId")) {
            throw new BoardValidationException("boardId 파라미터를 입력해주세요.");
        }
        String boardIdString = paramMap.get("boardId");
        int boardId;
        try {
            boardId = Integer.parseInt(boardIdString);
        } catch (NumberFormatException e){
            throw new InvalidBoardIdException(boardIdString, e);
        }
        try {
            boardService.getBoardByBoardId(boardId);
        } catch (BoardNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.print("제목: ");
        String postName = scanner.nextLine();
        System.out.print("내용: ");
        String postContent = scanner.nextLine();
        postService.createPost(boardId, postName, currentLoginAccount, postContent);
        System.out.println("게시글이 작성되었습니다.");
    }

    // 게시글 목록
    // TODO : 게시글 목록 서비스로 분리 완료.
    private static void displayPostsByBoardName(Map<String, String> paramMap) {
        try {
            String boardName = paramMap.get("boardName");
            int boardId = boardService.getBoardIdByBoardName(boardName);
            System.out.println("글 번호\t/\t글 제목\t/\t작성일");
            List<Post> posts = postService.getPostListByBoardId(boardId);
        } catch (BoardValidationException e){
            System.out.println(e.getMessage());
        }
    }

    // 게시글 수정 (postId)
    // TODO : 게시글 수정 서비스로 분리 완료.
    private static void updatePostByPostId(Map<String, String>paramMap) {
        try {
            if (!paramMap.containsKey("postId")) {
                throw new PostValidationException("postId 파라미터를 입력해주세요.");
            }
            String postIdString = paramMap.get("postId");
            int postId;
            try {
                postId = Integer.parseInt(postIdString);
            } catch (NumberFormatException e) {
                throw new InvalidPostIdException(postIdString);
            }
            System.out.print("제목: ");
            String postName = scanner.nextLine();
            System.out.print("내용: ");
            String postContent = scanner.nextLine();
            postService.updatePost(postId, currentLoginAccount, postName, postContent);
            System.out.printf("%d번 게시물이 성공적으로 수정되었습니다!\n", postId);
        } catch (PostValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시물 삭제 (postId)
    // TODO : 게시글 삭제 서비스 분리 중.
    private static void deletePostByPostId(Map<String, String> paramMap) {
        try {
            String postIdString = paramMap.get("postId");
            int postId;
            try {
                postId = Integer.parseInt(postIdString);
            } catch (NumberFormatException e){
                throw new InvalidPostIdException(postIdString, e);
            }
            postService.deletePostByPostId(postId, currentLoginAccount);
            System.out.printf("%d번 게시물이 성공적으로 삭제되었습니다.\n", postId);
        } catch (PostValidationException | AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시글 출력
    // TODO : 게시판 출력 서비스 분리 완료.
    private static void displayPostByPostId(Map<String, String> paramMap){
        try {
            String postIdString = paramMap.get("postId");
            int postId;
            try {
                postId = Integer.parseInt(paramMap.get("postId"));
            } catch (NumberFormatException e) {
                throw new InvalidPostIdException(postIdString, e);
            }
            Post post = postService.getPostByPostId(postId);
            System.out.printf("\n%d번 게시물\n", postId);
            System.out.println("작성일 : " + post.getCreateTime());
            System.out.println("수정일 : " + post.getUpdateTime());
            System.out.println("제목 : " + post.getPostTitle());
            System.out.println("내용 : " + post.getPostContent());
        } catch (PostValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시판 작성
    // TODO : 게시판 작성 서비스 분리 완료.
    private static void insertBoard() {
        System.out.print("게시판 제목: ");
        String boardName = scanner.nextLine().trim();
        boardService.createBoard(boardName, currentLoginAccount);
        System.out.println("게시판이 작성되었습니다.");
    }

    // 게시판 수정 (boardId)
    // TODO : 게시판 수정 서비스 분리 완료.
    private static void updateBoardByBoardId(Map<String, String> paramMap) throws BoardValidationException, AccountValidationException {
        try {
            String boardIdString = paramMap.get("boardId");
            int boardId;
            try {
                boardId = Integer.parseInt(boardIdString);
            } catch (NumberFormatException e){
                // RuntimeException의 하위 예외를 포함하고 있기 때문에, cause를 포함.
                throw new InvalidBoardIdException(boardIdString, e);
            }
            System.out.print("게시판 제목: ");
            String boardName = scanner.nextLine();
            if (boardName.isEmpty()) {
                throw new BoardValidationException("게시판 제목을 입력해주세요.");
            }
            boardService.updateBoard(boardId, boardName, currentLoginAccount);
            System.out.printf("%d번 게시판이 성공적으로 수정되었습니다!\n", boardId);
        } catch (BoardValidationException | AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시판 삭제 (boardId)
    // TODO : 게시판 삭제 서비스 분리 완료.
    private static void deleteBoardByBoardId(Map<String, String> paramMap) throws BoardValidationException, AccountValidationException {
        String boardIdString = paramMap.get("boardId");
        int boardId;
        try {
            boardId = Integer.parseInt(boardIdString);
        } catch (NumberFormatException e){
            throw new InvalidBoardIdException(boardIdString, e);
        }
        boardService.deleteBoard(boardId, currentLoginAccount);
        System.out.printf("%d번 게시판이 성공적으로 삭제되었습니다!\n", boardId);
    }

    // TODO : 회원 가입 완료
    private static void createAccount() {
            System.out.print("계정: ");
            String userId = scanner.nextLine().trim();
            System.out.print("비밀번호: ");
            String password = scanner.nextLine().trim();
            System.out.print("닉네임: ");
            String username = scanner.nextLine().trim();
            System.out.print("이메일: ");
            String email = scanner.nextLine().trim();
            // 내부 로직을 서비스로 분리
            accountService.signUpAccount(userId, password, username, email);
            System.out.println("회원 가입이 성공적으로 완료되었습니다.");
    }

    // TODO : 로그인 완료
    private static void loginAccount() {
        if (currentLoginAccount != null) {
            throw new AccountStatusException("다른 아이디로 로그인 시에는 로그아웃 후에 로그인 해주세요.");
        }
        System.out.print("계정: ");
        String userId = scanner.nextLine().trim();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine().trim();
        // 내부 로직을 서비스로 분리
        currentLoginAccount = accountService.signInAccount(userId, password);
        System.out.printf("%s의 로그인에 성공하였습니다!\n", currentLoginAccount.getUsername());
    }

    // TODO : 로그아웃 완료.
    private static void logoutAccount() {
        accountService.logoutAccount(currentLoginAccount);
    }

    // TODO : 계정 표시
    private static void displayAccount(Map<String, String> paramMap) throws AccountValidationException {
        String accountIdString = paramMap.get("accountId");
        int accountId;
        try {
            accountId = Integer.parseInt(accountIdString);
        } catch (NumberFormatException e) {
            throw new InvalidAccountIdException(accountIdString);
        }
        Account account = accountService.getAccountByAccountId(accountId);
        System.out.printf("%d번 회원\n", accountId);
        System.out.println("계정 : " + account.getUsername());
        System.out.println("이메일 : " + account.getEmail());
        System.out.println("가입일 : " + account.getCreateTime());
    }

    // TODO : 계정 수정
    private static void editAccount(Map<String, String> paramMap) throws AccountValidationException {
        String accountIdString = paramMap.get("accountId");
        int accountId;
        try {
            accountId = Integer.parseInt(accountIdString);
        } catch (NumberFormatException e) {
            throw new InvalidAccountIdException(accountIdString);
        }
        System.out.print("비밀번호: ");
        String password = scanner.nextLine().trim();
        System.out.print("이메일: ");
        String email = scanner.nextLine().trim();
        accountService.updateAccount(accountId, password, email, currentLoginAccount);
    }

    // TODO : 계정 삭제
    private static void deleteAccount(Map<String, String> paramMap) throws AccountValidationException {
        String accountIdString = paramMap.get("accountId");
        int accountId;
        try {
            accountId = Integer.parseInt(accountIdString);
        } catch (NumberFormatException e) {
            throw new InvalidAccountIdException(accountIdString);
        }
        if (currentLoginAccount != null) {
            accountService.logoutAccount(currentLoginAccount);
        }
        String username = accountService.deleteAccount(accountId);
        System.out.printf("%s의 회원 탈퇴에 성공하였습니다!\n", username);
    }

}