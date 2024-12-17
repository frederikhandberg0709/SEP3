package via.sep.restful_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import via.sep.restful_server.model.Bookmark;
import via.sep.restful_server.model.Login;
import via.sep.restful_server.model.Property;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByAccount(Login account);
    List<Bookmark> findByProperty_PropertyId(Long propertyId);
    boolean existsByAccountAndProperty(Login account, Property property);
}
