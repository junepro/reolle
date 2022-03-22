package com.reolle.modules.study;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.reolle.modules.account.QAccount;
import com.reolle.modules.tag.QTag;
import com.reolle.modules.zone.QZone;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    //상위클래스에 생성자가없어서 만들지 않으면 compile 오류발생
    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyword(String keyword) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                        .and(study.title.containsIgnoreCase(keyword)) //seq문자열중 search관련 문자열 포함되면 true 리턴
                        .or(study.tags.any().title.containsIgnoreCase(keyword))
                        .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                //n+1 select 문제 해결 하기위해
                .leftJoin(study.tags, QTag.tag).fetchJoin()
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                .leftJoin(study.members, QAccount.account).fetchJoin() //join한 데이터만가져오는것
                .distinct();

        return query.fetch();
    }
}
