package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface InvoiceTaxService extends GenericService<InvoiceTax>{
    List<InvoiceTax> getTaxInstance();

}
