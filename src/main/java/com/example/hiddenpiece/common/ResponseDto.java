package com.example.hiddenpiece.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName : com.example.hiddenpiece.global.message
 * fileName : ResponseDto
 * author : gim-yeong-geun
 * date : 2023/08/17
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/17         gim-yeong-geun          최초 생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String message;

    public static ResponseDto getInstance(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }
}
