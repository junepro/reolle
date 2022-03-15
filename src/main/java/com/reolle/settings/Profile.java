package com.reolle.settings;

import com.reolle.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
//@NoArgsConstructor //빈생성자가 있을경우 널포인트 인셉션 발생 가능성
public class Profile {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String url;

    @Length(max = 50)
    private String occupation;

    @Length(max = 50)
    private String location;

    private String profileImage;

//    public Profile(Account account) {
//
//        this.bio = account.getBio();
//        this.url = account.getUrl();
//        this.occupation = account.getOccupation();
//        this.location = account.getLocation();
//        this.profileImage = account.getProfileImage();
//    }

}
