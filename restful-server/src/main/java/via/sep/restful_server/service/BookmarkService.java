package via.sep.restful_server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import via.sep.restful_server.dto.BookmarkDTO;
import via.sep.restful_server.exception.ResourceNotFoundException;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.BookmarkRepository;
import via.sep.restful_server.repository.LoginRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final PropertyRepository propertyRepository;
    private final LoginRepository loginRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, PropertyRepository propertyRepository, LoginRepository loginRespository) {
        this.bookmarkRepository = bookmarkRepository;
        this.propertyRepository = propertyRepository;
        this.loginRepository = loginRespository;
    }

    public BookmarkDTO createBookmark(Long propertyId, Long accountId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        Login account = loginRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Bookmark bookmark = new Bookmark();
        bookmark.setProperty(property);
        bookmark.setAccount(account);

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return new BookmarkDTO(savedBookmark);
    }

    public List<BookmarkDTO> getBookmarksByAccountId(Long accountId) {
        Login account = loginRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        List<Bookmark> bookmarks = bookmarkRepository.findByAccount(account);
        return bookmarks.stream()
                .map(BookmarkDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteBookmark(Long bookmarkId) {
        if (!bookmarkRepository.existsById(bookmarkId)) {
            throw new ResourceNotFoundException("Bookmark not found");
        }
        bookmarkRepository.deleteById(bookmarkId);
    }

    public boolean isBookmarkOwner(Long bookmarkId, Long accountId) {
        return bookmarkRepository.findById(bookmarkId)
                .map(bookmark -> bookmark.getAccount().getAccountId().equals(accountId))
                .orElse(false);
    }

    public boolean hasBookmarked(Long accountId, Long propertyId) {
        Login account = loginRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        return bookmarkRepository.existsByAccountAndProperty(account, property);
    }
}
