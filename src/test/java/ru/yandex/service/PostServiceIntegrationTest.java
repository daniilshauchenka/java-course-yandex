package ru.yandex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.model.dto.PostCreateRequest;
import ru.yandex.model.dto.PostDto;
import ru.yandex.repository.PostRepository;

@SpringBootTest
class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    void createPost_shouldSaveAndReturnPost() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("title");
        request.setText("text");
        request.setTags(List.of("tag1"));

        PostDto result = postService.createPost(request);

        assertNotNull(result.getId());
        assertEquals("title", result.getTitle());
        assertEquals(1, result.getTags().size());
    }

    @Test
    void getPostById_shouldReturnPost() {
        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("title");
        request.setText("text");
        request.setTags(List.of("tag"));

        PostDto created = postService.createPost(request);

        PostDto found = postService.getPostById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("title", found.getTitle());
    }
}