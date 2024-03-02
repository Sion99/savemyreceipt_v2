package com.smr.savemyreceipt_v2.DTO.group.response;

import com.smr.savemyreceipt_v2.domain.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupResponseDto {

    private final Long id;
    private final String name;
    private final String department;
    private final String description;

    @Builder
    public GroupResponseDto(Long id, String name, String department, String description) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.description = description;
    }

    public static GroupResponseDto convertToDto(Group group) {
        return GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .department(group.getDepartment())
                .description(group.getDescription())
                .build();
    }
}
