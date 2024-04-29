package org.pahappa.systems.web.procurement;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.procurement.Supplier;
import org.pahappa.systems.web.views.client.ClientView;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "supplierView")
@ViewScoped
@Getter
@Setter
public class SupplierView extends PaginatedTableView<Supplier, SupplierView, SupplierView> {


    @PostConstruct
    public void init() {

    }
    @Override
    public void reloadFromDB(int i, int i1, Map<String, Object> map) throws Exception {

    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }
}
