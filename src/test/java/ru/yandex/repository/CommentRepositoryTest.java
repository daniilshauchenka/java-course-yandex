package ru.yandex.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.model.entity.CommentEntity;
import ru.yandex.model.entity.PostEntity;

@JdbcTest
@Import({CommentRepository.class, PostRepository.class})
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void shouldSaveAndFindComment() {
        Long postId = createPost();
        CommentEntity comment = CommentEntity.builder()
            .text("hello")
            .postId(postId)
            .build();
        Long id = repository.save(comment);
        CommentEntity found = repository.findById(id);

        assertNotNull(found);
        assertEquals("hello", found.getText());
        assertEquals(postId, found.getPostId());
    }

    @Test
    void shouldFindCommentsByPostId() {
        Long postId = createPost();
        repository.save(CommentEntity.builder().text("c1").postId(postId).build());
        repository.save(CommentEntity.builder().text("c2").postId(postId).build());
        List<CommentEntity> comments = repository.findByPostId(postId);

        assertEquals(2, comments.size());
    }

    @Test
    void shouldUpdateComment() {
        Long postId = createPost();
        Long id = repository.save(CommentEntity.builder().text("old").postId(postId).build());
        repository.update(CommentEntity.builder()
            .id(id)
            .text("new")
            .postId(postId)
            .build());
        CommentEntity updated = repository.findById(id);

        assertEquals("new", updated.getText());
    }

    @Test
    void shouldDeleteComment() {
        Long postId = createPost();
        Long id = repository.save(
            CommentEntity.builder().text("test").postId(postId).build()
        );
        repository.delete(id);

        assertNull(repository.findById(id));
    }

    @Test
    void shouldDeleteByPostId() {
        Long postId = createPost();

        repository.save(CommentEntity.builder().text("c1").postId(postId).build());
        repository.save(CommentEntity.builder().text("c2").postId(postId).build());

        repository.deleteByPostId(postId);

        List<CommentEntity> comments = repository.findByPostId(postId);

        assertTrue(comments.isEmpty());
    }

    private Long createPost() {
        return postRepository.save(
            PostEntity.builder()
                .title("post")
                .text("text")
                .likesCount(0)
                .commentsCount(0)
                .build()
        );
    }
}