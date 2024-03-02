package com.smr.savemyreceipt_v2.DTO.group.request;

import com.smr.savemyreceipt_v2.domain.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupRequestDto {

    private String name;
    private String department;
    private String description;

    public Group toEntity() {
        return Group.builder()
            .name(this.name)
            .department(this.department)
            .description(this.description)
            .build();
    }

}
