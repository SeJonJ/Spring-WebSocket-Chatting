package webChat.dto.game;

import lombok.*;
import webChat.dto.User;

/**
 * catchMind 게임 유저 정보를 저장하기 위한 map
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatchMindUser extends User {
    private int score;
    private int winCount;
    private boolean isWiner;

    CatchMindUser(String userId, String nickName) {
        super(userId, nickName);
    }
}