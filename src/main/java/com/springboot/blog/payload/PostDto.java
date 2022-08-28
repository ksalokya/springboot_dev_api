package com.springboot.blog.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@ApiModel(description = "Post model information")
public class PostDto {
    @ApiModelProperty(value = "Blog Post ID")
    private Long id;

    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    @ApiModelProperty(value = "Blog Post Title")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    @ApiModelProperty(value = "Blog Post description")
    private String description;

    @NotEmpty
    @ApiModelProperty(value = "Blog Post content")
    private String content;

    @ApiModelProperty(value = "Blog Post comments")
    private Set<CommentDto> comments;
}
