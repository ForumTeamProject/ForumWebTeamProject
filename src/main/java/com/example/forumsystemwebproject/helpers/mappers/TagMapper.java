package com.example.forumsystemwebproject.helpers.mappers;

import com.example.forumsystemwebproject.models.DTOs.TagDto;
import com.example.forumsystemwebproject.models.Tag;
import com.example.forumsystemwebproject.services.contracts.TagService;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    private final TagService tagService;

    public TagMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public Tag fromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setContent(tagDto.getContent());
        return tag;
    }

    public Tag fromDto(int id, TagDto tagDto) {
        Tag tag = fromDto(tagDto);
        Tag tagFromService = tagService.getById(id);
        tagFromService.setContent(tagDto.getContent());

        return tagFromService;
    }
}
