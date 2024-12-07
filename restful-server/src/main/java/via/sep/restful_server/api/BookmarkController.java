package via.sep.restful_server.api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.BookmarkDTO;
import via.sep.restful_server.dto.CreateBookmarkDTO;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.exception.ResourceNotFoundException;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.notification.service.NotificationService;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.PropertyRepository;
import via.sep.restful_server.service.BookmarkService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final NotificationService notificationService;
    private final PropertyRepository propertyRepository;
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService, NotificationService notificationService, PropertyRepository propertyRepository, BookmarkRepository bookmarkRepository) {
        this.bookmarkService = bookmarkService;
        this.notificationService = notificationService;
        this.propertyRepository = propertyRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @PostMapping
    public ResponseEntity<?> createBookmark(@Valid @RequestBody CreateBookmarkDTO dto) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long accountId = jwt.getClaim("accountId");

            if (bookmarkService.hasBookmarked(accountId, dto.getPropertyId())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Property is already bookmarked");
            }

            BookmarkDTO createdBookmark = bookmarkService.createBookmark(dto.getPropertyId(), accountId);

            Property property = propertyRepository.findById(dto.getPropertyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

            PropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.setPropertyType(property.getPropertyType());
            propertyDTO.setAddress(property.getAddress());
            propertyDTO.setFloorArea(property.getFloorArea());
            propertyDTO.setPrice(property.getPrice());
            propertyDTO.setNumBedrooms(property.getNumBedrooms());
            propertyDTO.setNumBathrooms(property.getNumBathrooms());
            propertyDTO.setYearBuilt(property.getYearBuilt());
            propertyDTO.setDescription(property.getDescription());

            notificationService.notifyBookmarkCreated(createdBookmark, propertyDTO, createdBookmark.getBookmarkId().toString());

            return ResponseEntity.ok(createdBookmark);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: ", e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Error creating bookmark: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating bookmark");
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserBookmarks() {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long accountId = jwt.getClaim("accountId");

            List<BookmarkDTO> bookmarks = bookmarkService.getBookmarksByAccountId(accountId);
            return ResponseEntity.ok(bookmarks);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching bookmarks: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching bookmarks");
        }
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long bookmarkId) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long accountId = jwt.getClaim("accountId");

            if (!bookmarkService.isBookmarkOwner(bookmarkId, accountId)) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to delete this bookmark");
            }

            Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

            Property property = propertyRepository.findById(bookmark.getProperty().getPropertyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

            // Convert to DTOs
            BookmarkDTO bookmarkDTO = new BookmarkDTO(bookmark);
            PropertyDTO propertyDTO = new PropertyDTO();
            propertyDTO.setPropertyType(property.getPropertyType());
            propertyDTO.setAddress(property.getAddress());
            propertyDTO.setFloorArea(property.getFloorArea());
            propertyDTO.setPrice(property.getPrice());
            propertyDTO.setNumBedrooms(property.getNumBedrooms());
            propertyDTO.setNumBathrooms(property.getNumBathrooms());
            propertyDTO.setYearBuilt(property.getYearBuilt());
            propertyDTO.setDescription(property.getDescription());

            // Delete the bookmark
            bookmarkService.deleteBookmark(bookmarkId);

            notificationService.notifyBookmarkDeleted(bookmarkDTO, propertyDTO, bookmarkId.toString());

            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: ", e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting bookmark: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting bookmark");
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> hasBookmarked(@PathVariable Long propertyId) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long accountId = jwt.getClaim("accountId");

            boolean hasBookmarked = bookmarkService.hasBookmarked(accountId, propertyId);
            return ResponseEntity.ok(hasBookmarked);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: ", e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Error checking bookmark status: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking bookmark status");
        }
    }
}
