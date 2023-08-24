package org.pahappa.systems.web.core.dialogs;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.sers.webutils.model.BaseEntity;

import javax.faces.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for creating a dialog form
 *
 * @param <T>
 * @version 1.0
 */
@Getter
@Setter
public abstract class DialogForm<T extends BaseEntity> extends FormPresenter<T> {
    private static final long serialVersionUID = 1L;
    private final Map<String, Object> closeMap;
    private String name;
    private int width;
    private int height;

    /**
     * Constructor
     * @param name - The name of the dialog form / path to the dialog form: e.g. /views/dialogs/DialogForm.xhtml
     * @param width - The width of the dialog form
     * @param height - The height of the dialog form
     */
    public DialogForm(String name, int width, int height) {
        super();
        this.name = name;
        this.width = width;
        this.height = height;
        this.closeMap = new HashMap<String, Object>();
    }

    /**
     * Triggers the display of the dialog form
     *
     * @param actionEvent - The action event
     */
    public void show(ActionEvent actionEvent) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", height);
        options.put("contentWidth", width);
        options.put("style", "");
        options.put("widgetVar", this.name);
        options.put("id", this.name);
        this.closeMap.put("widgetVar", this.name);
        this.closeMap.put("id", this.name);
        PrimeFaces.current().dialog().openDynamic(name, options, null);
    }

    public void save() throws Exception {
        try {
            persist();
            resetModal();
            MessageComposer.compose("Action Successful", "Successfully saved record");
            hide();
        } catch (Exception e) {
            MessageComposer.compose("Action Failure", e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    public void hide() {
        PrimeFaces.current().dialog().closeDynamic(this.name);
    }

    /**
     * Do nothing in this abstract class. To be overridden by Subclasses that
     * wish to set any other form properties in case the set model is not null
     * i.e., editing mode.
     */
    @Override
    public void setFormProperties() {
        super.isEditing = true;
    }

    @Override
    public void resetModal() {
        super.isEditing = false;
    }

}