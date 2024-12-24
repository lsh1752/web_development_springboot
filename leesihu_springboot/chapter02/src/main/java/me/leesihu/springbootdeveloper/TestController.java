package me.leesihu.springbootdeveloper;
/*
    혹시나 기능 구현을 했는데 적용되지 않을 경우

    1. 서버 껐다가 키기
    2. 그래도 안되면 ctrl + s 눌러서 세이브
    3. intellij를 껐다가 키기
    4. 서버 재실행
    5. 그러면 보통 완료

 */
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }
}

