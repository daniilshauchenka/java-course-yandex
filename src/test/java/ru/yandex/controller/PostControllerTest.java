package ru.yandex.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.handler.GlobalExceptionHandler;
import ru.yandex.model.dto.PostDto;
import ru.yandex.model.dto.PostPageResponse;
import ru.yandex.service.PostService;


@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void getPosts_shouldReturnList() throws Exception {
        PostDto post = PostDto.builder()
                .id(1L)
                .title("title")
                .text("text")
                .tags(List.of("tag"))
                .likesCount(1)
                .commentsCount(1)
                .build();

        PostPageResponse response = PostPageResponse.builder()
                .posts(List.of(post))
                .hasPrev(false)
                .hasNext(false)
                .lastPage(1)
                .build();

        when(postService.getPosts("test", 1, 10))
                .thenReturn(response);

        mockMvc.perform(get("/api/posts")
                        .param("search", "test")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].id").value(1))
                .andExpect(jsonPath("$.posts[0].title").value("title"));
    }

    @Test
    void getPostById_shouldReturnPost() throws Exception {
        PostDto dto = PostDto.builder()
                .id(1L)
                .title("title")
                .text("text")
                .tags(List.of("tag"))
                .likesCount(1)
                .commentsCount(1)
                .build();

        when(postService.getPostById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void createPost_shouldReturnCreated() throws Exception {
        String request = """
                {
                  "title": "title",
                  "text": "text",
                  "tags": ["tag"]
                }
                """;

        PostDto response = PostDto.builder()
                .id(1L)
                .title("title")
                .text("text")
                .tags(List.of("tag"))
                .likesCount(0)
                .commentsCount(0)
                .build();

        when(postService.createPost(any())).thenReturn(response);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deletePost_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isOk());

        verify(postService).deletePost(1L);
    }

    @Test
    void incrementLikes_shouldReturnValue() throws Exception {
        when(postService.incrementLikes(1L)).thenReturn(5L);

        mockMvc.perform(post("/api/posts/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getPost_shouldReturn404_whenNotFound() throws Exception {
        when(postService.getPostById(1L))
                .thenThrow(new MyException(ExceptionType.POST_NOT_FOUND, 1));

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("POST_NOT_FOUND"));
    }
}
