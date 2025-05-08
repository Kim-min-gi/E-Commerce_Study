package com.study.ecommerce;

import org.springframework.restdocs.payload.FieldDescriptor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class PageResponseFieldsSnippet {

    public static List<FieldDescriptor> getPageResponseFields(FieldDescriptor... contentDescriptor) {
        List<FieldDescriptor> fields = new ArrayList<>(Arrays.asList(contentDescriptor));

        fields.addAll(List.of(
                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
                fieldWithPath("pageable.pageSize").description("페이지 크기"),
                fieldWithPath("pageable.offset").description("데이터 offset"),
                fieldWithPath("pageable.paged").description("페이지 여부"),
                fieldWithPath("pageable.unpaged").description("페이지 아님 여부"),
                fieldWithPath("pageable.sort.empty").description("정렬 비어있는지 여부"),
                fieldWithPath("pageable.sort.sorted").description("정렬 적용 여부"),
                fieldWithPath("pageable.sort.unsorted").description("정렬 안됨 여부"),

                fieldWithPath("sort.empty").description("정렬 비어있는지 여부"),
                fieldWithPath("sort.sorted").description("정렬 적용 여부"),
                fieldWithPath("sort.unsorted").description("정렬 안됨 여부"),

                fieldWithPath("totalPages").description("전체 페이지 수"),
                fieldWithPath("totalElements").description("전체 데이터 수"),
                fieldWithPath("last").description("마지막 페이지 여부"),
                fieldWithPath("first").description("첫 페이지 여부"),
                fieldWithPath("number").description("현재 페이지 번호"),
                fieldWithPath("size").description("페이지당 요소 수"),
                fieldWithPath("numberOfElements").description("현재 페이지 요소 수"),
                fieldWithPath("empty").description("결과 비어있는지 여부")
        ));

        return fields;
    }

}
