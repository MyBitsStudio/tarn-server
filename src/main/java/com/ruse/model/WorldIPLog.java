package com.ruse.model;

import lombok.Getter;

public record WorldIPLog(@Getter String username, @Getter String content, @Getter String ip, @Getter String date, @Getter String gameMode) {

}
