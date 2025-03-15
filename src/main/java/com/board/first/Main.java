package com.board.first;

import com.board.first.exception.*;
import com.board.first.exception.board.*;
import com.board.first.exception.command.CommandValidationException;
import com.board.first.exception.command.InvalidCommandException;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    private static final List<Post> posts = new ArrayList<>();
    private static final List<Board> boards = new ArrayList<>();
    private static int postId = 1;
    private static int boardId = 1;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.print("a");
                String input = scanner.nextLine().trim();

                if (input.equals("종료")) {
                    System.out.println("프로그램이 종료됩니다.");
                    return;
                }

                if (input.isEmpty() || input.charAt(0) != '/') {
                    throw new CommandValidationException("잘못된 URL이 입력되었습니다.");
                }

                String[] urls = input.split("/", 3);
                String category = urls[1];
                String[] str = urls[2].split("\\?", 2);
                String function = str[0];

                // Key값의 중복이 허용되지 않는 Map을 이용.
                Map<String, String> paramMap = new HashMap<>();
                if (str.length >= 2 && !str[1].isEmpty()) {
                    if (str[1].contains("&")) {
                        String[] params = str[1].split("&");
                        for (String param : params) {
                            String[] keyValue = param.split("=", 2);
                            if (keyValue.length == 2) {
                                paramMap.put(keyValue[0], keyValue[1]);
                            }
                        }
                    } else {
                        String[] keyValue = str[1].split("=", 2);
                        if (keyValue.length == 2) {
                            paramMap.put(keyValue[0], keyValue[1]);
                        }
                    }
                }
                switch (category) {
                    case "boards":
                        boardsFunctions(function, paramMap);
                        break;
                    case "posts":
                        postsFunctions(function, paramMap);
                        break;
                }
            }
        } catch (BoardAppException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void postsFunctions(String function, Map<String, String> paramMap){
        switch (function) {
            case "add":
                insertPost(paramMap);
                break;
            case "view":
                displayPostByPostId(paramMap);
                break;
            case "remove":
                deletePostByPostId(paramMap);
                break;
            case "edit":
                updatePostByPostId(paramMap);
                break;
            default:
                throw new InvalidCommandException(function);
        }
    }

    private static void boardsFunctions(String function, Map<String, String> paramMap){
        switch (function) {
            case "add":
                insertBoard();
                break;
            case "edit":
                updateBoardByBoardId(paramMap);
                break;
            case "remove":
                deleteBoardByBoardId(paramMap);
                break;
            case "view":
                displayPostsByBoardName(paramMap);
                break;
            default:
                throw new InvalidCommandException(function);
        }
    }



    // 게시글 작성
    private static void insertPost(Map<String, String> paramMap) {
        try {
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
            boolean boardExistFlag = false;
            for (Board board : boards) {
                if (board.getBoardId() == boardId) {
                    boardExistFlag = true;
                    break;
                }
            }
            if (!boardExistFlag) {
                throw new BoardNotFoundException(boardId);
            }
            System.out.print("제목: ");
            String title = scanner.nextLine();
            System.out.print("내용: ");
            String content = scanner.nextLine();
            Post post = new Post(postId++, title, content, boardId);
            posts.add(post);
            System.out.println("게시글이 작성되었습니다.");
        } catch (BoardValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시글 목록
    private static void displayPostsByBoardName(Map<String, String> paramMap) {
        try {
            if (posts.isEmpty()) {
                System.out.println("등록된 게시글이 없습니다.");
                return;
            }
            if (!paramMap.containsKey("boardName")) {
                throw new BoardValidationException("boardId 파라미터를 입력해주세요.");
            }
            String boardName = paramMap.get("boardName");
            int boardId = 0;
            boolean boardExistFlag = false;
            for (Board board : boards) {
                if (board.getBoardName().equals(boardName)) {
                    boardId = board.getBoardId();
                    boardExistFlag = true;
                    break;
                }
            }
            if (!boardExistFlag) {
                throw new BoardNotFoundException(boardName);
            }
            System.out.println("글 번호\t/\t글 제목\t/\t작성일");
            for (Post post : posts) {
                if (post.getBoardId() == boardId) {
                    System.out.println(post.getPostId() + "\t/\t" + post.getPostTitle() + "\t/\t" + post.getCreateTime());
                }
            }
        } catch (BoardValidationException e){
            System.out.println(e.getMessage());
        }
    }

    // 게시물 수정 (index)
    @Deprecated
    private static void updatePostByIndex() {
        System.out.print("어떤 게시물을 수정할까요?");
        Integer indexingNumber = getPostIndexFromUser();
        if (indexingNumber == null) return;

        Post updatedPost = posts.get(indexingNumber);
        System.out.printf("%d번 게시물을 수정합니다.\n", indexingNumber + 1);
        System.out.print("제목: ");
        String title = scanner.nextLine();
        updatedPost.setPostTitle(title);
        System.out.print("내용: ");
        String content = scanner.nextLine();
        updatedPost.setPostContent(content);
        updatedPost.setUpdateTime(LocalDateTime.now());
        System.out.printf("%d번 게시물이 성공적으로 수정되었습니다!\n", indexingNumber + 1);
    }

    // 게시글 수정 (postId)
    private static void updatePostByPostId(Map<String, String>paramMap) {
        try {
            if (posts.isEmpty()) {
                System.out.println("등록된 게시글이 없습니다.");
                return;
            }
            if (!paramMap.containsKey("postId")) {
                throw new PostValidationException("postId 파라미터를 입력해주세요.");
            }
            String postIdString = paramMap.get("postId");
            int postId;
            try {
                postId = Integer.parseInt(paramMap.get("postId"));
            } catch (NumberFormatException e) {
                throw new InvalidPostIdException(paramMap.get("postId"));
            }

            boolean postUpdatedFlag = false;
            for (Post post : posts) {
                if (post.getPostId() == postId){

                    System.out.print("제목: ");
                    String postTitle = scanner.nextLine();
                    System.out.print("내용: ");
                    String postContent = scanner.nextLine();

                    post.setPostTitle(postTitle);
                    post.setPostContent(postContent);
                    post.setUpdateTime(LocalDateTime.now());
                    postUpdatedFlag = true;
                    break;
                }
            }
            if (!postUpdatedFlag) {
                throw new PostNotFoundException(postId);
            }
            System.out.printf("%d번 게시물이 성공적으로 수정되었습니다!\n", postId);
        } catch (PostValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시물 조회 (index)
    @Deprecated
    private static void displayPostByIndex() {
        System.out.print("어떤 게시물을 조회할까요?");
        Integer indexingNumber = getPostIndexFromUser();
        if (indexingNumber == null) return;
        System.out.printf("\n%d번 게시물\n", indexingNumber + 1);

        System.out.println("제목: " + posts.get(indexingNumber).getPostTitle());
        System.out.println("내용: " + posts.get(indexingNumber).getPostContent());
    }

    // 게시물 삭제 (index)
    @Deprecated
    private static void deletePostByIndex() {
        if (posts.isEmpty()){
            System.out.println("등록된 게시글이 없습니다.");
            return;
        }
        System.out.print("어떤 게시물을 삭제할까요?");
        Integer indexingNumber = getPostIndexFromUser();
        if (indexingNumber == null) return;
        posts.remove((int)indexingNumber);
        System.out.printf("%d번 게시물이 성공적으로 삭제되었습니다.\n", indexingNumber + 1);
    }

    // 게시물 삭제 (postId)
    private static void deletePostByPostId(Map<String, String> paramMap) {
        try {
            if (posts.isEmpty()){
                System.out.println("등록된 게시글이 없습니다.");
                return;
            }
            if (!paramMap.containsKey("postId")){
                throw new PostValidationException("postId 파라미터를 입력해주세요.");
            }
            String postIdString = paramMap.get("postId");
            int postId;
            try {
                postId = Integer.parseInt(postIdString);
            } catch (NumberFormatException e){
                throw new InvalidPostIdException(postIdString, e);
            }
            boolean postDeletedFlag = false;
            for (Post post : posts){
                if (post.getPostId() == postId){
                    posts.remove(post);
                    postDeletedFlag = true;
                    break;
                }
            }
            if (!postDeletedFlag){
                throw new PostNotFoundException(postId);
            }
            System.out.printf("%d번 게시물이 성공적으로 삭제되었습니다.\n", postId);
        } catch (PostValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Deprecated
    private static Integer getPostIndexFromUser() {
        String input = scanner.nextLine();
        String numberStr = input.replaceAll("\\D", "");
        // NumberFormatException을 처리하기 위함
        if(numberStr.isEmpty()){
            System.out.println("숫자를 입력해주세요.");
            return null;
        }
        int number = Integer.parseInt(numberStr);
        if (number < 1 || number > posts.size()){
            System.out.printf("\n%d번 게시물은 존재하지 않습니다.\n", number);
            return null;
        }
        return number - 1;
    }

    // postId로 조회하는 것이 아니라 'n번째 게시글'을 조회하는 인식.
    private static Post getPostByIndex(int number) {
        return posts.get(number);
    }

    // 추후에 PostId로 조회하는 작업이 필요할 수 있기에 미리 작성해둠.
    private static void displayPostByPostId(Map<String, String> paramMap){
        try {
            if (posts.isEmpty()){
                System.out.println("등록된 게시글이 없습니다.");
                return;
            }
            if (!paramMap.containsKey("postId")){
                throw new PostValidationException("postId 파라미터를 입력해주세요.");
            }
            String postIdString = paramMap.get("postId");
            int postId;
            try {
                postId = Integer.parseInt(paramMap.get("postId"));
            } catch (NumberFormatException e) {
                throw new InvalidPostIdException(postIdString, e);
            }
            boolean postDisplayedFlag = false;
            for(Post post : posts){
                if(post.getPostId() == postId){
                    System.out.printf("\n%d번 게시물\n", postId);
                    System.out.println("작성일 : " + post.getCreateTime());
                    System.out.println("수정일 : " + post.getUpdateTime());
                    System.out.println("제목 : " + post.getPostTitle());
                    System.out.println("내용 : " + post.getPostContent());
                    postDisplayedFlag = true;
                    break;
                }
            }
            if (!postDisplayedFlag){
                throw new PostNotFoundException(postId);
            }
        } catch (PostValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시판 작성
    private static void insertBoard() {
        System.out.print("게시판 제목: ");
        // 앞뒤 공백 제거.
        String boardName = scanner.nextLine().trim();
        Board board = new Board(boardId++, boardName);
        boards.add(board);
        System.out.println("게시판이 작성되었습니다.");
    }

    // 게시판 수정 (boardId)
    private static void updateBoardByBoardId(Map<String, String> paramMap) {
        try {
            if(!paramMap.containsKey("boardId")){
                // Business Logic내에서 완결되는 예외이기에 cause를 포함하지 않음.
                throw new BoardValidationException("boardId 파라미터를 입력해주세요.");
            }
            String boardIdString = paramMap.get("boardId");
            int boardId;
            try {
                boardId = Integer.parseInt(boardIdString);
            } catch (NumberFormatException e){
                // RuntimeException의 하위 예외를 포함하고 있기 때문에, cause를 포함.
                throw new InvalidBoardIdException(boardIdString, e);
            }
            boolean boardUpdatedFlag = false;
            for(Board board : boards) {
                if (board.getBoardId() == boardId) {
                    System.out.print("게시판 제목: ");
                    String title = scanner.nextLine();
                    board.setBoardName(title);
                    board.setUpdateTime(LocalDateTime.now());
                    boardUpdatedFlag = true;
                    break;
                }
            }
            if(!boardUpdatedFlag){
                throw new BoardNotFoundException(boardId);
            }
            System.out.printf("%d번 게시판이 성공적으로 수정되었습니다!\n", boardId);
        } catch (BoardValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // 게시판 삭제 (boardId)
    private static void deleteBoardByBoardId(Map<String, String> paramMap) {
        try {
            if(!paramMap.containsKey("boardId")){
                throw new BoardValidationException("boardId 파라미터를 입력해주세요.");
            }
            String boardIdString = paramMap.get("boardId");
            int boardId;
            try {
                boardId = Integer.parseInt(boardIdString);
            } catch (NumberFormatException e){
                throw new InvalidBoardIdException(boardIdString, e);
            }
            boolean boardDeletedFlag = false;
            for (Board board : boards){
                if (board.getBoardId() == boardId){
                    boards.remove(board);
                    boardDeletedFlag = true;
                    break;
                }
            }
            if(!boardDeletedFlag){
                throw new BoardNotFoundException(boardId);
            }
            System.out.printf("%d번 게시판이 성공적으로 삭제되었습니다!\n", boardId);
        } catch (BoardValidationException e){
            System.out.println(e.getMessage());
        }
    }
}