package com.idlelife.myasset.models.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class FamilyEntity {
    private Long familyId;
    private String memberId;
    private String householderYn;
    private String householderRelation;
}
