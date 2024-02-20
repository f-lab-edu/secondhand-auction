package com.js.secondhandauction.core.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.js.secondhandauction.core.member.domain.Member;
import com.js.secondhandauction.core.member.domain.Role;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGetResponse implements Serializable {
    private long userNo;
    private String userId;
    private String nickname;
    private int totalBalance;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime uptDate;
    private Role role;

    public static MemberGetResponse of(Member member) {
        return MemberGetResponse.builder()
                .userNo(member.getUserNo())
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .totalBalance(member.getTotalBalance())
                .regDate(member.getRegDate())
                .uptDate(member.getUptDate())
                .role(member.getRole())
                .build();
    }
}
