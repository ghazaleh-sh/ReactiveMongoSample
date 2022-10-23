package com.reactive.template.demo.configs;

import com.reactive.template.demo.model.Profile;
import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {

    public ProfileCreatedEvent(Profile source) {
        super(source);
    }
}
