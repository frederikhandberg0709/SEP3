package via.sep.restful_server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import via.sep.restful_server.dto.BookmarkDTO;
import via.sep.restful_server.exception.ResourceNotFoundException;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest {
    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private Property testProperty;
    private Login testLogin;
    private Bookmark testBookmark;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setPropertyId(1L);
        testProperty.setPropertyType("Apartment");
        testProperty.setAddress("123 Test St");
        testProperty.setFloorArea(BigDecimal.valueOf(100.0));
        testProperty.setPrice(BigDecimal.valueOf(200000.0));
        testProperty.setNumBedrooms(2);
        testProperty.setNumBathrooms(1);

        testLogin = new Login();
        testLogin.setAccountId(1L);
        testLogin.setUsername("testuser");

        testBookmark = new Bookmark();
        testBookmark.setBookmarkId(1L);
        testBookmark.setProperty(testProperty);
        testBookmark.setAccount(testLogin);
    }

    @Test
    void createBookmark_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(loginRepository.findById(1L)).thenReturn(Optional.of(testLogin));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(testBookmark);

        BookmarkDTO result = bookmarkService.createBookmark(1L, 1L);

        assertNotNull(result);
        assertEquals(testBookmark.getBookmarkId(), result.getBookmarkId());
        verify(bookmarkRepository).save(any(Bookmark.class));
    }

    @Test
    void createBookmark_PropertyNotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookmarkService.createBookmark(1L, 1L)
        );
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    void createBookmark_AccountNotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(loginRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookmarkService.createBookmark(1L, 1L)
        );
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    void getBookmarksByAccountId_Success() {
        when(loginRepository.findById(1L)).thenReturn(Optional.of(testLogin));
        when(bookmarkRepository.findByAccount(testLogin)).thenReturn(Arrays.asList(testBookmark));

        List<BookmarkDTO> results = bookmarkService.getBookmarksByAccountId(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testBookmark.getBookmarkId(), results.get(0).getBookmarkId());
    }

    @Test
    void getBookmarksByAccountId_AccountNotFound() {
        when(loginRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookmarkService.getBookmarksByAccountId(1L)
        );
        verify(bookmarkRepository, never()).findByAccount(any());
    }

    @Test
    void deleteBookmark_Success() {
        when(bookmarkRepository.existsById(1L)).thenReturn(true);

        bookmarkService.deleteBookmark(1L);

        verify(bookmarkRepository).deleteById(1L);
    }

    @Test
    void deleteBookmark_NotFound() {
        when(bookmarkRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                bookmarkService.deleteBookmark(1L)
        );
        verify(bookmarkRepository, never()).deleteById(any());
    }

    @Test
    void isBookmarkOwner_True() {
        when(bookmarkRepository.findById(1L)).thenReturn(Optional.of(testBookmark));

        boolean result = bookmarkService.isBookmarkOwner(1L, 1L);

        assertTrue(result);
    }

    @Test
    void isBookmarkOwner_False() {
        when(bookmarkRepository.findById(1L)).thenReturn(Optional.of(testBookmark));

        boolean result = bookmarkService.isBookmarkOwner(1L, 2L);

        assertFalse(result);
    }

    @Test
    void isBookmarkOwner_BookmarkNotFound() {
        when(bookmarkRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = bookmarkService.isBookmarkOwner(1L, 1L);

        assertFalse(result);
    }

    @Test
    void hasBookmarked_True() {
        when(loginRepository.findById(1L)).thenReturn(Optional.of(testLogin));
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(bookmarkRepository.existsByAccountAndProperty(testLogin, testProperty)).thenReturn(true);

        boolean result = bookmarkService.hasBookmarked(1L, 1L);

        assertTrue(result);
    }

    @Test
    void hasBookmarked_False() {
        when(loginRepository.findById(1L)).thenReturn(Optional.of(testLogin));
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(bookmarkRepository.existsByAccountAndProperty(testLogin, testProperty)).thenReturn(false);

        boolean result = bookmarkService.hasBookmarked(1L, 1L);

        assertFalse(result);
    }

    @Test
    void hasBookmarked_AccountNotFound() {
        when(loginRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookmarkService.hasBookmarked(1L, 1L)
        );
        verify(bookmarkRepository, never()).existsByAccountAndProperty(any(), any());
    }

    @Test
    void hasBookmarked_PropertyNotFound() {
        when(loginRepository.findById(1L)).thenReturn(Optional.of(testLogin));
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookmarkService.hasBookmarked(1L, 1L)
        );
        verify(bookmarkRepository, never()).existsByAccountAndProperty(any(), any());
    }
}
