package com.board.first;

import com.board.first.exception.*;
import com.board.first.exception.account.AccountStatusException;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.account.InvalidAccountIdException;
import com.board.first.exception.board.*;
import com.board.first.exception.command.CommandValidationException;
import com.board.first.exception.command.InvalidCommandException;
import com.board.first.exception.post.PostValidationException;
import com.board.first.service.*;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    // 다형성과 업캐스팅 -> 정말 기초적인 부분. 이걸 햇갈리다니 정말 창피하다.
    private static final AccountService accountService = new AccountServiceImpl();
    private static final PostService postService = new PostServiceImpl();
    private static final BoardService boardService = new BoardServiceImpl(postService);

    public static void main(String[] args) {
        Request request = new Request();
        while (true) {
            try {
                System.out.print("a");
                String input = scanner.nextLine().trim();

                if (input.equals("종료")) {
                    System.out.println("프로그램이 종료됩니다.");
                    return;
                }
                request.updateUrl(input);
                switch (request.getCategory()) {
                    case "boards":
                        boardsFunctions(request);
                        break;
                    case "posts":
                        postsFunctions(request);
                        break;
                    case "accounts":
                        accountsFunctions(request);
                        break;
                    default:
                        throw new InvalidCommandException(request.getCategory());
                }
            } catch (BoardAppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // TODO :
    private static void accountsFunctions(Request request) {
        String function = request.getFunction();
        try {
            switch (function) {
                case "signup":
                    createAccount();
                    break;
                case "signin":
                    loginAccount(request);
                    break;
                case "signout":
                    logoutAccount(request);
                    break;
                case "detail":
                    requireParam(request, "accountId");
                    displayAccount(request);
                    break;
                case "edit":
                    requireParam(request, "accountId");
                    editAccount(request);
                    break;
                case "remove":
                    requireParam(request, "accountId");
                    deleteAccount(request);
                    break;
                default:
                    throw new InvalidCommandException(function);
            }
        } catch (AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void postsFunctions(Request request){
        String function = request.getFunction();
        Map<String, String> paramMap = request.getParamMap();
        try {
            switch (function) {
                case "add":
                    requireParam(request, "boardId");
                    insertPost(request);
                    break;
                case "view":
                    requireParam(request, "postId");
                    displayPostByPostId(request);
                    break;
                case "remove":
                    requireParam(request, "postId");
                    deletePostByPostId(request);
                    break;
                case "edit":
                    requireParam(request, "postId");
                    updatePostByPostId(request);
                    break;
                default:
                    throw new InvalidCommandException(function);
            }
        } catch (PostValidationException | AccountValidationException | BoardValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void boardsFunctions(Request request){
        String function = request.getFunction();
        Map<String, String> paramMap = request.getParamMap();
        try {
            switch (function) {
                case "add":
                    insertBoard(request);
                    break;
                case "edit":
                    requireParam(request, "boardId");
                    updateBoardByBoardId(request);
                    break;
                case "remove":
                    requireParam(request, "boardId");
                    deleteBoardByBoardId(request);
                    break;
                case "view":
                    requireParam(request, "boardName");
                    displayPostsByBoardName(request);
                    break;
                default:
                    throw new InvalidCommandException(function);
            }
        } catch (BoardValidationException | AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void requireParam(Request request, String paramName) {
        if (!request.getParamMap().containsKey(paramName)) {
            throw new CommandValidationException(paramName + " 파라미터를 입력해주세요.");
        }
    }

    // 게시글 작성
    private static void insertPost(Request request) throws BoardValidationException {
        if (!request.getParamMap().containsKey("boardId")) {
            throw new BoardValidationException("boardId 파라미터를 입력해주세요.");
        }
        String boardIdString = request.getParamMap().get("boardId");
        int boardId;
        try {
            boardId = Integer.parseInt(boardIdString);
        } catch (NumberFormatException e){
            throw new InvalidBoardIdException(boardIdString, e);
        }
        try {
            boardService.getBoardByBoardId(boardId);
        } catch (BoardNotFoundException e) {
            throw new BoardNotFoundException(boardId);
        }
        System.out.print("제목: ");
        String postName = scanner.nextLine();
        System.out.print("내용: ");
        String postContent = scanner.nextLine();
        postService.createPost(boardId, postName, request.getCurrentAccount(), postContent);
        System.out.println("게시글이 작성되었습니다.");
    }

    // 게시글 목록
    // TODO : 게시글 목록 서비스로 분리 완료.
    private static void displayPostsByBoardName(Request request) {
        try {
            String boardName = request.getParamMap().get("boardName");
            int boardId = boardService.getBoardIdByBoardName(boardName);
            System.out.println("글 번호\t/\t글 제목\t/\t작성일");
            List<Post> posts = postService.getPostListByBoardId(boardId);
        } catch (BoardValidationException e){
            System.out.println(e.getMessage());
        }
    }

    // 게시글 수정 (postId)
    // TODO : 게시글 수정 서비스로 분리 완료.
    private static void updatePostByPostId(Request request) {
        try {
            if (!request.getParamMap().containsKey("postId")) {
                throw new PostValidationException("postId 파라미터를 입력해주세요.");
            }
            String postIdString = request.getParamMap().get("postId");
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
            postService.updatePost(postId, request.getCurrentAccount(), postName, postContent);
            System.out.printf("%d번 게시물이 성공적으로 수정되었습니다!\n", postId);
        } catch (PostValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시물 삭제 (postId)
    // TODO : 게시글 삭제 서비스 분리 중.
    private static void deletePostByPostId(Request request) {
        try {
            String postIdString = request.getParamMap().get("postId");
            int postId;
            try {
                postId = Integer.parseInt(postIdString);
            } catch (NumberFormatException e){
                throw new InvalidPostIdException(postIdString, e);
            }
            postService.deletePostByPostId(postId, request.getCurrentAccount());
            System.out.printf("%d번 게시물이 성공적으로 삭제되었습니다.\n", postId);
        } catch (PostValidationException | AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시글 출력
    private static void displayPostByPostId(Request request){
        try {
            String postIdString = request.getParamMap().get("postId");
            int postId;
            try {
                postId = Integer.parseInt(request.getParamMap().get("postId"));
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
    private static void insertBoard(Request request) {
        System.out.print("게시판 제목: ");
        String boardName = scanner.nextLine().trim();
        boardService.createBoard(boardName, request.getCurrentAccount());
        System.out.println("게시판이 작성되었습니다.");
    }

    // 게시판 수정 (boardId)
    // TODO : 게시판 수정 서비스 분리 완료.
    private static void updateBoardByBoardId(Request request) throws BoardValidationException, AccountValidationException {
        try {
            String boardIdString = request.getParamMap().get("boardId");
            int boardId;
            try {
                boardId = Integer.parseInt(boardIdString);
            } catch (NumberFormatException e){
                // RuntimeException의 하위 예외를 포함하고 있기 때문에, cause를 포함.
                throw new InvalidBoardIdException(boardIdString, e);
            }
            System.out.print("게시판 제목: ");
            String boardName = scanner.nextLine().trim();
            if (boardName.isEmpty()) {
                throw new BoardValidationException("게시판 제목을 입력해주세요.");
            }
            boardService.updateBoard(boardId, boardName, request.getCurrentAccount());
            System.out.printf("%d번 게시판이 성공적으로 수정되었습니다!\n", boardId);
        } catch (BoardValidationException | AccountValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시판 삭제 (boardId)
    // TODO : 게시판 삭제 서비스 분리 완료.
    private static void deleteBoardByBoardId(Request request) throws BoardValidationException, AccountValidationException {
        String boardIdString = request.getParamMap().get("boardId");
        int boardId;
        try {
            boardId = Integer.parseInt(boardIdString);
        } catch (NumberFormatException e){
            throw new InvalidBoardIdException(boardIdString, e);
        }
        boardService.deleteBoard(boardId, request.getCurrentAccount());
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
    private static void loginAccount(Request request) {
        if (request.getCurrentAccount() != null) {
            throw new AccountStatusException("다른 아이디로 로그인 시에는 로그아웃 후에 로그인 해주세요.");
        }
        System.out.print("계정: ");
        String userId = scanner.nextLine().trim();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine().trim();
        // 내부 로직을 서비스로 분리
        request.setCurrentAccount(accountService.signInAccount(userId, password));
        System.out.printf("%s의 로그인에 성공하였습니다!\n", request.getCurrentAccount().getUsername());
    }

    // TODO : 로그아웃 완료.
    private static void logoutAccount(Request request) {
        accountService.logoutAccount(request.getCurrentAccount());
    }

    // TODO : 계정 표시
    private static void displayAccount(Request request) throws AccountValidationException {
        String accountIdString = request.getParamMap().get("accountId");
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
    private static void editAccount(Request request) throws AccountValidationException {
        String accountIdString = request.getParamMap().get("accountId");
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
        accountService.updateAccount(accountId, password, email, request.getCurrentAccount());
    }

    // TODO : 계정 삭제
    private static void deleteAccount(Request request) throws AccountValidationException {
        String accountIdString = request.getParamMap().get("accountId");
        int accountId;
        try {
            accountId = Integer.parseInt(accountIdString);
        } catch (NumberFormatException e) {
            throw new InvalidAccountIdException(accountIdString);
        }
        if (request.getCurrentAccount() != null) {
            accountService.logoutAccount(request.getCurrentAccount());
        }
        String username = accountService.deleteAccount(accountId);
        System.out.printf("%s의 회원 탈퇴에 성공하였습니다!\n", username);
    }

}