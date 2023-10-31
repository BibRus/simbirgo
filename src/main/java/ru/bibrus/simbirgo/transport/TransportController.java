package ru.bibrus.simbirgo.transport;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Transport")
public class TransportController {

    @PostMapping
    public String addTransport(@RequestBody TransportAddRequest request) {
        return null;
    }

    @PutMapping("/{id}")
    public String updateTransport(@PathVariable long id) {
        return null;
    }

    @GetMapping("/{id}")
    public String getInformationById(@PathVariable long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteTransportById(@PathVariable long id) {
        return null;
    }

}