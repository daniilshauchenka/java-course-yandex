package ru.yandex.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.model.entity.PostEntity;


@JdbcTest
@Import({TagRepository.class, PostRepository.class, PostTagRepository.class})
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Test
    void shouldReturnTagsGroupedByPostIds() {
        PostEntity post1 = PostEntity.builder()
            .title("post1")
            .text("text1")
            .likesCount(0)
            .commentsCount(0)
            .build();

        PostEntity post2 = PostEntity.builder()
            .title("post2")
            .text("text2")
            .likesCount(0)
            .commentsCount(0)
            .build();

        Long postId1 = postRepository.save(post1);
        Long postId2 = postRepository.save(post2);

        Long tagId1 = tagRepository.save("tag1");
        Long tagId2 = tagRepository.save("tag2");

        postTagRepository.save(postId1, tagId1);
        postTagRepository.save(postId1, tagId2);
        postTagRepository.save(postId2, tagId2);

        Map<Long, List<String>> result =
            tagRepository.findTagsByPostIds(List.of(postId1, postId2));

        assertEquals(2, result.size());

        assertTrue(result.get(postId1).contains("tag1"));
        assertTrue(result.get(postId1).contains("tag2"));

        assertEquals(1, result.get(postId2).size());
        assertTrue(result.get(postId2).contains("tag2"));
    }

    @Test
    void shouldReturnEmptyListWhenPostHasNoTags() {
        PostEntity post = PostEntity.builder()
            .title("post")
            .text("text")
            .likesCount(0)
            .commentsCount(0)
            .build();

        Long postId = postRepository.save(post);

        Map<Long, List<String>> result =
            tagRepository.findTagsByPostIds(List.of(postId));

        assertTrue(result.getOrDefault(postId, List.of()).isEmpty());
    }
}