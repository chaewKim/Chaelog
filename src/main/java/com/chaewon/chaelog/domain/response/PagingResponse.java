package com.chaewon.chaelog.domain.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

//페이지 정보, 사이즈, 전체 개수, listItem
@Getter
public class PagingResponse<T>{
    private final long totalCount;
    private final long page;
    private final long size;

    private final List<T> items; //페이지 적용된 응답객체

    public PagingResponse(Page<?> page, Class<T> clazz) { //Page화 된 엔티티(Page<?>) 객체를 전달받아 특정 응답 클래스로 변환
        this.totalCount = page.getTotalElements(); //전체 데이터 개수
        this.page = page.getNumber() + 1 ; //현재 페이지 번호
        this.size = page.getSize(); //한 페이지에 들어가는 데이터 개수
        this.items = page.getContent().stream() //페이지 안의 데이터를 리스트 형태로 가져옴
                .map(content -> {
                    try {
                        return clazz.getConstructor(content.getClass()).newInstance(content);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
