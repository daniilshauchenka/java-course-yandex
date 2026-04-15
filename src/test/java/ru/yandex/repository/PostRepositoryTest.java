package ru.yandex.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.model.entity.PostEntity;


@JdbcTest
@Import({PostRepository.class})
class PostRepositoryTest {

    @Autowired
    private PostRepository repository;

    @Test
    void shouldSaveAndFindPost() {
        PostEntity post = PostEntity.builder()
            .title("test")
            .text("text")
            .likesCount(0)
            .commentsCount(0)
            .build();

        Long id = repository.save(post);

        PostEntity found = repository.findById(id);

        assertNotNull(found);
        assertEquals("test", found.getTitle());
    }
}