package org.pahappa.systems.web.core.converters;

import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.InvoiceTaxService;
import org.pahappa.systems.web.core.converters.base.GenericConverter;

import javax.faces.convert.FacesConverter;

@FacesConverter("invoiceTaxConverter")
public class InvoiceTaxConverter extends GenericConverter<InvoiceTax, InvoiceTaxService> {
}
