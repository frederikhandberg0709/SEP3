//package via.sep.restful_server.service;
//
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//import via.sep.restful_server.dto.FormDTO;
//import via.sep.restful_server.model.Forms;
//import via.sep.restful_server.notification.mapper.NotificationMapper;
//import via.sep.restful_server.notification.service.NotificationService;
//import via.sep.restful_server.repository.FormRepository;
//
//@Service
//public class PropertyFormService {
//
//    private final FormRepository formsRepository;
//    private final NotificationMapper notificationMapper;
//    private final NotificationService notificationService;
//
//    public PropertyFormService(FormRepository formsRepository, NotificationMapper notificationMapper, NotificationService notificationService) {
//        this.formsRepository = formsRepository;
//        this.notificationMapper = notificationMapper;
//        this.notificationService = notificationService;
//    }
//
//    @Transactional
//    public void registerProperty(FormDTO formDTO) {
//        // Map DTO to Entity
//        Forms form = notificationMapper.convertToFormDTO(formDTO);
//
//        // Save to Database
//        Forms savedForm = formsRepository.save(form);
//
//        // Send Notification
//        notificationService.notifyFormSubmitted(savedForm, savedForm.getFormID().toString());
//    }
//}
