package com.dnd.mountclim.domain.jeongkyun;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WorldCupDto {

    //document
    private String placeName;
    private String distance;
    private String phone;

    //크롤링 필요 (이미지, 메뉴,리뷰, 별점)
    private String[] placeImg;
    private String[] menu;
    private String review;
    private String rating;


}
