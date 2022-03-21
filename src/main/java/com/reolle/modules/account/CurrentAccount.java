package com.reolle.modules.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  //어느 시점까지 어노테이션 메모리 가져갈지 설정
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this=='anonymousUser'? null : account")
public @interface CurrentAccount {
}
