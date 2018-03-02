package app.controller;

import app.Message;
import app.UrlUtil;
import app.entities.ContactsEntity;
import app.service.ContactService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@RequestMapping("/contacts")
@Controller
public class ContactsController {
    private final Logger logger = LoggerFactory.getLogger(ContactsController.class);
    private ContactService contactService;
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        logger.info("Listing contacts");
        List<ContactsEntity> contacts = contactService.findAll();
        uiModel.addAttribute("contacts", contacts);
        logger.info("No. of contacts: " + contacts.size());
        return "contacts/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        ContactsEntity contact = contactService.findById(id);
        uiModel.addAttribute("contact", contact);
        return "contacts/show";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("contact") ContactsEntity contact, BindingResult bindingResult,
                         Model uiModel, HttpServletRequest httpServletRequest,
                         RedirectAttributes redirectAttributes, Locale locale,
                         @RequestParam(value = "file", required = false) Part file) {
        logger.info("Updating contact");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("contact_save_fail",
                            new Object[]{}, locale)));
            uiModel.addAttribute("contact", contact);
            return "contacts/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message",
                new Message("success",
                        messageSource.getMessage("contact_save_success",
                                new Object[]{}, locale)));
        if (file != null) {
            logger.info("File name: " + file.getName());
            logger.info("File size: " + file.getSize());
            logger.info("File content type: " + file.getContentType());
            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) logger.info("File inputstream is null");
                fileContent = IOUtils.toByteArray(inputStream);
                contact.setPhoto(fileContent);
            } catch (IOException ех) {
                logger.error("Error saving uploaded file");
            }
            //contact.setPhoto(fileContent);
        }
        contactService.save(contact);
        return "redirect:/contacts/"
                + UrlUtil.encodeUrlPathSegment(contact.getId().toString(),
                httpServletRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("contact", contactService.findById(id));
        return "contacts/update";
    }

    @RequestMapping(params = "form", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("contact") ContactsEntity contact, BindingResult bindingResult,
                         Model uiModel, HttpServletRequest httpServletRequest,
                         RedirectAttributes redirectAttributes, Locale locale,
                         @RequestParam(value = "file", required = false) Part file) {
        logger.info("Creating contact");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("contact_save_fail",
                            new Object[]{}, locale)));
            uiModel.addAttribute("contact", contact);
            return "contacts/create";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message",
                new Message("success",
                        messageSource.getMessage("contact_save_success",
                                new Object[]{}, locale)));
        logger.info("Contact id: " + contact.getId());
        if (file != null) {
            logger.info("File name: " + file.getName());
            logger.info("File size: " + file.getSize());
            logger.info("File content type: " + file.getContentType());
            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) logger.info("File inputstream is null");
                fileContent = IOUtils.toByteArray(inputStream);
                contact.setPhoto(fileContent);
            } catch (IOException ех) {
                logger.error("Error saving uploaded file");
            }
            //contact.setPhoto(fileContent);
        }
        contactService.save(contact);
        return "redirect:/contacts/" +
                UrlUtil.encodeUrlPathSegment(contact.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        ContactsEntity contact = contactService.findById(id);
        if (contact.getPhoto() != null) {
            logger.info("Downloading photo for id: {} with size: {}",
                    contact.getId(),
                    contact.getPhoto().length);
        }
        return contact.getPhoto();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        ContactsEntity contact = new ContactsEntity();
        uiModel.addAttribute("contact", contact);
        return "contacts/create";
    }

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
} // class ContactsController
