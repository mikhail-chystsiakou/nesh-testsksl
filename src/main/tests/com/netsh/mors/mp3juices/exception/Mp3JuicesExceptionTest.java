package com.netsh.mors.mp3juices.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.nesh.mors.mp3juices.exception.Mp3JuicesException;
import org.junit.jupiter.api.Test;

public class Mp3JuicesExceptionTest {
    @Test
    public void testExceptionMessage() {
        assertEquals(
            "Loading from 'https://vk.com' failed, args was: 'user', 'null', 'false'",
            new Mp3JuicesException(
                "Loading from {} failed, args was: ", "https://vk.com", "user", null, "false"
            ).getMessage()
        );

    }
}
