package org.pahappa.systems.web.core.converters;

import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.services.EmailsToCcService;
import org.pahappa.systems.web.core.converters.base.GenericConverter;

import javax.faces.convert.FacesConverter;

@FacesConverter("ccEmailConverter")
public class CCEmailConverter  extends GenericConverter<EmailsToCc, EmailsToCcService> {
}
