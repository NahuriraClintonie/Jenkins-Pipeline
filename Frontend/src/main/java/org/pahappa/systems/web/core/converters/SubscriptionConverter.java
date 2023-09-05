package org.pahappa.systems.web.core.converters;

import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.converters.base.GenericConverter;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("subscriptionConverter")
public class SubscriptionConverter extends GenericConverter<Subscription,SubscriptionService> {

}
