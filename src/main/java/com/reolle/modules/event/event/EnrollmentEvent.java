package com.reolle.modules.event.event;

import com.reolle.modules.event.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {  //abstract 자식 클래스에서 반드시 오버라이딩해야만 사용할 수 있는 메소드를 의미합니다.
                                         // 자바에서 추상 메소드를 선언하여 사용하는 목적은 추상 메소드가
                                         // 포함된 클래스를 상속받는 자식 클래스가 반드시 추상 메소드를 구현하도록 하기 위함입니다.
    protected final Enrollment enrollment;

    protected final String message;

}
