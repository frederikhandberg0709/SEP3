package via.sep.restful_server.dto;

import lombok.Data;
import via.sep.restful_server.model.Bookmark;

import java.time.LocalDateTime;

@Data
public class BookmarkDTO {
    private Long bookmarkId;
    private Long propertyId;
    private Long accountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookmarkDTO() {}

    public BookmarkDTO(Bookmark bookmark) {
        this.bookmarkId = bookmark.getBookmarkId();
        this.propertyId = bookmark.getProperty().getPropertyId();
        this.accountId = bookmark.getAccount().getAccountId();
        this.createdAt = bookmark.getCreatedAt();
        this.updatedAt = bookmark.getUpdatedAt();
    }
}
