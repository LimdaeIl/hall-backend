package com.hall.backend.common.response;

import java.util.List;
import java.util.function.Function;

public record CursorResponse<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext,
        int size
) {

    public static <T> CursorResponse<T> from(
            List<T> fetchedContent,
            int requestedSize,
            Function<T, Long> cursorExtractor
    ) {
        boolean hasNext =
                fetchedContent.size() > requestedSize;

        List<T> content = hasNext
                ? fetchedContent.subList(0, requestedSize)
                : fetchedContent;

        Long nextCursor =
                hasNext && !content.isEmpty()
                        ? cursorExtractor.apply(
                        content.getLast()
                )
                        : null;

        return new CursorResponse<>(
                List.copyOf(content),
                nextCursor,
                hasNext,
                content.size()
        );
    }

    public static <T> CursorResponse<T> empty() {
        return new CursorResponse<>(
                List.of(),
                null,
                false,
                0
        );
    }
}
