package ru.yandex.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.model.entity.TagEntity;
import ru.yandex.repository.TagRepository;

@SpringBootTest
class TagServiceIntegrationTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;


    @Test
    void attachTags_shouldPersistTags() {
        Long postId = 1L;
        tagService.attachTagsToPost(postId, List.of("tag3", "tag4"));
        TagEntity tag = tagRepository.findByName("tag3");
        assertNotNull(tag);
    }
}