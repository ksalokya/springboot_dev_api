package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // creating Sort object depending on param
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        // get content of page object
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> contents = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        // map results to postResponse and return
        return mapToPostResponse(contents, posts);
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "ID", id));
        return mapToDto(post);
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // covert DTO to entity
        Post post = mapToEntity(postDto);
        // save entity in DB
        Post newPost = postRepository.save(post);
        // convert entity to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "ID", id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "ID", id));

        postRepository.deleteById(id);
    }


    // coverts DTO to entity
    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    // converts entity to DTO
    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }

    // converts List of postDto to postResponse
    private PostResponse mapToPostResponse(List<PostDto> contents, Page<Post> posts) {
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }
}
