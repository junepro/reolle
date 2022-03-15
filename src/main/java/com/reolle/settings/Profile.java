package com.reolle.settings;

import com.reolle.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //빈생성자가 있을경우 널포인트 인셉션 발생 가능성
public class Profile {

    private String bio;

    private String url;

    private String occupation;

    private String location;

    public Profile(Account account) {

        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }

}
