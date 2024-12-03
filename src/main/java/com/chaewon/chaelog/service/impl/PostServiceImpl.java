package com.chaewon.chaelog.service.impl;

import com.chaewon.chaelog.domain.Post;
import com.chaewon.chaelog.domain.PostEditor;
import com.chaewon.chaelog.domain.request.PostCreateRequest;
import com.chaewon.chaelog.domain.request.PostEditRequest;
import com.chaewon.chaelog.domain.request.PostSearchRequest;
import com.chaewon.chaelog.domain.response.PagingResponse;
import com.chaewon.chaelog.domain.response.PostResponse;
import com.chaewon.chaelog.exception.MemberNotFound;
import com.chaewon.chaelog.exception.PostNotFound;
import com.chaewon.chaelog.exception.Unauthorized;
import com.chaewon.chaelog.repository.MemberRepository;
import com.chaewon.chaelog.repository.post.PostRepository;
import com.chaewon.chaelog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final MemberRepository memberRepository;


    @Override
    public void write(Long memberId, PostCreateRequest postCreateRequest) {
//        Post post = Post.builder()
//                .title(postCreateRequest.getTitle())
//                .content(postCreateRequest.getContent())
//                .build();
        var member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new); //회원 없으면


        Post post = Post.createPost(postCreateRequest, member);
        postRepository.save(post);
    }

    //1개 조회
    @Override
    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return new PostResponse(post);
    }
    //list 조회
    @Override
    public PagingResponse<PostResponse> getList(PostSearchRequest postSearchRequest) {
        Page<Post> postPage = postRepository.getList(postSearchRequest);
        PagingResponse<PostResponse> postList = new PagingResponse<>(postPage, PostResponse.class);
        return postList;
    }

    @Override
    public PagingResponse<PostResponse> search(PostSearchRequest request) {
        Page<Post> posts = postRepository.searchByKeyword(request);
        return new PagingResponse<>(posts, PostResponse.class);
    }
    @Override
    @Transactional
    public void edit(Long id, PostEditRequest postEditRequest, Long memberId) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        if(!post.getMember().getId().equals(memberId)) {
            throw new Unauthorized();
        }

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder
                .title(postEditRequest.getTitle())
                .content(postEditRequest.getContent())
                .build();

        post.edit(postEditor);
    }

    @Override
    public void delete(Long id, Long memberId) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        if(!post.getMember().getId().equals(memberId)) {
            throw new Unauthorized();
        }
        postRepository.delete(post);
    }
}
