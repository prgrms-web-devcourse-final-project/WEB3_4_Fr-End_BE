package com.frend.planit.domain.image.controller;

import com.frend.planit.domain.image.dto.request.ImageTestRequest;
import com.frend.planit.domain.image.dto.response.ImageTestResponse;
import com.frend.planit.domain.image.service.ImageTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequestMapping("/test/image")
public class ImageTestController {

    @Autowired
    private ImageTestService imageTestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody ImageTestRequest request) {
        return imageTestService.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImageTestResponse read(@PathVariable long id) {
        return imageTestService.read(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImageTestResponse update(
            @PathVariable long id,
            @RequestBody ImageTestRequest request) {
        return imageTestService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        imageTestService.delete(id);
    }
}