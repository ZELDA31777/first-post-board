package com.board.first;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Post> posts = new ArrayList<>();
    private static int postId = 1;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.print("명령어 > ");
            String input = scanner.nextLine();
            switch (input) {
                case "종료": System.out.print("프로그램이 종료됩니다."); return;
                case "작성": insertPost(); break;
                case "조회": displayPostByIndex(); break;
                case "삭제": deletePostByIndex(); break;
                case "수정": updatePostByIndex(); break;
                case "목록": displayAllPosts(); break;
                default: System.out.println("존재하지 않는 명령어 입니다.");
            }
        }
    }

    // 게시글 작성
    private static void insertPost() {
        System.out.print("제목: ");
        String title = scanner.nextLine();
        System.out.print("내용: ");
        String content = scanner.nextLine();
        Post post = new Post(postId++, title, content);
        posts.add(post);
        System.out.println("게시글이 작성되었습니다.");
    }

    // 게시글 목록
    private static void displayAllPosts() {
        if(posts.isEmpty()) {
            System.out.println("등록된 게시글이 없습니다.");
            return;
        }
        int postSize = posts.size();
        System.out.printf("총 게시글은 %d개 작성되어있습니다.\n", postSize);
        for(int i=0; i < postSize; i++){
            System.out.printf("\n%d번 게시글\n", (i + 1) );
            System.out.println("제목 : " +posts.get(i).getPostTitle());
            System.out.println("내용 : " +posts.get(i).getPostContent());
        }
    }

    // 게시물 수정 (index)
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

    // 게시물 조회 (index)
    private static void displayPostByIndex() {
        System.out.print("어떤 게시물을 조회할까요?");
        Integer indexingNumber = getPostIndexFromUser();
        if (indexingNumber == null) return;
        System.out.printf("\n%d번 게시물\n", indexingNumber + 1);

        System.out.println("제목: " + posts.get(indexingNumber).getPostTitle());
        System.out.println("내용: " + posts.get(indexingNumber).getPostContent());
    }

    // 게시물 삭제 (index)
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
    private static Post getPostByPostId(int id){
        for(Post post : posts){
            if(post.getPostId() == id){
                return post;
            }
        }
        return null;
    }

}