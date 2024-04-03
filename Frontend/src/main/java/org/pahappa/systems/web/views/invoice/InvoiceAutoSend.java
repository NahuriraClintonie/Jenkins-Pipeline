package org.pahappa.systems.web.views.invoice;


import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name="invoiceAutoSend")
@Setter
@Getter
public class InvoiceAutoSend  {
    private ClientService clientService;


    @PostConstruct
    public void init() {
        this.clientService = ApplicationContextProvider.getBean(ClientService.class);
    }

    public void toggleAutoSend(@NotNull Client client) {
        // Toggle the state of auto sending for the specific client
        client.setAutoSendStatus(!client.getAutoSendStatus());

        // Update the client entity in the database
        clientService.updateAutoSendStatus(client);

        // Display a message to inform the user about the change
        UiUtils.showMessageBox("Auto Send Status",
                client.getAutoSendStatus() ? "Auto sending activated." : "Auto sending deactivated.");
    }
}
