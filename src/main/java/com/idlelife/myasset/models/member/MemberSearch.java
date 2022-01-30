package com.idlelife.myasset.models.member;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearch {
    private Long memberId;
    private String memberName;
    private String email;
    private String deleteYn;

    public MemberSearch(Long memberId) {
        this.memberId = memberId;
    }
}
